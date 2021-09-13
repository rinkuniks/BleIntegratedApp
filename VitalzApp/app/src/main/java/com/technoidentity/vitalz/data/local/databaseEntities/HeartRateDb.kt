package com.technoidentity.vitalz.data.local.databaseEntities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "heartratetable")
data class HeartRateDb(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name ="patientId")
    var patchId: String = "",

    @ColumnInfo(name = "telemetryKey")
    var telemetryKey: String="",

    @ColumnInfo(name ="heartRate")
    var heartRateValue: List<Byte>

)



