package com.talhapps.climabit.presentation.settings

import com.talhapps.climabit.core.theme.ThemeManager
import com.talhapps.climabit.core.ui.mvi.MviViewModel
import com.talhapps.climabit.core.ui.mvi.UiEffect
import com.talhapps.climabit.core.ui.mvi.UiIntent
import com.talhapps.climabit.core.ui.mvi.UiState

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

class SettingsViewModel(
    private val themeManager: ThemeManager
) : MviViewModel<SettingsState, SettingsIntent, SettingsEffect>(
    initialState = SettingsState(isDarkTheme = themeManager.isDarkTheme)
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
                themeManager.toggleTheme()
                updateState {
                    copy(isDarkTheme = themeManager.isDarkTheme)
                }
            }
        }
    }
}

