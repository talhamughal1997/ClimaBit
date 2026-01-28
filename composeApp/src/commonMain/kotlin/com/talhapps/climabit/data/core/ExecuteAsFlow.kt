package com.talhapps.climabit.data.core

import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

internal inline fun <reified T> executeAsFlow(crossinline block: suspend () -> HttpResponse): Flow<T> =
    flow {
        val response = block()
        emit(response.body<T>())
    }
