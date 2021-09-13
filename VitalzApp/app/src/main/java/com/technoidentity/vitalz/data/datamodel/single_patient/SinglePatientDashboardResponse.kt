package com.technoidentity.vitalz.data.datamodel.single_patient

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

sealed class SinglePatientData

@Parcelize
data class SinglePatientDashboardResponse(
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
):Parcelable, SinglePatientData()

@Parcelize
data class SinglePatientBleData(
    @Expose
    @SerializedName("address")
    val address: String? = null,
    @Expose
    @SerializedName("age")
    val age: Int? = null,
    @Expose
    @SerializedName("bloodPressure")
    val bloodPressure: BloodPressure? = null,
    @Expose
    @SerializedName("contactNumber")
    val contactNumber: String? = null,
    @Expose
    @SerializedName("device")
    val device: Device? = null,
    @Expose
    @SerializedName("doctorContactNumber")
    val doctorContactNumber: String? = null,
    @Expose
    @SerializedName("doctorName")
    val doctorName: String? = null,
    @Expose
    @SerializedName("emergencyContactName")
    val emergencyContactName: String? = null,
    @Expose
    @SerializedName("emergencyContactNumber")
    val emergencyContactNumber: String? = null,
    @Expose
    @SerializedName("gender")
    val gender: String? = null,
    
    @Expose
    @SerializedName("height")
    val height: Double? = null,
    @Expose
    @SerializedName("hospitalId")
    val hospitalId: String? = null,
    @Expose
    @SerializedName("name")
    val name: String? = null,
    @Expose
    @SerializedName("otherHealthIssues")
    val otherHealthIssues: String? = null,
    
):Parcelable, SinglePatientData()