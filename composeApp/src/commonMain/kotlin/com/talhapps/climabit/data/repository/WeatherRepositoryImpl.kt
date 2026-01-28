package com.talhapps.climabit.data.repository

import com.talhapps.climabit.data.remote.WeatherApi
import com.talhapps.climabit.domain.model.weather.AirQualityResponse
import com.talhapps.climabit.domain.model.weather.GeocodingResponse
import com.talhapps.climabit.domain.model.weather.HistoricalWeatherResponse
import com.talhapps.climabit.domain.model.weather.OpenMeteoResponse
import com.talhapps.climabit.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class WeatherRepositoryImpl(val weatherApi: WeatherApi) : WeatherRepository {
    override fun getData() = flowOf("Hello Weather Repository Impl")

    override fun getCurrentWeatherData(lat: Double, lng: Double): Flow<OpenMeteoResponse> =
        weatherApi.getCurrentWeatherByCoordinates(lat = lat, lon = lng, timezone = "auto")

    override fun getOneCall(lat: Double, lng: Double): Flow<OpenMeteoResponse> =
        weatherApi.getOneCall(lat = lat, lon = lng, timezone = "auto", forecastDays = 7)

    override fun getHistoricalWeather(
        lat: Double,
        lng: Double,
        startDate: String,
        endDate: String
    ): Flow<HistoricalWeatherResponse> =
        weatherApi.getHistoricalWeather(
            lat = lat,
            lon = lng,
            startDate = startDate,
            endDate = endDate,
            timezone = "auto"
        )

    override fun getCurrentAirQuality(lat: Double, lng: Double): Flow<AirQualityResponse> =
        weatherApi.getCurrentAirQuality(lat = lat, lon = lng, timezone = "auto")

    override fun getAirQualityForecast(lat: Double, lng: Double): Flow<AirQualityResponse> =
        weatherApi.getAirQualityForecast(lat = lat, lon = lng, timezone = "auto", forecastDays = 3)

    override fun searchLocations(name: String, count: Int): Flow<List<GeocodingResponse>> =
        weatherApi.searchLocations(name = name, count = count, language = "en", format = "json")

    override fun getLocationByCoordinates(lat: Double, lon: Double): Flow<GeocodingResponse?> =
        weatherApi.getLocationByCoordinates(lat = lat, lon = lon, count = 1, language = "en")
}