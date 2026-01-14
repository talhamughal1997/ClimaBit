package com.talhapps.climabit.presentation.dashboard

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.talhapps.climabit.domain.usecase.weather.GetCurrentWeatherDataUseCase
import kotlinx.coroutines.launch

class DashboardViewModel(
    private val getCurrentWeatherDataUseCase: GetCurrentWeatherDataUseCase
) : ViewModel() {

    var state by mutableStateOf("")
        private set


    init {
        viewModelScope.launch {
            getCurrentWeatherDataUseCase(24.8607,67.0011).collect {
//                state = it.count ?: ""
                state = it.wind.toString()
            }
        }
    }

}