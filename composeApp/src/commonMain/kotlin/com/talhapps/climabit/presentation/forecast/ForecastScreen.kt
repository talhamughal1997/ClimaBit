package com.talhapps.climabit.presentation.forecast

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.talhapps.climabit.core.ui.mvi.useMvi
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ForecastScreen(
    onForecastItemSelected: (Double, Double) -> Unit = { _, _ -> },
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
            }
        }
    )

    ForecastListPane(
        state = state,
        onItemClick = { item ->
            // Use forecast city coordinates or default location
            val lat = state.forecast?.city?.let { 24.8607 } ?: 24.8607
            val lon = state.forecast?.city?.let { 67.0011 } ?: 67.0011
            onForecastItemSelected(lat, lon)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ForecastListPane(
    state: ForecastState,
    onItemClick: (com.talhapps.climabit.domain.model.weather.Forecast5Response.ForecastItem) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("5-Day Forecast") }
            )
        }
    ) { paddingValues ->
        if (state.isLoading && state.forecast == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
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
                state.forecast?.list?.let { forecastList ->
                    items(forecastList) { item ->
                        ForecastItemCard(
                            forecastItem = item,
                            onClick = { onItemClick(item) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ForecastItemCard(
    forecastItem: com.talhapps.climabit.domain.model.weather.Forecast5Response.ForecastItem,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        onClick = onClick
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
                    text = forecastItem.dtTxt ?: "",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                forecastItem.weather.firstOrNull()?.let { weather ->
                    Text(
                        text = weather.description?.replaceFirstChar { it.uppercase() } ?: "",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
            forecastItem.main?.let { main ->
                Text(
                    text = "${main.temp?.toInt() ?: 0}°",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
