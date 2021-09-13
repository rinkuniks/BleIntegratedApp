package com.technoidentity.vitalz.data.datamodel.dashboardDetail

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DashboardDetailsRequest(
    @Expose
    @SerializedName("endDate")
    val endDate: String,
    @Expose
    @SerializedName("item")
    val item: String,
    @Expose
    @SerializedName("patientId")
    val patientId: String,
    @Expose
    @SerializedName("startDate")
    val startDate: String
): Parcelable