package com.example.gweather.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import javax.inject.Inject

class WeatherApi @Inject constructor(private val client: HttpClient) {
    suspend fun getWeather(lat: Double, lon: Double, apiKey: String): WeatherResponse =
        client.get("https://api.openweathermap.org/data/2.5/weather"){
            parameter("lat", lat)
            parameter("lon", lon)
            parameter("appid", apiKey)
    }.body()
}