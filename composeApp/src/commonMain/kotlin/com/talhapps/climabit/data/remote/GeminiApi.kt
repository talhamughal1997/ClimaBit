package com.talhapps.climabit.data.remote

import com.talhapps.climabit.domain.model.gemini.GeminiRequest
import com.talhapps.climabit.domain.model.gemini.GeminiResponse
import kotlinx.coroutines.flow.Flow

/**
 * Gemini API Interface
 * Docs: https://ai.google.dev/api/rest
 */
interface GeminiApi {
    /**
     * Generate content using Gemini API
     *
     * @param prompt The prompt message to send to Gemini
     * @param model The model to use (e.g., "gemini-3-flash-preview", "gemini-3-flash-preview-vision")
     * @return Flow of GeminiResponse
     */
    fun generateContent(
        prompt: String,
        model: String = "gemini-3-flash-preview"
    ): Flow<GeminiResponse>

    /**
     * Generate content with custom request
     *
     * @param request The GeminiRequest object
     * @param model The model to use
     * @return Flow of GeminiResponse
     */
    fun generateContent(
        request: GeminiRequest,
        model: String = "gemini-3-flash-preview"
    ): Flow<GeminiResponse>
}

