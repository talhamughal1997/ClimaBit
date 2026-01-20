package com.talhapps.climabit.domain.usecase.weather

import com.talhapps.climabit.core.domain.UseCase
import com.talhapps.climabit.domain.model.weather.OpenMeteoResponse
import com.talhapps.climabit.domain.model.weather.WeatherRequest
import com.talhapps.climabit.domain.repository.WeatherRepository

class GetOneCallUseCase(private val weatherRepository: WeatherRepository) :
    UseCase<WeatherRequest, OpenMeteoResponse> {

    override fun invoke(params: WeatherRequest?) =
        weatherRepository.getOneCall(params?.lat ?: 0.0, params?.lng ?: 0.0)
}

