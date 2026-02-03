package com.talhapps.climabit.presentation.details.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.talhapps.climabit.core.ui.components.shimmer.ShimmerBox
import com.talhapps.climabit.core.ui.components.shimmer.ShimmerCard
import com.talhapps.climabit.core.ui.components.shimmer.ShimmerText

@Composable
fun WeatherDetailsShimmer(
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        item {
            ShimmerHeroSection()
        }

        item {
            ShimmerAdditionalInfo()
        }

        item {
            ShimmerAQISection()
        }

        item {
            ShimmerKeyMetricsRow()
        }

        item {
            ShimmerDetailedSection()
        }

        item {
            ShimmerSunTimesSection()
        }

        item {
            ShimmerText(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                height = 24.dp
            )
        }

        items(7) {
            ShimmerForecastDayCard()
        }
    }
}

@Composable
private fun ShimmerHeroSection() {
    ShimmerCard(
        modifier = Modifier.fillMaxWidth(),
        height = 200.dp
    )
}

@Composable
private fun ShimmerAdditionalInfo() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        ShimmerBox(
            modifier = Modifier
                .weight(1f)
                .height(100.dp),
            shape = RoundedCornerShape(16.dp)
        )
        ShimmerBox(
            modifier = Modifier
                .weight(1f)
                .height(100.dp),
            shape = RoundedCornerShape(16.dp)
        )
    }
}

@Composable
private fun ShimmerAQISection() {
    ShimmerCard(
        modifier = Modifier.fillMaxWidth(),
        height = 180.dp
    )
}

@Composable
private fun ShimmerKeyMetricsRow() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        repeat(3) {
            ShimmerBox(
                modifier = Modifier
                    .weight(1f)
                    .height(140.dp),
                shape = RoundedCornerShape(16.dp)
            )
        }
    }
}

@Composable
private fun ShimmerDetailedSection() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ShimmerText(
            modifier = Modifier.fillMaxWidth(0.4f),
            height = 24.dp
        )

        repeat(4) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                ) {
                    ShimmerBox(
                        modifier = Modifier.size(24.dp),
                        shape = CircleShape
                    )
                    ShimmerText(
                        modifier = Modifier.width(100.dp),
                        height = 16.dp
                    )
                }
                ShimmerText(
                    modifier = Modifier.width(60.dp),
                    height = 16.dp
                )
            }
        }
    }
}

@Composable
private fun ShimmerSunTimesSection() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        repeat(2) {
            Column(
                horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ShimmerBox(
                    modifier = Modifier.size(32.dp),
                    shape = CircleShape
                )
                ShimmerText(
                    modifier = Modifier.width(60.dp),
                    height = 12.dp
                )
                ShimmerText(
                    modifier = Modifier.width(80.dp),
                    height = 18.dp
                )
            }
        }
    }
}

@Composable
private fun ShimmerForecastDayCard() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
    ) {
        ShimmerBox(
            modifier = Modifier.size(56.dp),
            shape = RoundedCornerShape(8.dp)
        )

        Column(
            modifier = Modifier.weight(1f).padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            ShimmerText(
                modifier = Modifier.width(100.dp),
                height = 18.dp
            )
            ShimmerText(
                modifier = Modifier.width(120.dp),
                height = 14.dp
            )
            ShimmerText(
                modifier = Modifier.width(60.dp),
                height = 12.dp
            )
        }

        Column(
            horizontalAlignment = androidx.compose.ui.Alignment.End,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            ShimmerText(
                modifier = Modifier.width(40.dp),
                height = 20.dp
            )
            ShimmerText(
                modifier = Modifier.width(35.dp),
                height = 16.dp
            )
        }
    }
}

