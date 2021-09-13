package com.technoidentity.vitalz.user

import androidx.lifecycle.*
import com.technoidentity.vitalz.data.datamodel.careTakerLogin.CareTakerOtpResponse
import com.technoidentity.vitalz.data.datamodel.careTakerLogin.CareTakerRequest
import com.technoidentity.vitalz.data.repository.UserRepositoryImpl
import com.technoidentity.vitalz.utils.CoroutinesDispatcherProvider
import com.technoidentity.vitalz.utils.ResultHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CareTakerMobileViewModel @Inject constructor(
    private val userRepositoryImpl: UserRepositoryImpl,
    private val dispatcher: CoroutinesDispatcherProvider
) : ViewModel() {

    fun getCareTakerResponse(mobile: String): LiveData<CareTakerOtpResponse> {
        val request = CareTakerRequest()
        request.phoneNo = mobile
        return liveData {
            emit(userRepositoryImpl.doMobileOTPCall(request))
        }
    }
}
