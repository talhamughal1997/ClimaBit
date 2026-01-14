package com.talhapps.climabit.domain.usecase.weather

import com.talhapps.climabit.core.domain.UseCase
import com.talhapps.climabit.domain.model.weather.CurrentWeatherResponse
import com.talhapps.climabit.domain.model.weather.WeatherResponse
import com.talhapps.climabit.domain.repository.WeatherRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetCurrentWeatherDataUseCase(private val weatherRepository: WeatherRepository) : UseCase<Double, CurrentWeatherResponse> {

    override fun invoke(vararg params: Double?): Flow<CurrentWeatherResponse> {

        return flow {
//            delay(3000)
//            emit(WeatherResponse(count = "Hello from Current Weather Use Case"))
//            delay(3000)
//            emit(WeatherResponse(count = weatherRepository.getData()))

            emit(weatherRepository.getCurrentWeatherData(params[0]?:0.0,params[1]?:0.0))

        }
    }
}