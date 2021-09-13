package com.technoidentity.vitalz.data.repository

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
import com.technoidentity.vitalz.utils.ResultHandler

interface UserRepository {

    suspend fun doMobileOTPCall(mobile: CareTakerRequest): CareTakerOtpResponse

    suspend fun doOTPSendCall(otpRequest: OtpRequest): OtpResponse

    suspend fun sendDocNurseCredentials(docNurseLogin: DocNurseRequest): DocNurseResponse

    suspend fun getHospitalList(mobile: HospitalListRequest): HospitalListData

    suspend fun getPatientList(request: PatientRequest): PatientDataList

    suspend fun getSinglePatientDashboardList(id: String): ResultHandler<SinglePatientDashboardResponse>?

    suspend fun getMultiplePatientDashboardList(): MultiplePatientDashboardResponse

    suspend fun searchMultiplePatientDashboardList(request: String): MultiplePatientDashboardResponse

    suspend fun searchHospitalList(request: SearchHospitalRequest): HospitalListData

    suspend fun searchPatientList(request: String): PatientDataList

    suspend fun getNotificationCareTakerList(request:NotificationCareTakerRequest): NotificationResponse

    suspend fun getNotificationDoctorList(request: NotificationDoctorRequest): NotificationResponse

    suspend fun getNotificationNurseList(): NotificationResponse

    suspend fun updatePatientData(request: ProfileUpdateRequest): ProfileUpdateResponse

    suspend fun getDashboardDetailsList(request: DashboardDetailsRequest): DashboardDetailResponse
}