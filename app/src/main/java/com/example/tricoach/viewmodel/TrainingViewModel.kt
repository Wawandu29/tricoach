package com.example.tricoach.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tricoach.data.dao.TrainingSessionDao
import com.example.tricoach.data.entity.TrainingSession
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TrainingViewModel(private val trainingSessionDao: TrainingSessionDao) : ViewModel() {

    val allSessions: LiveData<List<TrainingSession>> = trainingSessionDao.getAllSessions()


    fun addSession(session: TrainingSession) {
        viewModelScope.launch {
            trainingSessionDao.insertSession(session)
            Log.d("TrainingViewModel", "Session added: $session")
        }
    }

    fun logSessions() {
        allSessions.observeForever { sessions ->
            Log.d("TrainingViewModel", "Sessions: $sessions")
        }
    }



    // Function to delete a session
    fun deleteSession(session: TrainingSession) {
        viewModelScope.launch {
            trainingSessionDao.deleteSession(session)
        }
    }
}
