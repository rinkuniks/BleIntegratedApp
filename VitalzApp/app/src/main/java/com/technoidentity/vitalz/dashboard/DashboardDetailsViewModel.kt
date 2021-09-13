package com.technoidentity.vitalz.dashboard

import androidx.lifecycle.*
import com.technoidentity.vitalz.data.datamodel.careTakerLogin.CareTakerRequest
import com.technoidentity.vitalz.data.datamodel.dashboardDetail.DashboardDetailResponse
import com.technoidentity.vitalz.data.datamodel.dashboardDetail.DashboardDetailsRequest
import com.technoidentity.vitalz.data.datamodel.multiple_patient.MultiplePatientDashboardResponse
import com.technoidentity.vitalz.data.repository.UserRepository
import com.technoidentity.vitalz.data.repository.UserRepositoryImpl
import com.technoidentity.vitalz.utils.CoroutinesDispatcherProvider
import com.technoidentity.vitalz.utils.ResultHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardDetailsViewModel @Inject constructor(
    private val userRepositoryImpl: UserRepositoryImpl,
    private val dispatcher: CoroutinesDispatcherProvider
) : ViewModel() {

    fun getDashboardDetailsData(patientId: String, item: String, startDate: String, endDate: String): LiveData<DashboardDetailResponse> {
        val request = DashboardDetailsRequest(
            patientId = patientId,
            item = item,
            startDate = startDate,
            endDate = endDate)
        return liveData {
            emit(userRepositoryImpl.getDashboardDetailsList(request))
        }
    }
}
