package com.technoidentity.vitalz.data.repository

import com.technoidentity.vitalz.data.datamodel.careTakerLogin.CareTakerOtpResponse
import com.technoidentity.vitalz.data.datamodel.careTakerLogin.CareTakerRequest
import com.technoidentity.vitalz.data.datamodel.dashboardDetail.DashboardDetailResponse
import com.technoidentity.vitalz.data.datamodel.dashboardDetail.DashboardDetailsRequest
import com.technoidentity.vitalz.data.datamodel.docNurseLogin.DocNurseResponse
import com.technoidentity.vitalz.data.datamodel.docNurseLogin.DocNurseRequest
import com.technoidentity.vitalz.data.datamodel.hospital_list.HospitalListData
import com.technoidentity.vitalz.data.datamodel.hospital_list.HospitalListRequest
import com.technoidentity.vitalz.data.datamodel.multiple_patient.MultiplePatientDashboardResponse
import com.technoidentity.vitalz.data.datamodel.otp.OtpRequest
import com.technoidentity.vitalz.data.datamodel.otp.OtpResponse
import com.technoidentity.vitalz.data.datamodel.patient_list.PatientDataList
import com.technoidentity.vitalz.data.datamodel.patient_list.PatientRequest
import com.technoidentity.vitalz.data.datamodel.single_patient.SinglePatientDashboardResponse
import com.technoidentity.vitalz.utils.ResultHandler

interface MainRepository {

    suspend fun doMobileOTPCall(mobile: CareTakerRequest): CareTakerOtpResponse

    suspend fun doOTPSendCall(otpRequest: OtpRequest): ResultHandler<OtpResponse>?

    suspend fun sendDocNurseCredentials(docNurseLogin: DocNurseRequest): ResultHandler<DocNurseResponse>?

    suspend fun getHospitalList(mobile: HospitalListRequest): ResultHandler<HospitalListData>?

    suspend fun getPatientList(request: PatientRequest): ResultHandler<PatientDataList>?

    suspend fun getSinglePatientDashboardList(id: String): ResultHandler<SinglePatientDashboardResponse>?

    suspend fun getMultiplePatientDashboardList(): ResultHandler<MultiplePatientDashboardResponse>?

    suspend fun getDashboardDetailsList(request: DashboardDetailsRequest): DashboardDetailResponse
}