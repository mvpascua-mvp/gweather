package com.example.gweather

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.gweather.data.local.Weather
import com.example.gweather.data.local.WeatherDao

@Database(entities = [Weather::class], version = 1, exportSchema = false)
        abstract class AppDatabase : RoomDatabase() {
            abstract fun weatherDao(): WeatherDao
        }