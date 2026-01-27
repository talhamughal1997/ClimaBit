package com.talhapps.climabit.di.module.domain.usecase

import com.talhapps.climabit.core.domain.UseCase
import com.talhapps.climabit.domain.model.gemini.GeminiInsightRequest
import com.talhapps.climabit.domain.model.gemini.GeminiResponse
import com.talhapps.climabit.domain.model.weather.AirQualityResponse
import com.talhapps.climabit.domain.model.weather.GeocodingResponse
import com.talhapps.climabit.domain.model.weather.OpenMeteoResponse
import com.talhapps.climabit.domain.model.weather.WeatherRequest
import com.talhapps.climabit.domain.usecase.gemini.GetGeminiInsightsUseCase
import com.talhapps.climabit.domain.usecase.weather.GetAirQualityUseCase
import com.talhapps.climabit.domain.usecase.weather.GetCurrentWeatherDataUseCase
import com.talhapps.climabit.domain.usecase.weather.GetGeocodingUseCase
import com.talhapps.climabit.domain.usecase.weather.GetOneCallUseCase
import com.talhapps.climabit.domain.usecase.weather.GetReverseGeocodingUseCase
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

expect val platformUseCaseModule: Module

val useCaseModule = module {

    // Weather Forecast
    singleOf(::GetCurrentWeatherDataUseCase).bind<UseCase<WeatherRequest, OpenMeteoResponse>>()
    singleOf(::GetOneCallUseCase).bind<UseCase<WeatherRequest, OpenMeteoResponse>>()

    // Air Quality
    singleOf(::GetAirQualityUseCase).bind<UseCase<WeatherRequest, AirQualityResponse>>()

    // Geocoding
    singleOf(::GetGeocodingUseCase).bind<UseCase<String, List<GeocodingResponse>>>()
    singleOf(::GetReverseGeocodingUseCase).bind<UseCase<WeatherRequest, GeocodingResponse?>>()

    // Gemini AI
    singleOf(::GetGeminiInsightsUseCase).bind<UseCase<GeminiInsightRequest, GeminiResponse>>()

}