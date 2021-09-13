package com.technoidentity.vitalz.bluetooth.connection

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattService
import android.content.Context
import androidx.lifecycle.LiveData
import com.technoidentity.vitalz.bluetooth.data.BleDevice
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.StateFlow
import java.util.*

@ViewModelScoped
interface IBleManager {

        /**
         * Bluetooth device data
         */

        fun startScan()
        fun stopScan()
        var isScanning: StateFlow<Boolean>
        var connectedBleDeviceLiveData: LiveData<BleDevice>
        var battery: LiveData<Int>
        var isDeviceConnected: StateFlow<Boolean>
        var scanChannel: Channel<BluetoothDevice>
        fun connectDevice(device: BluetoothDevice, context: Context)
        fun readCharacteristic(device: BluetoothDevice, uuid: UUID, service: BluetoothGattService)
        fun enableNotifications(device: BluetoothDevice, characteristic: BluetoothGattCharacteristic)

        /**
         * Patient Data
         */
        var heartRateCharacteristic: StateFlow<ByteArray>
        var ecgCharacteristic: StateFlow<ByteArray>
        var bodyPosture: LiveData<String>
        var bodyTemperature: LiveData<String>
    }
