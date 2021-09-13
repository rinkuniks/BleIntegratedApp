package com.technoidentity.vitalz.data.repository

import com.technoidentity.vitalz.bluetooth.data.BleMac
import com.technoidentity.vitalz.bluetooth.data.RegisteredDevice
import com.technoidentity.vitalz.data.datamodel.updatedregistereddevice.UpdateRegisteredDeviceRequest
import com.technoidentity.vitalz.data.datamodel.updatedregistereddevice.UpdatedRegisteredDeviceResponse
import com.technoidentity.vitalz.data.local.dao.EcgDataDao
import com.technoidentity.vitalz.data.local.dao.HeartRateDao
import com.technoidentity.vitalz.data.local.dao.RegisteredDeviceDao
import com.technoidentity.vitalz.data.local.databaseEntities.EcgDataDb
import com.technoidentity.vitalz.data.local.databaseEntities.HeartRateDb
import com.technoidentity.vitalz.data.local.databaseEntities.RegisteredDeviceDb
import com.technoidentity.vitalz.data.network.VitalzApi
import com.technoidentity.vitalz.notifications.datamodel.VitalzTelemetryNotification
import kotlinx.coroutines.flow.Flow
import timber.log.Timber
import javax.inject.Inject

class DeviceRepositoryImpl @Inject constructor(
    private val api: VitalzApi,
    private val registeredDeviceDao: RegisteredDeviceDao
) : DeviceRepository {

    override suspend fun registeredDevice(deviceMac: BleMac): RegisteredDevice {
        return kotlin.runCatching {

            when (isDeviceExist(deviceMac.macId)){
                false -> {
                    api.sendDevicesForRegisteration(deviceMac).apply {
                        insertRegisteredDeviceDb(RegisteredDeviceDb(patchId = this.patchId, macId = this.macId)).also {
                            Timber.d("Device registered in backend and local db")
                        }
                    }
                }
                true -> {
                    with(getRegsteredDeviceDb()){
                        RegisteredDevice(patchId, macId)
                    }
                }
            }
        }.getOrElse {
            RegisteredDevice(message = "Something went wrong device not available")
        }
    }


    override suspend fun getRegisteredDevices(): List<RegisteredDevice> {
        return kotlin.runCatching {
            api.getRegisteredDevices()
        }.getOrElse {
            listOf(RegisteredDevice(message = it.message.toString()))
        }
    }

    override suspend fun updateDevice(updateRegisteredDeviceRequest: UpdateRegisteredDeviceRequest): UpdatedRegisteredDeviceResponse {
        return api.updateRegisteredDevice(updateRegisteredDeviceRequest)
    }



    override suspend fun insertRegisteredDeviceDb(registeredDeviceDb: RegisteredDeviceDb) {
        registeredDeviceDao.insert(registeredDeviceDb)
    }

    override suspend fun getRegsteredDeviceDb(): RegisteredDeviceDb =
        registeredDeviceDao.registeredDeviceDb()

    override fun isDeviceExist(macId: String): Boolean =
        registeredDeviceDao.checkDeviceMacId(macId)
}


