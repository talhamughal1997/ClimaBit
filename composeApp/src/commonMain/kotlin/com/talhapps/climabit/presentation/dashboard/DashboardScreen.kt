package com.talhapps.climabit.presentation.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.talhapps.climabit.core.ui.components.card.ErrorCard
import com.talhapps.climabit.core.ui.components.card.WeatherDetailCard
import com.talhapps.climabit.core.ui.mvi.useMvi
import com.talhapps.climabit.domain.model.weather.OpenMeteoResponse
import com.talhapps.climabit.presentation.dashboard.components.AdditionalInfoCard
import com.talhapps.climabit.presentation.dashboard.components.DashboardHeader
import com.talhapps.climabit.presentation.dashboard.components.MainWeatherCard
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


