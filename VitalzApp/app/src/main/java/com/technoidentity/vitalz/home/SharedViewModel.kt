package com.technoidentity.vitalz.home

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattService
import android.content.Context
import androidx.lifecycle.*
import com.technoidentity.vitalz.bluetooth.connection.IBleManager
import com.technoidentity.vitalz.bluetooth.data.BleDevice
import com.technoidentity.vitalz.bluetooth.data.BleMac
import com.technoidentity.vitalz.bluetooth.data.RegisteredDevice
import com.technoidentity.vitalz.data.local.databaseEntities.HeartRateDb
import com.technoidentity.vitalz.data.repository.DeviceRepository
import com.technoidentity.vitalz.data.repository.PatientRepository
import com.technoidentity.vitalz.notifications.datamodel.VitalzTelemetryNotification
import com.technoidentity.vitalz.utils.HEART_RATE_DATA
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import timber.log.Timber
import java.util.*
import javax.inject.Inject
import kotlin.properties.Delegates

/**
 * This will be used for singlepatient or multipledashbaord
 */
@HiltViewModel
class SharedViewModel @Inject constructor(
    private val bleManager: IBleManager,
    private val deviceRepository: DeviceRepository,
    private val patientRepository: PatientRepository
) : ViewModel() {

    /**
     * Device connection livedata and functions
     */

    val connectedDeviceData: LiveData<BleDevice> = bleManager.connectedBleDeviceLiveData

    //To call in activity or fragment to dispaly device connectivity common to all screens
    var isDeviceConnected: LiveData<Boolean> = bleManager.isDeviceConnected.asLiveData().also {
        Timber.d("homeviewmodel ${it.value}")
    }

    var scanFlow: Flow<BluetoothDevice> = bleManager.scanChannel.receiveAsFlow()

    var deviceBattery: LiveData<Int> = bleManager.battery

    var isScanning = bleManager.isScanning.asLiveData()

    fun startScan() = bleManager.startScan()

    fun toggleScan() {
        bleManager.apply { if (isScanning.value) stopScan() else startScan() }
    }

    fun stopScan() = bleManager.stopScan()

    fun connectDevice(device: BluetoothDevice, context: Context) {

        bleManager.connectDevice(device, context)
    }

    var registeredDevice: RegisteredDevice? = null


    fun readCharacteristics(device: BluetoothDevice, uuid: UUID, service: BluetoothGattService) {
        viewModelScope.launch(Dispatchers.IO) {
            bleManager.readCharacteristic(device, uuid, service)
        }
    }

    fun enableNotifications(device: BluetoothDevice, characteristic: BluetoothGattCharacteristic) {
        bleManager.enableNotifications(device, characteristic)
    }

    fun deviceForRegisteration(deviceMacID: BleMac): RegisteredDevice? {

        viewModelScope.launch(Dispatchers.IO) {

            deviceRepository.registeredDevice(deviceMacID).apply {
                withContext(Dispatchers.Main) {
                    registeredDevice = this@apply
                    Timber.i("sharevm ${this@apply.patchId} ${this@apply.macId}")
                }
            }

        }
        return registeredDevice
    }

    fun registeredDevice(device: BluetoothDevice): Triple<Boolean, String?, String?> {

        registeredDevice?.let {
            return if (it.macId == device.address) {
                Triple(true, it.patchId, it.macId)
            } else {
                Triple(false, device.name, device.address)
            }
        }
        return Triple(false, device.name, device.address)
    }

    /**
     * Patient data livedata and functions
     */

    private var _updatePatientId: MutableLiveData<String> = MutableLiveData()
    val updatePatientId:LiveData<String> = _updatePatientId

    fun updatePatientId(patientId: String) {
        _updatePatientId.value = patientId
    }

    var heartRateData: Flow<ByteArray> = bleManager.heartRateCharacteristic

    private var _telemetryNotification = MutableLiveData<Pair<Boolean, String>>()
     val telemetryNotification: LiveData<Pair<Boolean, String>> = _telemetryNotification

    suspend fun sendTelemetryNotification(heartRate: String): Boolean {

            return patientRepository.sendTelemetryNotification(VitalzTelemetryNotification (
                    registeredDevice?.patchId, HEART_RATE_DATA, heartRate)).apply {
                        _telemetryNotification.value = Pair(this, heartRate)
            }
        }


    var dataToServer by Delegates.notNull<Boolean>()

    fun sendHeartRateToServer(patchId: String, telemetryKey: String, heartRate: List<Byte>) {

        viewModelScope.launch(Dispatchers.IO) {

            while (isActive) {
                // Data is failed to send to server
                    when (patientRepository.isHeartRateExists()) {
                        true -> {
                            patientRepository.getHeartRateDb().collect { heartRateDb ->
                                senData(
                                    heartRateDb.patchId,
                                    heartRateDb.telemetryKey,
                                    heartRateDb.heartRateValue
                                ).apply {
                                    require(this) {
                                        patientRepository.deleteHeartRateData(heartRateDb)
                                    }
                                }
                            }
                        }
                        false -> {
                            senData(patchId, telemetryKey, heartRate)
                            // sending data very 10 secs
                            delay(10000L)
                        }
                    }
                }
            }
        }


    private suspend fun senData(
        patchId: String,
        telemetryKey: String,
        heartRateValue: List<Byte>
    ): Boolean {

        patientRepository.sendHeartRate(patchId, telemetryKey, heartRateValue).apply {
            return if (!this) {
                dataToServer = false
                patientRepository.insertHeartRateDb(
                    HeartRateDb(
                        patchId = patchId,
                        telemetryKey = telemetryKey,
                        heartRateValue = heartRateValue
                    )
                )
                //start service to again send data in api
                // if returns true from api data
                //database.delete(heartRate)
                false
            } else {
                dataToServer = this // data sent to server
                true
            }
        }

    }

    var ecgData: Flow<ByteArray> = bleManager.ecgCharacteristic

    val bodyTemperature: LiveData<String> = bleManager.bodyTemperature

    val bodyPosture: LiveData<String> = bleManager.bodyPosture

    //Check User Selected What
    private var _isCareTaker = MutableLiveData(false)
    val isSelected: LiveData<Boolean> = _isCareTaker

    fun isCareTakerSelected(selected: Boolean) {
        _isCareTaker.value = selected
    }

    //check the role of User
    private var _role = MutableStateFlow("Un-Authorized")
    val assignedRole: MutableStateFlow<String> = _role

    fun checkRole(role: String) {
        _role.value = role
    }

    //check the Notification Count of User
    var notificationCount = MutableStateFlow(0)
}


