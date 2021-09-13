package com.technoidentity.vitalz.hospital

import androidx.lifecycle.*
import com.technoidentity.vitalz.data.datamodel.multiple_patient.MultiplePatientDashboardResponse
import com.technoidentity.vitalz.data.datamodel.patient_list.PatientDataList
import com.technoidentity.vitalz.data.datamodel.patient_list.PatientRequest
import com.technoidentity.vitalz.data.repository.UserRepositoryImpl
import com.technoidentity.vitalz.utils.CoroutinesDispatcherProvider
import com.technoidentity.vitalz.utils.ResultHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PatientViewModel @Inject constructor(
    private val userRepositoryImpl: UserRepositoryImpl,
    private val dispatcher: CoroutinesDispatcherProvider
) : ViewModel() {

    fun getPatientListData(mobile: String, hospitalId: String): LiveData<PatientDataList> {
        val request = PatientRequest()
        request.hospitalId = hospitalId
        request.phoneNo = mobile
        return liveData {
            emit(userRepositoryImpl.getPatientList(request))
        }
    }

    fun searchPatientInList(text: CharSequence) : LiveData<PatientDataList>{
        return liveData {
            emit(userRepositoryImpl.searchPatientList(text.toString()))
        }
    }
}
