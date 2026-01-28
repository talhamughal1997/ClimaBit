package com.talhapps.climabit.core.ui.mvi

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

sealed class MviResult<out T> {
    data class Success<T>(val data: T) : MviResult<T>()
    data class Error(val exception: Throwable) : MviResult<Nothing>()
    object Loading : MviResult<Nothing>()

    val isSuccess: Boolean get() = this is Success
    val isError: Boolean get() = this is Error
    val isLoading: Boolean get() = this is Loading

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

    inline fun <R> map(transform: (T) -> R): MviResult<R> = when (this) {
        is Success -> Success(transform(data))
        is Error -> this
        is Loading -> this
    }

    fun getOrNull(): T? = (this as? Success)?.data

    fun getOrThrow(): T = when (this) {
        is Success -> data
        is Error -> throw exception
        is Loading -> throw IllegalStateException("Result is still loading")
    }
}

fun <T> Flow<T>.asMviResult(): Flow<MviResult<T>> = flow {
    emit(MviResult.Loading)
    emitAll(
        this@asMviResult
            .map<T, MviResult<T>> { MviResult.Success(it) }
            .catch { emit(MviResult.Error(it)) }
    )
}

