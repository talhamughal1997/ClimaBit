package com.talhapps.climabit.domain.model.weather

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Model for OpenWeather "Current weather data" response.
 * Docs: https://openweathermap.org/current
 */
@Serializable
data class CurrentWeatherResponse(
    @SerialName("coord") val coord: Coordinates? = null,
    @SerialName("weather") val weather: List<WeatherInfo> = emptyList(),
    @SerialName("base") val base: String? = null,
    @SerialName("main") val main: MainWeather? = null,
    @SerialName("visibility") val visibility: Int? = null,
    @SerialName("wind") val wind: Wind? = null,
    @SerialName("clouds") val clouds: Clouds? = null,
    @SerialName("dt") val dt: Long? = null,
    @SerialName("sys") val sys: SystemInfo? = null,
    @SerialName("timezone") val timezone: Int? = null,
    @SerialName("id") val id: Long? = null,
    @SerialName("name") val name: String? = null,
    @SerialName("cod") val cod: Int? = null
) {
    @Serializable
    data class Coordinates(
        @SerialName("lon") val lon: Double? = null,
        @SerialName("lat") val lat: Double? = null
    )

    @Serializable
    data class WeatherInfo(
        @SerialName("id") val id: Int? = null,
        @SerialName("main") val main: String? = null,
        @SerialName("description") val description: String? = null,
        @SerialName("icon") val icon: String? = null
    )

    @Serializable
    data class MainWeather(
        @SerialName("temp") val temp: Double? = null,
        @SerialName("feels_like") val feelsLike: Double? = null,
        @SerialName("temp_min") val tempMin: Double? = null,
        @SerialName("temp_max") val tempMax: Double? = null,
        @SerialName("pressure") val pressure: Int? = null,
        @SerialName("humidity") val humidity: Int? = null
    )

    @Serializable
    data class Wind(
        @SerialName("speed") val speed: Double? = null,
        @SerialName("deg") val deg: Int? = null,
        @SerialName("gust") val gust: Double? = null
    )

    @Serializable
    data class Clouds(
        @SerialName("all") val all: Int? = null
    )

    @Serializable
    data class SystemInfo(
        @SerialName("country") val country: String? = null,
        @SerialName("sunrise") val sunrise: Long? = null,
        @SerialName("sunset") val sunset: Long? = null
    )
}


