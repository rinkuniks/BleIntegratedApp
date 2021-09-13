package com.technoidentity.vitalz.bluetooth.connection


import android.bluetooth.*
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.technoidentity.vitalz.bluetooth.data.*
import com.technoidentity.vitalz.data.datamodel.updatedregistereddevice.UpdateRegisteredDeviceRequest
import com.technoidentity.vitalz.data.network.VitalzApi
import com.technoidentity.vitalz.utils.*
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import timber.log.Timber
import java.io.UnsupportedEncodingException
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentLinkedQueue

const val TAG = "BleManager"

@ViewModelScoped
class BleManager(private val bleScanner: BleScanner, private val api: VitalzApi) : IBleManager {

    /**
     * Bluetooth device data
     */
    lateinit var bluetoothGatt: BluetoothGatt

    private var _isScanning = MutableStateFlow(false)
    override var isScanning: StateFlow<Boolean> = _isScanning

    private var _connectedBleDeviceLiveData = MutableLiveData<BleDevice>()
    override var connectedBleDeviceLiveData: LiveData<BleDevice> = _connectedBleDeviceLiveData

    private var _isDeviceConnected = MutableStateFlow(false)
    override var isDeviceConnected: StateFlow<Boolean> = _isDeviceConnected

    lateinit var connectedDevice: BleDevice

    /**
     * Patient data to be updated
     */

    private var _heartRateCharacteristic = MutableStateFlow(byteArrayOf(0))
    override var heartRateCharacteristic: StateFlow<ByteArray> = _heartRateCharacteristic

    private var _deviceBattery = MutableLiveData<Int>()
    override var battery: LiveData<Int> = _deviceBattery

    private var _ecgCharacteristic = MutableStateFlow(byteArrayOf(0))
    override var ecgCharacteristic: StateFlow<ByteArray> = _ecgCharacteristic

    private var _bodyPosture = MutableLiveData<String>()
    override var bodyPosture: LiveData<String> = _bodyPosture

    private var _bodyTemperature = MutableLiveData<String>()
    override var bodyTemperature: LiveData<String> = _bodyTemperature

    private val deviceGattMap = ConcurrentHashMap<BluetoothDevice, BluetoothGatt>()
    private val operationQueue = ConcurrentLinkedQueue<BleOperationType>()
    private var pendingOperation: BleOperationType? = null
    lateinit var service: List<BluetoothGattService>

    fun BluetoothDevice.isConnected() = isDeviceConnected.value

    override fun startScan() {
        _isScanning.value = true
        bleScanner.startScan()
    }

    override fun stopScan() {
        _isScanning.value = false
        bleScanner.stopScan()
    }

    override var scanChannel: Channel<BluetoothDevice> = bleScanner.channel

    override fun connectDevice(device: BluetoothDevice, context: Context) {
        connectedDevice =
            BleDevice(device, connectionStatus = BleConnection.DeviceConnectionLoading(device))
        if (device.isConnected()) {
            Timber.e("Already connected to ${device.address}!")
        } else {
            enqueueOperation(Connect(device, context.applicationContext))
        }
    }

    fun servicesOnDevice(device: BluetoothDevice): List<BluetoothGattService>? =
        deviceGattMap[device]?.services.apply {
            this?.let {
                service = it
            }
        }

    override fun readCharacteristic(device: BluetoothDevice, uuid: UUID, service: BluetoothGattService) {

                service.getCharacteristic(uuid).apply {
                    if (this.isReadable()) {
                        enqueueOperation(CharacteristicRead(device, uuid))
                    }
                }
    }



    fun connect(device: BluetoothDevice, context: Context) {
        if (device.isConnected()) {
            Timber.e("Already connected to ${device.address}!")
        } else {
            enqueueOperation(Connect(device, context.applicationContext))
        }
    }

    fun teardownConnection(device: BluetoothDevice) {
        if (device.isConnected()) {
            enqueueOperation(Disconnect(device))
        } else {
            Timber.e("Not connected to ${device.address}, cannot teardown connection!")
        }
    }

    fun readDescriptor(device: BluetoothDevice, descriptor: BluetoothGattDescriptor) {
        if (device.isConnected() && descriptor.isReadable()) {
            enqueueOperation(DescriptorRead(device, descriptor.uuid))
        } else if (!descriptor.isReadable()) {
            Timber.e("Attempting to read ${descriptor.uuid} that isn't readable!")
        } else if (!device.isConnected()) {
            Timber.e("Not connected to ${device.address}, cannot perform descriptor read")
        }
    }

