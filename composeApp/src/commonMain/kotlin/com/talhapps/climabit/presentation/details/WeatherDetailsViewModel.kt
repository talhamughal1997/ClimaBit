package com.talhapps.climabit.presentation.details

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

data class WeatherDetailsState(
    val isLoading: Boolean = false,
    val isLoadingWeather: Boolean = false,
    val isLoadingAirQuality: Boolean = false,
    val weather: OpenMeteoResponse? = null,
    val airQuality: AirQualityResponse? = null,
    val location: GeocodingResponse? = null,
    val error: String? = null
) : UiState {
    val isAnyLoading: Boolean
        get() = isLoading || isLoadingWeather || isLoadingAirQuality
}

sealed interface WeatherDetailsIntent : UiIntent {
    data class LoadDetails(
        val lat: Double,
        val lon: Double,
        val location: GeocodingResponse? = null
    ) : WeatherDetailsIntent
}

sealed interface WeatherDetailsEffect : UiEffect {
    data class ShowError(val message: String) : WeatherDetailsEffect
}

class WeatherDetailsViewModel(
    private val getOneCallUseCase: GetOneCallUseCase,
    private val getAirQualityUseCase: GetAirQualityUseCase,
    private val getReverseGeocodingUseCase: GetReverseGeocodingUseCase
) : MviViewModel<WeatherDetailsState, WeatherDetailsIntent, WeatherDetailsEffect>(
    initialState = WeatherDetailsState()
) {
    override fun onIntent(intent: WeatherDetailsIntent) {
        when (intent) {
            is WeatherDetailsIntent.LoadDetails -> {
                loadDetails(intent.lat, intent.lon, intent.location)
            }
        }
    }

    private fun loadDetails(lat: Double, lon: Double, location: GeocodingResponse?) {
        viewModelScope.launch {
            if (location != null) {
                updateState { copy(location = location) }
            }

            updateState {
                copy(
                    isLoading = true,
                    isLoadingWeather = true,
                    isLoadingAirQuality = true,
                    error = null
                )
            }

            var weatherLoaded = false
            var airQualityLoaded = false

            getOneCallUseCase(WeatherRequest(lat = lat, lng = lon))
                .observe(
                    onLoading = { updateState { copy(isLoadingWeather = true) } },
                    onSuccess = { weather ->
                        weatherLoaded = true
                        updateState {
                            copy(
                                weather = weather,
                                isLoadingWeather = false,
                                isLoading = !airQualityLoaded
                            )
                        }
                    },
                    onError = { error ->
                        weatherLoaded = true
                        updateState {
                            copy(
                                isLoading = !airQualityLoaded,
                                isLoadingWeather = false,
                                error = error.message ?: "Failed to load weather data"
                            )
                        }
                        sendEffect(WeatherDetailsEffect.ShowError(error.message ?: "Unknown error"))
                    }
                )

            getAirQualityUseCase(WeatherRequest(lat = lat, lng = lon))
                .observe(
                    onLoading = { updateState { copy(isLoadingAirQuality = true) } },
                    onSuccess = { airQuality ->
                        airQualityLoaded = true
                        updateState {
                            copy(
                                airQuality = airQuality,
                                isLoadingAirQuality = false,
                                isLoading = !weatherLoaded
                            )
                        }
                    },
                    onError = {
                        airQualityLoaded = true
                        updateState {
                            copy(
                                isLoadingAirQuality = false,
                                isLoading = !weatherLoaded
                            )
                        }
                    }
                )

            if (location == null) {
                getReverseGeocodingUseCase(WeatherRequest(lat = lat, lng = lon))
                    .observe(
                        onLoading = {},
                        onSuccess = { fetchedLocation ->
                            updateState { copy(location = fetchedLocation) }
                        },
                        onError = {
                        }
                    )
            }
        }
    }
}

