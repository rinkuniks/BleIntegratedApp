package com.technoidentity.vitalz.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.technoidentity.vitalz.data.local.databaseEntities.EcgDataDb
import com.technoidentity.vitalz.data.local.databaseEntities.HeartRateDb
import kotlinx.coroutines.flow.Flow

@Dao
abstract class EcgDataDao: BaseDao<EcgDataDb> {

    @Query("SELECT * FROM ecgDatatable")
    abstract fun getEcgData(): Flow<EcgDataDb>
}