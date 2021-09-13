package com.technoidentity.vitalz.data.datamodel.updatedregistereddevice

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class UpdateRegisteredDeviceRequest(

    @SerializedName("battery")
    val battery: String,

    @SerializedName("patchId")
    val patchId: String,

    @SerializedName("status")
    val status: String,

    @SerializedName("connection")
    val connection: String,

): Parcelable
