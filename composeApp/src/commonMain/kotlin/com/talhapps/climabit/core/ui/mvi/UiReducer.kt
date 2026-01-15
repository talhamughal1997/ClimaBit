package com.talhapps.climabit.core.ui.mvi

/**
 * Reducer interface for MVI architecture.
 * Reducers are pure functions that transform state based on intents.
 *
 * @param State The UI state type
 * @param Intent The user intent type
 */
interface UiReducer<State : UiState, Intent : UiIntent> {
    /**
     * Reduces the current state based on the given intent.
     * This is a pure function - it should not have side effects.
     *
     * @param currentState The current UI state
     * @param intent The user intent/action
     * @return The new UI state
     */
    fun reduce(currentState: State, intent: Intent): State
}

/**
 * Creates a reducer from a lambda function.
 * Useful for simple reducers without creating a separate class.
 *
 * @param reduceFn The reduction function
 * @return A UiReducer instance
 */
fun <State : UiState, Intent : UiIntent> createReducer(
    reduceFn: (State, Intent) -> State
): UiReducer<State, Intent> = object : UiReducer<State, Intent> {
    override fun reduce(currentState: State, intent: Intent): State {
        return reduceFn(currentState, intent)
    }
}

