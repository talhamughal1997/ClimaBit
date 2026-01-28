package com.talhapps.climabit.core.ui.mvi

interface UiReducer<State : UiState, Intent : UiIntent> {
    fun reduce(currentState: State, intent: Intent): State
}

fun <State : UiState, Intent : UiIntent> createReducer(
    reduceFn: (State, Intent) -> State
): UiReducer<State, Intent> = object : UiReducer<State, Intent> {
    override fun reduce(currentState: State, intent: Intent): State {
        return reduceFn(currentState, intent)
    }
}

