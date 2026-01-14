package com.talhapps.climabit.data.remote

import com.talhapps.climabit.domain.model.weather.AirPollutionResponse
import com.talhapps.climabit.domain.model.weather.CurrentWeatherResponse
import com.talhapps.climabit.domain.model.weather.Forecast16Response
import com.talhapps.climabit.domain.model.weather.Forecast5Response
import com.talhapps.climabit.domain.model.weather.GeocodingResponse
import com.talhapps.climabit.domain.model.weather.OneCallResponse
import com.talhapps.climabit.domain.model.weather.ReverseGeocodingResponse

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
    suspend fun getCurrentWeatherByCoordinates(
        lat: Double,
        lon: Double
    ): CurrentWeatherResponse

    /**
     * Current weather by city name (optionally country code, e.g. "London,GB").
     */
    suspend fun getCurrentWeatherByCityName(
        cityName: String,
        countryCode: String? = null
    ): CurrentWeatherResponse

    // --- One Call 3.0 -------------------------------------------------------

    /**
     * One Call 3.0 – current + hourly + daily forecast in a single response.
     *
     * Docs: /data/3.0/onecall
     */
    suspend fun getOneCall(
        lat: Double,
        lon: Double,
        exclude: String? = null,
        units: String? = null,
        language: String? = null
    ): OneCallResponse

    /**
     * Historical One Call data (Time Machine).
     *
     * Docs: /data/3.0/onecall/timemachine
     */
    suspend fun getOneCallHistorical(
        lat: Double,
        lon: Double,
        dt: Long,
        exclude: String? = null,
        units: String? = null,
        language: String? = null
    ): OneCallResponse

    // --- Forecasts ----------------------------------------------------------

    /**
     * 5 day / 3 hour forecast by coordinates.
     * Docs: /data/2.5/forecast
     */
    suspend fun getFiveDayForecastByCoordinates(
        lat: Double,
        lon: Double,
        units: String? = null,
        language: String? = null
    ): Forecast5Response

    /**
     * 16 day daily forecast by coordinates.
     * Docs: /data/2.5/forecast/daily (paid)
     */
    suspend fun getSixteenDayForecastByCoordinates(
        lat: Double,
        lon: Double,
        units: String? = null,
        language: String? = null
    ): Forecast16Response

    // --- Air pollution ------------------------------------------------------

    /**
     * Current air pollution data.
     * Docs: /data/2.5/air_pollution
     */
    suspend fun getCurrentAirPollution(
        lat: Double,
        lon: Double
    ): AirPollutionResponse

    /**
     * Air pollution forecast.
     * Docs: /data/2.5/air_pollution/forecast
     */
    suspend fun getForecastAirPollution(
        lat: Double,
        lon: Double
    ): AirPollutionResponse

    /**
     * Historical air pollution.
     * Docs: /data/2.5/air_pollution/history
     */
    suspend fun getHistoricalAirPollution(
        lat: Double,
        lon: Double,
        start: Long,
        end: Long
    ): AirPollutionResponse

    // --- Geocoding ----------------------------------------------------------

    /**
     * Direct geocoding (city name -> coordinates).
     */
    suspend fun getGeocodingDirect(
        cityName: String,
        stateCode: String? = null,
        countryCode: String? = null,
        limit: Int? = null
    ): List<GeocodingResponse>

    /**
     * Reverse geocoding (coordinates -> place).
     */
    suspend fun getGeocodingReverse(
        lat: Double,
        lon: Double,
        limit: Int? = null
    ): List<ReverseGeocodingResponse>
}