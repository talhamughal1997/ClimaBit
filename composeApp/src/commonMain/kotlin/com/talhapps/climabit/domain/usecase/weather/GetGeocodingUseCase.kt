package com.talhapps.climabit.domain.usecase.weather

import com.talhapps.climabit.core.domain.UseCase
import com.talhapps.climabit.domain.model.weather.GeocodingResponse
import com.talhapps.climabit.domain.repository.WeatherRepository

class GetGeocodingUseCase(private val weatherRepository: WeatherRepository) :
    UseCase<String, List<GeocodingResponse>> {

    override fun invoke(params: String?) =
        if (params.isNullOrBlank()) {
            kotlinx.coroutines.flow.flowOf(emptyList())
        } else {
            weatherRepository.searchLocations(params, count = 10)
        }
}

