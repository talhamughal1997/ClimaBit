package com.talhapps.climabit.data.remote

import com.talhapps.climabit.data.core.executeAsFlow
import com.talhapps.climabit.domain.model.weather.AirQualityResponse
import com.talhapps.climabit.domain.model.weather.GeocodingApiResponse
import com.talhapps.climabit.domain.model.weather.GeocodingResponse
import com.talhapps.climabit.domain.model.weather.HistoricalWeatherResponse
import com.talhapps.climabit.domain.model.weather.OpenMeteoResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.path
import io.ktor.http.takeFrom
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Implementation backed by Open-Meteo Weather Forecast API.
 *
 * Base docs: https://open-meteo.com/en/docs
 */
class WeatherApiImpl(
    private val client: HttpClient,
    private val baseUrl: String = DEFAULT_BASE_URL
) : WeatherApi {

    override fun getCurrentWeatherByCoordinates(
        lat: Double,
        lon: Double,
        timezone: String
    ): Flow<OpenMeteoResponse> = executeAsFlow {
        client.get {
            url {
                takeFrom(baseUrl)
                path("v1", "forecast")
                parameter("latitude", lat)
                parameter("longitude", lon)
                parameter("timezone", timezone)
                // Current weather variables
                parameter(
                    "current",
                    "temperature_2m,relative_humidity_2m,apparent_temperature,is_day,precipitation,rain,showers,snowfall,weather_code,cloud_cover,pressure_msl,surface_pressure,wind_speed_10m,wind_direction_10m,wind_gusts_10m"
                )
            }
        }
    }

    override fun getOneCall(
        lat: Double,
        lon: Double,
        timezone: String,
        forecastDays: Int
    ): Flow<OpenMeteoResponse> = executeAsFlow {
        client.get {
            url {
                takeFrom(baseUrl)
                path("v1", "forecast")
                parameter("latitude", lat)
                parameter("longitude", lon)
                parameter("timezone", timezone)
                parameter("forecast_days", forecastDays.coerceIn(1, 16))
                // Current weather
                parameter(
                    "current",
                    "temperature_2m,relative_humidity_2m,apparent_temperature,is_day,precipitation,rain,showers,snowfall,weather_code,cloud_cover,pressure_msl,surface_pressure,wind_speed_10m,wind_direction_10m,wind_gusts_10m"
                )
                // Hourly weather variables
                parameter(
                    "hourly",
                    "temperature_2m,relative_humidity_2m,apparent_temperature,precipitation_probability,precipitation,rain,showers,snowfall,weather_code,cloud_cover,pressure_msl,surface_pressure,wind_speed_10m,wind_direction_10m,wind_gusts_10m,is_day"
                )
                // Daily weather variables
                parameter(
                    "daily",
                    "weather_code,temperature_2m_max,temperature_2m_min,apparent_temperature_max,apparent_temperature_min,sunrise,sunset,daylight_duration,sunshine_duration,uv_index_max,precipitation_sum,rain_sum,showers_sum,snowfall_sum,precipitation_hours,precipitation_probability_max,wind_speed_10m_max,wind_gusts_10m_max,wind_direction_10m_dominant"
                )
            }
        }
    }

    override fun getHistoricalWeather(
        lat: Double,
        lon: Double,
        startDate: String,
        endDate: String,
        timezone: String
    ): Flow<HistoricalWeatherResponse> = executeAsFlow {
        client.get {
            url {
                takeFrom(HISTORICAL_BASE_URL)
                path("v1", "archive")
                parameter("latitude", lat)
                parameter("longitude", lon)
                parameter("start_date", startDate)
                parameter("end_date", endDate)
                parameter("timezone", timezone)
                parameter(
                    "hourly",
                    "temperature_2m,relative_humidity_2m,precipitation,weather_code,pressure_msl,wind_speed_10m,wind_direction_10m"
                )
                parameter(
                    "daily",
                    "weather_code,temperature_2m_max,temperature_2m_min,precipitation_sum,wind_speed_10m_max"
                )
            }
        }
    }

    override fun getCurrentAirQuality(
        lat: Double,
        lon: Double,
        timezone: String
    ): Flow<AirQualityResponse> = executeAsFlow {
        client.get {
            url {
                takeFrom(AIR_QUALITY_BASE_URL)
                path("v1", "air-quality")
                parameter("latitude", lat)
                parameter("longitude", lon)
                parameter("timezone", timezone)
                parameter(
                    "current",
                    "european_aqi,us_aqi,pm2_5,pm10,carbon_monoxide,nitrogen_dioxide,sulphur_dioxide,ozone"
                )
            }
        }
    }

    override fun getAirQualityForecast(
        lat: Double,
        lon: Double,
        timezone: String,
        forecastDays: Int
    ): Flow<AirQualityResponse> = executeAsFlow {
        client.get {
            url {
                takeFrom(AIR_QUALITY_BASE_URL)
                path("v1", "air-quality")
                parameter("latitude", lat)
                parameter("longitude", lon)
                parameter("timezone", timezone)
                parameter("forecast_days", forecastDays.coerceIn(1, 7))
                parameter(
                    "current",
                    "european_aqi,us_aqi,pm2_5,pm10,carbon_monoxide,nitrogen_dioxide,sulphur_dioxide,ozone"
                )
                parameter(
                    "hourly",
                    "european_aqi,us_aqi,pm2_5,pm10,carbon_monoxide,nitrogen_dioxide,sulphur_dioxide,ozone"
                )
                parameter("daily", "european_aqi_max,us_aqi_max,pm2_5_max,pm10_max")
            }
        }
    }

    override fun searchLocations(
        name: String,
        count: Int,
        language: String,
        format: String
    ): Flow<List<GeocodingResponse>> = executeAsFlow<GeocodingApiResponse> {
        client.get {
            url {
                takeFrom(GEOCODING_BASE_URL)
                path("v1", "search")
                parameter("name", name)
                parameter("count", count.coerceIn(1, 100))
                parameter("language", language)
                parameter("format", format)
            }
        }
    }.map { response ->
        response.results
    }

    override fun getLocationByCoordinates(
        lat: Double,
        lon: Double,
        count: Int,
        language: String
    ): Flow<GeocodingResponse?> = executeAsFlow<GeocodingApiResponse> {
        client.get {
            url {
                takeFrom(GEOCODING_BASE_URL)
                path("v1", "reverse")
                parameter("latitude", lat)
                parameter("longitude", lon)
                parameter("count", count.coerceIn(1, 100))
                parameter("language", language)
            }
        }
    }.map { response ->
        response.results.firstOrNull()
    }

    companion object {
        const val DEFAULT_BASE_URL: String = "https://api.open-meteo.com/"
        const val HISTORICAL_BASE_URL: String = "https://archive-api.open-meteo.com/"
        const val AIR_QUALITY_BASE_URL: String = "https://air-quality-api.open-meteo.com/"
        const val GEOCODING_BASE_URL: String = "https://geocoding-api.open-meteo.com/"
    }
}