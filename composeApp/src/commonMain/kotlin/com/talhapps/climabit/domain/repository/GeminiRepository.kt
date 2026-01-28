package com.talhapps.climabit.domain.repository

import com.talhapps.climabit.domain.model.gemini.GeminiRequest
import com.talhapps.climabit.domain.model.gemini.GeminiResponse
import kotlinx.coroutines.flow.Flow

interface GeminiRepository {
    fun generateInsights(
        prompt: String,
        model: String = "gemini-3-flash-preview"
    ): Flow<GeminiResponse>

    fun generateInsights(
        request: GeminiRequest,
        model: String = "gemini-3-flash-preview"
    ): Flow<GeminiResponse>
}


