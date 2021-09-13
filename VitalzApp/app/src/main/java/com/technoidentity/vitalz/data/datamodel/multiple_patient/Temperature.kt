package com.technoidentity.vitalz.data.datamodel.multiple_patient

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Temperature(
    @Expose
    @SerializedName("bodyTemprature")
    val bodyTemperature: List<Double>,
    @Expose
    @SerializedName("date")
    val date: String
):Parcelable