package com.talhapps.climabit.core.ui.mvi

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember

@Composable
fun <State : UiState> collectMviState(viewModel: MviViewModel<State, *, *>): State {
    return viewModel.state.collectAsState().value
}

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

@Composable
fun <Intent : UiIntent> sendMviIntent(
    viewModel: MviViewModel<*, Intent, *>,
    intent: Intent
) {
    LaunchedEffect(intent) {
        viewModel.handleIntent(intent)
    }
}

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
