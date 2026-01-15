package com.talhapps.climabit.data.remote

import com.talhapps.climabit.domain.model.weather.AirPollutionResponse
import com.talhapps.climabit.domain.model.weather.CurrentWeatherResponse
import com.talhapps.climabit.domain.model.weather.Forecast16Response
import com.talhapps.climabit.domain.model.weather.Forecast5Response
import com.talhapps.climabit.domain.model.weather.GeocodingResponse
import com.talhapps.climabit.domain.model.weather.OneCallResponse
import com.talhapps.climabit.domain.model.weather.ReverseGeocodingResponse
import kotlinx.coroutines.flow.Flow

/**
 * Wrapper over OpenWeather APIs.
 *
 * Docs: see [https://openweathermap.org/api](https://openweathermap.org/api)
 */
interface WeatherApi {

    // --- Current weather ----------------------------------------------------

    /**
     * Current weather for coordinates.
     * Uses OpenWeather "Current weather data" API: /data/2.5/weather
     */
    fun getCurrentWeatherByCoordinates(
        lat: Double,
        lon: Double
    ): Flow<CurrentWeatherResponse>

    /**
     * Current weather by city name (optionally country code, e.g. "London,GB").
     */
    fun getCurrentWeatherByCityName(
        cityName: String,
        countryCode: String? = null
    ): Flow<CurrentWeatherResponse>

    // --- One Call 3.0 -------------------------------------------------------

    /**
     * One Call 3.0 – current + hourly + daily forecast in a single response.
     *
     * Docs: /data/3.0/onecall
     */
     fun getOneCall(
        lat: Double,
        lon: Double,
        exclude: String? = null,
        units: String? = null,
        language: String? = null
    ): Flow<OneCallResponse>

    /**
     * Historical One Call data (Time Machine).
     *
     * Docs: /data/3.0/onecall/timemachine
     */
     fun getOneCallHistorical(
        lat: Double,
        lon: Double,
        dt: Long,
        exclude: String? = null,
        units: String? = null,
        language: String? = null
    ): Flow<OneCallResponse>

    // --- Forecasts ----------------------------------------------------------

    /**
     * 5 day / 3 hour forecast by coordinates.
     * Docs: /data/2.5/forecast
     */
     fun getFiveDayForecastByCoordinates(
        lat: Double,
        lon: Double,
        units: String? = null,
        language: String? = null
    ): Flow<Forecast5Response>

    /**
     * 16 day daily forecast by coordinates.
     * Docs: /data/2.5/forecast/daily (paid)
     */
     fun getSixteenDayForecastByCoordinates(
        lat: Double,
        lon: Double,
        units: String? = null,
        language: String? = null
    ): Flow<Forecast16Response>

    // --- Air pollution ------------------------------------------------------

    /**
     * Current air pollution data.
     * Docs: /data/2.5/air_pollution
     */
    suspend fun getCurrentAirPollution(
        lat: Double,
        lon: Double
    ): Flow<AirPollutionResponse>

    /**
     * Air pollution forecast.
     * Docs: /data/2.5/air_pollution/forecast
     */
    suspend fun getForecastAirPollution(
        lat: Double,
        lon: Double
    ): Flow<AirPollutionResponse>

    /**
     * Historical air pollution.
     * Docs: /data/2.5/air_pollution/history
     */
    suspend fun getHistoricalAirPollution(
        lat: Double,
        lon: Double,
        start: Long,
        end: Long
    ): Flow<AirPollutionResponse>

    // --- Geocoding ----------------------------------------------------------

    /**
     * Direct geocoding (city name -> coordinates).
     */
    suspend fun getGeocodingDirect(
        cityName: String,
        stateCode: String? = null,
        countryCode: String? = null,
        limit: Int? = null
    ): Flow<List<GeocodingResponse>>

    /**
     * Reverse geocoding (coordinates -> place).
     */
    suspend fun getGeocodingReverse(
        lat: Double,
        lon: Double,
        limit: Int? = null
    ): Flow<List<ReverseGeocodingResponse>>
}