package com.talhapps.climabit.di.module.data

import com.talhapps.climabit.data.remote.GeminiApi
import com.talhapps.climabit.data.remote.GeminiApiImpl
import com.talhapps.climabit.data.remote.WeatherApi
import com.talhapps.climabit.data.remote.WeatherApiImpl
import com.talhapps.climabit.data.repository.GeminiRepositoryImpl
import com.talhapps.climabit.data.repository.WeatherRepositoryImpl
import com.talhapps.climabit.domain.repository.GeminiRepository
import com.talhapps.climabit.domain.repository.WeatherRepository
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

expect val platformDataModule: Module

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
            install(Logging) {
                logger = Logger.SIMPLE
                level = LogLevel.ALL
            }
        }
    }

    single<WeatherApi> {
        WeatherApiImpl(
            client = get()
        )
    }

    single<GeminiApi> {
        GeminiApiImpl(
            client = get()
        )
    }

    single(named("geminiModel")) {
        "gemini-2.5-flash-lite"
    }

    singleOf(::GeminiRepositoryImpl).bind<GeminiRepository>()

}