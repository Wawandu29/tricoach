package com.example.tricoach

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.tricoach.data.database.AppDatabase
import com.example.tricoach.ui.screens.AddSessionScreen
import com.example.tricoach.ui.screens.CalendarScreen
import com.example.tricoach.ui.screens.HomeScreen
import com.example.tricoach.ui.screens.LastSessionsScreen
import com.example.tricoach.viewmodel.TrainingViewModel
import com.example.tricoach.viewmodel.TrainingViewModelFactory
import com.example.tricoach.viewmodel.WeatherViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class MainActivity : ComponentActivity() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var weatherViewModel: WeatherViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialiser le WeatherViewModel
        weatherViewModel = WeatherViewModel()

        // Initialiser le FusedLocationProviderClient pour la géolocalisation
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // Demander les permissions de localisation et récupérer les coordonnées GPS
        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestLocationPermission()
        } else {
            getCurrentLocation()
        }

        // Initialiser le TrainingViewModel
        val database = AppDatabase.getDatabase(applicationContext)
        val trainingSessionDao = database.trainingSessionDao()
        val viewModelFactory = TrainingViewModelFactory(trainingSessionDao)
        val trainingViewModel = ViewModelProvider(this, viewModelFactory)[TrainingViewModel::class.java]

        setContent {
            var currentScreen by remember { mutableStateOf("Home") }

            when (currentScreen) {
                "Home" -> HomeScreen(
                    onNavigateToCalendar = { currentScreen = "Calendar" },
                    onNavigateToAddSession = { currentScreen = "AddSession" },
                    onNavigateToLastSessions = { currentScreen = "LastSessions" }
                )
                "Calendar" -> CalendarScreen(
                    weatherViewModel = weatherViewModel,
                    trainingViewModel = trainingViewModel,
                    onAddClick = { date ->
                        currentScreen = "AddSession"
                    },
                    onNavigateBack = { currentScreen = "Home" } // Gérer le retour à l'écran "Home"
                )
                "AddSession" -> AddSessionScreen(
                    onSave = { session ->
                        trainingViewModel.addSession(session)
                        currentScreen = "Home"
                    },
                    onCancel = { currentScreen = "Home" }
                )
                "LastSessions" -> LastSessionsScreen(
                    viewModel = trainingViewModel,
                    onAddClick = { currentScreen = "AddSession" }
                )
            }
        }
    }

    // Demander les permissions de localisation
    private fun requestLocationPermission() {
        val locationPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                if (isGranted) {
                    getCurrentLocation()
                } else {
                    Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    // Récupérer les coordonnées GPS actuelles
    @SuppressLint("MissingPermission")
    private fun getCurrentLocation() {
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                val latitude = location.latitude
                val longitude = location.longitude
                // Appeler fetchWeather avec les coordonnées GPS
                weatherViewModel.fetchWeather(latitude, longitude)
            } else {
                Toast.makeText(this, "Unable to get location", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {
            Toast.makeText(this, "Failed to get location: ${it.message}", Toast.LENGTH_SHORT).show()
        }
    }
}
