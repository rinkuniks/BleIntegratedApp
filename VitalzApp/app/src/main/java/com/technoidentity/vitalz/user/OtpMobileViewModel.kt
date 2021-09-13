package com.technoidentity.vitalz.user

import androidx.lifecycle.*
import com.technoidentity.vitalz.data.datamodel.otp.OtpRequest
import com.technoidentity.vitalz.data.datamodel.otp.OtpResponse
import com.technoidentity.vitalz.data.repository.UserRepositoryImpl
import com.technoidentity.vitalz.utils.CoroutinesDispatcherProvider
import com.technoidentity.vitalz.utils.ResultHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OtpMobileViewModel @Inject constructor(
    private val userRepositoryImpl: UserRepositoryImpl,
    private val dispatcher: CoroutinesDispatcherProvider
) : ViewModel() {

    fun getOtpResponse(mobile: String, otpReceived: Int): LiveData<OtpResponse> {
        val request = OtpRequest()
        request.phoneNo = mobile
        request.otp = otpReceived
        return liveData {
            emit(userRepositoryImpl.doOTPSendCall(request))
        }
    }
}
