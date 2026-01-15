package com.talhapps.climabit.domain.usecase.weather

import com.talhapps.climabit.core.domain.UseCase
import com.talhapps.climabit.domain.model.weather.CurrentWeatherResponse
import com.talhapps.climabit.domain.model.weather.WeatherRequest
import com.talhapps.climabit.domain.repository.WeatherRepository

class GetCurrentWeatherDataUseCase(private val weatherRepository: WeatherRepository) :
    UseCase<WeatherRequest, CurrentWeatherResponse> {

    override fun invoke(params: WeatherRequest?) =
        weatherRepository.getCurrentWeatherData(params?.lat ?: 0.0, params?.lng ?: 0.0)
}