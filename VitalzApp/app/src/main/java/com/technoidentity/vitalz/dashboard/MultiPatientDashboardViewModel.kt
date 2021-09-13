package com.technoidentity.vitalz.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.technoidentity.vitalz.data.datamodel.multiple_patient.MultiplePatientDashboardResponse
import com.technoidentity.vitalz.data.repository.UserRepositoryImpl
import com.technoidentity.vitalz.utils.CoroutinesDispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MultiPatientDashboardViewModel @Inject constructor(
    private val userRepositoryImpl: UserRepositoryImpl,
    private val dispatcher: CoroutinesDispatcherProvider
) : ViewModel() {

    fun getMultiplePatientData(): LiveData<MultiplePatientDashboardResponse> {
        return liveData {
            emit(userRepositoryImpl.getMultiplePatientDashboardList())
        }
    }

    fun searchPatientInList(text: CharSequence): LiveData<MultiplePatientDashboardResponse>{
        return liveData {
            emit(userRepositoryImpl.searchMultiplePatientDashboardList(text.toString()))
        }
    }
}

