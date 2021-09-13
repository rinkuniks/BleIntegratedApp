package com.technoidentity.vitalz.data.local.databaseEntities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "registeredDevice")
data class RegisteredDeviceDb(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name ="patchId")
    val patchId: String = "Invalid_Patch",

    @ColumnInfo(name ="macId")
    val macId: String= "macId"
)
