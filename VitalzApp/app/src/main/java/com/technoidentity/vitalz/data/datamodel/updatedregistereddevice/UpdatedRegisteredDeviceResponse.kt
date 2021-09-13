package com.technoidentity.vitalz.data.datamodel.updatedregistereddevice

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class UpdatedRegisteredDeviceResponse(

    @Expose
    @SerializedName("patientId")
    val patientId: String,

    @Expose
    @SerializedName("patientName")
    val patientName: String
): Parcelable
