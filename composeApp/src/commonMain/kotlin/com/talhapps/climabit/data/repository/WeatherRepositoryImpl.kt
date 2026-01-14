package com.talhapps.climabit.data.repository

import com.talhapps.climabit.data.remote.WeatherApi
import com.talhapps.climabit.domain.model.weather.CurrentWeatherResponse
import com.talhapps.climabit.domain.repository.WeatherRepository
import kotlinx.coroutines.delay

class WeatherRepositoryImpl(val weatherApi: WeatherApi): WeatherRepository {
    override suspend fun getData(): String {
        delay(2000)
        return "Hello Weather Repository Impl"
    }

    override suspend fun getCurrentWeatherData(lat: Double, lng: Double): CurrentWeatherResponse {
       return weatherApi.getCurrentWeatherByCoordinates(lat = lat, lon = lng)
    }
}