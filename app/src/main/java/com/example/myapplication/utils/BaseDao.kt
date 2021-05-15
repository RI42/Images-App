package com.example.myapplication.utils

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Transaction
import androidx.room.Update

@Dao
abstract class BaseDao<T> {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun insertOrIgnore(obj: T): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun insertOrIgnore(obj: List<T>): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertOrReplace(obj: List<T>): List<Long>

    @Update
    abstract suspend fun update(obj: T)

    @Update
    abstract suspend fun update(obj: List<T>)

    @Transaction
    open suspend fun insertOrUpdate(obj: T) {
        val id = insertOrIgnore(obj)
        if (id == -1L) {
            update(obj)
        }
    }

    @Transaction
    open suspend fun insertOrUpdate(objList: List<T>) {
        val insertResult = insertOrIgnore(objList)
        val updateList = mutableListOf<T>()

        for (i in insertResult.indices) {
            if (insertResult[i] == -1L) {
                updateList.add(objList[i])
            }
        }

        if (updateList.isNotEmpty()) {
            update(updateList)
        }
    }
}