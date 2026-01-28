package com.talhapps.climabit.presentation.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Directions
import androidx.compose.material.icons.filled.ShutterSpeed
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Water
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.filled.WbTwilight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.talhapps.climabit.core.ui.mvi.useMvi
import com.talhapps.climabit.domain.model.weather.AirQualityResponse
import com.talhapps.climabit.domain.model.weather.OpenMeteoResponse
import com.talhapps.climabit.presentation.dashboard.getWeatherDescription
import com.talhapps.climabit.presentation.dashboard.getWeatherIcon
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import org.koin.compose.viewmodel.koinViewModel
import kotlin.time.Clock
import kotlin.time.Instant

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherDetailsScreen(
    lat: Double,
    lon: Double,
    locationName: String? = null,
    locationCountry: String? = null,
    viewModel: WeatherDetailsViewModel = koinViewModel()
) {
    val location = if (locationName != null || locationCountry != null) {
        com.talhapps.climabit.domain.model.weather.GeocodingResponse(
            name = locationName,
            country = locationCountry,
            latitude = lat,
            longitude = lon
        )
    } else null

    val state by useMvi(
        viewModel = viewModel,
        initialIntent = WeatherDetailsIntent.LoadDetails(lat, lon, location),
        onEffect = { effect ->
            when (effect) {
                is WeatherDetailsEffect.ShowError -> {

                }
            }
        }
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Weather Details")
                        state.location?.let { loc ->
                            Text(
                                text = "${loc.name ?: ""}${if (loc.country != null) ", ${loc.country}" else ""}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        } ?: run {
                            Text(
                                text = "$lat, $lon",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        if (state.isAnyLoading && state.weather == null && state.airQuality == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .consumeWindowInsets(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(paddingValues)
                    .consumeWindowInsets(paddingValues),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                state.weather?.let { weather ->

                    item {
                        WeatherHeroSection(weather = weather)
                    }


                    item {
                        AdditionalInfo(weather)
                    }

                    item {
                        state.airQuality?.let { airQuality ->
                            AQIProgressSection(airQuality = airQuality)
                        }
                    }


                    item {
                        KeyMetricsRow(weather = weather)
                    }


                    item {
                        DetailedWeatherSection(weather = weather)
                    }


                    item {
                        weather.daily?.let { daily ->
                            SunTimesSection(
                                sunrise = daily.sunrise.firstOrNull(),
                                sunset = daily.sunset.firstOrNull(),
                                timezone = weather.timezone
                            )
                        }
                    }
                }


                state.weather?.daily?.let { daily ->
                    item {
                        Text(
                            text = "7-Day Forecast",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                    itemsIndexed(daily.time.take(7)) { index, timeStr ->
                        EnhancedForecastDayCard(
                            dateStr = timeStr,
                            minTemp = daily.temperature2mMin.getOrNull(index),
                            maxTemp = daily.temperature2mMax.getOrNull(index),
                            weatherCode = daily.weatherCode.getOrNull(index),
                            precipitation = daily.precipitationProbabilityMax.getOrNull(index),
                            timezone = state.weather?.timezone
                        )
                    }
                }
            }
        }
    }
}


@Composable
private fun WeatherHeroSection(weather: OpenMeteoResponse) {
    val current = weather.current
    val weatherCode = current?.weatherCode
    val weatherIcon = getWeatherIcon(weatherCode, current?.isDay ?: 1)
    val temperature = current?.temperature2m
    val feelsLike = current?.apparentTemperature
    val description = getWeatherDescription(weatherCode)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.extraLarge,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLowest
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            weatherIcon?.let { icon ->
                AsyncImage(
                    model = "https:
                    contentDescription = description,
                    modifier = Modifier.size(120.dp)
                )
            }


            Column(
                modifier = Modifier.weight(1f).padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                temperature?.let { temp ->
                    Text(
                        text = "${temp.toInt()}°",
                        style = MaterialTheme.typography.displayLarge,
                        fontWeight = FontWeight.Medium
                    )
                }
                Text(
                    text = description,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
                feelsLike?.let { feels ->
                    Text(
                        text = "Feels like ${feels.toInt()}°",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                    )
                }
            }


        }
    }
}

@Composable
private fun AdditionalInfo(weather: OpenMeteoResponse) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        weather.current?.relativeHumidity2m?.let { humidity ->
            MetricItem(
                modifier = Modifier.weight(1f),
                icon = Icons.Default.WaterDrop,
                value = "$humidity%",
                label = "Humidity"
            )
        }
        weather.current?.windSpeed10m?.let { wind ->
            MetricItem(
                modifier = Modifier.weight(1f),
                icon = Icons.Default.Air,
                value = "${wind.toInt()}km/h",
                label = "Wind Speed"
            )
        }
    }
}

@Composable
private fun MetricItem(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    value: String,
    label: String
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow),
        shape = MaterialTheme.shapes.large,
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                modifier = Modifier.size(20.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun AQIProgressSection(airQuality: AirQualityResponse) {
    val currentAqi = airQuality.current
    val europeanAqi = currentAqi?.europeanAqi
    val usAqi = currentAqi?.usAqi
    val aqiValue = europeanAqi ?: usAqi ?: 0
    val (aqiLabel, aqiColor) = getAQIInfo(aqiValue)
    val progress = (aqiValue / 300.0).coerceIn(0.0, 1.0)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLowest
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Air Quality Index",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = aqiLabel,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Medium,
                        color = aqiColor
                    )
                }
                Text(
                    text = "$aqiValue",
                    style = MaterialTheme.typography.displayMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = aqiColor
                )
            }


            LinearProgressIndicator(
                progress = { progress.toFloat() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp),
                color = aqiColor,
                trackColor = aqiColor.copy(alpha = 0.2f)
            )


            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                currentAqi?.pm25?.let { pm25 ->
                    AQIMetricItem("PM2.5", "${pm25.toInt()}", "μg/m³")
                }
                currentAqi?.pm10?.let { pm10 ->
                    AQIMetricItem("PM10", "${pm10.toInt()}", "μg/m³")
                }
            }
        }
    }
}

