package com.talhapps.climabit.domain.repository

import com.talhapps.climabit.domain.model.weather.AirQualityResponse
import com.talhapps.climabit.domain.model.weather.GeocodingResponse
import com.talhapps.climabit.domain.model.weather.HistoricalWeatherResponse
import com.talhapps.climabit.domain.model.weather.OpenMeteoResponse
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {

    fun getData(): Flow<String>

    // Weather Forecast
    fun getCurrentWeatherData(lat: Double, lng: Double): Flow<OpenMeteoResponse>
    fun getOneCall(lat: Double, lng: Double): Flow<OpenMeteoResponse>

    // Historical Weather
    fun getHistoricalWeather(
        lat: Double,
        lng: Double,
        startDate: String,
        endDate: String
    ): Flow<HistoricalWeatherResponse>

    // Air Quality
    fun getCurrentAirQuality(lat: Double, lng: Double): Flow<AirQualityResponse>
    fun getAirQualityForecast(lat: Double, lng: Double): Flow<AirQualityResponse>

    // Geocoding
    fun searchLocations(name: String, count: Int = 10): Flow<List<GeocodingResponse>>
    fun getLocationByCoordinates(lat: Double, lon: Double): Flow<GeocodingResponse?>
}