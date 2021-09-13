package com.technoidentity.vitalz.data.datamodel.hospital_list

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class HospitalListDataItem(
    @Expose
    @SerializedName("address")
    val address: Address?,
    @Expose
    @SerializedName("hospitalName")
    val hospitalName: String?,
    @Expose
    @SerializedName("id")
    val id: String,
    @Expose
    @SerializedName("status")
    val status: Boolean
):Parcelable