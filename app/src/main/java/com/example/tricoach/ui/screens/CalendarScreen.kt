package com.example.tricoach.ui.screens

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.tricoach.data.entity.TrainingSession
import com.example.tricoach.viewmodel.TrainingViewModel
import com.example.tricoach.viewmodel.WeatherViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class CalendarDayData(
    val date: String,
    val maxTemp: Double?,
    val minTemp: Double?,
    val conditionDescription: String?,
    val sessions: List<TrainingSession>
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(
    weatherViewModel: WeatherViewModel,
    trainingViewModel: TrainingViewModel,
    onAddClick: (String) -> Unit,
    onNavigateBack: () -> Unit // Ajoute une fonction pour gérer la navigation arrière
) {
    val weatherState by weatherViewModel.weatherState.collectAsState()
    val sessions by trainingViewModel.allSessions.observeAsState(initial = emptyList())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Calendar") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) { // Bouton "Retour"
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { padding ->
        // Affichage du contenu principal (météo + séances prévues)
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            val daysOfWeek = generateWeekDays()
            val calendarData = daysOfWeek.map { date ->
                val weather = weatherState.dailyWeather.find { it.date == date }
                val sessionsForDay = sessions.filter { it.date == date }
                CalendarDayData(
                    date = date,
                    maxTemp = weather?.maxTemp,
                    minTemp = weather?.minTemp,
                    conditionDescription = weather?.conditionDescription,
                    sessions = sessionsForDay
                )
            }

            items(calendarData) { dayData ->
                CalendarDay(
                    data = dayData,
                    onAddClick = { onAddClick(dayData.date) }
                )
            }
        }
    }
}




@Composable
fun CalendarDay(
    data: CalendarDayData,
    onAddClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = "Date: ${data.date}",
            style = MaterialTheme.typography.titleMedium
        )

        // Afficher les prévisions météo si disponibles
        if (data.maxTemp != null && data.minTemp != null && data.conditionDescription != null) {
            Text(
                text = "Weather: ${data.conditionDescription}, Max: ${data.maxTemp}°C, Min: ${data.minTemp}°C",
                style = MaterialTheme.typography.bodySmall
            )
        } else {
            Text("No weather data available", style = MaterialTheme.typography.bodySmall)
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Afficher les séances prévues
        if (data.sessions.isEmpty()) {
            Text(
                text = "No sessions planned",
                style = MaterialTheme.typography.bodySmall
            )
        } else {
            data.sessions.forEach { session ->
                TrainingSessionCard(session)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Bouton pour ajouter une séance
        Button(onClick = onAddClick) {
            Text("Add Session")
        }
    }
}



@SuppressLint("NewApi")
fun generateWeekDays(): List<String> {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val startOfWeek = LocalDate.now().plusDays(1) // Start tomorrow
    return (0..6).map { startOfWeek.plusDays(it.toLong()).format(formatter) }
}
