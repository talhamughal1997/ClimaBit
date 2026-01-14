package com.talhapps.climabit.domain.model.weather

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Models for OpenWeather Geocoding API.
 * Docs: https://openweathermap.org/api/geocoding-api
 */

@Serializable
data class GeocodingResponse(
    @SerialName("name") val name: String? = null,
    @SerialName("local_names") val localNames: Map<String, String>? = null,
    @SerialName("lat") val lat: Double? = null,
    @SerialName("lon") val lon: Double? = null,
    @SerialName("country") val country: String? = null,
    @SerialName("state") val state: String? = null
)

@Serializable
data class ReverseGeocodingResponse(
    @SerialName("name") val name: String? = null,
    @SerialName("local_names") val localNames: Map<String, String>? = null,
    @SerialName("lat") val lat: Double? = null,
    @SerialName("lon") val lon: Double? = null,
    @SerialName("country") val country: String? = null,
    @SerialName("state") val state: String? = null
)


