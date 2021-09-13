package com.technoidentity.vitalz.data.repository

import com.technoidentity.vitalz.data.local.databaseEntities.EcgDataDb
import com.technoidentity.vitalz.data.local.databaseEntities.HeartRateDb
import com.technoidentity.vitalz.notifications.datamodel.VitalzTelemetryNotification
import kotlinx.coroutines.flow.Flow

interface PatientRepository {

    suspend fun sendHeartRate(patchId: String, telemetryKey: String, heartRate: List<Byte>): Boolean

    suspend fun insertHeartRateDb(heartRateDb: HeartRateDb)

    suspend fun sendEcgData(patientId: String, telemetryKey: String, ecgData: List<Byte>): Boolean

    suspend fun insertEcgDataDb(ecgDataDb: EcgDataDb)

    suspend fun getHeartRateDb(): Flow<HeartRateDb>

    suspend fun deleteHeartRateData(heartRateDb: HeartRateDb)

    suspend fun deleteEcgData(ecgDataDb: EcgDataDb)

    suspend fun getEcgDataDb(): Flow<EcgDataDb>

    suspend fun sendTelemetryNotification(vitalz: VitalzTelemetryNotification): Boolean

    suspend fun isHeartRateExists(): Boolean
}