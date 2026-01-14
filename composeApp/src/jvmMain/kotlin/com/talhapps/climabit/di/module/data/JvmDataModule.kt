package com.talhapps.climabit.di.module.data

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.cio.CIO
import org.koin.dsl.module

actual val platformDataModule = module {
    single<HttpClientEngine> {
        CIO.create()
    }
}