    fun writeDescriptor(
        device: BluetoothDevice,
        descriptor: BluetoothGattDescriptor,
        payload: ByteArray
    ) {
        if (device.isConnected() && (descriptor.isWritable() || descriptor.isCccd())) {
            enqueueOperation(DescriptorWrite(device, descriptor.uuid, payload))
        } else if (!device.isConnected()) {
            Timber.e("Not connected to ${device.address}, cannot perform descriptor write")
        } else if (!descriptor.isWritable() && !descriptor.isCccd()) {
            Timber.e("Descriptor ${descriptor.uuid} cannot be written to")
        }
    }

    override fun enableNotifications(device: BluetoothDevice, characteristic: BluetoothGattCharacteristic) {
        if (device.isConnected() &&
            (characteristic.isIndicatable() || characteristic.isNotifiable())
        ) {
            enqueueOperation(EnableNotifications(device, characteristic.uuid))
        } else if (!device.isConnected()) {
            Timber.e("Not connected to ${device.address}, cannot enable notifications")
        } else if (!characteristic.isIndicatable() && !characteristic.isNotifiable()) {
            Timber.e("Characteristic ${characteristic.uuid} doesn't support notifications/indications")
        }
    }

    @Synchronized
    private fun enqueueOperation(operation: BleOperationType) {
        operationQueue.add(operation)
        if (pendingOperation == null) {
            doNextOperation()
        }
    }

    @Synchronized
    private fun signalEndOfOperation() {
        Timber.d("End of $pendingOperation")
        pendingOperation = null
        if (operationQueue.isNotEmpty()) {
            doNextOperation()
        }
    }

    /**
     * Perform a given [BleOperationType]. All permission checks are performed before an operation
     * can be enqueued by [enqueueOperation].
     */
    @Synchronized
    private fun doNextOperation() {
        if (pendingOperation != null) {
            Timber.e("doNextOperation() called when an operation is pending! Aborting.")
            return
        }

        val operation = operationQueue.poll() ?: run {
            Timber.v("Operation queue empty, returning")
            return
        }
        pendingOperation = operation

        // Handle Connect separately from other operations that require device to be connected
        if (operation is Connect) {
            with(operation) {
                Timber.w("Connecting to ${device.address}")
                bluetoothGatt = device.connectGatt(context, false, gattClientCallback)
            }
            return
        }

        // Check BluetoothGatt availability for other operations
        val gatt = deviceGattMap[operation.device] ?: this.run {
            Timber.e("Not connected to ${operation.device.address}! Aborting $operation operation.")
            signalEndOfOperation()
            return
        }

        // TODO: Make sure each operation ultimately leads to signalEndOfOperation()
        // TODO: Refactor this into an BleOperationType abstract or extension function
        when (operation) {
            is Disconnect -> with(operation) {
                Timber.w("Disconnecting from ${device.address}")
                gatt.close()
                deviceGattMap.remove(device)
                signalEndOfOperation()
            }
            is CharacteristicWrite -> with(operation) {
                gatt.findCharacteristic(characteristicUuid)?.let { characteristic ->
                    characteristic.writeType = writeType
                    characteristic.value = payload
                    gatt.writeCharacteristic(characteristic)
                } ?: this.run {
                    Timber.e("Cannot find $characteristicUuid to write to")
                    signalEndOfOperation()
                }
            }
            is CharacteristicRead -> with(operation) {
                gatt.findCharacteristic(characteristicUuid)
                    ?.let { characteristic ->
                        gatt.readCharacteristic(characteristic)
                    } ?: this.run {
                    Timber.e("Cannot find $characteristicUuid to read from")
                    signalEndOfOperation()
                }
            }

            is DescriptorWrite -> with(operation) {
                gatt.findDescriptor(descriptorUuid)?.let { descriptor ->
                    descriptor.value = payload
                    gatt.writeDescriptor(descriptor)
                } ?: this.run {
                    Timber.e("Cannot find $descriptorUuid to write to")
                    signalEndOfOperation()
                }
            }
            is DescriptorRead -> with(operation) {

                gatt.findDescriptor(descriptorUuid)?.let { descriptor ->
                    gatt.readDescriptor(descriptor)
                } ?: this.run {
                    Timber.e("Cannot find $descriptorUuid to read from")
                    signalEndOfOperation()
                }
            }
            is EnableNotifications -> with(operation) {
                gatt.findCharacteristic(characteristicUuid)?.let { characteristic ->

                    val payload = when {
                        characteristic.isIndicatable() ->
                            BluetoothGattDescriptor.ENABLE_INDICATION_VALUE
                        characteristic.isNotifiable() ->
                            BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
                        else ->
                            error("${characteristic.uuid} doesn't support notifications/indications")
                    }

                    characteristic.getDescriptor(UUID.fromString(CCC_DESCRIPTOR_UUID))
                        ?.let { cccDescriptor ->
                            if (!gatt.setCharacteristicNotification(characteristic, true)) {
                                Timber.e("setCharacteristicNotification failed for ${characteristic.uuid}")
                                signalEndOfOperation()
                                return
                            }
                            cccDescriptor.value = payload
                            //cccDescriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE)
                            gatt.writeDescriptor(cccDescriptor)
                        } ?: this.run {
                        Timber.e("${characteristic.uuid} doesn't contain the CCC descriptor!")
                        signalEndOfOperation()
                    }
                } ?: this.run {
                    Timber.e("Cannot find $characteristicUuid! Failed to enable notifications.")
                    signalEndOfOperation()
                }
            }
        }
    }

