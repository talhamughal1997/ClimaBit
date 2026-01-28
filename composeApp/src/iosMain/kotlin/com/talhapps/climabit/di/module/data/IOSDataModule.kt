package com.talhapps.climabit.di.module.data

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.darwin.Darwin
import org.koin.dsl.module

actual val platformDataModule = module {
    single<HttpClientEngine> {
        Darwin.create()
    }
}