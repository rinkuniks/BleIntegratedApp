package com.technoidentity.vitalz.data.datamodel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class SearchHospitalRequest {
    @Expose
    @SerializedName("phoneNo")
    var phoneNo: String? = null
    @Expose
    @SerializedName("hospitalName")
    var hospitalName: String? = null
}