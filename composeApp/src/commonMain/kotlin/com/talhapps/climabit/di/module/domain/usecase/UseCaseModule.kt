package com.talhapps.climabit.di.module.domain.usecase

import com.talhapps.climabit.core.domain.UseCase
import com.talhapps.climabit.domain.model.weather.CurrentWeatherResponse
import com.talhapps.climabit.domain.model.weather.WeatherResponse
import com.talhapps.climabit.domain.usecase.weather.GetCurrentWeatherDataUseCase
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

expect val platformUseCaseModule: Module

val useCaseModule = module {

    singleOf(::GetCurrentWeatherDataUseCase).bind<UseCase<Nothing, CurrentWeatherResponse>>()

}