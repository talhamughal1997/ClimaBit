package com.talhapps.climabit.domain.usecase.weather

import com.talhapps.climabit.core.domain.UseCase
import com.talhapps.climabit.domain.model.weather.AirQualityResponse
import com.talhapps.climabit.domain.model.weather.WeatherRequest
import com.talhapps.climabit.domain.repository.WeatherRepository

class GetAirQualityUseCase(private val weatherRepository: WeatherRepository) :
    UseCase<WeatherRequest, AirQualityResponse> {

    override fun invoke(params: WeatherRequest?) =
        weatherRepository.getCurrentAirQuality(params?.lat ?: 0.0, params?.lng ?: 0.0)
}