@Composable
private fun AQIMetricItem(label: String, value: String, unit: String) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = "$value $unit",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun KeyMetricsRow(weather: OpenMeteoResponse) {
    val current = weather.current
    val uvIndex = weather.daily?.uvIndexMax?.firstOrNull()

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        current?.pressureMsl?.let { pressure ->
            MetricCard(
                modifier = Modifier.weight(1f),
                icon = Icons.Default.Cloud,
                value = "${pressure.toInt()}",
                unit = "hPa",
                label = "Pressure"
            )
        }


        uvIndex?.let { uv ->
            MetricCard(
                modifier = Modifier.weight(1f),
                icon = Icons.Default.WbTwilight,
                value = "${uv.toInt()}",
                unit = "",
                label = "UV Index"
            )
        }


        current?.cloudCover?.let { clouds ->
            MetricCard(
                modifier = Modifier.weight(1f),
                icon = Icons.Default.Cloud,
                value = "$clouds",
                unit = "%",
                label = "Clouds"
            )
        }
    }
}

@Composable
private fun MetricCard(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    value: String,
    unit: String,
    label: String
) {
    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLowest
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                modifier = Modifier.size(28.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = value,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.SemiBold
                )
                if (unit.isNotEmpty()) {
                    Text(
                        text = unit,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun DetailedWeatherSection(weather: OpenMeteoResponse) {
    val current = weather.current

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLowest
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Weather Details",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Medium
            )



            current?.relativeHumidity2m?.let { humidity ->
                DetailRowWithProgress(
                    icon = Icons.Default.WaterDrop,
                    label = "Humidity",
                    value = "$humidity%",
                    progress = humidity / 100.0f
                )
            }


            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                current?.windSpeed10m?.let { speed ->
                    DetailItem(
                        icon = Icons.Default.Speed,
                        label = "Wind Speed",
                        value = "${speed.toInt()} km/h"
                    )
                }
                current?.windDirection10m?.let { direction ->
                    DetailItem(
                        icon = Icons.Default.Directions,
                        label = "Wind Direction",
                        value = "$direction°"
                    )
                }
            }


            current?.windGusts10m?.let { gusts ->
                DetailItem(
                    icon = Icons.Default.ShutterSpeed,
                    label = "Wind Gusts",
                    value = "${gusts.toInt()} km/h"
                )
            }
        }
    }
}

