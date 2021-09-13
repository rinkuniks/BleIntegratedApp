package com.technoidentity.vitalz.data.datamodel.patient_list

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PatientDataListItem(
    @Expose
    @SerializedName("address")
    val address: String?,
    @Expose
    @SerializedName("age")
    val age: Int?,
    @Expose
    @SerializedName("gender")
    val gender: String?,
    @Expose
    @SerializedName("patientId")
    val id: String?,
    @Expose
    @SerializedName("name")
    val name: String?
):Parcelable