package com.talhapps.climabit.presentation.dashboard.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.talhapps.climabit.domain.model.weather.OpenMeteoResponse

@Composable
fun AdditionalInfoCard(weather: OpenMeteoResponse) {

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
            horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
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

