package com.talhapps.climabit.presentation.search

import androidx.lifecycle.viewModelScope
import com.talhapps.climabit.core.ui.mvi.MviViewModel
import com.talhapps.climabit.core.ui.mvi.UiEffect
import com.talhapps.climabit.core.ui.mvi.UiIntent
import com.talhapps.climabit.core.ui.mvi.UiState
import com.talhapps.climabit.domain.model.weather.GeocodingResponse
import kotlinx.coroutines.launch

data class SearchState(
    val isLoading: Boolean = false,
    val results: List<GeocodingResponse> = emptyList(),
    val error: String? = null
) : UiState

sealed interface SearchIntent : UiIntent {
    data class Search(val query: String) : SearchIntent
    data class SelectLocation(val lat: Double, val lon: Double) : SearchIntent
}

sealed interface SearchEffect : UiEffect {
    data class NavigateToDetails(val lat: Double, val lon: Double) : SearchEffect
    data class ShowError(val message: String) : SearchEffect
}

class SearchViewModel(
    // private val getGeocodingUseCase: GetGeocodingUseCase
) : MviViewModel<SearchState, SearchIntent, SearchEffect>(
    initialState = SearchState()
) {
    override fun onIntent(intent: SearchIntent) {
        when (intent) {
            is SearchIntent.Search -> {
                if (intent.query.length >= 3) {
                    searchLocation(intent.query)
                } else {
                    updateState { copy(results = emptyList()) }
                }
            }
            is SearchIntent.SelectLocation -> {
                sendEffect(SearchEffect.NavigateToDetails(intent.lat, intent.lon))
            }
        }
    }

    private fun searchLocation(query: String) {
        viewModelScope.launch {
            updateState { copy(isLoading = true, error = null) }
            // TODO: Implement geocoding search
            updateState { copy(isLoading = false, results = emptyList()) }
        }
    }
}

