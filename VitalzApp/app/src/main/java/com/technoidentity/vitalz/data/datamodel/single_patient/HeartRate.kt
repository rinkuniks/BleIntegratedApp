package com.technoidentity.vitalz.data.datamodel.single_patient

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize



@Parcelize
data class HeartRate(
    @Expose
    @SerializedName("date")
    val date: String,
    @Expose
    @SerializedName("ratePerMinute")
    val ratePerMinute: List<Int>
):Parcelable {
     fun changeIntToFloat(ratePerMinute: List<Int>) = ratePerMinute
}