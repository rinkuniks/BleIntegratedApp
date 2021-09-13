package com.technoidentity.vitalz.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.technoidentity.vitalz.bluetooth.data.RegisteredDevice
import com.technoidentity.vitalz.data.local.databaseEntities.RegisteredDeviceDb

@Dao
abstract class RegisteredDeviceDao: BaseDao<RegisteredDeviceDb> {

    @Query("SELECT * FROM registeredDevice")
    abstract fun registeredDeviceDb(): RegisteredDeviceDb

    @Query("SELECT EXISTS(SELECT * FROM registeredDevice WHERE macId = :macId)")
    abstract fun checkDeviceMacId(macId: String): Boolean
}