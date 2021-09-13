package com.technoidentity.vitalz.data.local.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface BaseDao<T> {


    /**
     * Insert an object in the database.
     *
     * @param obj the object to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg obj: T?)

    /**
     * Insert an array of objects in the database.
     *
     * @param obj the objects to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(obj: List<T>?)


//    /**
//     * Insert bytearray in database
//     *
//     * @param obj the objects to be inserted.
//     */
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    fun insert(obj: ByteArray)

    /**
     * Update an object from the database.
     *
     * @param obj the object to be updated
     */
    @Update
    suspend fun update(obj: List<T>?)

    /**
     * Delete an object from the database
     *
     * @param obj the object to be deleted
     */
    @Delete
    suspend fun delete(obj: T)

    /**
     * Delete an array of objects in the database.
     *
     * @param obj the objects to be deleted.
     */
    @Delete
    suspend fun deleteAll(obj: List<T>?)

}