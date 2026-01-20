package com.talhapps.climabit.domain.usecase.weather

import com.talhapps.climabit.core.domain.UseCase
import com.talhapps.climabit.domain.model.weather.GeocodingResponse
import com.talhapps.climabit.domain.model.weather.WeatherRequest
import com.talhapps.climabit.domain.repository.WeatherRepository

class GetReverseGeocodingUseCase(private val weatherRepository: WeatherRepository) :
    UseCase<WeatherRequest, GeocodingResponse?> {

    override fun invoke(params: WeatherRequest?) =
        weatherRepository.getLocationByCoordinates(params?.lat ?: 0.0, params?.lng ?: 0.0)
}

