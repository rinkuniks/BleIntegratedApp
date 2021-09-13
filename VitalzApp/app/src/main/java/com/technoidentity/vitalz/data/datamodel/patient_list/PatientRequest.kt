package com.technoidentity.vitalz.data.datamodel.patient_list

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PatientRequest (
    @Expose
    @SerializedName("hospitalId")
    var hospitalId: String? = null,
    @Expose
    @SerializedName("phoneNo")
    var phoneNo: String? = null
):Parcelable