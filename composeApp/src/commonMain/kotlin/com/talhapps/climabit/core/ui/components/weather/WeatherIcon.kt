package com.talhapps.climabit.core.ui.components.weather

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage

@Composable
fun WeatherIcon(
    weatherCode: Int?,
    isDay: Int,
    size: androidx.compose.ui.unit.Dp = 100.dp,
    iconSize: String = "@4x",
    modifier: Modifier = Modifier
) {
    val weatherIcon = getWeatherIcon(weatherCode, isDay)
    val weatherDescription = getWeatherDescription(weatherCode)

    if (weatherIcon != null) {
        AsyncImage(
            model = "https://openweathermap.org/img/wn/${weatherIcon}${iconSize}.png",
            contentDescription = weatherDescription,
            modifier = modifier.size(size)
        )
    } else {
        Box(modifier = modifier.size(size))
    }
}

