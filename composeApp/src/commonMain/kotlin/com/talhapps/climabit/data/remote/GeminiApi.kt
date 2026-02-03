package com.talhapps.climabit.data.remote

import com.talhapps.climabit.domain.model.gemini.GeminiRequest
import com.talhapps.climabit.domain.model.gemini.GeminiResponse
import kotlinx.coroutines.flow.Flow

interface GeminiApi {
    fun generateContent(
        prompt: String,
        model: String
    ): Flow<GeminiResponse>

    fun generateContent(
        request: GeminiRequest,
        model: String
    ): Flow<GeminiResponse>
}


