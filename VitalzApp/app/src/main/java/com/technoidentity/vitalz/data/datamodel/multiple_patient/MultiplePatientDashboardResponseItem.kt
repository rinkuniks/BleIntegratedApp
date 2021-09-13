package com.technoidentity.vitalz.data.datamodel.multiple_patient

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class MultiplePatientDashboardResponseItem(
    @Expose
    @SerializedName("address")
    val address: String,
    @Expose
    @SerializedName("age")
    val age: Int,
    @Expose
    @SerializedName("bloodPressure")
    val bloodPressure: BloodPressure,
    @Expose
    @SerializedName("contactNumber")
    val contactNumber: String,
    @Expose
    @SerializedName("device")
    val device: Device,
    @Expose
    @SerializedName("doctorContactNumber")
    val doctorContactNumber: String,
    @Expose
    @SerializedName("doctorName")
    val doctorName: String,
    @Expose
    @SerializedName("emergencyContactName")
    val emergencyContactName: String,
    @Expose
    @SerializedName("emergencyContactNumber")
    val emergencyContactNumber: String,
    @Expose
    @SerializedName("gender")
    val gender: String,
    @Expose
    @SerializedName("heartRate")
    val heartRate: HeartRate,
    @Expose
    @SerializedName("height")
    val height: Double,
    @Expose
    @SerializedName("hospitalId")
    val hospitalId: String,
    @Expose
    @SerializedName("name")
    val name: String,
    @Expose
    @SerializedName("otherHealthIssues")
    val otherHealthIssues: String,
    @Expose
    @SerializedName("oxygenSaturation")
    val oxygenSaturation: OxygenSaturation,
    @Expose
    @SerializedName("patientId")
    val patientId: String,
    @Expose
    @SerializedName("posture")
    val posture: Posture,
    @Expose
    @SerializedName("respiratoryRate")
    val respiratoryRate: RespiratoryRate,
    @Expose
    @SerializedName("step")
    val step: Step,
    @Expose
    @SerializedName("temprature")
    val temperature: Temperature,
    @Expose
    @SerializedName("weight")
    val weight: Weight
): Parcelable