package com.talhapps.climabit

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.talhapps.climabit.di.init.initKoin

fun main() = application {
    initKoin()
    Window(
        onCloseRequest = ::exitApplication,
        title = "ClimaBit",
    ) {
        App()
    }
}