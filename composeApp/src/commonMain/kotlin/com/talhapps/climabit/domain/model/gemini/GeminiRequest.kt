package com.talhapps.climabit.domain.model.gemini

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Gemini API Request Model
 * Docs: https://ai.google.dev/api/rest
 */
@Serializable
data class GeminiRequest(
    @SerialName("contents") val contents: List<Content>,
    @SerialName("generationConfig") val generationConfig: GenerationConfig? = null,
    @SerialName("safetySettings") val safetySettings: List<SafetySetting>? = null
) {
    @Serializable
    data class Content(
        @SerialName("parts") val parts: List<Part>,
        @SerialName("role") val role: String? = null
    ) {
        @Serializable
        data class Part(
            @SerialName("text") val text: String
        )
    }

    @Serializable
    data class GenerationConfig(
        @SerialName("temperature") val temperature: Double? = null,
        @SerialName("topK") val topK: Int? = null,
        @SerialName("topP") val topP: Double? = null,
        @SerialName("maxOutputTokens") val maxOutputTokens: Int? = null,
        @SerialName("stopSequences") val stopSequences: List<String>? = null
    )

    @Serializable
    data class SafetySetting(
        @SerialName("category") val category: String,
        @SerialName("threshold") val threshold: String
    )
}

