package com.talhapps.climabit.presentation.settings

import androidx.lifecycle.viewModelScope
import com.talhapps.climabit.core.ui.mvi.MviViewModel
import com.talhapps.climabit.core.ui.mvi.UiEffect
import com.talhapps.climabit.core.ui.mvi.UiIntent
import com.talhapps.climabit.core.ui.mvi.UiState
import kotlinx.coroutines.launch

data class SettingsState(
    val temperatureUnit: String = "Celsius",
    val windSpeedUnit: String = "m/s",
    val isDarkTheme: Boolean = false
) : UiState

sealed interface SettingsIntent : UiIntent {
    object ToggleTemperatureUnit : SettingsIntent
    object ToggleWindSpeedUnit : SettingsIntent
    object ToggleTheme : SettingsIntent
}

sealed interface SettingsEffect : UiEffect {
    data class ShowMessage(val message: String) : SettingsEffect
}

class SettingsViewModel() : MviViewModel<SettingsState, SettingsIntent, SettingsEffect>(
    initialState = SettingsState()
) {
    override fun onIntent(intent: SettingsIntent) {
        when (intent) {
            is SettingsIntent.ToggleTemperatureUnit -> {
                updateState {
                    copy(
                        temperatureUnit = if (temperatureUnit == "Celsius") "Fahrenheit" else "Celsius"
                    )
                }
            }
            is SettingsIntent.ToggleWindSpeedUnit -> {
                updateState {
                    copy(
                        windSpeedUnit = if (windSpeedUnit == "m/s") "km/h" else "m/s"
                    )
                }
            }
            is SettingsIntent.ToggleTheme -> {
                updateState {
                    copy(isDarkTheme = !isDarkTheme)
                }
            }
        }
    }
}

