package com.technoidentity.vitalz.data.datamodel.otp

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class OtpResponse(
    @Expose
    @SerializedName("token")
    val token: String?,
    @Expose
    @SerializedName("user")
    val user: User?
):Parcelable