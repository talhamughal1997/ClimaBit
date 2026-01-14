package com.talhapps.climabit.domain.model.weather

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Models for OpenWeather forecast APIs.
 * Docs: https://openweathermap.org/forecast5 and https://openweathermap.org/forecast16
 */

// 5 day / 3 hour forecast
@Serializable
data class Forecast5Response(
    @SerialName("cod") val cod: String? = null,
    @SerialName("message") val message: Double? = null,
    @SerialName("cnt") val cnt: Int? = null,
    @SerialName("list") val list: List<ForecastItem> = emptyList(),
    @SerialName("city") val city: City? = null
) {
    @Serializable
    data class ForecastItem(
        @SerialName("dt") val dt: Long? = null,
        @SerialName("main") val main: CurrentWeatherResponse.MainWeather? = null,
        @SerialName("weather") val weather: List<CurrentWeatherResponse.WeatherInfo> = emptyList(),
        @SerialName("clouds") val clouds: CurrentWeatherResponse.Clouds? = null,
        @SerialName("wind") val wind: CurrentWeatherResponse.Wind? = null,
        @SerialName("visibility") val visibility: Int? = null,
        @SerialName("pop") val pop: Double? = null,
        @SerialName("dt_txt") val dtTxt: String? = null
    )

    @Serializable
    data class City(
        @SerialName("id") val id: Long? = null,
        @SerialName("name") val name: String? = null,
        @SerialName("country") val country: String? = null,
        @SerialName("timezone") val timezone: Int? = null
    )
}

// 16 day daily forecast (simplified)
@Serializable
data class Forecast16Response(
    @SerialName("cod") val cod: String? = null,
    @SerialName("message") val message: Double? = null,
    @SerialName("cnt") val cnt: Int? = null,
    @SerialName("list") val list: List<DailyItem> = emptyList(),
    @SerialName("city") val city: Forecast5Response.City? = null
) {
    @Serializable
    data class DailyItem(
        @SerialName("dt") val dt: Long? = null,
        @SerialName("temp") val temp: OneCallResponse.Temperature? = null,
        @SerialName("feels_like") val feelsLike: OneCallResponse.FeelsLike? = null,
        @SerialName("pressure") val pressure: Int? = null,
        @SerialName("humidity") val humidity: Int? = null,
        @SerialName("weather") val weather: List<OneCallResponse.WeatherInfo> = emptyList(),
        @SerialName("speed") val speed: Double? = null,
        @SerialName("deg") val deg: Int? = null,
        @SerialName("clouds") val clouds: Int? = null,
        @SerialName("pop") val pop: Double? = null
    )
}


