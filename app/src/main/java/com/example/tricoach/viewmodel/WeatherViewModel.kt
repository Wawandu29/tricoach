package com.example.tricoach.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tricoach.api.WeatherService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class WeatherUiState(
    val dailyWeather: List<DailyWeatherUi> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

data class DailyWeatherUi(
    val date: String,
    val maxTemp: Double,
    val minTemp: Double,
    val conditionDescription: String
)

class WeatherViewModel : ViewModel() {
    private val _weatherState = MutableStateFlow(WeatherUiState())
    val weatherState: StateFlow<WeatherUiState> = _weatherState

    fun fetchWeather(latitude: Double, longitude: Double) {
        _weatherState.value = _weatherState.value.copy(isLoading = true)

        viewModelScope.launch {
            try {
                val response = WeatherService.api.getWeatherForecast(latitude, longitude)
                val dailyWeather = response.daily.time.mapIndexed { index, date ->
                    DailyWeatherUi(
                        date = date,
                        maxTemp = response.daily.temperature_2m_max[index],
                        minTemp = response.daily.temperature_2m_min[index],
                        conditionDescription = mapWeatherCode(response.daily.weathercode[index])
                    )
                }
                _weatherState.value = WeatherUiState(dailyWeather = dailyWeather)
            } catch (e: Exception) {
                _weatherState.value = WeatherUiState(error = e.message)
            } finally {
                _weatherState.value = _weatherState.value.copy(isLoading = false)
            }
        }
    }

    // Mappage des weather_code vers des descriptions lisibles
    private fun mapWeatherCode(code: Int): String {
        return when (code) {
            0 -> "Clear sky"
            1 -> "Mainly clear"
            2 -> "Partly cloudy"
            3 -> "Overcast"
            45 -> "Fog"
            48 -> "Depositing rime fog"
            51 -> "Drizzle: Light intensity"
            53 -> "Drizzle: Moderate intensity"
            55 -> "Drizzle: Dense intensity"
            56 -> "Freezing drizzle: Light intensity"
            57 -> "Freezing drizzle: Dense intensity"
            61 -> "Rain: Slight intensity"
            63 -> "Rain: Moderate intensity"
            65 -> "Rain: Heavy intensity"
            66 -> "Freezing rain: Light intensity"
            67 -> "Freezing rain: Heavy intensity"
            71 -> "Snow fall: Slight intensity"
            73 -> "Snow fall: Moderate intensity"
            75 -> "Snow fall: Heavy intensity"
            77 -> "Snow grains"
            80 -> "Rain showers: Slight intensity"
            81 -> "Rain showers: Moderate intensity"
            82 -> "Rain showers: Violent intensity"
            85 -> "Snow showers: Slight intensity"
            86 -> "Snow showers: Heavy intensity"
            95 -> "Thunderstorm: Slight or moderate"
            96 -> "Thunderstorm with slight hail"
            99 -> "Thunderstorm with heavy hail"
            else -> "Unknown weather condition"
        }
    }
}
