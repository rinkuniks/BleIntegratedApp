package com.technoidentity.vitalz.data.repository

import com.technoidentity.vitalz.data.local.dao.EcgDataDao
import com.technoidentity.vitalz.data.local.dao.HeartRateDao
import com.technoidentity.vitalz.data.local.databaseEntities.EcgDataDb
import com.technoidentity.vitalz.data.local.databaseEntities.HeartRateDb
import com.technoidentity.vitalz.data.network.VitalzApi
import com.technoidentity.vitalz.notifications.datamodel.VitalzTelemetryNotification
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PatientRepositoryImpl @Inject constructor(
    private val api: VitalzApi,
    private val heartRateDao: HeartRateDao,
    private val ecgDataDao: EcgDataDao
) : PatientRepository {

    override suspend fun sendHeartRate(
        patchId: String,
        telemetryKey: String,
        heartRate: List<Byte>
    ): Boolean {
        return kotlin.runCatching {
            api.sendHeartRate(patchId, telemetryKey, heartRate)
        }.getOrElse {
            false
        }
    }

    override suspend fun sendEcgData(
        patientId: String,
        telemetryKey: String,
        ecgData: List<Byte>
    ): Boolean {
        return kotlin.runCatching {
            api.sendEcgData(patientId, telemetryKey, ecgData)
        }.getOrElse {
            false
        }
    }

    override suspend fun sendTelemetryNotification(vitalz: VitalzTelemetryNotification): Boolean {
        return kotlin.runCatching {
            api.sendTelemetryNotification(vitalz)
        }.getOrElse {
            false
        }
    }

    override suspend fun isHeartRateExists() = heartRateDao.isExists()

    override suspend fun getHeartRateDb(): Flow<HeartRateDb> = heartRateDao.getHeartRate()

    override suspend fun getEcgDataDb(): Flow<EcgDataDb> = ecgDataDao.getEcgData()

    override suspend fun insertHeartRateDb(heartRateDb: HeartRateDb) {
        heartRateDao.insert(heartRateDb)
    }

    override suspend fun insertEcgDataDb(ecgDataDb: EcgDataDb) {
        ecgDataDao.insert(ecgDataDb)
    }

    override suspend fun deleteHeartRateData(heartRateDb: HeartRateDb) {
        heartRateDao.delete(heartRateDb)
    }

    override suspend fun deleteEcgData(ecgDataDb: EcgDataDb) {
        ecgDataDao.delete(ecgDataDb)
    }
}