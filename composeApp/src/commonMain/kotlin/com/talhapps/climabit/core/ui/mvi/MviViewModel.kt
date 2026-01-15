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

/**
 * Base ViewModel for MVI (Model-View-Intent) architecture.
 * Manages state, processes intents through reducers, and emits side effects.
 *
 * @param State The UI state type implementing [UiState]
 * @param Intent The user intent type implementing [UiIntent]
 * @param Effect The side effect type implementing [UiEffect]
 * @param initialState The initial state of the screen
 * @param reducer Optional reducer for state management. If provided, intents are automatically processed through the reducer.
 */
abstract class MviViewModel<State : UiState, Intent : UiIntent, Effect : UiEffect>(
    initialState: State,
    private val reducer: UiReducer<State, Intent>? = null
) : ViewModel() {

    private val _state = MutableStateFlow(initialState)
    val state: StateFlow<State> = _state.asStateFlow()

    private val _effect = Channel<Effect>(Channel.UNLIMITED)
    val effect: Flow<Effect> = _effect.receiveAsFlow()

    protected val currentState: State get() = _state.value

    /**
     * Handle user intents.
     * If a reducer is provided, it will be called automatically.
     * Override this method to add custom intent handling logic.
     *
     * @param intent The user intent to process
     */
    open fun handleIntent(intent: Intent) {
        // Apply reducer if available
        reducer?.let {
            updateState { it.reduce(this, intent) }
        }

        // Call custom intent handler
        onIntent(intent)
    }

    /**
     * Custom intent handler to be overridden by subclasses.
     * Called after the reducer (if provided).
     *
     * @param intent The intent to handle
     */
    protected open fun onIntent(intent: Intent) {}

    /**
     * Update state using a reducer function.
     * Ensures immutability by creating a new state instance.
     *
     * @param reducer Function that takes the current state and returns a new state
     */
    protected fun updateState(reducer: State.() -> State) {
        _state.value = _state.value.reducer()
    }

    /**
     * Update state directly with a new state instance.
     * Use this when you need to replace the entire state.
     *
     * @param newState The new state to set
     */
    protected fun setState(newState: State) {
        _state.value = newState
    }

    /**
     * Send a side effect (one-time event).
     * Effects are consumed once and should not be part of the state.
     *
     * @param effect The effect to send
     */
    protected fun sendEffect(effect: Effect) {
        viewModelScope.launch {
            _effect.send(effect)
        }
    }

    /**
     * Send multiple effects in sequence.
     *
     * @param effects The effects to send
     */
    protected fun sendEffects(vararg effects: Effect) {
        effects.forEach { sendEffect(it) }
    }

    /**
     * Observe a Flow with automatic loading/error handling.
     * This is a convenience method for common Flow observation patterns.
     *
     * @param onLoading Optional callback when loading starts
     * @param onSuccess Callback when data is received
     * @param onError Optional callback when an error occurs
     */
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
