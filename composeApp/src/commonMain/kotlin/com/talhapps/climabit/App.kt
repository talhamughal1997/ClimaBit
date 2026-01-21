package com.talhapps.climabit

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.talhapps.climabit.navigation.AppNavigation
import com.talhapps.climabit.theme.ClimaBitTheme

@Composable
@Preview
fun App() {
    ClimaBitTheme {
        AppNavigation()
    }
}
