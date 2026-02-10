package com.talhapps.climabit

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import com.talhapps.climabit.core.theme.ThemeManager
import com.talhapps.climabit.navigation.AppNavigation
import com.talhapps.climabit.theme.ClimaBitTheme
import org.koin.compose.koinInject

@Composable
@Preview
fun App() {
    val themeManager: ThemeManager = koinInject()
    val isDarkThemeState = themeManager.observeTheme()
    val isDarkTheme by isDarkThemeState
    var isInitialized by remember { mutableStateOf(false) }
    val systemIsDark = isSystemInDarkTheme()

    // Initialize theme from system on first launch
    LaunchedEffect(Unit) {
        if (!isInitialized) {
            themeManager.setDarkTheme(systemIsDark)
            isInitialized = true
        }
    }

    ClimaBitTheme(darkTheme = isDarkTheme) {
        AppNavigation()
    }
}
