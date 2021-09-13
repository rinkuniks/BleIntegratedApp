package com.technoidentity.vitalz.data.local.databaseEntities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ecgDatatable")
data class EcgDataDb(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name ="patientId")
    var patientId: String = "",

    @ColumnInfo(name = "telemetryKey")
    var ecgKey: String = "",

    @ColumnInfo(name ="ecg")
    var heartRateValue: List<Byte>

)



