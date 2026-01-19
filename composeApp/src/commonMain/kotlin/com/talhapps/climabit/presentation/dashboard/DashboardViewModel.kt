package com.talhapps.climabit.presentation.dashboard

import androidx.lifecycle.viewModelScope
import com.talhapps.climabit.core.ui.mvi.MviViewModel
import com.talhapps.climabit.core.ui.mvi.UiEffect
import com.talhapps.climabit.core.ui.mvi.UiIntent
import com.talhapps.climabit.core.ui.mvi.UiState
import com.talhapps.climabit.domain.model.weather.CurrentWeatherResponse
import com.talhapps.climabit.domain.model.weather.WeatherRequest
import com.talhapps.climabit.domain.usecase.weather.GetCurrentWeatherDataUseCase
import kotlinx.coroutines.launch

/**
 * Dashboard UI State
 */
data class DashboardState(
    val isLoading: Boolean = false,
    val weather: CurrentWeatherResponse? = null,
    val error: String? = null
) : UiState

/**
 * Dashboard Intents
 */
sealed interface DashboardIntent : UiIntent {
    object LoadWeather : DashboardIntent
    object RefreshWeather : DashboardIntent
    object NavigateToSettings : DashboardIntent
    data class LoadWeatherByLocation(val lat: Double, val lon: Double) : DashboardIntent
}

/**
 * Dashboard Effects
 */
sealed interface DashboardEffect : UiEffect {
    data class ShowError(val message: String) : DashboardEffect
    data class ShowMessage(val message: String) : DashboardEffect
    object NavigateToSettings : DashboardEffect
}

/**
 * Dashboard ViewModel using MVI architecture
 */
class DashboardViewModel(
    private val getCurrentWeatherDataUseCase: GetCurrentWeatherDataUseCase
) : MviViewModel<DashboardState, DashboardIntent, DashboardEffect>(
    initialState = DashboardState()
) {

    init {
        // Load initial weather data for Karachi
        handleIntent(DashboardIntent.LoadWeatherByLocation(24.8607, 67.0011))
    }

    override fun onIntent(intent: DashboardIntent) {
        when (intent) {
            is DashboardIntent.LoadWeather -> {
                loadWeather(24.8607, 67.0011) // Default: Karachi
            }

            is DashboardIntent.RefreshWeather -> {
                val currentLocation = currentState.weather?.coord
                if (currentLocation != null) {
                    loadWeather(currentLocation.lat ?: 24.8607, currentLocation.lon ?: 67.0011)
                } else {
                    loadWeather(24.8607, 67.0011)
                }
            }

            is DashboardIntent.NavigateToSettings -> {
                sendEffect(DashboardEffect.NavigateToSettings)
            }

            is DashboardIntent.LoadWeatherByLocation -> {
                loadWeather(intent.lat, intent.lon)
            }
        }
    }

    private fun loadWeather(lat: Double, lon: Double) {
        viewModelScope.launch {
            updateState { copy(isLoading = true, error = null) }

            getCurrentWeatherDataUseCase(WeatherRequest(lat = lat, lng = lon))
                .observe(
                    onLoading = { updateState { copy(isLoading = true) } },
                    onSuccess = { weather ->
                        updateState {
                            copy(
                                isLoading = false,
                                weather = weather,
                                error = null
                            )
                        }
                    },
                    onError = { error ->
                        updateState {
                            copy(
                                isLoading = false,
                                error = error.message ?: "Failed to load weather data"
                            )
                        }
                        sendEffect(DashboardEffect.ShowError(error.message ?: "Unknown error"))
                    }
                )
        }
    }
}
