package com.technoidentity.vitalz.data.datamodel.careTakerLogin

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CareTakerRequest (
  @Expose
  @SerializedName("phoneNo")
  var  phoneNo : String? = null
): Parcelable