    private val gattClientCallback = object : BluetoothGattCallback() {

        override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
            super.onConnectionStateChange(gatt, status, newState)

            if (status != BluetoothGatt.GATT_SUCCESS) {
                disconnectGattServer(gatt, BleConnection.DeviceConnectingError)
                if (pendingOperation is Connect) {
                    signalEndOfOperation()
                }
                return
            }
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                _isDeviceConnected.value = true.also {
                    Timber.d("BleManager device connected")
                }
                deviceGattMap[gatt.device] = gatt
                gatt.discoverServices()
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                disconnectGattServer(gatt, BleConnection.DeviceConnectingError)
            }
        }


        override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
            super.onServicesDiscovered(gatt, status)
            if (status != BluetoothGatt.GATT_SUCCESS) {
                return
            }

            connectedDevice.run { connectedDevice = BleDevice(this.device, gatt, gatt?.services,
                        connectionStatus = BleConnection.DeviceConnected)
                _connectedBleDeviceLiveData.postValue(connectedDevice)
            }

            if (pendingOperation is Connect) {
                signalEndOfOperation()
            }

//                    it.getService(DEVICE_BATTERY_SER_UUID)?.let { batteryService ->
//                        bluetoothGattService = batteryService
//                        readCharacteristic(DEVICE_BATTERY_CHAR_UUID, batteryService)
//                        Timber.d("battery uuid: ${batteryService.uuid}")
//                    }
//
//
//                it.getService(HEART_RATE_SER_UUID)?.let { heartRateService ->
//                    bluetoothGattService = heartRateService
//                    readCharacteristic(HEART_RATE_CHAR_UUID, heartRateService)
//                    Timber.d("heartrate uuid: ${heartRateService.uuid}")
//                }

