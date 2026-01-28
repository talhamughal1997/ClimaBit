package com.talhapps.climabit.core.ui.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

abstract class MviViewModel<State : UiState, Intent : UiIntent, Effect : UiEffect>(
    initialState: State,
    private val reducer: UiReducer<State, Intent>? = null
) : ViewModel() {

    private val _state = MutableStateFlow(initialState)
    val state: StateFlow<State> = _state.asStateFlow()

    private val _effect = Channel<Effect>(Channel.UNLIMITED)
    val effect: Flow<Effect> = _effect.receiveAsFlow()

    protected val currentState: State get() = _state.value

    open fun handleIntent(intent: Intent) {
        reducer?.let {
            updateState { it.reduce(this, intent) }
        }

        onIntent(intent)
    }

    protected open fun onIntent(intent: Intent) {}

    protected fun updateState(reducer: State.() -> State) {
        _state.value = _state.value.reducer()
    }

    protected fun setState(newState: State) {
        _state.value = newState
    }

    protected fun sendEffect(effect: Effect) {
        viewModelScope.launch {
            _effect.send(effect)
        }
    }

    protected fun sendEffects(vararg effects: Effect) {
        effects.forEach { sendEffect(it) }
    }

    protected fun <T> Flow<T>.observe(
        onLoading: () -> Unit = {},
        onSuccess: (T) -> Unit,
        onError: (Throwable) -> Unit = {}
    ) {
        viewModelScope.launch {
            this@observe
                .onStart { onLoading() }
                .catch { onError(it) }
                .collect(onSuccess)
        }
    }
}
