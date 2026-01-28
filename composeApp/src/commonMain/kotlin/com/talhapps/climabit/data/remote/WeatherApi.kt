package com.talhapps.climabit.data.remote

import com.talhapps.climabit.domain.model.weather.AirQualityResponse
import com.talhapps.climabit.domain.model.weather.GeocodingResponse
import com.talhapps.climabit.domain.model.weather.HistoricalWeatherResponse
import com.talhapps.climabit.domain.model.weather.OpenMeteoResponse
import kotlinx.coroutines.flow.Flow

interface WeatherApi {

    fun getCurrentWeatherByCoordinates(
        lat: Double,
        lon: Double,
        timezone: String = "auto"
    ): Flow<OpenMeteoResponse>

    fun getOneCall(
        lat: Double,
        lon: Double,
        timezone: String = "auto",
        forecastDays: Int = 7
    ): Flow<OpenMeteoResponse>

    fun getHistoricalWeather(
        lat: Double,
        lon: Double,
        startDate: String,
        endDate: String,
        timezone: String = "auto"
    ): Flow<HistoricalWeatherResponse>

    fun getCurrentAirQuality(
        lat: Double,
        lon: Double,
        timezone: String = "auto"
    ): Flow<AirQualityResponse>

    fun getAirQualityForecast(
        lat: Double,
        lon: Double,
        timezone: String = "auto",
        forecastDays: Int = 3
    ): Flow<AirQualityResponse>

    fun searchLocations(
        name: String,
        count: Int = 10,
        language: String = "en",
        format: String = "json"
    ): Flow<List<GeocodingResponse>>

    fun getLocationByCoordinates(
        lat: Double,
        lon: Double,
        count: Int = 1,
        language: String = "en"
    ): Flow<GeocodingResponse?>
}