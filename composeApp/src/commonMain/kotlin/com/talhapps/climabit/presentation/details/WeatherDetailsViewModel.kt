package com.talhapps.climabit.presentation.details

import androidx.lifecycle.viewModelScope
import com.talhapps.climabit.core.ui.mvi.MviViewModel
import com.talhapps.climabit.core.ui.mvi.UiEffect
import com.talhapps.climabit.core.ui.mvi.UiIntent
import com.talhapps.climabit.core.ui.mvi.UiState
import com.talhapps.climabit.domain.model.weather.CurrentWeatherResponse
import kotlinx.coroutines.launch

data class WeatherDetailsState(
    val isLoading: Boolean = false,
    val weather: CurrentWeatherResponse? = null,
    val error: String? = null
) : UiState

sealed interface WeatherDetailsIntent : UiIntent {
    data class LoadDetails(val lat: Double, val lon: Double) : WeatherDetailsIntent
}

sealed interface WeatherDetailsEffect : UiEffect {
    data class ShowError(val message: String) : WeatherDetailsEffect
}

class WeatherDetailsViewModel(
    // private val getWeatherUseCase: GetCurrentWeatherDataUseCase
) : MviViewModel<WeatherDetailsState, WeatherDetailsIntent, WeatherDetailsEffect>(
    initialState = WeatherDetailsState()
) {
    override fun onIntent(intent: WeatherDetailsIntent) {
        when (intent) {
            is WeatherDetailsIntent.LoadDetails -> {
                loadDetails(intent.lat, intent.lon)
            }
        }
    }

    private fun loadDetails(lat: Double, lon: Double) {
        viewModelScope.launch {
            updateState { copy(isLoading = true, error = null) }
            // TODO: Implement weather details loading
            updateState { copy(isLoading = false) }
        }
    }
}