//
////                it.getService(BODY_POS_SER_UUID)?.let { bodyPostureService ->
////                    bluetoothGattService = bodyPostureService
////                    readCharacteristic(bluetoothGattService.getCharacteristic(BODY_POS_SER_UUID))
////                }
////
////                it.getService(TEMP_SER_UUID)?.let { tempService ->
////                    bluetoothGattService = tempService
////                    readCharacteristic(bluetoothGattService.getCharacteristic(TEMP_SER_UUID))
////                }
//


        }

        override fun onCharacteristicChanged(gatt: BluetoothGatt?, characteristic: BluetoothGattCharacteristic?) {
            super.onCharacteristicChanged(gatt, characteristic)
            val messageString: String = try {
                characteristic?.value?.convertToString() ?: ""
            } catch (e: UnsupportedEncodingException) {
                Log.e(TAG, "Unable to convert message bytes to string")
                "Error"
            }
            Log.i(TAG, "characteristic value: $messageString")
            setValueToCharacteristic(characteristic)
        }

        override fun onCharacteristicWrite(gatt: BluetoothGatt?, characteristic: BluetoothGattCharacteristic?, status: Int) {
            super.onCharacteristicWrite(gatt, characteristic, status)
            Log.i(TAG, "onCharacteristicWrite status: $status")

            if (pendingOperation is CharacteristicWrite) {
                signalEndOfOperation()
            }
        }

        override fun onCharacteristicRead(gatt: BluetoothGatt?, characteristic: BluetoothGattCharacteristic?, status: Int) {
            super.onCharacteristicRead(gatt, characteristic, status)
            if (status != BluetoothGatt.GATT_SUCCESS) {
                return
            }

            Log.i(TAG, "onCharacteristicRead status: $status, value: ${String(characteristic?.value ?: byteArrayOf(0))}")
            setValueToCharacteristic(characteristic)

            if (pendingOperation is CharacteristicRead) {
                signalEndOfOperation()
            }

        }

        override fun onDescriptorRead(gatt: BluetoothGatt?, descriptor: BluetoothGattDescriptor?, status: Int) {
            super.onDescriptorRead(gatt, descriptor, status)

            if (pendingOperation is DescriptorRead) {
                signalEndOfOperation()
            }
        }

        override fun onDescriptorWrite(gatt: BluetoothGatt?, descriptor: BluetoothGattDescriptor?, status: Int) {
            super.onDescriptorWrite(gatt, descriptor, status)

            descriptor?.let {
                if (descriptor.isCccd() &&
                    (pendingOperation is EnableNotifications || pendingOperation is DisableNotifications)
                ) {
                    signalEndOfOperation()
                } else if (!descriptor.isCccd() && pendingOperation is DescriptorWrite) {
                    signalEndOfOperation()
                }
            }
        }
    }


    private fun setValueToCharacteristic(characteristic: BluetoothGattCharacteristic?) {
        connectedDevice.let { bleDevice ->
            if (bleDevice.services?.isEmpty() == true) {
                Log.i(TAG, "No services with characteristics found.")
                return
            }

            when (characteristic?.uuid) {
                HEART_RATE_CHAR_UUID -> {
                    characteristic?.let {
                        _heartRateCharacteristic.value = it.value

                        //check for heartrate threshold value
                    }
                    setCharacteristicNotification(HEART_RATE_CHAR_UUID)
                }
                DEVICE_BATTERY_CHAR_UUID -> {
                    characteristic?.let {
                        _deviceBattery.postValue(it.value.filter { it > 0 }.last().toInt()).also {
                            Timber.i("Battery ${it}")
                        }

                    }
                    setCharacteristicNotification(DEVICE_BATTERY_CHAR_UUID)
                }
                ECG_RATE_CHAR_UUID -> {
                    characteristic?.let {
                        _ecgCharacteristic.value = it.value
                    }
                    //setCharacteristicNotification(ECG_RATE_CHAR_UUID)
                }
                BODY_POS_CHAR_UUID -> {
                    characteristic?.let {
                        _bodyPosture.value = it.value.last().toString()
                    }
                    //setCharacteristicNotification(BODY_POS_CHAR_UUID)
                } else ->
                 {
                    characteristic?.let {
                        _bodyTemperature.value = it.value.last().toString()
                    }
                }

            }
//            _connectedBleDeviceLiveData.postValue(
//                BleDevice(bleDevice.device, connectionStatus = BleConnection.DeviceConnected))
        }
    }

    /**
     * Enables notification to start receiving updates from device
     */

    private fun setCharacteristicNotification(uuid: UUID) {

        bluetoothGatt.findCharacteristic(uuid)?.let { characteristic ->

            val payload = when {
                characteristic.isIndicatable() ->
                    BluetoothGattDescriptor.ENABLE_INDICATION_VALUE
                characteristic.isNotifiable() ->
                    BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
                else ->
                    error("${characteristic.uuid} doesn't support notifications/indications")
            }

            characteristic.getDescriptor(UUID.fromString(CCC_DESCRIPTOR_UUID))
                ?.let { cccDescriptor ->
                    if (!bluetoothGatt.setCharacteristicNotification(characteristic, true)) {
                        Timber.e("setCharacteristicNotification failed for ${characteristic.uuid}")
                        return
                    }
                    cccDescriptor.value = payload
                    bluetoothGatt.writeDescriptor(cccDescriptor)
                } ?: run {
                Timber.e("${characteristic.uuid} doesn't contain the CCC descriptor!")
                signalEndOfOperation()
            }
        }
        signalEndOfOperation()
    }

    fun disconnectGattServer(gatt: BluetoothGatt?, status: BleConnection) {
        connectedDevice.let {
            _connectedBleDeviceLiveData.postValue(
                BleDevice(it.device, services = it.services, connectionStatus = status))
        }
        _isDeviceConnected.value = false.also {
            Timber.d("BleManager device disconnected")
        }
        gatt?.disconnect()
        gatt?.close()
    }
}