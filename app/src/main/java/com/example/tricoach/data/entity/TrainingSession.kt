package com.example.tricoach.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "training_sessions")
data class TrainingSession(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val discipline: String, // Swim, Bike, Run
    val date: String, // YYYY-MM-DD
    val duration: Int, // In minutes
    val objective: String // Technique, Endurance, etc.
)


