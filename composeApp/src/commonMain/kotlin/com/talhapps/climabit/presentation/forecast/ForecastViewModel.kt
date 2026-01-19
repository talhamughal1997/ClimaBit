package com.talhapps.climabit.presentation.forecast

import androidx.lifecycle.viewModelScope
import com.talhapps.climabit.core.ui.mvi.MviViewModel
import com.talhapps.climabit.core.ui.mvi.UiEffect
import com.talhapps.climabit.core.ui.mvi.UiIntent
import com.talhapps.climabit.core.ui.mvi.UiState
import com.talhapps.climabit.domain.model.weather.Forecast5Response
import com.talhapps.climabit.domain.model.weather.WeatherRequest
import kotlinx.coroutines.launch

data class ForecastState(
    val isLoading: Boolean = false,
    val forecast: Forecast5Response? = null,
    val error: String? = null
) : UiState

sealed interface ForecastIntent : UiIntent {
    object LoadForecast : ForecastIntent
    data class LoadForecastByLocation(val lat: Double, val lon: Double) : ForecastIntent
}

sealed interface ForecastEffect : UiEffect {
    data class ShowError(val message: String) : ForecastEffect
}

class ForecastViewModel(
    // private val getForecastUseCase: GetForecastUseCase
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
        }
    }

    private fun loadForecast(lat: Double, lon: Double) {
        viewModelScope.launch {
            updateState { copy(isLoading = true, error = null) }
            // TODO: Implement forecast loading
            updateState { copy(isLoading = false) }
        }
    }
}

