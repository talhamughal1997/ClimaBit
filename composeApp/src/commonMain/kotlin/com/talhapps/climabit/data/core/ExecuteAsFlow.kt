package com.talhapps.climabit.data.core

import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Internal extension function to convert suspend HTTP calls to Flow.
 * Only accessible within the data package.
 * 
 * All exceptions (HTTP errors, network errors, serialization errors, etc.) 
 * are propagated and should be handled in the usecase or viewmodel layer.
 *
 * @param block The suspend function block that returns HttpResponse
 * @return Flow emitting the deserialized response body
 * 
 * @example
 * ```
 * // In WeatherApiImpl:
 * fun getCurrentWeather(...): Flow<OpenMeteoResponse> {
 *     return executeAsFlow {
 *         client.get(baseUrl) {
 *             url {
 *                 path("data", "2.5", "weather")
 *                 parameter("lat", lat)
 *                 parameter("lon", lon)
 *                 parameter("appid", apiKey)
 *             }
 *         }
 *     }
 * }
 * ```
 */
internal inline fun <reified T> executeAsFlow(crossinline block: suspend () -> HttpResponse): Flow<T> = flow {
    val response = block()
    emit(response.body<T>())
}
