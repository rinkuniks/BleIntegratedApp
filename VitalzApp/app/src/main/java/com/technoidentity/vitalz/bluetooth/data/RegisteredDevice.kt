package com.technoidentity.vitalz.bluetooth.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Parcelize
data class RegisteredDevice(
    @SerializedName("patchId")
    val patchId: String = "Invalid_Patch",
    @SerializedName("macId")
    val macId: String= "macId",
    var message: String= "Invalid device"
): Parcelable
