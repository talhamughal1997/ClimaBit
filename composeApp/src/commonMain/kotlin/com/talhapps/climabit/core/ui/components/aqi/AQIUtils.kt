package com.talhapps.climabit.core.ui.components.aqi

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun getAQIInfo(aqi: Int): Pair<String, Color> {
    val colorScheme = MaterialTheme.colorScheme
    return when {
        aqi <= 50 -> Pair("Good", colorScheme.primary)
        aqi <= 100 -> Pair("Moderate", colorScheme.secondary)
        aqi <= 150 -> Pair("Unhealthy for Sensitive", colorScheme.tertiary)
        aqi <= 200 -> Pair("Unhealthy", colorScheme.error)
        aqi <= 300 -> Pair("Very Unhealthy", colorScheme.errorContainer)
        else -> Pair("Hazardous", colorScheme.error)
    }
}

