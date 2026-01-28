package com.talhapps.climabit.domain.repository

import com.talhapps.climabit.domain.model.gemini.GeminiRequest
import com.talhapps.climabit.domain.model.gemini.GeminiResponse
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for Gemini AI operations
 */
interface GeminiRepository {
    /**
     * Generate insights from a prompt message
     *
     * @param prompt The prompt message
     * @param model The model to use (default: "gemini-3-flash-preview")
     * @return Flow of GeminiResponse
     */
    fun generateInsights(
        prompt: String,
        model: String = "gemini-3-flash-preview"
    ): Flow<GeminiResponse>

    /**
     * Generate insights with custom request
     *
     * @param request The GeminiRequest object
     * @param model The model to use (default: "gemini-3-flash-preview")
     * @return Flow of GeminiResponse
     */
    fun generateInsights(
        request: GeminiRequest,
        model: String = "gemini-3-flash-preview"
    ): Flow<GeminiResponse>
}


