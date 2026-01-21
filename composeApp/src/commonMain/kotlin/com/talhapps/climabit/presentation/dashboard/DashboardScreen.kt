package com.talhapps.climabit.presentation.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.talhapps.climabit.core.ui.mvi.useMvi
import com.talhapps.climabit.domain.model.weather.OpenMeteoResponse
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun DashboardScreen(
    onLocationSelected: (Double, Double, String?, String?) -> Unit = { _, _, _, _ -> },
    onSettingsClick: () -> Unit = {},
    viewModel: DashboardViewModel = koinViewModel()
) {
    val state by useMvi(
        viewModel = viewModel, initialIntent = DashboardIntent.LoadWeather, onEffect = { effect ->
            when (effect) {
                is DashboardEffect.ShowError -> {
                    // Handle error (could show snackbar)
                }

                is DashboardEffect.ShowMessage -> {
                    // Handle message
                }

                is DashboardEffect.NavigateToSettings -> {
                    onSettingsClick()
                }
            }
        })

    DashboardContent(
        state = state,
        onRefresh = { viewModel.handleIntent(DashboardIntent.RefreshWeather) },
        onLocationClick = {
            state.weather?.let { weather ->
                val lat = weather.latitude ?: 0.0
                val lon = weather.longitude ?: 0.0
                val locationName = state.location?.name ?: state.location?.timezone
                val locationCountry = state.location?.country ?: state.location?.timezone
                onLocationSelected(lat, lon, locationName, locationCountry)
            }
        },
        onSettingsClick = { viewModel.handleIntent(DashboardIntent.NavigateToSettings) })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DashboardContent(
    state: DashboardState,
    onRefresh: () -> Unit,
    onLocationClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    // Pull to refresh state
    val pullRefreshState = rememberPullToRefreshState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("ClimaBit") }, actions = {
                IconButton(onClick = onSettingsClick) {
                    Icon(Icons.Default.Settings, contentDescription = "Settings")
                }
            })
        }) { paddingValues ->
        PullToRefreshBox(
            modifier = Modifier.fillMaxSize().padding(paddingValues)
                .consumeWindowInsets(paddingValues),
            isRefreshing = state.isLoading,
            onRefresh = onRefresh,
            state = pullRefreshState
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Header
                item {
                    val locationName = state.location?.name
                        ?: "${state.weather?.timezone}"

                    val precipitationProb =
                        state.oneCallData?.hourly?.precipitationProbability?.firstOrNull()?.let {
                            it
                        } ?: 0
                    DashboardHeader(
                        location = locationName,
                        chancesOfRain = if (precipitationProb > 0) precipitationProb.toString() else "",
                        onRefresh = onRefresh,
                        isLoading = state.isLoading
                    )
                }

                // Error State
                state.error?.let { error ->
                    item {
                        ErrorCard(message = error)
                    }
                }

                // Weather Content
                state.weather?.let { weather ->
                    // Main Weather Card (clickable to show details)
                    item {
                        MainWeatherCard(
                            weather = weather,
                            oneCallData = state.oneCallData,
                            onClick = onLocationClick
                        )
                    }

                    // Today's Forecast Section
                    state.oneCallData?.hourly?.let { hourlyData ->
                        item {
                            TodayForecastSection(
                                hourlyData = hourlyData,
                                timezoneOffset = state.oneCallData.utcOffsetSeconds,
                                timezone = state.oneCallData.timezone,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }
                    }

                    // Weather Details Grid
                    item {
                        WeatherDetailsGrid(weather = weather)
                    }

                    // Additional Info
                    item {
                        AdditionalInfoCard(weather = weather)
                    }
                }
            }
        }
    }
}


