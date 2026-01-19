package com.talhapps.climabit.core.ui.mvi

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember

/**
 * Collects the current state from an MVI ViewModel.
 * Use this in composables to observe state changes.
 *
 * @param viewModel The MVI ViewModel
 * @return The current UI state
 */
@Composable
fun <State : UiState> collectMviState(viewModel: MviViewModel<State, *, *>): State {
    return viewModel.state.collectAsState().value
}

/**
 * Handles effects from an MVI ViewModel.
 * Effects are one-time events that should be handled (navigation, snackbars, etc.).
 *
 * @param viewModel The MVI ViewModel
 * @param onEffect Callback to handle each effect
 */
@Composable
fun <Effect : UiEffect> handleMviEffects(
    viewModel: MviViewModel<*, *, Effect>,
    onEffect: (Effect) -> Unit
) {
    val effectFlow = remember { viewModel.effect }
    LaunchedEffect(effectFlow) {
        effectFlow.collect(onEffect)
    }
}

/**
 * Sends an intent to an MVI ViewModel.
 * Use this to trigger user actions from composables.
 *
 * @param viewModel The MVI ViewModel
 * @param intent The intent to send
 */
@Composable
fun <Intent : UiIntent> sendMviIntent(
    viewModel: MviViewModel<*, Intent, *>,
    intent: Intent
) {
    LaunchedEffect(intent) {
        viewModel.handleIntent(intent)
    }
}

/**
 * All-in-one MVI hook for Compose.
 * Combines state collection, effect handling, and optional initial intent.
 *
 * @param viewModel The MVI ViewModel
 * @param initialIntent Optional initial intent to send when the composable is first created
 * @param onEffect Callback to handle effects
 * @return State object that can be used with property delegation
 */
@Composable
fun <State : UiState, Intent : UiIntent, Effect : UiEffect> useMvi(
    viewModel: MviViewModel<State, Intent, Effect>,
    initialIntent: Intent? = null,
    onEffect: (Effect) -> Unit = {}
): androidx.compose.runtime.State<State> {
    handleMviEffects(viewModel, onEffect)

    LaunchedEffect(initialIntent) {
        initialIntent?.let { viewModel.handleIntent(it) }
    }

    return viewModel.state.collectAsState()
}
