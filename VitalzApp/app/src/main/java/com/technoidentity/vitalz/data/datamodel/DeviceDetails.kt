package com.technoidentity.vitalz.data.datamodel

import android.bluetooth.BluetoothDevice
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DeviceDetails(
    val patchId: Int,
    val deviceStatus: DeviceStatus,
    val connection: String
) : Parcelable

fun deviceDetails(device: BluetoothDevice) {

}

@Parcelize
data class DeviceStatus(
    val active: String,
    val inActive: String
) : Parcelable


