package com.example.gweather.di


import android.content.Context
import androidx.room.Room
import com.example.gweather.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

//data injection

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    //create room instance
    @Provides
    @Singleton
    fun provideWeatherDatabase(@ApplicationContext appcontext: Context): AppDatabase = Room.databaseBuilder(appcontext, AppDatabase::class.java, "weather_database").build()

    @Provides
    fun provideWeatherDao(appDatabase: AppDatabase) = appDatabase.weatherDao()
}