package com.example.tricoach.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tricoach.data.dao.TrainingSessionDao

class TrainingViewModelFactory(private val dao: TrainingSessionDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TrainingViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TrainingViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}