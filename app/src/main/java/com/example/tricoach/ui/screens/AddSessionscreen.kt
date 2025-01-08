package com.example.tricoach.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.tricoach.data.entity.TrainingSession

@Composable
fun AddSessionScreen(
    onSave: (TrainingSession) -> Unit,
    onCancel: () -> Unit
) {
    var discipline by remember { mutableStateOf(TextFieldValue("")) }
    var date by remember { mutableStateOf(TextFieldValue("")) }
    var duration by remember { mutableStateOf(TextFieldValue("")) }
    var objective by remember { mutableStateOf(TextFieldValue("")) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        TextField(
            value = discipline,
            onValueChange = { discipline = it },
            label = { Text("Discipline (e.g., Swim, Bike, Run)") },
            modifier = Modifier.fillMaxWidth()
        )

        TextField(
            value = date,
            onValueChange = { date = it },
            label = { Text("Date (YYYY-MM-DD)") },
            modifier = Modifier.fillMaxWidth()
        )

        TextField(
            value = duration,
            onValueChange = { duration = it },
            label = { Text("Duration (minutes)") },
            modifier = Modifier.fillMaxWidth()
        )

        TextField(
            value = objective,
            onValueChange = { objective = it },
            label = { Text("Objective (e.g., Endurance, Speed)") },
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(onClick = onCancel) {
                Text("Cancel")
            }
            Button(onClick = {
                // Sauvegarde la séance avec les champs saisis
                if (discipline.text.isNotEmpty() && date.text.isNotEmpty() && duration.text.isNotEmpty()) {
                    onSave(
                        TrainingSession(
                            discipline = discipline.text,
                            date = date.text,
                            duration = duration.text.toInt(),
                            objective = objective.text
                        )
                    )
                }
            }) {
                Text("Save")
            }
        }
    }
}
