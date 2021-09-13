package com.technoidentity.vitalz.data.datamodel.updateProfile

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class ProfileUpdateRequest(
    @Expose
    @SerializedName("patientName")
    var patientName: String? = null,
    @Expose
    @SerializedName("age")
    var age: String? = null,
    @Expose
    @SerializedName("gender")
    var gender: String? = null,
    @Expose
    @SerializedName("height")
    var height: String? = null,
    @Expose
    @SerializedName("weight")
    var weight: String? = null,
    @Expose
    @SerializedName("address")
    var address: String? = null,
    @Expose
    @SerializedName("contactNumber")
    var phoneNumber: String? = null,
    @Expose
    @SerializedName("emergencyContactName")
    var emergencyContactName: String? = null,
    @Expose
    @SerializedName("emergencyContactNumber")
    var emergencyContactNumber: String? = null,
    @Expose
    @SerializedName("doctorName")
    var attendingDoctor: String? = null,
    @Expose
    @SerializedName("hospitalId")
    var hospitalId: String? = null,

    @Expose
    @SerializedName("patchId")
    var patchId: String? = null
) : Parcelable