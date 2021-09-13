package com.technoidentity.vitalz.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.technoidentity.vitalz.data.local.databaseEntities.HeartRateDb
import kotlinx.coroutines.flow.Flow

@Dao
abstract class HeartRateDao: BaseDao<HeartRateDb> {

    @Query("SELECT * FROM heartratetable")
    abstract fun getHeartRate(): Flow<HeartRateDb>

    @Query("SELECT EXISTS(SELECT * FROM heartratetable)")
    abstract fun isExists(): Boolean
}