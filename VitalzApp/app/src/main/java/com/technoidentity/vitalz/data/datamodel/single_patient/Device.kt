package com.technoidentity.vitalz.data.datamodel.single_patient

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Device(
    @Expose
    @SerializedName("betteryPercentage")
    val batteryPercentage: Double,
    @Expose
    @SerializedName("connection")
    val connection: String,
    @Expose
    @SerializedName("patchId")
    val patchId: String,
    @Expose
    @SerializedName("status")
    val status: String
): Parcelable