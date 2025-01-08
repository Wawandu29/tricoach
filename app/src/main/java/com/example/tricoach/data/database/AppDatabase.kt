package com.example.tricoach.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.tricoach.data.dao.TrainingSessionDao
import com.example.tricoach.data.entity.TrainingSession

@Database(entities = [TrainingSession::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun trainingSessionDao(): TrainingSessionDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "training_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}

