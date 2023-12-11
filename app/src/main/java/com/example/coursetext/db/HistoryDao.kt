package com.example.coursetext.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoryDao {

    @Query("SELECT * FROM Item")
    fun getAll(): Flow<List<Item>>

    @Insert
    fun insert(item: Item)
}