@Composable
private fun DashboardHeader(
    location: String, chancesOfRain: String, onRefresh: () -> Unit, isLoading: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = location,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Medium
                )
                if (chancesOfRain.isNotEmpty()) {
                    Text(
                        text = "Chances of rain: $chancesOfRain%",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        IconButton(
            onClick = onRefresh, enabled = !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp), strokeWidth = 2.dp
                )
            } else {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Refresh",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
private fun MainWeatherCard(
    weather: OpenMeteoResponse,
    oneCallData: OpenMeteoResponse? = null,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLowest
        ),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Weather Description (using weather code)
                val weatherCode = weather.current?.weatherCode
                getWeatherDescription(weatherCode)
                val weatherIcon =
                    getWeatherIcon(weatherCode, weather.hourly?.isDay?.getOrNull(0) ?: 1)
//            Text(
//                text = weatherDescription,
//                style = MaterialTheme.typography.titleMedium,
//                color = MaterialTheme.colorScheme.onPrimaryContainer
//            )

                AsyncImage(
                    model = "https://openweathermap.org/img/wn/${weatherIcon}@4x.png",
                    contentDescription = getWeatherDescription(weatherCode),
                    modifier = Modifier.size(100.dp)
                )


                Column {
                    // Temperature
                    weather.current?.temperature2m?.let { temp ->
                        Text(
                            text = "${temp.toInt()}°C",
                            style = MaterialTheme.typography.displayLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Feels Like
                    weather.current?.apparentTemperature?.let { feelsLike ->
                        Text(
                            text = "Feels like ${feelsLike.toInt()}°",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                        )
                    }

                }


            }
            // Min/Max Temperature for today (from oneCallData which has daily forecast)
            Row(
                modifier = Modifier.align(Alignment.End),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                oneCallData?.daily?.temperature2mMin?.getOrNull(0)?.let { minTemp ->
                    Text(
                        modifier = Modifier.background(
                            shape = MaterialTheme.shapes.medium,
                            color = MaterialTheme.colorScheme.surfaceVariant
                        ).padding(vertical = 8.dp, horizontal = 12.dp),
                        text = buildAnnotatedString {
                            append("Low ↓ ")
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                append("${minTemp.toInt()}°")
                            }
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                    )
                }
                oneCallData?.daily?.temperature2mMax?.getOrNull(0)?.let { maxTemp ->
                    Text(
                        modifier = Modifier.background(
                            shape = MaterialTheme.shapes.medium,
                            color = MaterialTheme.colorScheme.surfaceVariant
                        ).padding(vertical = 8.dp, horizontal = 12.dp),
                        text = buildAnnotatedString {
                            append("High ↑ ")
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                append("${maxTemp.toInt()}°")
                            }
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                    )
                }
            }
        }
    }
}

@Composable
private fun WeatherDetailsGrid(weather: OpenMeteoResponse) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        FlowRow(
            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp),
            maxItemsInEachRow = 2,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Humidity
            WeatherDetailCard(
                modifier = Modifier.weight(1f),
                title = "Humidity",
                value = "${weather.current?.relativeHumidity2m ?: 0}%"
            )

            // Pressure
            WeatherDetailCard(
                modifier = Modifier.weight(1f),
                title = "Pressure",
                value = "${weather.current?.pressureMsl?.toInt() ?: 0} hPa"
            )

            // Wind Speed
            WeatherDetailCard(
                modifier = Modifier.weight(1f),
                title = "Wind Speed",
                value = "${weather.current?.windSpeed10m ?: 0.0} km/h"
            )

            // Wind Direction
            WeatherDetailCard(
                modifier = Modifier.weight(1f),
                title = "Wind Direction",
                value = "${weather.current?.windDirection10m ?: 0}°"
            )
        }
    }
}

@Composable
private fun WeatherDetailCard(
    modifier: Modifier = Modifier, title: String, value: String
) {
    Card(
        modifier = modifier, shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        )
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun AdditionalInfoCard(weather: OpenMeteoResponse) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        )
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Additional Information",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Divider()

            // Cloud Coverage
            weather.current?.cloudCover?.let { clouds ->
                InfoRow(
                    label = "Cloud Coverage", value = "$clouds%"
                )
            }

            // Wind Direction
            weather.current?.windDirection10m?.let { deg ->
                InfoRow(
                    label = "Wind Direction", value = "${deg}°"
                )
            }

            // Wind Gust
            weather.current?.windGusts10m?.let { gust ->
                InfoRow(
                    label = "Wind Gust", value = "$gust km/h"
                )
            }
        }
    }
}

@Composable
private fun InfoRow(
    label: String, value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun ErrorCard(message: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer
        )
    ) {
        Text(
            text = message,
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onErrorContainer
        )
    }
}
