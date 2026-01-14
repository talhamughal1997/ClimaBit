package com.talhapps.climabit.di.module.data

import com.talhapps.climabit.data.remote.WeatherApi
import com.talhapps.climabit.data.remote.WeatherApiImpl
import com.talhapps.climabit.data.repository.WeatherRepositoryImpl
import com.talhapps.climabit.domain.repository.WeatherRepository
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.json.Json
import io.ktor.serialization.kotlinx.json.json
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

expect val platformDataModule : Module

val dataModule = module {

    singleOf(::WeatherRepositoryImpl).bind<WeatherRepository>()

    single {
        HttpClient(get()) {
            defaultRequest {
                contentType(ContentType.Application.Json)
            }
            install(ContentNegotiation) {
                json(
                    Json {
                        encodeDefaults = true
                        isLenient = true
                        coerceInputValues = true
                        ignoreUnknownKeys = true
                    }
                )
            }
        }
    }

    single<WeatherApi> {
        WeatherApiImpl(
            client = get(),
            apiKey = "3fca50336862189043dc9a14b26ba238"
        )
    }

}