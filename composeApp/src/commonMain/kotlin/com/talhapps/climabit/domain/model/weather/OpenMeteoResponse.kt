package com.talhapps.climabit.domain.model.weather

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OpenMeteoResponse(
    @SerialName("latitude") val latitude: Double? = null,
    @SerialName("longitude") val longitude: Double? = null,
    @SerialName("elevation") val elevation: Double? = null,
    @SerialName("generationtime_ms") val generationTimeMs: Double? = null,
    @SerialName("utc_offset_seconds") val utcOffsetSeconds: Int? = null,
    @SerialName("timezone") val timezone: String? = null,
    @SerialName("timezone_abbreviation") val timezoneAbbreviation: String? = null,
    @SerialName("current") val current: CurrentWeather? = null,
    @SerialName("current_units") val currentUnits: CurrentUnits? = null,
    @SerialName("hourly") val hourly: HourlyData? = null,
    @SerialName("hourly_units") val hourlyUnits: HourlyUnits? = null,
    @SerialName("daily") val daily: DailyData? = null,
    @SerialName("daily_units") val dailyUnits: DailyUnits? = null
) {
    @Serializable
    data class CurrentWeather(
        @SerialName("time") val time: String? = null,
        @SerialName("interval") val interval: Int? = null,
        @SerialName("temperature_2m") val temperature2m: Double? = null,
        @SerialName("relative_humidity_2m") val relativeHumidity2m: Int? = null,
        @SerialName("apparent_temperature") val apparentTemperature: Double? = null,
        @SerialName("is_day") val isDay: Int? = null,
        @SerialName("precipitation") val precipitation: Double? = null,
        @SerialName("rain") val rain: Double? = null,
        @SerialName("showers") val showers: Double? = null,
        @SerialName("snowfall") val snowfall: Double? = null,
        @SerialName("weather_code") val weatherCode: Int? = null,
        @SerialName("cloud_cover") val cloudCover: Int? = null,
        @SerialName("pressure_msl") val pressureMsl: Double? = null,
        @SerialName("surface_pressure") val surfacePressure: Double? = null,
        @SerialName("wind_speed_10m") val windSpeed10m: Double? = null,
        @SerialName("wind_direction_10m") val windDirection10m: Int? = null,
        @SerialName("wind_gusts_10m") val windGusts10m: Double? = null
    )

    @Serializable
    data class CurrentUnits(
        @SerialName("temperature_2m") val temperature2m: String? = null,
        @SerialName("relative_humidity_2m") val relativeHumidity2m: String? = null,
        @SerialName("apparent_temperature") val apparentTemperature: String? = null,
        @SerialName("precipitation") val precipitation: String? = null,
        @SerialName("rain") val rain: String? = null,
        @SerialName("showers") val showers: String? = null,
        @SerialName("snowfall") val snowfall: String? = null,
        @SerialName("weather_code") val weatherCode: String? = null,
        @SerialName("cloud_cover") val cloudCover: String? = null,
        @SerialName("pressure_msl") val pressureMsl: String? = null,
        @SerialName("surface_pressure") val surfacePressure: String? = null,
        @SerialName("wind_speed_10m") val windSpeed10m: String? = null,
        @SerialName("wind_direction_10m") val windDirection10m: String? = null,
        @SerialName("wind_gusts_10m") val windGusts10m: String? = null
    )

    @Serializable
    data class HourlyData(
        @SerialName("time") val time: List<String> = emptyList(),
        @SerialName("temperature_2m") val temperature2m: List<Double?> = emptyList(),
        @SerialName("relative_humidity_2m") val relativeHumidity2m: List<Int?> = emptyList(),
        @SerialName("apparent_temperature") val apparentTemperature: List<Double?> = emptyList(),
        @SerialName("precipitation_probability") val precipitationProbability: List<Int?> = emptyList(),
        @SerialName("precipitation") val precipitation: List<Double?> = emptyList(),
        @SerialName("rain") val rain: List<Double?> = emptyList(),
        @SerialName("showers") val showers: List<Double?> = emptyList(),
        @SerialName("snowfall") val snowfall: List<Double?> = emptyList(),
        @SerialName("weather_code") val weatherCode: List<Int?> = emptyList(),
        @SerialName("cloud_cover") val cloudCover: List<Int?> = emptyList(),
        @SerialName("pressure_msl") val pressureMsl: List<Double?> = emptyList(),
        @SerialName("surface_pressure") val surfacePressure: List<Double?> = emptyList(),
        @SerialName("wind_speed_10m") val windSpeed10m: List<Double?> = emptyList(),
        @SerialName("wind_direction_10m") val windDirection10m: List<Int?> = emptyList(),
        @SerialName("wind_gusts_10m") val windGusts10m: List<Double?> = emptyList(),
        @SerialName("is_day") val isDay: List<Int?> = emptyList()
    )

    @Serializable
    data class HourlyUnits(
        @SerialName("temperature_2m") val temperature2m: String? = null,
        @SerialName("relative_humidity_2m") val relativeHumidity2m: String? = null,
        @SerialName("apparent_temperature") val apparentTemperature: String? = null,
        @SerialName("precipitation_probability") val precipitationProbability: String? = null,
        @SerialName("precipitation") val precipitation: String? = null,
        @SerialName("rain") val rain: String? = null,
        @SerialName("showers") val showers: String? = null,
        @SerialName("snowfall") val snowfall: String? = null,
        @SerialName("weather_code") val weatherCode: String? = null,
        @SerialName("cloud_cover") val cloudCover: String? = null,
        @SerialName("pressure_msl") val pressureMsl: String? = null,
        @SerialName("surface_pressure") val surfacePressure: String? = null,
        @SerialName("wind_speed_10m") val windSpeed10m: String? = null,
        @SerialName("wind_direction_10m") val windDirection10m: String? = null,
        @SerialName("wind_gusts_10m") val windGusts10m: String? = null,
        @SerialName("is_day") val isDay: String? = null
    )

    @Serializable
    data class DailyData(
        @SerialName("time") val time: List<String> = emptyList(),
        @SerialName("weather_code") val weatherCode: List<Int?> = emptyList(),
        @SerialName("temperature_2m_max") val temperature2mMax: List<Double?> = emptyList(),
        @SerialName("temperature_2m_min") val temperature2mMin: List<Double?> = emptyList(),
        @SerialName("apparent_temperature_max") val apparentTemperatureMax: List<Double?> = emptyList(),
        @SerialName("apparent_temperature_min") val apparentTemperatureMin: List<Double?> = emptyList(),
        @SerialName("sunrise") val sunrise: List<String?> = emptyList(),
        @SerialName("sunset") val sunset: List<String?> = emptyList(),
        @SerialName("daylight_duration") val daylightDuration: List<Double?> = emptyList(),
        @SerialName("sunshine_duration") val sunshineDuration: List<Double?> = emptyList(),
        @SerialName("uv_index_max") val uvIndexMax: List<Double?> = emptyList(),
        @SerialName("precipitation_sum") val precipitationSum: List<Double?> = emptyList(),
        @SerialName("rain_sum") val rainSum: List<Double?> = emptyList(),
        @SerialName("showers_sum") val showersSum: List<Double?> = emptyList(),
        @SerialName("snowfall_sum") val snowfallSum: List<Double?> = emptyList(),
        @SerialName("precipitation_hours") val precipitationHours: List<Double?> = emptyList(),
        @SerialName("precipitation_probability_max") val precipitationProbabilityMax: List<Int?> = emptyList(),
        @SerialName("wind_speed_10m_max") val windSpeed10mMax: List<Double?> = emptyList(),
        @SerialName("wind_gusts_10m_max") val windGusts10mMax: List<Double?> = emptyList(),
        @SerialName("wind_direction_10m_dominant") val windDirection10mDominant: List<Int?> = emptyList()
    )

    @Serializable
    data class DailyUnits(
        @SerialName("weather_code") val weatherCode: String? = null,
        @SerialName("temperature_2m_max") val temperature2mMax: String? = null,
        @SerialName("temperature_2m_min") val temperature2mMin: String? = null,
        @SerialName("apparent_temperature_max") val apparentTemperatureMax: String? = null,
        @SerialName("apparent_temperature_min") val apparentTemperatureMin: String? = null,
        @SerialName("sunrise") val sunrise: String? = null,
        @SerialName("sunset") val sunset: String? = null,
        @SerialName("daylight_duration") val daylightDuration: String? = null,
        @SerialName("sunshine_duration") val sunshineDuration: String? = null,
        @SerialName("uv_index_max") val uvIndexMax: String? = null,
        @SerialName("precipitation_sum") val precipitationSum: String? = null,
        @SerialName("rain_sum") val rainSum: String? = null,
        @SerialName("showers_sum") val showersSum: String? = null,
        @SerialName("snowfall_sum") val snowfallSum: String? = null,
        @SerialName("precipitation_hours") val precipitationHours: String? = null,
        @SerialName("precipitation_probability_max") val precipitationProbabilityMax: String? = null,
        @SerialName("wind_speed_10m_max") val windSpeed10mMax: String? = null,
        @SerialName("wind_gusts_10m_max") val windGusts10mMax: String? = null,
        @SerialName("wind_direction_10m_dominant") val windDirection10mDominant: String? = null
    )
}

