package com.technoidentity.vitalz.data.datamodel.updateProfile

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProfileUpdateResponse(
    @Expose
    @SerializedName("reason")
    val reason: String?,
    @Expose
    @SerializedName("success")
    val success: Boolean?
) : Parcelable