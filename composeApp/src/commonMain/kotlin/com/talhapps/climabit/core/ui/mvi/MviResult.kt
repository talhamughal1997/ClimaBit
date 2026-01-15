package com.talhapps.climabit.core.ui.mvi

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

/**
 * Result wrapper for operations with loading, success, and error states.
 * Useful for wrapping use case results and handling them uniformly.
 *
 * @param T The success data type
 */
sealed class MviResult<out T> {
    data class Success<T>(val data: T) : MviResult<T>()
    data class Error(val exception: Throwable) : MviResult<Nothing>()
    object Loading : MviResult<Nothing>()

    val isSuccess: Boolean get() = this is Success
    val isError: Boolean get() = this is Error
    val isLoading: Boolean get() = this is Loading

    /**
     * Executes the appropriate callback based on the result type.
     *
     * @param onSuccess Callback for success case
     * @param onError Callback for error case
     * @param onLoading Callback for loading case
     */
    inline fun fold(
        onSuccess: (T) -> Unit,
        onError: (Throwable) -> Unit,
        onLoading: () -> Unit = {}
    ) {
        when (this) {
            is Success -> onSuccess(data)
            is Error -> onError(exception)
            is Loading -> onLoading()
        }
    }

    /**
     * Maps the success value to a new type.
     */
    inline fun <R> map(transform: (T) -> R): MviResult<R> = when (this) {
        is Success -> Success(transform(data))
        is Error -> this
        is Loading -> this
    }

    /**
     * Gets the success value or null.
     */
    fun getOrNull(): T? = (this as? Success)?.data

    /**
     * Gets the success value or throws the error.
     */
    fun getOrThrow(): T = when (this) {
        is Success -> data
        is Error -> throw exception
        is Loading -> throw IllegalStateException("Result is still loading")
    }
}

/**
 * Converts a Flow to MviResult Flow with automatic error handling.
 * Wraps the data in MviResult.Success and catches errors as MviResult.Error.
 *
 * @return Flow of MviResult wrapping the original data
 */
fun <T> Flow<T>.asMviResult(): Flow<MviResult<T>> = flow {
    emit(MviResult.Loading)
    emitAll(
        this@asMviResult
            .map<T, MviResult<T>> { MviResult.Success(it) }
            .catch { emit(MviResult.Error(it)) }
    )
}

