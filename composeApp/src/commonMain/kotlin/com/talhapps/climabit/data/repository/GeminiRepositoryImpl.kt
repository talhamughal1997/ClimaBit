package com.talhapps.climabit.data.repository

import com.talhapps.climabit.data.remote.GeminiApi
import com.talhapps.climabit.domain.model.gemini.GeminiRequest
import com.talhapps.climabit.domain.model.gemini.GeminiResponse
import com.talhapps.climabit.domain.repository.GeminiRepository
import kotlinx.coroutines.flow.Flow

class GeminiRepositoryImpl(
    private val geminiApi: GeminiApi
) : GeminiRepository {

    override fun generateInsights(
        prompt: String,
        model: String
    ): Flow<GeminiResponse> {
        return geminiApi.generateContent(prompt, model)
    }

    override fun generateInsights(
        request: GeminiRequest,
        model: String
    ): Flow<GeminiResponse> {
        return geminiApi.generateContent(request, model)
    }
}


