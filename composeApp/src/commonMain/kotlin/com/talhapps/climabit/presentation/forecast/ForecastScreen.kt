package com.talhapps.climabit.presentation.forecast

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Water
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.filled.WbTwilight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.talhapps.climabit.core.ui.mvi.useMvi
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

@Composable
fun ForecastScreen(
    onForecastItemSelected: (Double, Double, String?, String?) -> Unit = { _, _, _, _ -> },
    viewModel: ForecastViewModel = koinViewModel()
) {
    val state by useMvi(
        viewModel = viewModel,
        initialIntent = ForecastIntent.LoadForecast,
        onEffect = { effect ->
            when (effect) {
                is ForecastEffect.ShowError -> {
                    // Handle error
                }
                is ForecastEffect.NavigateToDetails -> {
                    onForecastItemSelected(
                        effect.lat,
                        effect.lon,
                        effect.location?.name,
                        effect.location?.country
                    )
                }
            }
        }
    )

    ForecastContent(
        state = state,
        onDaySelected = { index ->
            viewModel.handleIntent(ForecastIntent.SelectDay(index))
        },
        onItemClick = { index ->
            val lat = state.forecast?.latitude ?: 24.8607
            val lon = state.forecast?.longitude ?: 67.0011
            viewModel.handleIntent(ForecastIntent.SelectForecastItem(lat, lon))
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ForecastContent(
    state: ForecastState,
    onDaySelected: (Int) -> Unit,
    onItemClick: (Int) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("7-Day Forecast")
                        // Location name
                        state.location?.let { location ->
                            Text(
                                text = "${location.name ?: ""}${if (location.country != null) ", ${location.country}" else ""}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        } ?: state.forecast?.let { forecast ->
                            Text(
                                text = "${forecast.latitude ?: ""}, ${forecast.longitude ?: ""}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        if (state.isLoading && state.forecast == null) {
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
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                state.forecast?.let { forecast ->
                    val selectedIndex = state.selectedDayIndex
                    val dailyData = forecast.daily

                    // Selected Day Weather Card
                    item {
                        SelectedDayWeatherCard(
                            forecast = forecast,
                            dayIndex = selectedIndex
                        )
                    }

                    // AQI Card
                    item {
                        state.airQuality?.let { airQuality ->
                            AQICard(airQuality = airQuality, dayIndex = selectedIndex)
                        }
                    }

                    // Weather Info
                    item {
                        WeatherInfoCard(
                            weatherCode = dailyData?.weatherCode?.getOrNull(selectedIndex),
                            description = getWeatherDescription(
                                dailyData?.weatherCode?.getOrNull(selectedIndex)
                            )
                        )
                    }

                    // Day Selection Tab Bar
                    item {
                        DaySelectionTabBar(
                            forecast = forecast,
                            selectedIndex = selectedIndex,
                            onDaySelected = onDaySelected
                        )
                    }

                    // Weather Details Grid
                    item {
                        WeatherDetailsGrid(
                            forecast = forecast,
                            dayIndex = selectedIndex
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SelectedDayWeatherCard(
    forecast: OpenMeteoResponse,
    dayIndex: Int
) {
    val dailyData = forecast.daily
    val dateStr = dailyData?.time?.getOrNull(dayIndex) ?: ""
    val maxTemp = dailyData?.temperature2mMax?.getOrNull(dayIndex)
    val minTemp = dailyData?.temperature2mMin?.getOrNull(dayIndex)
    val weatherCode = dailyData?.weatherCode?.getOrNull(dayIndex)
    val weatherIcon = getWeatherIcon(weatherCode, isDay = 1)

    val dayName = try {
        val date = LocalDate.parse(dateStr)
        val timeZone = TimeZone.of(forecast.timezone ?: "UTC")
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

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.extraLarge,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHighest
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = dayName,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            weatherIcon?.let { icon ->
                AsyncImage(
                    model = "https://openweathermap.org/img/wn/${icon}@4x.png",
                    contentDescription = getWeatherDescription(weatherCode),
                    modifier = Modifier.size(120.dp)
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                minTemp?.let { min ->
                    Text(
                        text = "${min.toInt()}°",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Text(
                    text = "|",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                )
                maxTemp?.let { max ->
                    Text(
                        text = "${max.toInt()}°",
                        style = MaterialTheme.typography.displaySmall,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun AQICard(
    airQuality: com.talhapps.climabit.domain.model.weather.AirQualityResponse,
    dayIndex: Int
) {
    val dailyAqi = airQuality.daily
    val europeanAqi = dailyAqi?.europeanAqiMax?.getOrNull(dayIndex)
    val usAqi = dailyAqi?.usAqiMax?.getOrNull(dayIndex)

    val aqiValue = europeanAqi ?: usAqi ?: 0
    val (aqiLabel, aqiColor) = getAQIInfo(aqiValue)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = aqiColor.copy(alpha = 0.2f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Air Quality Index",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = aqiLabel,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = aqiColor
                )
            }
            Icon(
                imageVector = Icons.Default.Air,
                contentDescription = "Air Quality",
                modifier = Modifier.size(48.dp),
                tint = aqiColor
            )
        }
    }
}

@Composable
private fun WeatherInfoCard(
    weatherCode: Int?,
    description: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = description,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium
            )
            Icon(
                imageVector = Icons.Default.Cloud,
                contentDescription = "Weather",
                modifier = Modifier.size(32.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun DaySelectionTabBar(
    forecast: OpenMeteoResponse,
    selectedIndex: Int,
    onDaySelected: (Int) -> Unit
) {
    val dailyData = forecast.daily
    val timeZone = TimeZone.of(forecast.timezone ?: "UTC")
    val today = Clock.System.now().toLocalDateTime(timeZone).date

    // Get days starting from tomorrow (index 1) to 7 days
    val days = dailyData?.time?.take(7)?.mapIndexedNotNull { index, dateStr ->
        if (index == 0) null else { // Skip today
            try {
                val date = LocalDate.parse(dateStr)
                val dayName = when {
                    date == today.plus(1, DateTimeUnit.DAY) -> "Tomorrow"
                    else -> {
                        date.dayOfWeek.name.lowercase()
                            .replaceFirstChar { it.uppercaseChar() }
                            .take(3) // Short form: Mon, Tue, etc.
                    }
                }
                Pair(index, dayName)
            } catch (e: Exception) {
                null
            }
        }
    }?.filterNotNull() ?: emptyList()

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 4.dp)
    ) {
        itemsIndexed(days) { listIndex, (dayIndex, dayName) ->
            val isSelected = dayIndex == selectedIndex
            Card(
                modifier = Modifier
                    .clickable { onDaySelected(dayIndex) },
                shape = MaterialTheme.shapes.medium,
                colors = CardDefaults.cardColors(
                    containerColor = if (isSelected) {
                        MaterialTheme.colorScheme.primaryContainer
                    } else {
                        MaterialTheme.colorScheme.surfaceContainerLow
                    }
                )
            ) {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = dayName,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        color = if (isSelected) {
                            MaterialTheme.colorScheme.onPrimaryContainer
                        } else {
                            MaterialTheme.colorScheme.onSurfaceVariant
                        }
                    )

                }
            }
        }
    }
}

@Composable
private fun WeatherDetailsGrid(
    forecast: OpenMeteoResponse,
    dayIndex: Int
) {
    val dailyData = forecast.daily
    val hourlyData = forecast.hourly
    val currentData = forecast.current

    val feelsLike = dailyData?.apparentTemperatureMax?.getOrNull(dayIndex)
    // Use hourly data for humidity (get max from hourly data for the selected day)
    val humidity = if (dayIndex == 0 && currentData != null) {
        currentData.relativeHumidity2m
    } else {
        // Get max humidity from hourly data for the selected day (24 hours per day)
        hourlyData?.relativeHumidity2m?.let { humidityList ->
            val startIndex = dayIndex * 24
            val endIndex = minOf(startIndex + 24, humidityList.size)
            if (startIndex < humidityList.size) {
                humidityList.subList(startIndex, endIndex).maxOfOrNull { it ?: 0 }
            } else null
        }
    }
    val rainChance = dailyData?.precipitationProbabilityMax?.getOrNull(dayIndex)
    val windSpeed = dailyData?.windSpeed10mMax?.getOrNull(dayIndex)
    val uvIndex = dailyData?.uvIndexMax?.getOrNull(dayIndex)
    // Use hourly data for pressure (get average from hourly data for the selected day)
    val pressure = if (dayIndex == 0 && currentData != null) {
        currentData.pressureMsl
    } else {
        // Get average pressure from hourly data for the selected day
        hourlyData?.pressureMsl?.let { pressureList ->
            val startIndex = dayIndex * 24
            val endIndex = minOf(startIndex + 24, pressureList.size)
            if (startIndex < pressureList.size) {
                val dayPressures = pressureList.subList(startIndex, endIndex).filterNotNull()
                if (dayPressures.isNotEmpty()) {
                    dayPressures.average()
                } else null
            } else null
        }
    }
    dailyData?.sunrise?.getOrNull(dayIndex)
    dailyData?.sunset?.getOrNull(dayIndex)

    val details = listOfNotNull(
        feelsLike?.let { WeatherDetail("Feels Like", "${it.toInt()}°", Icons.Default.WbSunny) },
        humidity?.let { WeatherDetail("Humidity", "$it%", Icons.Default.WaterDrop) },
        rainChance?.let { WeatherDetail("Rain Chance", "$it%", Icons.Default.Water) },
        windSpeed?.let { WeatherDetail("Wind Speed", "${it.toInt()} km/h", Icons.Default.Air) },
        uvIndex?.let { WeatherDetail("UV Index", "${it.toInt()}", Icons.Default.WbTwilight) },
        pressure?.let { WeatherDetail("Pressure", "${it.toInt()} hPa", Icons.Default.Cloud) }
    )

    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Weather Details",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        // Grid layout (2 columns)
        details.chunked(2).forEach { rowItems ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                rowItems.forEach { detail ->
                    WeatherDetailCard(
                        modifier = Modifier.weight(1f),
                        detail = detail
                    )
                }
                // Fill empty space if odd number of items
                if (rowItems.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun WeatherDetailCard(
    modifier: Modifier = Modifier,
    detail: WeatherDetail
) {
    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
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
                imageVector = detail.icon,
                contentDescription = detail.label,
                modifier = Modifier.size(32.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Text(
                text = detail.value,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = detail.label,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}

private data class WeatherDetail(
    val label: String,
    val value: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)

private fun getAQIInfo(aqi: Int): Pair<String, Color> {
    return when {
        aqi <= 50 -> Pair("Good", Color(0xFF4CAF50))
        aqi <= 100 -> Pair("Moderate", Color(0xFFFFEB3B))
        aqi <= 150 -> Pair("Unhealthy for Sensitive", Color(0xFFFF9800))
        aqi <= 200 -> Pair("Unhealthy", Color(0xFFFF5722))
        aqi <= 300 -> Pair("Very Unhealthy", Color(0xFF9C27B0))
        else -> Pair("Hazardous", Color(0xFFE91E63))
    }
}
