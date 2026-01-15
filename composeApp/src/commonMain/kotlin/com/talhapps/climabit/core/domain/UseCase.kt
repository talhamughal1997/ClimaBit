package com.talhapps.climabit.core.domain

import kotlinx.coroutines.flow.Flow

interface UseCase<in Params,out Result>  {
    operator fun invoke(params: Params? = null) : Flow<Result>
}