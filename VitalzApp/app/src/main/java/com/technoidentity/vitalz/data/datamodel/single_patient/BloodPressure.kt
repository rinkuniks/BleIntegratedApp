package com.technoidentity.vitalz.data.datamodel.single_patient

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BloodPressure(
    @Expose
    @SerializedName("date")
    val date: String,
    @Expose
    @SerializedName("diastolicPressure")
    val diastolicPressure: List<Int>,
    @Expose
    @SerializedName("systolicPressure")
    val systolicPressure: List<Int>
): Parcelable