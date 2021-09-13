package com.technoidentity.vitalz.data.datamodel.docNurseLogin

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DocNurseResponse(
    @Expose
    @SerializedName("token")
    val token: String?,
    @Expose
    @SerializedName("user")
    val user: User?,
    @Expose
    @SerializedName("statusCode")
    val statusCode: String? = null,
    @Expose
    @SerializedName("message")
    val message: String? = null
):Parcelable