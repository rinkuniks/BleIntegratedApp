package com.technoidentity.vitalz.user

import android.util.Log
import androidx.lifecycle.*
import com.technoidentity.vitalz.data.datamodel.docNurseLogin.DocNurseRequest
import com.technoidentity.vitalz.data.datamodel.docNurseLogin.DocNurseResponse
import com.technoidentity.vitalz.data.network.VitalzService
import com.technoidentity.vitalz.data.repository.UserRepositoryImpl
import com.technoidentity.vitalz.utils.CoroutinesDispatcherProvider
import com.technoidentity.vitalz.utils.ResultHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DoctorNurseLoginViewModel @Inject constructor(
    private val userRepositoryImpl: UserRepositoryImpl,
    private val dispatcher: CoroutinesDispatcherProvider
) : ViewModel() {
    fun sendDocNurseCredentials(username: String, password: String): LiveData<DocNurseResponse> {
        val request = DocNurseRequest()
        request.username = username
        request.password = password
        return liveData {
            emit(userRepositoryImpl.sendDocNurseCredentials(request))
        }
    }
}
