package com.talhapps.climabit.domain.model.weather

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AirQualityResponse(
    @SerialName("latitude") val latitude: Double? = null,
    @SerialName("longitude") val longitude: Double? = null,
    @SerialName("generationtime_ms") val generationTimeMs: Double? = null,
    @SerialName("utc_offset_seconds") val utcOffsetSeconds: Int? = null,
    @SerialName("timezone") val timezone: String? = null,
    @SerialName("timezone_abbreviation") val timezoneAbbreviation: String? = null,
    @SerialName("elevation") val elevation: Double? = null,
    @SerialName("current") val current: CurrentAirQuality? = null,
    @SerialName("current_units") val currentUnits: CurrentAirQualityUnits? = null,
    @SerialName("hourly") val hourly: HourlyAirQuality? = null,
    @SerialName("hourly_units") val hourlyUnits: HourlyAirQualityUnits? = null,
    @SerialName("daily") val daily: DailyAirQuality? = null,
    @SerialName("daily_units") val dailyUnits: DailyAirQualityUnits? = null
) {
    @Serializable
    data class CurrentAirQuality(
        @SerialName("time") val time: String? = null,
        @SerialName("interval") val interval: Int? = null,
        @SerialName("european_aqi") val europeanAqi: Int? = null,
        @SerialName("european_aqi_pm2_5") val europeanAqiPm25: Int? = null,
        @SerialName("european_aqi_pm10") val europeanAqiPm10: Int? = null,
        @SerialName("european_aqi_no2") val europeanAqiNo2: Int? = null,
        @SerialName("european_aqi_o3") val europeanAqiO3: Int? = null,
        @SerialName("european_aqi_so2") val europeanAqiSo2: Int? = null,
        @SerialName("us_aqi") val usAqi: Int? = null,
        @SerialName("us_aqi_pm2_5") val usAqiPm25: Int? = null,
        @SerialName("us_aqi_pm10") val usAqiPm10: Int? = null,
        @SerialName("us_aqi_no2") val usAqiNo2: Int? = null,
        @SerialName("us_aqi_o3") val usAqiO3: Int? = null,
        @SerialName("us_aqi_co") val usAqiCo: Double? = null,
        @SerialName("us_aqi_so2") val usAqiSo2: Int? = null,
        @SerialName("pm2_5") val pm25: Double? = null,
        @SerialName("pm10") val pm10: Double? = null,
        @SerialName("carbon_monoxide") val carbonMonoxide: Double? = null,
        @SerialName("nitrogen_dioxide") val nitrogenDioxide: Double? = null,
        @SerialName("sulphur_dioxide") val sulphurDioxide: Double? = null,
        @SerialName("ozone") val ozone: Double? = null,
        @SerialName("aerosol_optical_depth") val aerosolOpticalDepth: Double? = null,
        @SerialName("dust") val dust: Double? = null,
        @SerialName("uv_index") val uvIndex: Double? = null,
        @SerialName("uv_index_clear_sky") val uvIndexClearSky: Double? = null,
        @SerialName("ammonia") val ammonia: Double? = null,
        @SerialName("alder_pollen") val alderPollen: Double? = null,
        @SerialName("birch_pollen") val birchPollen: Double? = null,
        @SerialName("grass_pollen") val grassPollen: Double? = null,
        @SerialName("mugwort_pollen") val mugwortPollen: Double? = null,
        @SerialName("olive_pollen") val olivePollen: Double? = null,
        @SerialName("ragweed_pollen") val ragweedPollen: Double? = null
    )

    @Serializable
    data class CurrentAirQualityUnits(
        @SerialName("time") val time: String? = null,
        @SerialName("european_aqi") val europeanAqi: String? = null,
        @SerialName("us_aqi") val usAqi: String? = null,
        @SerialName("pm2_5") val pm25: String? = null,
        @SerialName("pm10") val pm10: String? = null,
        @SerialName("carbon_monoxide") val carbonMonoxide: String? = null,
        @SerialName("nitrogen_dioxide") val nitrogenDioxide: String? = null,
        @SerialName("sulphur_dioxide") val sulphurDioxide: String? = null,
        @SerialName("ozone") val ozone: String? = null
    )

    @Serializable
    data class HourlyAirQuality(
        @SerialName("time") val time: List<String> = emptyList(),
        @SerialName("european_aqi") val europeanAqi: List<Int?> = emptyList(),
        @SerialName("european_aqi_pm2_5") val europeanAqiPm25: List<Int?> = emptyList(),
        @SerialName("european_aqi_pm10") val europeanAqiPm10: List<Int?> = emptyList(),
        @SerialName("european_aqi_no2") val europeanAqiNo2: List<Int?> = emptyList(),
        @SerialName("european_aqi_o3") val europeanAqiO3: List<Int?> = emptyList(),
        @SerialName("us_aqi") val usAqi: List<Int?> = emptyList(),
        @SerialName("us_aqi_pm2_5") val usAqiPm25: List<Int?> = emptyList(),
        @SerialName("us_aqi_pm10") val usAqiPm10: List<Int?> = emptyList(),
        @SerialName("pm2_5") val pm25: List<Double?> = emptyList(),
        @SerialName("pm10") val pm10: List<Double?> = emptyList(),
        @SerialName("carbon_monoxide") val carbonMonoxide: List<Double?> = emptyList(),
        @SerialName("nitrogen_dioxide") val nitrogenDioxide: List<Double?> = emptyList(),
        @SerialName("sulphur_dioxide") val sulphurDioxide: List<Double?> = emptyList(),
        @SerialName("ozone") val ozone: List<Double?> = emptyList(),
        @SerialName("aerosol_optical_depth") val aerosolOpticalDepth: List<Double?> = emptyList(),
        @SerialName("dust") val dust: List<Double?> = emptyList(),
        @SerialName("uv_index") val uvIndex: List<Double?> = emptyList()
    )

    @Serializable
    data class HourlyAirQualityUnits(
        @SerialName("time") val time: String? = null,
        @SerialName("european_aqi") val europeanAqi: String? = null,
        @SerialName("us_aqi") val usAqi: String? = null,
        @SerialName("pm2_5") val pm25: String? = null,
        @SerialName("pm10") val pm10: String? = null
    )

    @Serializable
    data class DailyAirQuality(
        @SerialName("time") val time: List<String> = emptyList(),
        @SerialName("european_aqi_max") val europeanAqiMax: List<Int?> = emptyList(),
        @SerialName("european_aqi_pm2_5_max") val europeanAqiPm25Max: List<Int?> = emptyList(),
        @SerialName("european_aqi_pm10_max") val europeanAqiPm10Max: List<Int?> = emptyList(),
        @SerialName("us_aqi_max") val usAqiMax: List<Int?> = emptyList(),
        @SerialName("us_aqi_pm2_5_max") val usAqiPm25Max: List<Int?> = emptyList(),
        @SerialName("us_aqi_pm10_max") val usAqiPm10Max: List<Int?> = emptyList(),
        @SerialName("pm2_5_max") val pm25Max: List<Double?> = emptyList(),
        @SerialName("pm10_max") val pm10Max: List<Double?> = emptyList()
    )

    @Serializable
    data class DailyAirQualityUnits(
        @SerialName("time") val time: String? = null,
        @SerialName("european_aqi_max") val europeanAqiMax: String? = null,
        @SerialName("us_aqi_max") val usAqiMax: String? = null
    )
}

