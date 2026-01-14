package com.talhapps.climabit.di.init

import com.talhapps.climabit.di.module.data.dataModule
import com.talhapps.climabit.di.module.data.platformDataModule
import com.talhapps.climabit.di.module.domain.usecase.platformUseCaseModule
import com.talhapps.climabit.di.module.domain.usecase.useCaseModule
import com.talhapps.climabit.di.module.presentation.platformPresentationModule
import com.talhapps.climabit.di.module.presentation.presentationModule
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initKoin(koinAppDeclaration: KoinAppDeclaration? = null){
    startKoin {
        koinAppDeclaration?.invoke(this)
        modules(
            dataModule,
            platformDataModule,
            presentationModule,
            platformPresentationModule,
            useCaseModule,
            platformUseCaseModule,
        )
    }
}