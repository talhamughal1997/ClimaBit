package com.talhapps.climabit.presentation.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.talhapps.climabit.domain.model.weather.OpenMeteoResponse
import kotlinx.datetime.LocalDateTime

/**
 * Today's Forecast section displaying hourly weather data
 */
@Composable
fun TodayForecastSection(
    hourlyData: OpenMeteoResponse.HourlyData,
    timezoneOffset: Int? = null,
    timezone: String? = null,
    modifier: Modifier = Modifier
) {
    if (hourlyData.time.isEmpty()) return

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        // Header
        Text(
            text = "Today's Forecast",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Hourly forecast list
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(horizontal = 4.dp)
        ) {
            itemsIndexed(hourlyData.time.take(24)) { index, timeStr ->
                HourlyForecastItem(
                    timeStr = timeStr,
                    temperature = hourlyData.temperature2m.getOrNull(index),
                    weatherCode = hourlyData.weatherCode.getOrNull(index),
                    isDay = hourlyData.isDay.getOrNull(index) ?: 1,
                    timezoneOffset = timezoneOffset,
                    timezone = timezone
                )
            }
        }
    }
}

@Composable
private fun HourlyForecastItem(
    timeStr: String,
    temperature: Double?,
    weatherCode: Int?,
    isDay: Int,
    timezoneOffset: Int? = null,
    timezone: String? = null
) {
    val time = try {
        // Parse ISO8601 format: "2022-07-01T00:00"
        val normalizedTime = if (timeStr.length == 16) "${timeStr}:00" else timeStr
        val localDateTime = LocalDateTime.parse(normalizedTime)
        formatTime(localDateTime.hour, localDateTime.minute)
    } catch (e: Exception) {
        ""
    }

    val tempDisplay = temperature?.toInt()?.let { "${it}°" } ?: "--"
    val weatherIcon = getWeatherIcon(weatherCode, isDay)

    Card(
        modifier = Modifier
            .size(width = 80.dp, height = 120.dp),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Time
            Text(
                text = time,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            // Weather Icon
            if (weatherIcon != null) {
                AsyncImage(
                    model = "https://openweathermap.org/img/wn/${weatherIcon}@2x.png",
                    contentDescription = getWeatherDescription(weatherCode),
                    modifier = Modifier.size(40.dp)
                )
            } else {
                Box(modifier = Modifier.size(40.dp))
            }

            // Temperature
            Text(
                text = tempDisplay,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

/**
 * Format time in 12-hour format (e.g., "6:00 AM", "12:00 PM")
 */
private fun formatTime(hour: Int, minute: Int): String {
    val period = if (hour < 12) "AM" else "PM"
    val displayHour = when {
        hour == 0 -> 12
        hour > 12 -> hour - 12
        else -> hour
    }
    val displayMinute = if (minute == 0) "" else ":${minute.toString().padStart(2, '0')}"
    return "$displayHour$displayMinute $period"
}

/**
 * Get weather icon code from WMO weather code
 */
fun getWeatherIcon(code: Int?, isDay: Int): String? {
    if (code == null) return null
    val dayNight = if (isDay == 1) "d" else "n"
    return when (code) {
        0 -> "01$dayNight" // Clear sky
        in 1..3 -> "02$dayNight" // Partly cloudy
        45, 48 -> "50$dayNight" // Fog
        in 51..57 -> "09$dayNight" // Drizzle
        in 61..67 -> "10$dayNight" // Rain
        in 71..77 -> "13$dayNight" // Snow
        in 80..82 -> "09$dayNight" // Rain showers
        in 85..86 -> "13$dayNight" // Snow showers
        95, 96, 99 -> "11$dayNight" // Thunderstorm
        else -> "01$dayNight"
    }
}

/**
 * Get weather description from WMO weather code
 */
internal fun getWeatherDescription(code: Int?): String {
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

