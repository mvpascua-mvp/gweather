package com.example.gweather.di

import com.example.gweather.data.local.WeatherDao
import com.example.gweather.data.remote.WeatherApi
import com.example.gweather.data.repository.WeatherRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    //for api
    @Provides
    @Singleton
    fun provideHttpClient(): HttpClient = HttpClient(OkHttp){
        install(ContentNegotiation){
            json(Json {
                ignoreUnknownKeys = true
            })
        }
    }

    @Provides
    @Singleton
    fun provideWeatherApi(client: HttpClient): WeatherApi = WeatherApi(client)

    @Provides
    @Singleton
    fun provideWeatherRepository(weatherApi: WeatherApi,dao: WeatherDao): WeatherRepository = WeatherRepository(weatherApi, dao)

}