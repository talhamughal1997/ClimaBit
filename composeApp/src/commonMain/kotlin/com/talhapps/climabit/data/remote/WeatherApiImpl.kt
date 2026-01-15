package com.talhapps.climabit.data.remote

import com.talhapps.climabit.data.core.executeAsFlow
import com.talhapps.climabit.domain.model.weather.AirPollutionResponse
import com.talhapps.climabit.domain.model.weather.CurrentWeatherResponse
import com.talhapps.climabit.domain.model.weather.Forecast16Response
import com.talhapps.climabit.domain.model.weather.Forecast5Response
import com.talhapps.climabit.domain.model.weather.GeocodingResponse
import com.talhapps.climabit.domain.model.weather.OneCallResponse
import com.talhapps.climabit.domain.model.weather.ReverseGeocodingResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.path
import kotlinx.coroutines.flow.Flow

/**
 * Default implementation backed by OpenWeather APIs.
 *
 * Base docs: [https://openweathermap.org/api](https://openweathermap.org/api)
 */
class WeatherApiImpl(
    private val client: HttpClient,
    private val apiKey: String,
    private val baseUrl: String = DEFAULT_BASE_URL
) : WeatherApi {

    override fun getCurrentWeatherByCoordinates(
        lat: Double,
        lon: Double
    ): Flow<CurrentWeatherResponse> = executeAsFlow {
        client.get(baseUrl) {
            url {
                path("data", "2.5", "weather")
                parameter("lat", lat)
                parameter("lon", lon)
                parameter("appid", apiKey)
            }
        }
    }

    override fun getCurrentWeatherByCityName(
        cityName: String,
        countryCode: String?
    ): Flow<CurrentWeatherResponse> = executeAsFlow {
        val q = if (countryCode.isNullOrBlank()) cityName else "$cityName,$countryCode"
        client.get(baseUrl) {
            url {
                path("data", "2.5", "weather")
                parameter("q", q)
                parameter("appid", apiKey)
            }
        }
    }

    override fun getOneCall(
        lat: Double,
        lon: Double,
        exclude: String?,
        units: String?,
        language: String?
    ): Flow<OneCallResponse> = executeAsFlow {
        client.get(baseUrl) {
            url {
                path("data", "3.0", "onecall")
                parameter("lat", lat)
                parameter("lon", lon)
                if (!exclude.isNullOrBlank()) parameter("exclude", exclude)
                if (!units.isNullOrBlank()) parameter("units", units)
                if (!language.isNullOrBlank()) parameter("lang", language)
                parameter("appid", apiKey)
            }
        }
    }

    override fun getOneCallHistorical(
        lat: Double,
        lon: Double,
        dt: Long,
        exclude: String?,
        units: String?,
        language: String?
    ): Flow<OneCallResponse> = executeAsFlow {
        client.get(baseUrl) {
            url {
                path("data", "3.0", "onecall", "timemachine")
                parameter("lat", lat)
                parameter("lon", lon)
                parameter("dt", dt)
                if (!exclude.isNullOrBlank()) parameter("exclude", exclude)
                if (!units.isNullOrBlank()) parameter("units", units)
                if (!language.isNullOrBlank()) parameter("lang", language)
                parameter("appid", apiKey)
            }
        }
    }

    override fun getFiveDayForecastByCoordinates(
        lat: Double,
        lon: Double,
        units: String?,
        language: String?
    ): Flow<Forecast5Response> = executeAsFlow {
        client.get(baseUrl) {
            url {
                path("data", "2.5", "forecast")
                parameter("lat", lat)
                parameter("lon", lon)
                if (!units.isNullOrBlank()) parameter("units", units)
                if (!language.isNullOrBlank()) parameter("lang", language)
                parameter("appid", apiKey)
            }
        }
    }

    override fun getSixteenDayForecastByCoordinates(
        lat: Double,
        lon: Double,
        units: String?,
        language: String?
    ): Flow<Forecast16Response> = executeAsFlow {
        client.get(baseUrl) {
            url {
                // 16 day daily forecast (paid plan)
                path("data", "2.5", "forecast", "daily")
                parameter("lat", lat)
                parameter("lon", lon)
                if (!units.isNullOrBlank()) parameter("units", units)
                if (!language.isNullOrBlank()) parameter("lang", language)
                parameter("appid", apiKey)
            }
        }
    }

    override suspend fun getCurrentAirPollution(
        lat: Double,
        lon: Double
    ): Flow<AirPollutionResponse> = executeAsFlow {
        client.get(baseUrl) {
            url {
                path("data", "2.5", "air_pollution")
                parameter("lat", lat)
                parameter("lon", lon)
                parameter("appid", apiKey)
            }
        }
    }

    override suspend fun getForecastAirPollution(
        lat: Double,
        lon: Double
    ): Flow<AirPollutionResponse> = executeAsFlow {
        client.get(baseUrl) {
            url {
                path("data", "2.5", "air_pollution", "forecast")
                parameter("lat", lat)
                parameter("lon", lon)
                parameter("appid", apiKey)
            }
        }
    }

    override suspend fun getHistoricalAirPollution(
        lat: Double,
        lon: Double,
        start: Long,
        end: Long
    ): Flow<AirPollutionResponse> = executeAsFlow {
        client.get(baseUrl) {
            url {
                path("data", "2.5", "air_pollution", "history")
                parameter("lat", lat)
                parameter("lon", lon)
                parameter("start", start)
                parameter("end", end)
                parameter("appid", apiKey)
            }
        }
    }

    override suspend fun getGeocodingDirect(
        cityName: String,
        stateCode: String?,
        countryCode: String?,
        limit: Int?
    ): Flow<List<GeocodingResponse>> = executeAsFlow {
        val q = buildString {
            append(cityName)
            if (!stateCode.isNullOrBlank()) append(",$stateCode")
            if (!countryCode.isNullOrBlank()) append(",$countryCode")
        }
        client.get(baseUrl) {
            url {
                path("geo", "1.0", "direct")
                parameter("q", q)
                if (limit != null) parameter("limit", limit)
                parameter("appid", apiKey)
            }
        }
    }

    override suspend fun getGeocodingReverse(
        lat: Double,
        lon: Double,
        limit: Int?
    ): Flow<List<ReverseGeocodingResponse>> = executeAsFlow {
        client.get(baseUrl) {
            url {
                path("geo", "1.0", "reverse")
                parameter("lat", lat)
                parameter("lon", lon)
                if (limit != null) parameter("limit", limit)
                parameter("appid", apiKey)
            }
        }
    }

    companion object {
        const val DEFAULT_BASE_URL: String = "https://api.openweathermap.org/"
    }
}