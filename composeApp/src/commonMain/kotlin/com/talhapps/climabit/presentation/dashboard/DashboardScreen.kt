package com.talhapps.climabit.presentation.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.talhapps.climabit.core.ui.mvi.useMvi
import com.talhapps.climabit.domain.model.weather.CurrentWeatherResponse
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun DashboardScreen(
    onLocationSelected: (Double, Double) -> Unit = { _, _ -> },
    onSettingsClick: () -> Unit = {},
    viewModel: DashboardViewModel = koinViewModel()
) {
    val state by useMvi(
        viewModel = viewModel,
        initialIntent = DashboardIntent.LoadWeather,
        onEffect = { effect ->
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
        }
    )

    DashboardContent(
        state = state,
        onRefresh = { viewModel.handleIntent(DashboardIntent.RefreshWeather) },
        onLocationClick = {
            state.weather?.coord?.let { coord ->
                onLocationSelected(coord.lat ?: 0.0, coord.lon ?: 0.0)
            }
        },
        onSettingsClick = { viewModel.handleIntent(DashboardIntent.NavigateToSettings) }
    )
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
            TopAppBar(
                title = { Text("ClimaBit") },
                actions = {
                    IconButton(onClick = onSettingsClick) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings")
                    }
                }
            )
        }
    ) { paddingValues ->
        PullToRefreshBox(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            isRefreshing = state.isLoading,
            onRefresh = onRefresh,
            state = pullRefreshState
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Header
                item {
                    DashboardHeader(
                        location = state.weather?.name ?: "Loading...",
                        country = state.weather?.sys?.country ?: "",
                        onRefresh = onRefresh,
                        isLoading = state.isLoading
                    )
                }

                // Loading State
                if (state.isLoading && state.weather == null) {
                    item {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
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
                            onClick = onLocationClick
                        )
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
    location: String,
    country: String,
    onRefresh: () -> Unit,
    isLoading: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = "Location",
                tint = MaterialTheme.colorScheme.primary
            )
            Column {
                Text(
                    text = location,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                if (country.isNotEmpty()) {
                    Text(
                        text = country,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        IconButton(
            onClick = onRefresh,
            enabled = !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    strokeWidth = 2.dp
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
    weather: CurrentWeatherResponse,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Weather Icon/Description
            weather.weather.firstOrNull()?.let { weatherInfo ->
                Text(
                    text = weatherInfo.description?.replaceFirstChar { it.uppercase() } ?: "",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Temperature
            weather.main?.temp?.let { temp ->
                Text(
                    text = "${temp.toInt()}°",
                    style = MaterialTheme.typography.displayLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Feels Like
            weather.main?.feelsLike?.let { feelsLike ->
                Text(
                    text = "Feels like ${feelsLike.toInt()}°",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Min/Max Temperature
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                weather.main?.tempMin?.let { min ->
                    Text(
                        text = "↓ ${min.toInt()}°",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                    )
                }
                weather.main?.tempMax?.let { max ->
                    Text(
                        text = "↑ ${max.toInt()}°",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                    )
                }
            }
        }
    }
}

@Composable
private fun WeatherDetailsGrid(weather: CurrentWeatherResponse) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Humidity
            WeatherDetailCard(
                modifier = Modifier.weight(1f),
                title = "Humidity",
                value = "${weather.main?.humidity ?: 0}%"
            )

            // Pressure
            WeatherDetailCard(
                modifier = Modifier.weight(1f),
                title = "Pressure",
                value = "${weather.main?.pressure ?: 0} hPa"
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Wind Speed
            WeatherDetailCard(
                modifier = Modifier.weight(1f),
                title = "Wind Speed",
                value = "${weather.wind?.speed ?: 0.0} m/s"
            )

            // Visibility
            WeatherDetailCard(
                modifier = Modifier.weight(1f),
                title = "Visibility",
                value = "${(weather.visibility ?: 0) / 1000} km"
            )
        }
    }
}

@Composable
private fun WeatherDetailCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
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
private fun AdditionalInfoCard(weather: CurrentWeatherResponse) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Additional Information",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Divider()

            // Cloud Coverage
            weather.clouds?.all?.let { clouds ->
                InfoRow(
                    label = "Cloud Coverage",
                    value = "$clouds%"
                )
            }

            // Wind Direction
            weather.wind?.deg?.let { deg ->
                InfoRow(
                    label = "Wind Direction",
                    value = "${deg}°"
                )
            }

            // Wind Gust
            weather.wind?.gust?.let { gust ->
                InfoRow(
                    label = "Wind Gust",
                    value = "$gust m/s"
                )
            }
        }
    }
}

@Composable
private fun InfoRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
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
