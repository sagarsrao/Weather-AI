package com.example.myapplication.data

import com.example.myapplication.api.WeatherApiService
import com.example.myapplication.model.WeatherResponse
import retrofit2.Response

class WeatherRepository(private val apiService: WeatherApiService) {

    suspend fun getFullWeather(
        lat: Double,
        lon: Double
    ): Response<WeatherResponse> {
        return apiService.getFullWeather(lat, lon)
    }
}
