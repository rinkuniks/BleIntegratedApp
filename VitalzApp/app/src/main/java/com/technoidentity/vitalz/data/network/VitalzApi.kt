package com.technoidentity.vitalz.data.network

import com.technoidentity.vitalz.bluetooth.data.BleMac
import com.technoidentity.vitalz.bluetooth.data.RegisteredDevice
import com.technoidentity.vitalz.data.datamodel.SearchHospitalRequest
import com.technoidentity.vitalz.data.datamodel.careTakerLogin.CareTakerOtpResponse
import com.technoidentity.vitalz.data.datamodel.careTakerLogin.CareTakerRequest
import com.technoidentity.vitalz.data.datamodel.dashboardDetail.DashboardDetailResponse
import com.technoidentity.vitalz.data.datamodel.dashboardDetail.DashboardDetailsRequest
import com.technoidentity.vitalz.data.datamodel.docNurseLogin.DocNurseRequest
import com.technoidentity.vitalz.data.datamodel.docNurseLogin.DocNurseResponse
import com.technoidentity.vitalz.data.datamodel.hospital_list.HospitalListData
import com.technoidentity.vitalz.data.datamodel.hospital_list.HospitalListRequest
import com.technoidentity.vitalz.data.datamodel.multiple_patient.MultiplePatientDashboardResponse
import com.technoidentity.vitalz.data.datamodel.notification.NotificationCareTakerRequest
import com.technoidentity.vitalz.data.datamodel.notification.NotificationDoctorRequest
import com.technoidentity.vitalz.data.datamodel.notification.NotificationResponse
import com.technoidentity.vitalz.data.datamodel.otp.OtpRequest
import com.technoidentity.vitalz.data.datamodel.otp.OtpResponse
import com.technoidentity.vitalz.data.datamodel.patient_list.PatientDataList
import com.technoidentity.vitalz.data.datamodel.patient_list.PatientRequest
import com.technoidentity.vitalz.data.datamodel.single_patient.SinglePatientDashboardResponse
import com.technoidentity.vitalz.data.datamodel.updateProfile.ProfileUpdateRequest
import com.technoidentity.vitalz.data.datamodel.updateProfile.ProfileUpdateResponse
import com.technoidentity.vitalz.data.datamodel.updatedregistereddevice.UpdateRegisteredDeviceRequest
import com.technoidentity.vitalz.data.datamodel.updatedregistereddevice.UpdatedRegisteredDeviceResponse
import com.technoidentity.vitalz.data.network.Urls.CARETAKER_LOGIN
import com.technoidentity.vitalz.data.network.Urls.CARE_TAKER_NOTIFICATION
import com.technoidentity.vitalz.data.network.Urls.DASHBOARD_DETAIL
import com.technoidentity.vitalz.data.network.Urls.DOCTOR_NOTIFICATION
import com.technoidentity.vitalz.data.network.Urls.DOC_NURSE_LOGIN
import com.technoidentity.vitalz.data.network.Urls.GET_DEVICE_LIST
import com.technoidentity.vitalz.data.network.Urls.HOSPITAL_LIST
import com.technoidentity.vitalz.data.network.Urls.MULTIPLE_PATIENT_DASHBOARD
import com.technoidentity.vitalz.data.network.Urls.NURSE_NOTIFICATION
import com.technoidentity.vitalz.data.network.Urls.PATIENT_LIST
import com.technoidentity.vitalz.data.network.Urls.PROFILE_UPDATE
import com.technoidentity.vitalz.data.network.Urls.REGISTER_DEVICE
import com.technoidentity.vitalz.data.network.Urls.SEARCH_HOSPITAL
import com.technoidentity.vitalz.data.network.Urls.SEARCH_MULTI_PATIENT
import com.technoidentity.vitalz.data.network.Urls.SEARCH_PATIENT
import com.technoidentity.vitalz.data.network.Urls.SEND_OTP
import com.technoidentity.vitalz.data.network.Urls.SEND_TELEMETRY
import com.technoidentity.vitalz.data.network.Urls.SEND_TELEMETRY_NOTIFICATION
import com.technoidentity.vitalz.data.network.Urls.SINGLE_PATIENT_DASHBOARD
import com.technoidentity.vitalz.data.network.Urls.UPDATE_DEVICE
import com.technoidentity.vitalz.notifications.datamodel.PushNotification
import com.technoidentity.vitalz.notifications.datamodel.VitalzTelemetryNotification
import com.technoidentity.vitalz.utils.FirebaseConstants.CONTENT_TYPE
import com.technoidentity.vitalz.utils.FirebaseConstants.SERVER_KEY
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface VitalzApi {

    @POST(SEND_OTP)
    suspend fun getOTP(@Body request: CareTakerRequest) : CareTakerOtpResponse

    @POST(CARETAKER_LOGIN)
    suspend fun getLogin(@Body request: OtpRequest) : OtpResponse

    @POST(DOC_NURSE_LOGIN)
    suspend fun getDocNurseLogin(@Body request: DocNurseRequest) : DocNurseResponse

    @POST(HOSPITAL_LIST)
    suspend fun getHospitalList(@Body request: HospitalListRequest) : HospitalListData

    @POST(PATIENT_LIST)
    suspend fun getPatientList(@Body request: PatientRequest) : PatientDataList

    @GET(SINGLE_PATIENT_DASHBOARD)
    suspend fun getSinglePatientDashboardList(@Path("id") id: String) : Response<SinglePatientDashboardResponse>

    @GET(MULTIPLE_PATIENT_DASHBOARD)
    suspend fun getMultiplePatientDashboardList() : MultiplePatientDashboardResponse

    @POST(REGISTER_DEVICE)
    suspend fun sendDevicesForRegisteration(@Body request: BleMac): RegisteredDevice

    @GET(GET_DEVICE_LIST)
    suspend fun getRegisteredDevices(): List<RegisteredDevice>

    @PUT(UPDATE_DEVICE)
    suspend fun updateRegisteredDevice(@Body request: UpdateRegisteredDeviceRequest): UpdatedRegisteredDeviceResponse

    @POST(SEND_TELEMETRY)
    suspend fun sendHeartRate(patientId: String, telemetryKey: String, heartRate :List<Byte>): Boolean

    @POST(SEND_TELEMETRY)
    suspend fun sendEcgData(patientId: String, telemetryKey: String, ecgData :List<Byte>): Boolean

    @GET(SEARCH_MULTI_PATIENT)
    suspend fun searchMultiPatientList(@Path("parameter") parameter:String): MultiplePatientDashboardResponse

    @POST(SEARCH_HOSPITAL)
    suspend fun searchHospitalList(@Body request: SearchHospitalRequest): HospitalListData

    @GET(SEARCH_PATIENT)
    suspend fun searchPatientList(@Path("parameter") parameter:String): PatientDataList

    @Headers("Authorization: key=$SERVER_KEY", "Content-Type:$CONTENT_TYPE")
    @POST("fcm/send")
    suspend fun postNotification(
        @Body notification: PushNotification
    ): Response<ResponseBody>

    @POST(DOCTOR_NOTIFICATION)
    suspend fun getDoctorNotification(@Body request: NotificationDoctorRequest): NotificationResponse

    @GET(NURSE_NOTIFICATION)
    suspend fun getNurseNotification(): NotificationResponse

    @POST(CARE_TAKER_NOTIFICATION)
    suspend fun getCareTakerNotification(@Body request: NotificationCareTakerRequest): NotificationResponse

    @POST(PROFILE_UPDATE)
    suspend fun updateProfileData(@Body request: ProfileUpdateRequest): ProfileUpdateResponse

    @POST(DASHBOARD_DETAIL)
    suspend fun getDashboardDetailsList(@Body request: DashboardDetailsRequest) : DashboardDetailResponse

    @POST(SEND_TELEMETRY_NOTIFICATION)
    suspend fun sendTelemetryNotification(@Body request: VitalzTelemetryNotification): Boolean

}