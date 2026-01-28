package com.talhapps.climabit.presentation.dashboard

import androidx.lifecycle.viewModelScope
import com.talhapps.climabit.core.ui.mvi.MviViewModel
import com.talhapps.climabit.core.ui.mvi.UiEffect
import com.talhapps.climabit.core.ui.mvi.UiIntent
import com.talhapps.climabit.core.ui.mvi.UiState
import com.talhapps.climabit.domain.model.weather.GeocodingResponse
import com.talhapps.climabit.domain.model.weather.OpenMeteoResponse
import com.talhapps.climabit.domain.model.weather.WeatherRequest
import com.talhapps.climabit.domain.usecase.weather.GetCurrentWeatherDataUseCase
import com.talhapps.climabit.domain.usecase.weather.GetOneCallUseCase
import com.talhapps.climabit.domain.usecase.weather.GetReverseGeocodingUseCase
import kotlinx.coroutines.launch

data class DashboardState(
    val isLoading: Boolean = false,
    val weather: OpenMeteoResponse? = null,
    val oneCallData: OpenMeteoResponse? = null,
    val location: GeocodingResponse? = null,
    val error: String? = null
) : UiState

sealed interface DashboardIntent : UiIntent {
    object LoadWeather : DashboardIntent
    object RefreshWeather : DashboardIntent
    object NavigateToSettings : DashboardIntent
    data class LoadWeatherByLocation(val lat: Double, val lon: Double) : DashboardIntent
}

sealed interface DashboardEffect : UiEffect {
    data class ShowError(val message: String) : DashboardEffect
    data class ShowMessage(val message: String) : DashboardEffect
    object NavigateToSettings : DashboardEffect
}

class DashboardViewModel(
    private val getCurrentWeatherDataUseCase: GetCurrentWeatherDataUseCase,
    private val getOneCallUseCase: GetOneCallUseCase,
    private val getReverseGeocodingUseCase: GetReverseGeocodingUseCase
) : MviViewModel<DashboardState, DashboardIntent, DashboardEffect>(
    initialState = DashboardState()
) {

    init {
        handleIntent(DashboardIntent.LoadWeatherByLocation(24.8607, 67.0011))
    }

    override fun onIntent(intent: DashboardIntent) {
        when (intent) {
            is DashboardIntent.LoadWeather -> {
                loadWeather(24.8607, 67.0011)
            }

            is DashboardIntent.RefreshWeather -> {
                val currentLocation = currentState.weather
                if (currentLocation != null) {
                    loadWeather(
                        currentLocation.latitude ?: 24.8607,
                        currentLocation.longitude ?: 67.0011
                    )
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

            getOneCallUseCase(WeatherRequest(lat = lat, lng = lon))
                .observe(
                    onLoading = {},
                    onSuccess = { oneCall ->
                        updateState {
                            copy(oneCallData = oneCall)
                        }
                    },
                    onError = {
                    }
                )

            getReverseGeocodingUseCase(WeatherRequest(lat = lat, lng = lon))
                .observe(
                    onLoading = {},
                    onSuccess = { location ->
                        updateState { copy(location = location) }
                    },
                    onError = {
                    }
                )
        }
    }
}
