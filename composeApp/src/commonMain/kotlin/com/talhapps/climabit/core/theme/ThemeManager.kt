package com.talhapps.climabit.core.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf

class ThemeManager {
    val isDarkThemeState = mutableStateOf(false)

    val isDarkTheme: Boolean
        get() = isDarkThemeState.value

    @Composable
    fun observeTheme(): State<Boolean> {
        return isDarkThemeState
    }

    fun toggleTheme() {
        isDarkThemeState.value = !isDarkThemeState.value
    }

    fun setDarkTheme(isDark: Boolean) {
        isDarkThemeState.value = isDark
    }
}

