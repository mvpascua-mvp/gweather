package com.example.gweather.ui.homepage

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gweather.data.local.Weather
import com.example.gweather.data.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val repo: WeatherRepository
) : ViewModel() {
    var current by mutableStateOf<Weather?>(null)
    var history by mutableStateOf<List<Weather>>(emptyList())
    var loading by mutableStateOf(false)

    fun fetchWeather(uid: String, lat: Double, lon: Double, apiKey: String) {
        viewModelScope.launch {
            loading = true
            try {
                current = repo.getWeather(uid, lat, lon, apiKey)
                history = repo.getWeatherHistory(uid)
            } finally {
                loading = false
            }
        }
    }

    fun loadHistory(uid: String) {
        viewModelScope.launch {
            history = repo.getWeatherHistory(uid)
        }
    }
}