@Composable
private fun DetailRowWithProgress(
    icon: ImageVector,
    label: String,
    value: String,
    progress: Float
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    modifier = Modifier.size(24.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold
            )
        }
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp),
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant
        )
    }
}

@Composable
private fun DetailItem(
    icon: ImageVector,
    label: String,
    value: String
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            modifier = Modifier.size(24.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun SunTimesSection(
    sunrise: String?,
    sunset: String?,
    timezone: String?
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLowest
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            sunrise?.let { sunRise ->
                SunTimeItem(
                    icon = Icons.Default.WbSunny,
                    label = "Sunrise",
                    time = formatSunTime(sunRise, timezone)
                )
            }
            sunset?.let { sunSet ->
                SunTimeItem(
                    icon = Icons.Default.WbTwilight,
                    label = "Sunset",
                    time = formatSunTime(sunSet, timezone)
                )
            }
        }
    }
}

@Composable
private fun SunTimeItem(
    icon: ImageVector,
    label: String,
    time: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            modifier = Modifier.size(32.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = time,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun EnhancedForecastDayCard(
    dateStr: String,
    minTemp: Double?,
    maxTemp: Double?,
    weatherCode: Int?,
    precipitation: Int?,
    timezone: String?
) {
    val dayName = try {
        val date = LocalDate.parse(dateStr)
        val timeZone = TimeZone.of(timezone ?: "UTC")
        val today = Clock.System.now().toLocalDateTime(timeZone).date

        when {
            date == today -> "Today"
            date == today.plus(1, DateTimeUnit.DAY) -> "Tomorrow"
            else -> {
                date.dayOfWeek.name.lowercase()
                    .replaceFirstChar { it.uppercaseChar() }
            }
        }
    } catch (e: Exception) {
        dateStr
    }

    val weatherIcon = getWeatherIcon(weatherCode, isDay = 1)
    val weatherDescription = getWeatherDescription(weatherCode)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLowest
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            weatherIcon?.let { icon ->
                AsyncImage(
                    model = "https:
                    contentDescription = weatherDescription,
                    modifier = Modifier.size(56.dp)
                )
            } ?: Spacer(modifier = Modifier.size(56.dp))


            Column(
                modifier = Modifier.weight(1f).padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = dayName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = weatherDescription,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                precipitation?.let { precip ->
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Water,
                            contentDescription = "Precipitation",
                            modifier = Modifier.size(14.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "$precip%",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }


            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                maxTemp?.let { max ->
                    Text(
                        text = "${max.toInt()}°",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                minTemp?.let { min ->
                    Text(
                        text = "${min.toInt()}°",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

private fun formatSunTime(timeStr: String?, timezone: String?): String {
    if (timeStr == null) return "--:--"
    return try {
        val instant = Instant.parse(timeStr)
        val timeZone = TimeZone.of(timezone ?: "UTC")
        val localDateTime = instant.toLocalDateTime(timeZone)
        val hour = localDateTime.hour
        val minute = localDateTime.minute
        val period = if (hour < 12) "AM" else "PM"
        val displayHour = when {
            hour == 0 -> 12
            hour > 12 -> hour - 12
            else -> hour
        }
        val displayMinute = minute.toString().padStart(2, '0')
        "$displayHour:$displayMinute $period"
    } catch (e: Exception) {
        "--:--"
    }
}

@Composable
private fun getAQIInfo(aqi: Int): Pair<String, Color> {
    val colorScheme = MaterialTheme.colorScheme
    return when {
        aqi <= 50 -> Pair("Good", colorScheme.primary)
        aqi <= 100 -> Pair("Moderate", colorScheme.secondary)
        aqi <= 150 -> Pair("Unhealthy for Sensitive", colorScheme.tertiary)
        aqi <= 200 -> Pair("Unhealthy", colorScheme.error)
        aqi <= 300 -> Pair("Very Unhealthy", colorScheme.errorContainer)
        else -> Pair("Hazardous", colorScheme.error)
    }
}
