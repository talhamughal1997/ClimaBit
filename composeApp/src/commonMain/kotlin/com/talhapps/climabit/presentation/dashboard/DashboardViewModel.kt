package com.talhapps.climabit.presentation.dashboard

import androidx.lifecycle.viewModelScope
import com.talhapps.climabit.core.ui.mvi.MviViewModel
import com.talhapps.climabit.core.ui.mvi.UiEffect
import com.talhapps.climabit.core.ui.mvi.UiIntent
import com.talhapps.climabit.core.ui.mvi.UiState
import com.talhapps.climabit.domain.model.gemini.GeminiInsightRequest
import com.talhapps.climabit.domain.model.weather.GeocodingResponse
import com.talhapps.climabit.domain.model.weather.OpenMeteoResponse
import com.talhapps.climabit.domain.model.weather.WeatherRequest
import com.talhapps.climabit.domain.usecase.gemini.GetGeminiInsightsUseCase
import com.talhapps.climabit.domain.usecase.weather.GetCurrentWeatherDataUseCase
import com.talhapps.climabit.domain.usecase.weather.GetOneCallUseCase
import com.talhapps.climabit.domain.usecase.weather.GetReverseGeocodingUseCase
import kotlinx.coroutines.launch

data class DashboardState(
    val isLoading: Boolean = false,
    val weather: OpenMeteoResponse? = null,
    val oneCallData: OpenMeteoResponse? = null,
    val location: GeocodingResponse? = null,
    val error: String? = null,
    val aiInsights: String? = null,
    val isLoadingAIInsights: Boolean = false,
    val aiInsightsError: String? = null
) : UiState

sealed interface DashboardIntent : UiIntent {
    object LoadWeather : DashboardIntent
    object RefreshWeather : DashboardIntent
    object NavigateToSettings : DashboardIntent
    data class LoadWeatherByLocation(val lat: Double, val lon: Double) : DashboardIntent
    object LoadAIInsights : DashboardIntent
}

sealed interface DashboardEffect : UiEffect {
    data class ShowError(val message: String) : DashboardEffect
    data class ShowMessage(val message: String) : DashboardEffect
    object NavigateToSettings : DashboardEffect
}

class DashboardViewModel(
    private val getCurrentWeatherDataUseCase: GetCurrentWeatherDataUseCase,
    private val getOneCallUseCase: GetOneCallUseCase,
    private val getReverseGeocodingUseCase: GetReverseGeocodingUseCase,
    private val getGeminiInsightsUseCase: GetGeminiInsightsUseCase
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

            is DashboardIntent.LoadAIInsights -> {
                loadAIInsights()
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

    private fun loadAIInsights() {
        val weather = currentState.weather
        val oneCallData = currentState.oneCallData
        val location = currentState.location

        if (weather == null) {
            sendEffect(DashboardEffect.ShowError("Weather data not available"))
            return
        }

        viewModelScope.launch {
            updateState {
                copy(
                    isLoadingAIInsights = true,
                    aiInsightsError = null,
                    aiInsights = null
                )
            }

            val prompt =
                com.talhapps.climabit.core.ui.util.WeatherDataFormatter.formatDashboardPrompt(
                    weather = weather,
                    oneCallData = oneCallData,
                    locationName = location?.name
                )

            getGeminiInsightsUseCase(GeminiInsightRequest(prompt = prompt))
                .observe(
                    retries = 3,
                    onLoading = {
                        updateState { copy(isLoadingAIInsights = true) }
                    },
                    onSuccess = { response ->
                        val insights = response.getText()
                        updateState {
                            copy(
                                isLoadingAIInsights = false,
                                aiInsights = insights,
                                aiInsightsError = null
                            )
                        }
                    },
                    onError = { error ->
                        updateState {
                            copy(
                                isLoadingAIInsights = false,
                                aiInsightsError = error.message ?: "Failed to load AI insights"
                            )
                        }
                    }
                )
        }
    }
}
