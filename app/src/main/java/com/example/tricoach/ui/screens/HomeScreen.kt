package com.example.tricoach.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.tricoach.data.entity.TrainingSession
import com.example.tricoach.viewmodel.TrainingViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToCalendar: () -> Unit,
    onNavigateToAddSession: () -> Unit,
    onNavigateToLastSessions: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Home") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Welcome to TriCoach!",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Button(
                onClick = onNavigateToCalendar,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Go to Calendar")
            }

            Button(
                onClick = onNavigateToAddSession,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Add a New Session")
            }

            Button(
                onClick = onNavigateToLastSessions,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("View Completed Sessions")
            }
        }
    }
}
