package com.talhapps.climabit.domain.model.weather

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Open-Meteo Geocoding API Response
 * Docs: https://open-meteo.com/en/docs/geocoding-api
 *
 * The API returns a wrapper object with a "results" array
 */
@Serializable
data class GeocodingApiResponse(
    @SerialName("results") val results: List<GeocodingResponse> = emptyList(),
    @SerialName("generationtime_ms") val generationTimeMs: Double? = null
)

/**
 * Individual geocoding result
 */
@Serializable
data class GeocodingResponse(
    @SerialName("id") val id: Int? = null,
    @SerialName("name") val name: String? = null,
    @SerialName("latitude") val latitude: Double? = null,
    @SerialName("longitude") val longitude: Double? = null,
    @SerialName("elevation") val elevation: Double? = null,
    @SerialName("feature_code") val featureCode: String? = null,
    @SerialName("country_code") val countryCode: String? = null,
    @SerialName("admin1") val admin1: String? = null,
    @SerialName("admin2") val admin2: String? = null,
    @SerialName("admin3") val admin3: String? = null,
    @SerialName("admin4") val admin4: String? = null,
    @SerialName("timezone") val timezone: String? = null,
    @SerialName("population") val population: Int? = null,
    @SerialName("postcodes") val postcodes: List<String> = emptyList(),
    @SerialName("country_id") val countryId: Int? = null,
    @SerialName("country") val country: String? = null,
    @SerialName("admin1_id") val admin1Id: Int? = null,
    @SerialName("admin2_id") val admin2Id: Int? = null,
    @SerialName("admin3_id") val admin3Id: Int? = null,
    @SerialName("admin4_id") val admin4Id: Int? = null
)
