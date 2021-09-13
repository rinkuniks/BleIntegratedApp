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
import com.technoidentity.vitalz.data.network.VitalzApi
import com.technoidentity.vitalz.utils.ResultHandler
import com.technoidentity.vitalz.utils.ResultHandler.Error
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val api: VitalzApi
) : UserRepository {
    override suspend fun doMobileOTPCall(request: CareTakerRequest): CareTakerOtpResponse {
        return kotlin.runCatching {
            api.getOTP(request)
        }.getOrThrow()
    }

    override suspend fun doOTPSendCall(otpRequest: OtpRequest): OtpResponse {
        return kotlin.runCatching {
            api.getLogin(otpRequest)
        }.getOrThrow()
    }

    override suspend fun sendDocNurseCredentials(docNurseLogin: DocNurseRequest): DocNurseResponse {
        return kotlin.runCatching {
            api.getDocNurseLogin(docNurseLogin)
        }.getOrThrow()
    }

    override suspend fun getHospitalList(mobile: HospitalListRequest): HospitalListData {
        return kotlin.runCatching {
            api.getHospitalList(mobile)
        }.getOrThrow()
    }

    override suspend fun getPatientList(request: PatientRequest): PatientDataList {
        return kotlin.runCatching {
            api.getPatientList(request)
        }.getOrThrow()
    }

    override suspend fun getSinglePatientDashboardList(id: String): ResultHandler<SinglePatientDashboardResponse>? {
        val response = api.getSinglePatientDashboardList(id)
        return try {
            response.let { it ->
                if (it.isSuccessful) {
                    it.body()?.let {
                        ResultHandler.Success(it)
                    }
                } else {
                    Error(response.message())
                }
            }
        } catch (e: Exception) {
            Error(e.message ?: "Contact Admin")
        }
    }

    override suspend fun getMultiplePatientDashboardList(): MultiplePatientDashboardResponse {
        return kotlin.runCatching {
            api.getMultiplePatientDashboardList()
        }.getOrThrow()
    }

    override suspend fun searchMultiplePatientDashboardList(request: String): MultiplePatientDashboardResponse {
        return kotlin.runCatching {
            api.searchMultiPatientList(request)
        }.getOrThrow()
    }

    override suspend fun searchHospitalList(request: SearchHospitalRequest): HospitalListData {
        return kotlin.runCatching {
            api.searchHospitalList(request)
        }.getOrThrow()
    }

    override suspend fun searchPatientList(request: String): PatientDataList {
        return kotlin.runCatching {
            api.searchPatientList(request)
        }.getOrThrow()
    }

    override suspend fun getNotificationCareTakerList(request: NotificationCareTakerRequest): NotificationResponse {
        return kotlin.runCatching {
            api.getCareTakerNotification(request)
        }.getOrThrow()
    }

    override suspend fun getNotificationDoctorList(request: NotificationDoctorRequest): NotificationResponse {
        return kotlin.runCatching {
            api.getDoctorNotification(request)
        }.getOrThrow()
    }

    override suspend fun getNotificationNurseList(): NotificationResponse {
        return kotlin.runCatching {
            api.getNurseNotification()
        }.getOrThrow()
    }

    override suspend fun updatePatientData(request: ProfileUpdateRequest): ProfileUpdateResponse {
        return kotlin.runCatching {
            api.updateProfileData(request)
        }.getOrThrow()
    }

    override suspend fun getDashboardDetailsList(request: DashboardDetailsRequest): DashboardDetailResponse {
        return kotlin.runCatching {
            api.getDashboardDetailsList(request)
        }.getOrThrow()
    }
}
