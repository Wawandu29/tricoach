package com.example.tricoach.api

import retrofit2.http.GET
import retrofit2.http.Query

data class OpenMeteoResponse(
    val daily: DailyWeatherData
)

data class DailyWeatherData(
    val time: List<String>,
    val temperature_2m_max: List<Double>,
    val temperature_2m_min: List<Double>,
    val weathercode: List<Int>
)

interface WeatherApi {
    @GET("v1/forecast")
    suspend fun getWeatherForecast(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("daily") daily: String = "temperature_2m_max,temperature_2m_min,weathercode",
        @Query("timezone") timezone: String = "auto"
    ): OpenMeteoResponse
}
