package com.technoidentity.vitalz.dashboard

import androidx.lifecycle.*
import com.technoidentity.vitalz.data.datamodel.single_patient.SinglePatientDashboardResponse
import com.technoidentity.vitalz.data.datamodel.updatedregistereddevice.UpdateRegisteredDeviceRequest
import com.technoidentity.vitalz.data.datamodel.updatedregistereddevice.UpdatedRegisteredDeviceResponse
import com.technoidentity.vitalz.data.repository.DeviceRepository
import com.technoidentity.vitalz.data.repository.PatientRepository
import com.technoidentity.vitalz.data.repository.UserRepositoryImpl
import com.technoidentity.vitalz.notifications.datamodel.VitalzTelemetryNotification
import com.technoidentity.vitalz.utils.CoroutinesDispatcherProvider
import com.technoidentity.vitalz.utils.ResultHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SinglePatientDashboardViewModel @Inject constructor(
    private val userRepositoryImpl: UserRepositoryImpl,
    private val deviceRepository: DeviceRepository,
    private val patientRepository: PatientRepository
) : ViewModel() {

    sealed class SinglePatient {
        class Success(val resultText: String, val data: SinglePatientDashboardResponse) : SinglePatient()
        class Failure(val errorText: String) : SinglePatient()
        object Loading : SinglePatient()
        object Empty : SinglePatient()
    }

    private val _updateDevice = MutableLiveData<UpdatedRegisteredDeviceResponse>()
    val updateDevice: LiveData<UpdatedRegisteredDeviceResponse> = _updateDevice

    fun updateDevice(updateRegisteredDeviceRequest: UpdateRegisteredDeviceRequest){
        viewModelScope.launch {
            _updateDevice.value = deviceRepository.updateDevice(updateRegisteredDeviceRequest)
        }

    }

    private val _singlePatientData = MutableLiveData<SinglePatient>(
        SinglePatient.Empty)
    val singlePatientData: LiveData<SinglePatient> = _singlePatientData

    fun getSinglePatientData(patientId: String) {

        viewModelScope.launch{
            _singlePatientData.value = SinglePatient.Loading
            when (val response = userRepositoryImpl.getSinglePatientDashboardList(patientId)) {
                is ResultHandler.Error -> {
                    _singlePatientData.value =
                        SinglePatient.Failure(response.message.toString())
                    }
                is ResultHandler.Success -> {
                    if (response.data == null) {
                        _singlePatientData.value = SinglePatient.Failure("Unexpected Error")
                    } else {
                        _singlePatientData.value = SinglePatient.Success(resultText = "Single Patient Dashboard Data",data = response.data)
                    }
                }
            }
        }
    }

    fun sendTelemetryNotification(notification: VitalzTelemetryNotification) : Boolean? {
        return liveData {
            emit(patientRepository.sendTelemetryNotification(notification))
        }.value
    }
}
