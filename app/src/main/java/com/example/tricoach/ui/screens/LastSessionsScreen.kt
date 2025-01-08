package com.example.tricoach.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.tricoach.data.entity.TrainingSession
import com.example.tricoach.viewmodel.TrainingViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LastSessionsScreen(
    viewModel: TrainingViewModel,
    onAddClick: () -> Unit
) {
    val trainingSessions by viewModel.allSessions.observeAsState(initial = emptyList())

    Scaffold(
        topBar = { TopAppBar(title = { Text("Training Plan") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddClick) {
                Icon(Icons.Default.Add, contentDescription = "Add Session")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (trainingSessions.isEmpty()) {
                item {
                    Text("No training sessions yet.")
                }
            } else {
                items(trainingSessions) { session ->
                    TrainingSessionCard(session)
                }
            }
        }
    }
}




@Composable
fun TrainingSessionCard(session: TrainingSession) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Discipline: ${session.discipline}")
            Text("Date: ${session.date}")
            Text("Duration: ${session.duration} minutes")
            Text("Objective: ${session.objective}")
        }
    }
}
