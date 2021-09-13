package com.technoidentity.vitalz.hospital

import androidx.lifecycle.*
import com.technoidentity.vitalz.data.datamodel.SearchHospitalRequest
import com.technoidentity.vitalz.data.datamodel.hospital_list.HospitalListData
import com.technoidentity.vitalz.data.datamodel.hospital_list.HospitalListRequest
import com.technoidentity.vitalz.data.repository.UserRepositoryImpl
import com.technoidentity.vitalz.utils.CoroutinesDispatcherProvider
import com.technoidentity.vitalz.utils.ResultHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HospitalViewModel @Inject constructor(
    private val userRepositoryImpl: UserRepositoryImpl,
    private val dispatcher: CoroutinesDispatcherProvider
) : ViewModel() {

    fun getHospitalListData(mobile: String): LiveData<HospitalListData> {
        val request = HospitalListRequest()
        request.mobile = mobile
        return liveData {
            emit(userRepositoryImpl.getHospitalList(request))
        }
    }

    fun searchHospitalInList(request: SearchHospitalRequest): LiveData<HospitalListData> {
        return liveData {
            emit(userRepositoryImpl.searchHospitalList(request))
        }
    }
}
