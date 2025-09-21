package com.example.gweather.data.repository

import com.example.gweather.data.local.Weather
import com.example.gweather.data.local.WeatherDao
import com.example.gweather.data.remote.WeatherApi
import javax.inject.Inject
import kotlin.math.roundToInt

class WeatherRepository  @Inject constructor(private val weatherApi: WeatherApi, private val weatherDao: WeatherDao) {

    suspend fun getWeather(uid: String, lat: Double, lon: Double, apiKey: String): Weather{
        val response = weatherApi.getWeather(lat, lon, apiKey)
        val celsiusTemp = ((response.main.temp - 273.15) * 100).roundToInt() / 100.0
        val saveresponse = Weather(
            uid = uid,
            city = response.name,
            country = response.sys.country,
            tempCelsius = celsiusTemp,
            condition = response.weather.firstOrNull()?.main ?: "Unknown",
            sunriseTime = response.sys.sunrise,
            sunsetTime = response.sys.sunset,
            timeFetched = System.currentTimeMillis() / 1000
        )
        weatherDao.insert(saveresponse)
        return saveresponse
    }

    suspend fun getWeatherHistory(uid: String): List<Weather> = weatherDao.getAll(uid)



}