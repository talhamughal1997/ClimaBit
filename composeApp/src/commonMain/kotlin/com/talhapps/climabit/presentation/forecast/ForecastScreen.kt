package com.talhapps.climabit.presentation.forecast

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
import androidx.compose.foundation.shape.RoundedCornerShape
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

    ForecastListPane(
        state = state,
        onItemClick = { index ->
            // Use forecast coordinates or default location
            val lat = state.forecast?.latitude ?: 24.8607
            val lon = state.forecast?.longitude ?: 67.0011
            viewModel.handleIntent(ForecastIntent.SelectForecastItem(lat, lon))
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ForecastListPane(
    state: ForecastState,
    onItemClick: (Int) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("7-Day Forecast") }
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
                    .padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                state.forecast?.daily?.let { dailyData ->
                    itemsIndexed(dailyData.time) { index, timeStr ->
                        ForecastItemCard(
                            timeStr = timeStr,
                            maxTemp = dailyData.temperature2mMax.getOrNull(index),
                            minTemp = dailyData.temperature2mMin.getOrNull(index),
                            weatherCode = dailyData.weatherCode.getOrNull(index),
                            onClick = { onItemClick(index) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ForecastItemCard(
    timeStr: String,
    maxTemp: Double?,
    minTemp: Double?,
    weatherCode: Int?,
    onClick: () -> Unit
) {
    val dateDisplay = try {
        // Parse ISO8601 format: "2022-07-01" and convert to day name
        val date = LocalDate.parse(timeStr)
        val timeZone = TimeZone.currentSystemDefault()
        val today = Clock.System.now().toLocalDateTime(timeZone).date

        when {
            date == today -> "Today"
            date == today.plus(1, kotlinx.datetime.DateTimeUnit.DAY) -> "Tomorrow"
            else -> {
                // Get day of week name - format enum name to readable format
                val dayName = date.dayOfWeek.name.lowercase()
                    .replaceFirstChar { it.uppercaseChar() }
                dayName
            }
        }
    } catch (e: Exception) {
        timeStr
    }

    val weatherDescription = when (weatherCode) {
        0 -> "Clear sky"
        1 -> "Mainly clear"
        2 -> "Partly cloudy"
        3 -> "Overcast"
        45, 48 -> "Fog"
        in 51..57 -> "Drizzle"
        in 61..67 -> "Rain"
        in 71..77 -> "Snow"
        in 80..82 -> "Rain showers"
        in 85..86 -> "Snow showers"
        95, 96, 99 -> "Thunderstorm"
        else -> "Unknown"
    }
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        onClick = onClick,
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
                    text = dateDisplay,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = weatherDescription,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                minTemp?.let { min ->
                    Text(
                        modifier = Modifier.background(
                            shape = MaterialTheme.shapes.medium,
                            color = MaterialTheme.colorScheme.surfaceContainer
                        ).padding(vertical = 8.dp, horizontal = 12.dp),
                        text = "↓ ${min.toInt()}°",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                maxTemp?.let { max ->
                    Text(
                        modifier = Modifier.background(
                            shape = MaterialTheme.shapes.medium,
                            color = MaterialTheme.colorScheme.surfaceContainer
                        ).padding(vertical = 8.dp, horizontal = 12.dp),
                        text = "↑ ${max.toInt()}°",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}
