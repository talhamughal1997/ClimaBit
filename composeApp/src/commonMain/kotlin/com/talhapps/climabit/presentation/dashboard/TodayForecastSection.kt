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
import com.talhapps.climabit.core.ui.components.util.formatTime
import com.talhapps.climabit.core.ui.components.weather.getWeatherDescription
import com.talhapps.climabit.core.ui.components.weather.getWeatherIcon
import com.talhapps.climabit.domain.model.weather.OpenMeteoResponse
import kotlinx.datetime.LocalDateTime

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
        Text(
            text = "Today's Forecast",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 16.dp)
        )

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
        val normalizedTime = if (timeStr.length == 16) "${timeStr}:00" else timeStr
        val localDateTime = LocalDateTime.parse(normalizedTime)
        formatTime(localDateTime.hour, localDateTime.minute)
    } catch (e: Exception) {
        ""
    }

    val tempDisplay = temperature?.toInt()?.let { "${it}°" } ?: "--"
    val weatherIcon = getWeatherIcon(weatherCode, isDay)

    Card(
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
            Text(
                text = time,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            if (weatherIcon != null) {
                AsyncImage(
                    model = "https://openweathermap.org/img/wn/${weatherIcon}@2x.png",
                    contentDescription = getWeatherDescription(weatherCode),
                    modifier = Modifier.size(50.dp)
                )
            } else {
                Box(modifier = Modifier.size(40.dp))
            }

            Text(
                text = tempDisplay,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

