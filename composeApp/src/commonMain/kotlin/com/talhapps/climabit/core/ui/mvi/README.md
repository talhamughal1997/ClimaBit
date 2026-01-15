# MVI Architecture

Simple, generic, and effective MVI (Model-View-Intent) architecture with reducer pattern for Compose
Multiplatform.

## Architecture Overview

The MVI architecture consists of:

- **State**: Represents the UI state
- **Intent**: Represents user actions
- **Effect**: Represents one-time side effects (navigation, snackbars, etc.)
- **Reducer**: Pure functions that transform state based on intents
- **ViewModel**: Manages state, processes intents, and emits effects

## Core Components

### 1. UiState

Base interface for all UI states.

```kotlin
data class WeatherState(
    val isLoading: Boolean = false,
    val weather: String? = null,
    val error: String? = null
) : UiState
```

### 2. UiIntent

Base interface for all user actions.

```kotlin
sealed interface WeatherIntent : UiIntent {
    object Load : WeatherIntent
    data class Refresh(val lat: Double, val lon: Double) : WeatherIntent
    object Retry : WeatherIntent
}
```

### 3. UiEffect

Base interface for one-time side effects.

```kotlin
sealed interface WeatherEffect : UiEffect {
    data class ShowError(val message: String) : WeatherEffect
    data class ShowSnackbar(val message: String) : WeatherEffect
    object NavigateToDetails : WeatherEffect
}
```

### 4. UiReducer

Pure function that transforms state based on intents.

```kotlin
class WeatherReducer : UiReducer<WeatherState, WeatherIntent> {
    override fun reduce(currentState: WeatherState, intent: WeatherIntent): WeatherState {
        return when (intent) {
            is WeatherIntent.Load -> currentState.copy(isLoading = true, error = null)
            is WeatherIntent.Retry -> currentState.copy(isLoading = true, error = null)
            else -> currentState
        }
    }
}

// Or use functional reducer
val weatherReducer = createReducer<WeatherState, WeatherIntent> { state, intent ->
    when (intent) {
        is WeatherIntent.Load -> state.copy(isLoading = true)
        else -> state
    }
}
```

### 5. MviViewModel

Base ViewModel with state management, reducer support, and effect handling.

```kotlin
class WeatherViewModel(
    private val getWeatherUseCase: GetWeatherUseCase
) : MviViewModel<WeatherState, WeatherIntent, WeatherEffect>(
    initialState = WeatherState(),
    reducer = WeatherReducer() // Optional: reducer for automatic state updates
) {

    override fun onIntent(intent: WeatherIntent) {
        when (intent) {
            is WeatherIntent.Load -> loadWeather()
            is WeatherIntent.Refresh -> refreshWeather(intent.lat, intent.lon)
            is WeatherIntent.Retry -> {
                updateState { copy(error = null) }
                loadWeather()
            }
        }
    }

    private fun loadWeather() {
        getWeatherUseCase()
            .observe(
                onLoading = { updateState { copy(isLoading = true) } },
                onSuccess = { weather ->
                    updateState {
                        copy(
                            isLoading = false,
                            weather = weather,
                            error = null
                        )
                    }
                },
                onError = { error ->
                    updateState { copy(isLoading = false, error = error.message) }
                    sendEffect(WeatherEffect.ShowError(error.message ?: "Unknown error"))
                }
            )
    }
}
```

### 6. Compose Integration

```kotlin
@Composable
fun WeatherScreen(viewModel: WeatherViewModel = koinViewModel()) {
    val state by useMvi(
        viewModel = viewModel,
        initialIntent = WeatherIntent.Load,
        onEffect = { effect ->
            when (effect) {
                is WeatherEffect.ShowError -> showErrorDialog(effect.message)
                is WeatherEffect.ShowSnackbar -> showSnackbar(effect.message)
                is WeatherEffect.NavigateToDetails -> navigateToDetails()
            }
        }
    )

    when {
        state.isLoading -> LoadingIndicator()
        state.error != null -> ErrorView(
            message = state.error,
            onRetry = { viewModel.handleIntent(WeatherIntent.Retry) }
        )
        state.weather != null -> WeatherContent(
            weather = state.weather,
            onRefresh = {
                viewModel.handleIntent(WeatherIntent.Refresh(24.0, 67.0))
            }
        )
    }
}
```

## Helper Functions

### MviResult<T>

Wrapper for operations with loading, success, and error states.

```kotlin
getWeatherUseCase()
    .asMviResult()
    .collect { result ->
        result.fold(
            onSuccess = { weather -> updateState { copy(weather = it) } },
            onError = { error -> sendEffect(WeatherEffect.ShowError(error.message)) },
            onLoading = { updateState { copy(isLoading = true) } }
        )
    }
```

## Naming Conventions

- **State**: `{Screen}State` (e.g., `WeatherState`, `DashboardState`)
- **Intent**: `{Screen}Intent` (e.g., `WeatherIntent`, `DashboardIntent`)
- **Effect**: `{Screen}Effect` (e.g., `WeatherEffect`, `DashboardEffect`)
- **Reducer**: `{Screen}Reducer` (e.g., `WeatherReducer`, `DashboardReducer`)
- **ViewModel**: `{Screen}ViewModel` (e.g., `WeatherViewModel`, `DashboardViewModel`)

## Benefits

- **Simple**: Clean abstraction, easy to understand
- **Generic**: Works with any state/intent/effect types
- **Effective**: Clear separation of concerns with reducer pattern
- **Type-safe**: Compile-time safety with sealed interfaces
- **Testable**: Pure reducers and isolated ViewModels
- **Compose-friendly**: Built-in Compose integration helpers
