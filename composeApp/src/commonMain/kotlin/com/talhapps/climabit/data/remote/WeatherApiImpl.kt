package com.talhapps.climabit.data.remote

import com.talhapps.climabit.domain.model.weather.AirPollutionResponse
import com.talhapps.climabit.domain.model.weather.CurrentWeatherResponse
import com.talhapps.climabit.domain.model.weather.Forecast16Response
import com.talhapps.climabit.domain.model.weather.Forecast5Response
import com.talhapps.climabit.domain.model.weather.GeocodingResponse
import com.talhapps.climabit.domain.model.weather.OneCallResponse
import com.talhapps.climabit.domain.model.weather.ReverseGeocodingResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.path

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

    override suspend fun getCurrentWeatherByCoordinates(
        lat: Double,
        lon: Double
    ): CurrentWeatherResponse {
        return client.get(baseUrl) {
            url {
                path("data", "2.5", "weather")
                parameter("lat", lat)
                parameter("lon", lon)
                parameter("appid", apiKey)
            }
        }.body()
    }

    override suspend fun getCurrentWeatherByCityName(
        cityName: String,
        countryCode: String?
    ): CurrentWeatherResponse {
        val q = if (countryCode.isNullOrBlank()) cityName else "$cityName,$countryCode"
        return client.get(baseUrl) {
            url {
                path("data", "2.5", "weather")
                parameter("q", q)
                parameter("appid", apiKey)
            }
        }.body()
    }

    override suspend fun getOneCall(
        lat: Double,
        lon: Double,
        exclude: String?,
        units: String?,
        language: String?
    ): OneCallResponse {
        return client.get(baseUrl) {
            url {
                path("data", "3.0", "onecall")
                parameter("lat", lat)
                parameter("lon", lon)
                if (!exclude.isNullOrBlank()) parameter("exclude", exclude)
                if (!units.isNullOrBlank()) parameter("units", units)
                if (!language.isNullOrBlank()) parameter("lang", language)
                parameter("appid", apiKey)
            }
        }.body()
    }

    override suspend fun getOneCallHistorical(
        lat: Double,
        lon: Double,
        dt: Long,
        exclude: String?,
        units: String?,
        language: String?
    ): OneCallResponse {
        return client.get(baseUrl) {
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
        }.body()
    }

    override suspend fun getFiveDayForecastByCoordinates(
        lat: Double,
        lon: Double,
        units: String?,
        language: String?
    ): Forecast5Response {
        return client.get(baseUrl) {
            url {
                path("data", "2.5", "forecast")
                parameter("lat", lat)
                parameter("lon", lon)
                if (!units.isNullOrBlank()) parameter("units", units)
                if (!language.isNullOrBlank()) parameter("lang", language)
                parameter("appid", apiKey)
            }
        }.body()
    }

    override suspend fun getSixteenDayForecastByCoordinates(
        lat: Double,
        lon: Double,
        units: String?,
        language: String?
    ): Forecast16Response {
        return client.get(baseUrl) {
            url {
                // 16 day daily forecast (paid plan)
                path("data", "2.5", "forecast", "daily")
                parameter("lat", lat)
                parameter("lon", lon)
                if (!units.isNullOrBlank()) parameter("units", units)
                if (!language.isNullOrBlank()) parameter("lang", language)
                parameter("appid", apiKey)
            }
        }.body()
    }

    override suspend fun getCurrentAirPollution(
        lat: Double,
        lon: Double
    ): AirPollutionResponse {
        return client.get(baseUrl) {
            url {
                path("data", "2.5", "air_pollution")
                parameter("lat", lat)
                parameter("lon", lon)
                parameter("appid", apiKey)
            }
        }.body()
    }

    override suspend fun getForecastAirPollution(
        lat: Double,
        lon: Double
    ): AirPollutionResponse {
        return client.get(baseUrl) {
            url {
                path("data", "2.5", "air_pollution", "forecast")
                parameter("lat", lat)
                parameter("lon", lon)
                parameter("appid", apiKey)
            }
        }.body()
    }

    override suspend fun getHistoricalAirPollution(
        lat: Double,
        lon: Double,
        start: Long,
        end: Long
    ): AirPollutionResponse {
        return client.get(baseUrl) {
            url {
                path("data", "2.5", "air_pollution", "history")
                parameter("lat", lat)
                parameter("lon", lon)
                parameter("start", start)
                parameter("end", end)
                parameter("appid", apiKey)
            }
        }.body()
    }

    override suspend fun getGeocodingDirect(
        cityName: String,
        stateCode: String?,
        countryCode: String?,
        limit: Int?
    ): List<GeocodingResponse> {
        val q = buildString {
            append(cityName)
            if (!stateCode.isNullOrBlank()) append(",$stateCode")
            if (!countryCode.isNullOrBlank()) append(",$countryCode")
        }
        return client.get(baseUrl) {
            url {
                path("geo", "1.0", "direct")
                parameter("q", q)
                if (limit != null) parameter("limit", limit)
                parameter("appid", apiKey)
            }
        }.body()
    }

    override suspend fun getGeocodingReverse(
        lat: Double,
        lon: Double,
        limit: Int?
    ): List<ReverseGeocodingResponse> {
        return client.get(baseUrl) {
            url {
                path("geo", "1.0", "reverse")
                parameter("lat", lat)
                parameter("lon", lon)
                if (limit != null) parameter("limit", limit)
                parameter("appid", apiKey)
            }
        }.body()
    }

    companion object {
        const val DEFAULT_BASE_URL: String = "https://api.openweathermap.org/"
    }
}