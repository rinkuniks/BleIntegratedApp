package com.technoidentity.vitalz.data.datamodel.multiple_patient

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Posture(
    @Expose
    @SerializedName("bodyPosture")
    val bodyPosture: List<String>,
    @Expose
    @SerializedName("date")
    val date: String
): Parcelable