package com.talhapps.climabit.presentation.forecast

import androidx.lifecycle.viewModelScope
import com.talhapps.climabit.core.ui.mvi.MviViewModel
import com.talhapps.climabit.core.ui.mvi.UiEffect
import com.talhapps.climabit.core.ui.mvi.UiIntent
import com.talhapps.climabit.core.ui.mvi.UiState
import com.talhapps.climabit.domain.model.weather.GeocodingResponse
import com.talhapps.climabit.domain.model.weather.OpenMeteoResponse
import com.talhapps.climabit.domain.model.weather.WeatherRequest
import com.talhapps.climabit.domain.usecase.weather.GetOneCallUseCase
import kotlinx.coroutines.launch

data class ForecastState(
    val isLoading: Boolean = false,
    val forecast: OpenMeteoResponse? = null,
    val error: String? = null
) : UiState

sealed interface ForecastIntent : UiIntent {
    object LoadForecast : ForecastIntent
    data class LoadForecastByLocation(val lat: Double, val lon: Double) : ForecastIntent
    data class SelectForecastItem(val lat: Double, val lon: Double) : ForecastIntent
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
    private val getOneCallUseCase: GetOneCallUseCase
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
        }
    }

    private fun fetchLocationAndNavigate(lat: Double, lon: Double) {
        // Open-Meteo doesn't support reverse geocoding (coordinates to location name)
        // Navigate directly with coordinates - details screen will show coordinates or fetch location if needed
        sendEffect(ForecastEffect.NavigateToDetails(lat, lon, null))
    }

    private fun loadForecast(lat: Double, lon: Double) {
        viewModelScope.launch {
            updateState { copy(isLoading = true, error = null) }
            getOneCallUseCase(WeatherRequest(lat = lat, lng = lon))
                .observe(
                    onLoading = { updateState { copy(isLoading = true) } },
                    onSuccess = { forecast ->
                        updateState {
                            copy(
                                isLoading = false,
                                forecast = forecast,
                                error = null
                            )
                        }
                    },
                    onError = { error ->
                        updateState {
                            copy(
                                isLoading = false,
                                error = error.message ?: "Failed to load forecast"
                            )
                        }
                        sendEffect(ForecastEffect.ShowError(error.message ?: "Unknown error"))
                    }
                )
        }
    }
}

