package com.technoidentity.vitalz.data.datamodel.dashboardDetail

import com.google.gson.annotations.SerializedName

data class ItemData(
    @SerializedName("average")
    val average: Int,
    @SerializedName("date")
    val date: String
)