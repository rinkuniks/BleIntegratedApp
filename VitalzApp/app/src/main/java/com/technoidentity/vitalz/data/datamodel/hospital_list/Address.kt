package com.technoidentity.vitalz.data.datamodel.hospital_list

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Address(
    @Expose
    @SerializedName("city")
    val city: String?,
    @Expose
    @SerializedName("state")
    val state: String?,
    @Expose
    @SerializedName("street")
    val street: String?,
    @Expose
    @SerializedName("zipCode")
    val zipCode: String?
):Parcelable