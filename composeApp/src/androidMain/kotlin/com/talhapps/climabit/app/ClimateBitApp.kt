package com.talhapps.climabit.app

import android.app.Application
import com.talhapps.climabit.di.init.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class ClimateBitApp: Application() {

    override fun onCreate() {
        super.onCreate()

        initKoin {
            androidContext(this@ClimateBitApp)
        }
    }
}