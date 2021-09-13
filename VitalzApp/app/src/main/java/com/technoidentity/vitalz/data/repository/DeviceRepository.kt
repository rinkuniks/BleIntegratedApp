package com.technoidentity.vitalz.data.repository

import com.technoidentity.vitalz.bluetooth.data.BleMac
import com.technoidentity.vitalz.bluetooth.data.RegisteredDevice
import com.technoidentity.vitalz.data.datamodel.updatedregistereddevice.UpdateRegisteredDeviceRequest
import com.technoidentity.vitalz.data.datamodel.updatedregistereddevice.UpdatedRegisteredDeviceResponse
import com.technoidentity.vitalz.data.local.databaseEntities.EcgDataDb
import com.technoidentity.vitalz.data.local.databaseEntities.HeartRateDb
import com.technoidentity.vitalz.data.local.databaseEntities.RegisteredDeviceDb
import com.technoidentity.vitalz.notifications.datamodel.VitalzTelemetryNotification
import kotlinx.coroutines.flow.Flow

interface DeviceRepository {

    suspend fun registeredDevice(deviceMac: BleMac): RegisteredDevice

    suspend fun updateDevice(updateRegisteredDeviceRequest: UpdateRegisteredDeviceRequest): UpdatedRegisteredDeviceResponse

    suspend fun getRegisteredDevices(): List<RegisteredDevice>

    fun isDeviceExist(macId: String): Boolean



    suspend fun insertRegisteredDeviceDb(registeredDeviceDb: RegisteredDeviceDb)

    suspend fun getRegsteredDeviceDb(): RegisteredDeviceDb

}