package com.talhapps.climabit.domain.model.weather

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Model for OpenWeather Air Pollution API.
 * Docs: https://openweathermap.org/api/air-pollution
 */
@Serializable
data class AirPollutionResponse(
    @SerialName("coord") val coord: Coordinates? = null,
    @SerialName("list") val list: List<AirPollutionData> = emptyList()
) {
    @Serializable
    data class Coordinates(
        @SerialName("lon") val lon: Double? = null,
        @SerialName("lat") val lat: Double? = null
    )

    @Serializable
    data class AirPollutionData(
        @SerialName("dt") val dt: Long? = null,
        @SerialName("main") val main: MainAirQuality? = null,
        @SerialName("components") val components: AirComponents? = null
    )

    @Serializable
    data class MainAirQuality(
        @SerialName("aqi") val aqi: Int? = null
    )

    @Serializable
    data class AirComponents(
        @SerialName("co") val co: Double? = null,
        @SerialName("no") val no: Double? = null,
        @SerialName("no2") val no2: Double? = null,
        @SerialName("o3") val o3: Double? = null,
        @SerialName("so2") val so2: Double? = null,
        @SerialName("pm2_5") val pm2_5: Double? = null,
        @SerialName("pm10") val pm10: Double? = null,
        @SerialName("nh3") val nh3: Double? = null
    )
}


