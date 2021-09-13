package com.technoidentity.vitalz.data.datamodel.dashboardDetail

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DashboardDetailResponse(
    @Expose
    @SerializedName("itemData")
    val itemData: List<ItemData>,
    @Expose
    @SerializedName("patientId")
    val patientId: String,
    @Expose
    @SerializedName("weeklyAverage")
    val weeklyAverage: Int,
    @Expose
    @SerializedName("weeklyMax")
    val weeklyMax: Int,
    @Expose
    @SerializedName("weeklyMin")
    val weeklyMin: Int
)