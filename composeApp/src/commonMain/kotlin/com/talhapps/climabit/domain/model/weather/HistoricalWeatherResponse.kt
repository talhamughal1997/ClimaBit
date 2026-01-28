package com.talhapps.climabit.domain.model.weather

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HistoricalWeatherResponse(
    @SerialName("latitude") val latitude: Double? = null,
    @SerialName("longitude") val longitude: Double? = null,
    @SerialName("elevation") val elevation: Double? = null,
    @SerialName("generationtime_ms") val generationTimeMs: Double? = null,
    @SerialName("utc_offset_seconds") val utcOffsetSeconds: Int? = null,
    @SerialName("timezone") val timezone: String? = null,
    @SerialName("timezone_abbreviation") val timezoneAbbreviation: String? = null,
    @SerialName("hourly") val hourly: HistoricalHourlyData? = null,
    @SerialName("hourly_units") val hourlyUnits: HistoricalHourlyUnits? = null,
    @SerialName("daily") val daily: HistoricalDailyData? = null,
    @SerialName("daily_units") val dailyUnits: HistoricalDailyUnits? = null
) {
    @Serializable
    data class HistoricalHourlyData(
        @SerialName("time") val time: List<String> = emptyList(),
        @SerialName("temperature_2m") val temperature2m: List<Double?> = emptyList(),
        @SerialName("relative_humidity_2m") val relativeHumidity2m: List<Int?> = emptyList(),
        @SerialName("precipitation") val precipitation: List<Double?> = emptyList(),
        @SerialName("weather_code") val weatherCode: List<Int?> = emptyList(),
        @SerialName("pressure_msl") val pressureMsl: List<Double?> = emptyList(),
        @SerialName("wind_speed_10m") val windSpeed10m: List<Double?> = emptyList(),
        @SerialName("wind_direction_10m") val windDirection10m: List<Int?> = emptyList()
    )

    @Serializable
    data class HistoricalHourlyUnits(
        @SerialName("temperature_2m") val temperature2m: String? = null,
        @SerialName("relative_humidity_2m") val relativeHumidity2m: String? = null,
        @SerialName("precipitation") val precipitation: String? = null,
        @SerialName("weather_code") val weatherCode: String? = null,
        @SerialName("pressure_msl") val pressureMsl: String? = null,
        @SerialName("wind_speed_10m") val windSpeed10m: String? = null,
        @SerialName("wind_direction_10m") val windDirection10m: String? = null
    )

    @Serializable
    data class HistoricalDailyData(
        @SerialName("time") val time: List<String> = emptyList(),
        @SerialName("weather_code") val weatherCode: List<Int?> = emptyList(),
        @SerialName("temperature_2m_max") val temperature2mMax: List<Double?> = emptyList(),
        @SerialName("temperature_2m_min") val temperature2mMin: List<Double?> = emptyList(),
        @SerialName("precipitation_sum") val precipitationSum: List<Double?> = emptyList(),
        @SerialName("wind_speed_10m_max") val windSpeed10mMax: List<Double?> = emptyList()
    )

    @Serializable
    data class HistoricalDailyUnits(
        @SerialName("weather_code") val weatherCode: String? = null,
        @SerialName("temperature_2m_max") val temperature2mMax: String? = null,
        @SerialName("temperature_2m_min") val temperature2mMin: String? = null,
        @SerialName("precipitation_sum") val precipitationSum: String? = null,
        @SerialName("wind_speed_10m_max") val windSpeed10mMax: String? = null
    )
}

