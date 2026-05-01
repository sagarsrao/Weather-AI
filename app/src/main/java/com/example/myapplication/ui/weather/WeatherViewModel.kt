package com.example.myapplication.ui.weather

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.WeatherRepository
import com.example.myapplication.model.WeatherResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class WeatherViewModel(
    private val weatherRepository: WeatherRepository
) : ViewModel() {

    private val _weatherState = MutableStateFlow<WeatherState>(WeatherState.Loading)
    val weatherState: StateFlow<WeatherState> = _weatherState

    fun fetchWeather(lat: Double, lon: Double) {
        _weatherState.value = WeatherState.Loading
        viewModelScope.launch {
            try {
                val response = weatherRepository.getFullWeather(lat, lon)
                if (response.isSuccessful && response.body() != null) {
                    _weatherState.value = WeatherState.Success(response.body()!!)
                } else {
                    _weatherState.value = WeatherState.Error("Failed to load weather data: ${response.message()}")
                }
            } catch (e: Exception) {
                _weatherState.value = WeatherState.Error("An error occurred: ${e.message}")
            }
        }
    }

    sealed class WeatherState {
        object Loading : WeatherState()
        data class Success(val weatherData: WeatherResponse) : WeatherState()
        data class Error(val message: String) : WeatherState()
    }
}
