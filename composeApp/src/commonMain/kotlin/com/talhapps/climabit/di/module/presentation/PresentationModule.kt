package com.talhapps.climabit.di.module.presentation

import com.talhapps.climabit.core.theme.ThemeManager
import com.talhapps.climabit.presentation.dashboard.DashboardViewModel
import com.talhapps.climabit.presentation.details.WeatherDetailsViewModel
import com.talhapps.climabit.presentation.forecast.ForecastViewModel
import com.talhapps.climabit.presentation.search.SearchViewModel
import com.talhapps.climabit.presentation.settings.SettingsViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

expect val platformPresentationModule: Module

val presentationModule = module {
    singleOf(::ThemeManager)
    viewModelOf(::DashboardViewModel)
    viewModelOf(::ForecastViewModel)
    viewModelOf(::SearchViewModel)
    viewModelOf(::SettingsViewModel)
    viewModelOf(::WeatherDetailsViewModel)
}