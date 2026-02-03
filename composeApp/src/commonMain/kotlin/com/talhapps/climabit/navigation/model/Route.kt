package com.talhapps.climabit.navigation.model

import kotlinx.serialization.Serializable

@Serializable
sealed interface Route

@Serializable
sealed interface TopLevelRoute : Route

@Serializable
data object Dashboard : TopLevelRoute

@Serializable
data object Forecast : TopLevelRoute

@Serializable
data object Search : TopLevelRoute

@Serializable
data object Settings : Route

@Serializable
data class WeatherDetails(
    val lat: Double,
    val lon: Double,
    val locationName: String? = null,
    val locationCountry: String? = null
) : Route

