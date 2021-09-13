package com.technoidentity.vitalz.data.datamodel.notification

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class NotificationDoctorRequest {
    @Expose
    @SerializedName("phoneNo")
    var  phoneNo : String? = null
}