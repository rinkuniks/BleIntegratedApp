package com.technoidentity.vitalz.data.datamodel.docNurseLogin

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    @Expose
    @SerializedName("email")
    val email: String,
    @Expose
    @SerializedName("id")
    val id: String,
    @Expose
    @SerializedName("name")
    val name: String,
    @Expose
    @SerializedName("phoneExt")
    val phoneExt: String,
    @Expose
    @SerializedName("phoneNo")
    val phoneNo: String,
    @Expose
    @SerializedName("role")
    val role: String
):Parcelable