package com.talhapps.climabit.presentation

import androidx.compose.ui.window.ComposeUIViewController
import com.talhapps.climabit.App
import com.talhapps.climabit.di.init.initKoin

fun MainViewController() = ComposeUIViewController(configure = {
    initKoin()
}) { App() }