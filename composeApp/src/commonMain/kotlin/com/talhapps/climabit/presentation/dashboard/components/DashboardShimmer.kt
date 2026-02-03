package com.talhapps.climabit.presentation.dashboard.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.talhapps.climabit.core.ui.components.shimmer.ShimmerBox
import com.talhapps.climabit.core.ui.components.shimmer.ShimmerCard
import com.talhapps.climabit.core.ui.components.shimmer.ShimmerText

@Composable
fun DashboardShimmer(
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            ShimmerHeader()
        }

        item {
            ShimmerMainWeatherCard()
        }

        item {
            ShimmerHourlyForecast()
        }

        item {
            ShimmerWeatherDetailsGrid()
        }

        item {
            ShimmerAdditionalInfo()
        }
    }
}

@Composable
private fun ShimmerHeader() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ShimmerText(
                modifier = Modifier.width(150.dp),
                height = 24.dp
            )
            ShimmerText(
                modifier = Modifier.width(120.dp),
                height = 16.dp
            )
        }
        ShimmerBox(
            modifier = Modifier.size(48.dp),
            shape = CircleShape
        )
    }
}

@Composable
private fun ShimmerMainWeatherCard() {
    ShimmerCard(
        modifier = Modifier.fillMaxWidth(),
        height = 250.dp
    )
}

@Composable
private fun ShimmerHourlyForecast() {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        ShimmerText(
            modifier = Modifier.width(150.dp),
            height = 20.dp
        )
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            repeat(8) {
                item {
                    ShimmerHourlyItem()
                }
            }
        }
    }
}

@Composable
private fun ShimmerHourlyItem() {
    Column(
        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.width(80.dp)
    ) {
        ShimmerText(
            modifier = Modifier.width(50.dp),
            height = 14.dp
        )
        ShimmerBox(
            modifier = Modifier.size(48.dp),
            shape = RoundedCornerShape(8.dp)
        )
        ShimmerText(
            modifier = Modifier.width(40.dp),
            height = 16.dp
        )
        ShimmerText(
            modifier = Modifier.width(30.dp),
            height = 12.dp
        )
    }
}

@Composable
private fun ShimmerWeatherDetailsGrid() {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            maxItemsInEachRow = 2,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            repeat(4) {
                ShimmerBox(
                    modifier = Modifier
                        .weight(1f)
                        .height(100.dp),
                    shape = RoundedCornerShape(12.dp)
                )
            }
        }
    }
}

@Composable
private fun ShimmerAdditionalInfo() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        ShimmerText(
            modifier = Modifier.width(180.dp),
            height = 20.dp
        )
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            maxItemsInEachRow = 2,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            repeat(3) {
                ShimmerBox(
                    modifier = Modifier
                        .weight(1f)
                        .height(90.dp),
                    shape = RoundedCornerShape(12.dp)
                )
            }
        }
    }
}

