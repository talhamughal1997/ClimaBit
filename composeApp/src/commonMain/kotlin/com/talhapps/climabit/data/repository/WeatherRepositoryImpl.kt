package com.talhapps.climabit.data.repository

import com.talhapps.climabit.data.remote.WeatherApi
import com.talhapps.climabit.domain.model.weather.CurrentWeatherResponse
import com.talhapps.climabit.domain.repository.WeatherRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class WeatherRepositoryImpl(val weatherApi: WeatherApi): WeatherRepository {
    override  fun getData() =
        flowOf("Hello Weather Repository Impl")


    override fun getCurrentWeatherData(lat: Double, lng: Double)
       =  weatherApi.getCurrentWeatherByCoordinates(lat = lat, lon = lng)

}