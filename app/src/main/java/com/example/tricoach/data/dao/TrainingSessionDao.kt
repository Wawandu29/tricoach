package com.example.tricoach.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.tricoach.data.entity.TrainingSession

@Dao
interface TrainingSessionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: TrainingSession)

    @Query("SELECT * FROM training_sessions WHERE date BETWEEN :startDate AND :endDate ORDER BY date")
    fun getSessionsForWeek(startDate: String, endDate: String): LiveData<List<TrainingSession>>

    @Query("SELECT * FROM training_sessions ORDER BY date ASC")
    fun getAllSessions(): LiveData<List<TrainingSession>>

    @Delete
    suspend fun deleteSession(session: TrainingSession)
}


