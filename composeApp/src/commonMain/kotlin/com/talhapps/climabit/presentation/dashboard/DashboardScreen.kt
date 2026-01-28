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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
                }

                is DashboardEffect.ShowMessage -> {
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


                state.error?.let { error ->
                    item {
                        ErrorCard(message = error)
                    }
                }


                state.weather?.let { weather ->

                    item {
                        MainWeatherCard(
                            weather = weather,
                            oneCallData = state.oneCallData,
                            onClick = onLocationClick
                        )
                    }


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


                    item {
                        WeatherDetailsGrid(weather = weather)
                    }


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

                val weatherCode = weather.current?.weatherCode
                getWeatherDescription(weatherCode)
                getWeatherIcon(weatherCode, weather.hourly?.isDay?.getOrNull(0) ?: 1)

                AsyncImage(
                    model = "https:
                    contentDescription = getWeatherDescription(weatherCode),
                    modifier = Modifier.size(100.dp)
                )


                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    weather.current?.temperature2m?.let { temp ->
                        Text(
                            text = "${temp.toInt()}°C",
                            style = MaterialTheme.typography.displayLarge,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }



                    weather.current?.apparentTemperature?.let { feelsLike ->
                        Text(
                            text = "Feels like ${feelsLike.toInt()}°",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))


                    Row(
                        modifier = Modifier.align(Alignment.End),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        oneCallData?.daily?.temperature2mMin?.getOrNull(0)?.let { minTemp ->
                            Text(
                                modifier = Modifier.background(
                                    shape = MaterialTheme.shapes.medium,
                                    color = MaterialTheme.colorScheme.surfaceContainerLow
                                ).padding(vertical = 8.dp, horizontal = 12.dp),
                                text = buildAnnotatedString {
                                    append("L ↓ ")
                                    withStyle(style = SpanStyle(fontWeight = FontWeight.SemiBold)) {
                                        append("${minTemp.toInt()}°")
                                    }
                                },
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                            )
                        }
                        oneCallData?.daily?.temperature2mMax?.getOrNull(0)?.let { maxTemp ->
                            Text(
                                modifier = Modifier.background(
                                    shape = MaterialTheme.shapes.medium,
                                    color = MaterialTheme.colorScheme.surfaceContainerLow
                                ).padding(vertical = 8.dp, horizontal = 12.dp),
                                text = buildAnnotatedString {
                                    append("H ↑ ")
                                    withStyle(style = SpanStyle(fontWeight = FontWeight.SemiBold)) {
                                        append("${maxTemp.toInt()}°")
                                    }
                                },
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                            )
                        }
                    }

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

            WeatherDetailCard(
                modifier = Modifier.weight(1f),
                title = "Humidity",
                value = "${weather.current?.relativeHumidity2m ?: 0}%"
            )


            WeatherDetailCard(
                modifier = Modifier.weight(1f),
                title = "Pressure",
                value = "${weather.current?.pressureMsl?.toInt() ?: 0} hPa"
            )


            WeatherDetailCard(
                modifier = Modifier.weight(1f),
                title = "Wind Speed",
                value = "${weather.current?.windSpeed10m ?: 0.0} km/h"
            )


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
        modifier = modifier, shape = MaterialTheme.shapes.medium, colors = CardDefaults.cardColors(
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
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun AdditionalInfoCard(weather: OpenMeteoResponse) {

    Column(
        modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Additional Information",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Medium
        )

        FlowRow(
            modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            maxItemsInEachRow = 2,
        ) {


            weather.current?.cloudCover?.let { clouds ->
                InfoRow(
                    modifier = Modifier.weight(1f), label = "Cloud Coverage", value = "$clouds%"
                )
            }


            weather.current?.windDirection10m?.let { deg ->
                InfoRow(
                    modifier = Modifier.weight(1f), label = "Wind Direction", value = "${deg}°"
                )
            }


            weather.current?.windGusts10m?.let { gust ->
                InfoRow(
                    modifier = Modifier.weight(1f), label = "Wind Gust", value = "$gust km/h"
                )
            }
        }

    }
}

@Composable
private fun InfoRow(
    modifier: Modifier = Modifier, label: String, value: String
) {
    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        )
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun ErrorCard(message: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
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
