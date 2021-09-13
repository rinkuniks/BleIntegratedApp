package com.technoidentity.vitalz.data.datamodel.docNurseLogin

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DocNurseRequest (
  @Expose
  @SerializedName("userId")
  var  username : String? = null,
  @Expose
  @SerializedName("password")
  var  password : String? = null
):Parcelable