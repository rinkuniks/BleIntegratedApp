package com.technoidentity.vitalz.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.technoidentity.vitalz.data.datamodel.notification.NotificationCareTakerRequest
import com.technoidentity.vitalz.data.datamodel.notification.NotificationDoctorRequest
import com.technoidentity.vitalz.data.datamodel.notification.NotificationResponse
import com.technoidentity.vitalz.data.repository.UserRepositoryImpl
import com.technoidentity.vitalz.utils.CoroutinesDispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val userRepositoryImpl: UserRepositoryImpl,
    private val dispatcher: CoroutinesDispatcherProvider
) : ViewModel() {

    fun getNotificationsCareTakerListData(patientId: String): LiveData<NotificationResponse> {
        val request = NotificationCareTakerRequest()
        request.patientId = patientId
        return liveData {
            emit(userRepositoryImpl.getNotificationCareTakerList(request))
        }
    }

    fun getNotificationsDoctorListData(phoneNo: String): LiveData<NotificationResponse> {
        val request = NotificationDoctorRequest()
        request.phoneNo = phoneNo
        return liveData {
            emit(userRepositoryImpl.getNotificationDoctorList(request))
        }
    }

    fun getNotificationsAdminNurseListData(): LiveData<NotificationResponse> {
        return liveData {
            emit(userRepositoryImpl.getNotificationNurseList())
        }
    }
}
