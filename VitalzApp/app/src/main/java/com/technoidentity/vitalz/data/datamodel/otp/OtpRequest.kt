package com.technoidentity.vitalz.data.datamodel.otp

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class OtpRequest(
    @Expose
    @SerializedName("phoneNo")
    var phoneNo: String? = null,
    @Expose
    @SerializedName("otp")
    var otp: Int? = null
) : Parcelable