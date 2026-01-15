package com.talhapps.climabit.domain.repository

import com.talhapps.climabit.domain.model.weather.CurrentWeatherResponse
import com.talhapps.climabit.domain.model.weather.WeatherResponse
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {

    fun getData(): Flow<String>

    fun getCurrentWeatherData(lat: Double, lng: Double) : Flow<CurrentWeatherResponse>
}