package com.technoidentity.vitalz.bluetooth.data

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattService
import com.technoidentity.vitalz.bluetooth.connection.BleConnection


data class BleDevice(
    val device: BluetoothDevice,
    val gatt: BluetoothGatt? = null,
    val services: List<BluetoothGattService>? = emptyList(),
    var connectionStatus: BleConnection = BleConnection.DeviceDisconnected
)








