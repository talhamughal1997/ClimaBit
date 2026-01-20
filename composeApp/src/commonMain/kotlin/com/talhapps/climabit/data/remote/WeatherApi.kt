package com.talhapps.climabit.data.remote

import com.talhapps.climabit.domain.model.weather.AirQualityResponse
import com.talhapps.climabit.domain.model.weather.GeocodingResponse
import com.talhapps.climabit.domain.model.weather.HistoricalWeatherResponse
import com.talhapps.climabit.domain.model.weather.OpenMeteoResponse
import kotlinx.coroutines.flow.Flow

/**
 * Wrapper over Open-Meteo APIs.
 *
 * Docs: https://open-meteo.com/en/docs
 */
interface WeatherApi {

    // --- Weather Forecast API ---

    /**
     * Get current weather for coordinates.
     * Returns current weather data.
     *
     * @param lat Latitude
     * @param lon Longitude
     * @param timezone Timezone (e.g., "auto" or "Europe/Berlin"). Defaults to "auto"
     * @return OpenMeteoResponse with current weather data
     */
    fun getCurrentWeatherByCoordinates(
        lat: Double,
        lon: Double,
        timezone: String = "auto"
    ): Flow<OpenMeteoResponse>

    /**
     * Get comprehensive forecast data (current + hourly + daily).
     * Returns OpenMeteoResponse with hourly and daily forecasts.
     *
     * @param lat Latitude
     * @param lon Longitude
     * @param timezone Timezone (e.g., "auto" or "Europe/Berlin"). Defaults to "auto"
     * @param forecastDays Number of forecast days (1-16). Defaults to 7
     * @return OpenMeteoResponse with current, hourly, and daily forecast data
     */
    fun getOneCall(
        lat: Double,
        lon: Double,
        timezone: String = "auto",
        forecastDays: Int = 7
    ): Flow<OpenMeteoResponse>

    // --- Historical Weather API ---

    /**
     * Get historical weather data for a specific date range.
     * Docs: https://open-meteo.com/en/docs/historical-weather-api
     *
     * @param lat Latitude
     * @param lon Longitude
     * @param startDate Start date in YYYY-MM-DD format
     * @param endDate End date in YYYY-MM-DD format
     * @param timezone Timezone (e.g., "auto" or "Europe/Berlin"). Defaults to "auto"
     * @return HistoricalWeatherResponse with historical weather data
     */
    fun getHistoricalWeather(
        lat: Double,
        lon: Double,
        startDate: String,
        endDate: String,
        timezone: String = "auto"
    ): Flow<HistoricalWeatherResponse>

    // --- Air Quality API ---

    /**
     * Get current air quality data.
     * Docs: https://open-meteo.com/en/docs/air-quality-api
     *
     * @param lat Latitude
     * @param lon Longitude
     * @param timezone Timezone (e.g., "auto" or "Europe/Berlin"). Defaults to "auto"
     * @return AirQualityResponse with current air quality data
     */
    fun getCurrentAirQuality(
        lat: Double,
        lon: Double,
        timezone: String = "auto"
    ): Flow<AirQualityResponse>

    /**
     * Get air quality forecast (hourly and daily).
     *
     * @param lat Latitude
     * @param lon Longitude
     * @param timezone Timezone (e.g., "auto" or "Europe/Berlin"). Defaults to "auto"
     * @param forecastDays Number of forecast days (1-7). Defaults to 3
     * @return AirQualityResponse with air quality forecast
     */
    fun getAirQualityForecast(
        lat: Double,
        lon: Double,
        timezone: String = "auto",
        forecastDays: Int = 3
    ): Flow<AirQualityResponse>

    // --- Geocoding API ---

    /**
     * Direct geocoding - search for locations by name.
     * Docs: https://open-meteo.com/en/docs/geocoding-api
     *
     * @param name Location name (e.g., "London", "New York")
     * @param count Maximum number of results (1-100). Defaults to 10
     * @param language Language code (e.g., "en", "de"). Defaults to "en"
     * @param format Response format ("json" or "geojson"). Defaults to "json"
     * @return List of GeocodingResponse matching the search query
     */
    fun searchLocations(
        name: String,
        count: Int = 10,
        language: String = "en",
        format: String = "json"
    ): Flow<List<GeocodingResponse>>

    /**
     * Get location name from coordinates using search API.
     * Uses the search API with latitude/longitude parameters to find nearby locations.
     *
     * @param lat Latitude
     * @param lon Longitude
     * @param count Number of results (1-100). Defaults to 1
     * @param language Language code (e.g., "en", "de"). Defaults to "en"
     * @return GeocodingResponse with location information (first result)
     */
    fun getLocationByCoordinates(
        lat: Double,
        lon: Double,
        count: Int = 1,
        language: String = "en"
    ): Flow<GeocodingResponse?>
}