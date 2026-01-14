package com.talhapps.climabit.domain.repository

import com.talhapps.climabit.domain.model.weather.CurrentWeatherResponse
import com.talhapps.climabit.domain.model.weather.WeatherResponse

interface WeatherRepository {

    suspend fun getData(): String

    suspend fun getCurrentWeatherData(lat: Double, lng: Double) : CurrentWeatherResponse
}