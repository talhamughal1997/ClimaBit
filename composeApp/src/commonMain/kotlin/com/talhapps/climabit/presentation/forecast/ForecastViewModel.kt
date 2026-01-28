package com.talhapps.climabit.presentation.forecast

import androidx.lifecycle.viewModelScope
import com.talhapps.climabit.core.ui.mvi.MviViewModel
import com.talhapps.climabit.core.ui.mvi.UiEffect
import com.talhapps.climabit.core.ui.mvi.UiIntent
import com.talhapps.climabit.core.ui.mvi.UiState
import com.talhapps.climabit.domain.model.weather.AirQualityResponse
import com.talhapps.climabit.domain.model.weather.GeocodingResponse
import com.talhapps.climabit.domain.model.weather.OpenMeteoResponse
import com.talhapps.climabit.domain.model.weather.WeatherRequest
import com.talhapps.climabit.domain.usecase.weather.GetAirQualityUseCase
import com.talhapps.climabit.domain.usecase.weather.GetOneCallUseCase
import com.talhapps.climabit.domain.usecase.weather.GetReverseGeocodingUseCase
import kotlinx.coroutines.launch

data class ForecastState(
    val isLoading: Boolean = false,
    val forecast: OpenMeteoResponse? = null,
    val airQuality: AirQualityResponse? = null,
    val location: GeocodingResponse? = null,
    val selectedDayIndex: Int = 1,
    val error: String? = null
) : UiState

sealed interface ForecastIntent : UiIntent {
    object LoadForecast : ForecastIntent
    data class LoadForecastByLocation(val lat: Double, val lon: Double) : ForecastIntent
    data class SelectForecastItem(val lat: Double, val lon: Double) : ForecastIntent
    data class SelectDay(val dayIndex: Int) : ForecastIntent
}

sealed interface ForecastEffect : UiEffect {
    data class ShowError(val message: String) : ForecastEffect
    data class NavigateToDetails(
        val lat: Double,
        val lon: Double,
        val location: GeocodingResponse?
    ) : ForecastEffect
}

class ForecastViewModel(
    private val getOneCallUseCase: GetOneCallUseCase,
    private val getAirQualityUseCase: GetAirQualityUseCase,
    private val getReverseGeocodingUseCase: GetReverseGeocodingUseCase
) : MviViewModel<ForecastState, ForecastIntent, ForecastEffect>(
    initialState = ForecastState()
) {
    init {
        handleIntent(ForecastIntent.LoadForecastByLocation(24.8607, 67.0011))
    }

    override fun onIntent(intent: ForecastIntent) {
        when (intent) {
            is ForecastIntent.LoadForecast -> {
                loadForecast(24.8607, 67.0011)
            }
            is ForecastIntent.LoadForecastByLocation -> {
                loadForecast(intent.lat, intent.lon)
            }
            is ForecastIntent.SelectForecastItem -> {
                fetchLocationAndNavigate(intent.lat, intent.lon)
            }
            is ForecastIntent.SelectDay -> {
                updateState { copy(selectedDayIndex = intent.dayIndex) }
            }
        }
    }

    private fun fetchLocationAndNavigate(lat: Double, lon: Double) {
        viewModelScope.launch {
            getReverseGeocodingUseCase(WeatherRequest(lat = lat, lng = lon))
                .observe(
                    onLoading = {},
                    onSuccess = { location ->
                        sendEffect(ForecastEffect.NavigateToDetails(lat, lon, location))
                    },
                    onError = {
                        sendEffect(ForecastEffect.NavigateToDetails(lat, lon, null))
                    }
                )
        }
    }

    private fun loadForecast(lat: Double, lon: Double) {
        viewModelScope.launch {
            updateState { copy(isLoading = true, error = null) }

            var forecastLoaded = false
            var airQualityLoaded = false
            var locationLoaded = false

            getOneCallUseCase(WeatherRequest(lat = lat, lng = lon))
                .observe(
                    onLoading = { updateState { copy(isLoading = true) } },
                    onSuccess = { forecast ->
                        forecastLoaded = true
                        updateState {
                            copy(
                                isLoading = !airQualityLoaded && !locationLoaded,
                                forecast = forecast,
                                error = null
                            )
                        }
                    },
                    onError = { error ->
                        forecastLoaded = true
                        updateState {
                            copy(
                                isLoading = !airQualityLoaded && !locationLoaded,
                                error = error.message ?: "Failed to load forecast"
                            )
                        }
                        sendEffect(ForecastEffect.ShowError(error.message ?: "Unknown error"))
                    }
                )

            getAirQualityUseCase(WeatherRequest(lat = lat, lng = lon))
                .observe(
                    onLoading = {},
                    onSuccess = { airQuality ->
                        airQualityLoaded = true
                        updateState {
                            copy(
                                isLoading = !forecastLoaded && !locationLoaded,
                                airQuality = airQuality
                            )
                        }
                    },
                    onError = {
                        airQualityLoaded = true
                        updateState {
                            copy(isLoading = !forecastLoaded && !locationLoaded)
                        }
                    }
                )

            getReverseGeocodingUseCase(WeatherRequest(lat = lat, lng = lon))
                .observe(
                    onLoading = {},
                    onSuccess = { location ->
                        locationLoaded = true
                        updateState {
                            copy(
                                isLoading = !forecastLoaded && !airQualityLoaded,
                                location = location
                            )
                        }
                    },
                    onError = {
                        locationLoaded = true
                        updateState {
                            copy(isLoading = !forecastLoaded && !airQualityLoaded)
                        }
                    }
                )
        }
    }
}

