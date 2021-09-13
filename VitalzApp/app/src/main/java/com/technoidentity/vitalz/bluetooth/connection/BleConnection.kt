package com.technoidentity.vitalz.bluetooth.connection

import android.bluetooth.BluetoothDevice

sealed class BleConnection {

    object DeviceDisconnected : BleConnection()
    data class DeviceConnectionLoading(val device: BluetoothDevice) : BleConnection()
    object DeviceConnected : BleConnection()
    object DeviceConnectingError : BleConnection()
}
