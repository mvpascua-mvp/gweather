package com.example.gweather.data.remote

import kotlinx.serialization.Serializable

@Serializable
data class WeatherResponse (
    val name: String,
    val sys: Sys,
    val main: Main,
    val weather: List<WeatherData>
)
@Serializable
data class Sys(val country: String, val sunrise: Long, val sunset: Long)
@Serializable
data class Main(val temp: Double)
@Serializable
data class WeatherData(val main: String, val description: String)