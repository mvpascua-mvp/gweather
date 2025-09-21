package com.example.gweather.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "weather")
data class Weather(
    @PrimaryKey(autoGenerate = true)
    val id : Int = 0,
    val uid: String,
    val city: String,
    val country: String,
    val tempCelsius: Double,
    val condition: String,
    val sunriseTime: Long,
    val sunsetTime: Long,
    val timeFetched: Long
    )
