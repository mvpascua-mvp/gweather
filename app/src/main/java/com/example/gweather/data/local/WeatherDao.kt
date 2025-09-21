package com.example.gweather.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface WeatherDao {
    @Insert
    suspend fun insert(weather: Weather)
    // will fetch the weather att time fetched by the user

    @Query("SELECT * FROM weather WHERE uid = :uid ORDER BY timeFetched DESC")
    suspend fun getAll(uid: String): List<Weather>

}