package com.technoidentity.vitalz.data.datamodel.notification

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class NotificationCareTakerRequest {
    @Expose
    @SerializedName("patientId")
    var  patientId : String? = null
}