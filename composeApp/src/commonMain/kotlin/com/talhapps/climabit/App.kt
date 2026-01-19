package com.talhapps.climabit

import androidx.compose.runtime.Composable
import com.talhapps.climabit.navigation.AppNavigation
import com.talhapps.climabit.theme.ClimaBitTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    ClimaBitTheme {
        AppNavigation()
    }
}
