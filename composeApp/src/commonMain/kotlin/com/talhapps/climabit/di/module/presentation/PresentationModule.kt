package com.talhapps.climabit.di.module.presentation

import com.talhapps.climabit.data.repository.WeatherRepositoryImpl
import com.talhapps.climabit.domain.repository.WeatherRepository
import com.talhapps.climabit.presentation.dashboard.DashboardViewModel
import io.ktor.client.HttpClient
import io.ktor.client.plugins.defaultRequest
import io.ktor.http.ContentType
import io.ktor.http.contentType
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

expect val platformPresentationModule: Module

val presentationModule = module {
    viewModelOf(::DashboardViewModel)
}