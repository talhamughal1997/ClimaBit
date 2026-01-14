package com.talhapps.climabit.domain.model.weather


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WeatherResponse(
    @SerialName("count")
    val count: String? = "",
    @SerialName("data")
    val `data`: List<Data?>? = listOf()
) {
    @Serializable
    data class Data(
        @SerialName("app_temp")
        val appTemp: Double? = 0.0,
        @SerialName("aqi")
        val aqi: Int? = 0,
        @SerialName("city_name")
        val cityName: String? = "",
        @SerialName("clouds")
        val clouds: Int? = 0,
        @SerialName("country_code")
        val countryCode: String? = "",
        @SerialName("datetime")
        val datetime: String? = "",
        @SerialName("dewpt")
        val dewpt: Int? = 0,
        @SerialName("dhi")
        val dhi: Double? = 0.0,
        @SerialName("dni")
        val dni: Double? = 0.0,
        @SerialName("elev_angle")
        val elevAngle: Int? = 0,
        @SerialName("ghi")
        val ghi: Double? = 0.0,
        @SerialName("gust")
        val gust: Int? = 0,
        @SerialName("hour_angle")
        val hourAngle: Int? = 0,
        @SerialName("lat")
        val lat: Int? = 0,
        @SerialName("lon")
        val lon: Double? = 0.0,
        @SerialName("ob_time")
        val obTime: String? = "",
        @SerialName("pod")
        val pod: String? = "",
        @SerialName("precip")
        val precip: Int? = 0,
        @SerialName("pres")
        val pres: Int? = 0,
        @SerialName("rh")
        val rh: Int? = 0,
        @SerialName("slp")
        val slp: Double? = 0.0,
        @SerialName("snow")
        val snow: Int? = 0,
        @SerialName("solar_rad")
        val solarRad: Double? = 0.0,
        @SerialName("sources")
        val sources: List<String?>? = listOf(),
        @SerialName("state_code")
        val stateCode: String? = "",
        @SerialName("station")
        val station: String? = "",
        @SerialName("sunrise")
        val sunrise: String? = "",
        @SerialName("sunset")
        val sunset: String? = "",
        @SerialName("temp")
        val temp: Double? = 0.0,
        @SerialName("timezone")
        val timezone: String? = "",
        @SerialName("ts")
        val ts: Int? = 0,
        @SerialName("uv")
        val uv: Double? = 0.0,
        @SerialName("vis")
        val vis: Double? = 0.0,
        @SerialName("weather")
        val weather: Weather? = Weather(),
        @SerialName("wind_cdir")
        val windCdir: String? = "",
        @SerialName("wind_cdir_full")
        val windCdirFull: String? = "",
        @SerialName("wind_dir")
        val windDir: Int? = 0,
        @SerialName("wind_speed")
        val windSpeed: Double? = 0.0
    ) {
        @Serializable
        data class Weather(
            @SerialName("code")
            val code: Int? = 0,
            @SerialName("description")
            val description: String? = "",
            @SerialName("icon")
            val icon: String? = ""
        )
    }
}