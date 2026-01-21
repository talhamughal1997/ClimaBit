package com.talhapps.climabit.presentation.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.talhapps.climabit.core.ui.mvi.useMvi
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import org.koin.compose.viewmodel.koinViewModel
import kotlin.time.Clock

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherDetailsScreen(
    lat: Double,
    lon: Double,
    locationName: String? = null,
    locationCountry: String? = null,
    viewModel: WeatherDetailsViewModel = koinViewModel()
) {
    // Create location object from passed data if available
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
                    // Handle error
                }
            }
        }
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Weather Details") }
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
                    .padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Location Header
                item {
                    val locationName = state.location?.name
                        ?: "$lat $lon"
                    Text(
                        text = locationName,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                    state.location?.country?.let { country ->
                        Text(
                            text = country,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // Current Weather
                state.weather?.let { weather ->
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = MaterialTheme.shapes.large,
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = "Current Weather",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold
                                )
                                weather.current?.let { current ->
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            "Temperature:",
                                            style = MaterialTheme.typography.bodyLarge
                                        )
                                        Text(
                                            "${current.temperature2m?.toInt() ?: 0}°C",
                                            style = MaterialTheme.typography.bodyLarge,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            "Feels Like:",
                                            style = MaterialTheme.typography.bodyLarge
                                        )
                                        Text(
                                            "${current.apparentTemperature?.toInt() ?: 0}°C",
                                            style = MaterialTheme.typography.bodyLarge
                                        )
                                    }
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            "Humidity:",
                                            style = MaterialTheme.typography.bodyLarge
                                        )
                                        Text(
                                            "${current.relativeHumidity2m ?: 0}%",
                                            style = MaterialTheme.typography.bodyLarge
                                        )
                                    }
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            "Pressure:",
                                            style = MaterialTheme.typography.bodyLarge
                                        )
                                        Text(
                                            "${current.pressureMsl?.toInt() ?: 0} hPa",
                                            style = MaterialTheme.typography.bodyLarge
                                        )
                                    }
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            "Wind Speed:",
                                            style = MaterialTheme.typography.bodyLarge
                                        )
                                        Text(
                                            "${current.windSpeed10m ?: 0.0} km/h",
                                            style = MaterialTheme.typography.bodyLarge
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // Air Quality
                state.airQuality?.let { airQuality ->
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = MaterialTheme.shapes.large,
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = "Air Quality",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold
                                )
                                airQuality.current?.let { current ->
                                    current.europeanAqi?.let { aqi ->
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(
                                                "European AQI:",
                                                style = MaterialTheme.typography.bodyLarge
                                            )
                                            Text(
                                                "$aqi",
                                                style = MaterialTheme.typography.bodyLarge,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                    current.usAqi?.let { aqi ->
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(
                                                "US AQI:",
                                                style = MaterialTheme.typography.bodyLarge
                                            )
                                            Text(
                                                "$aqi",
                                                style = MaterialTheme.typography.bodyLarge,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                    current.pm25?.let { pm25 ->
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(
                                                "PM2.5:",
                                                style = MaterialTheme.typography.bodyLarge
                                            )
                                            Text(
                                                "${pm25.toInt()} μg/m³",
                                                style = MaterialTheme.typography.bodyLarge
                                            )
                                        }
                                    }
                                    current.pm10?.let { pm10 ->
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(
                                                "PM10:",
                                                style = MaterialTheme.typography.bodyLarge
                                            )
                                            Text(
                                                "${pm10.toInt()} μg/m³",
                                                style = MaterialTheme.typography.bodyLarge
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // Daily Forecast
                state.weather?.daily?.let { daily ->
                    item {
                        Text(
                            text = "7-Day Forecast",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    itemsIndexed(daily.time.take(7)) { index, timeStr ->
                        // Convert date string to day name
                        val dayName = try {
                            val date = LocalDate.parse(timeStr)
                            val timeZone = TimeZone.currentSystemDefault()
                            val today = Clock.System.now().toLocalDateTime(timeZone).date

                            when {
                                date == today -> "Today"
                                date == today.plus(
                                    1,
                                    kotlinx.datetime.DateTimeUnit.DAY
                                ) -> "Tomorrow"

                                else -> {
                                    // Get day of week name - format enum name to readable format
                                    date.dayOfWeek.name.lowercase()
                                        .replaceFirstChar { it.uppercaseChar() }
                                }
                            }
                        } catch (e: Exception) {
                            timeStr
                        }
                        
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = MaterialTheme.shapes.medium,
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = dayName,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    daily.temperature2mMin.getOrNull(index)?.let { min ->
                                        Text(
                                            "${min.toInt()}°",
                                            style = MaterialTheme.typography.bodyLarge,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                    daily.temperature2mMax.getOrNull(index)?.let { max ->
                                        Text(
                                            "${max.toInt()}°",
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

