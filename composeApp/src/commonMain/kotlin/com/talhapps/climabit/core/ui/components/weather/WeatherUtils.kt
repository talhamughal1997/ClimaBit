package com.talhapps.climabit.core.ui.components.weather

fun getWeatherIcon(code: Int?, isDay: Int): String? {
    if (code == null) return null
    val dayNight = if (isDay == 1) "d" else "n"
    return when (code) {
        0 -> "01$dayNight"
        in 1..3 -> "02$dayNight"
        45, 48 -> "50$dayNight"
        in 51..57 -> "09$dayNight"
        in 61..67 -> "10$dayNight"
        in 71..77 -> "13$dayNight"
        in 80..82 -> "09$dayNight"
        in 85..86 -> "13$dayNight"
        95, 96, 99 -> "11$dayNight"
        else -> "01$dayNight"
    }
}

fun getWeatherDescription(code: Int?): String {
    if (code == null) return "Unknown"
    return when (code) {
        0 -> "Clear sky"
        1 -> "Mainly clear"
        2 -> "Partly cloudy"
        3 -> "Overcast"
        45 -> "Fog"
        48 -> "Depositing rime fog"
        51 -> "Light drizzle"
        53 -> "Moderate drizzle"
        55 -> "Dense drizzle"
        56 -> "Light freezing drizzle"
        57 -> "Dense freezing drizzle"
        61 -> "Slight rain"
        63 -> "Moderate rain"
        65 -> "Heavy rain"
        66 -> "Light freezing rain"
        67 -> "Heavy freezing rain"
        71 -> "Slight snow fall"
        73 -> "Moderate snow fall"
        75 -> "Heavy snow fall"
        77 -> "Snow grains"
        80 -> "Slight rain showers"
        81 -> "Moderate rain showers"
        82 -> "Violent rain showers"
        85 -> "Slight snow showers"
        86 -> "Heavy snow showers"
        95 -> "Thunderstorm"
        96 -> "Thunderstorm with slight hail"
        99 -> "Thunderstorm with heavy hail"
        else -> "Unknown"
    }
}

