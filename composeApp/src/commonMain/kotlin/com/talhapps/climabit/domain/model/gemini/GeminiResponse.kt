package com.talhapps.climabit.domain.model.gemini

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Gemini API Response Model
 * Docs: https://ai.google.dev/api/rest
 */
@Serializable
data class GeminiResponse(
    @SerialName("candidates") val candidates: List<Candidate>? = null,
    @SerialName("promptFeedback") val promptFeedback: PromptFeedback? = null,
    @SerialName("error") val error: GeminiError? = null
) {
    @Serializable
    data class Candidate(
        @SerialName("content") val content: Content,
        @SerialName("finishReason") val finishReason: String? = null,
        @SerialName("safetyRatings") val safetyRatings: List<SafetyRating>? = null,
        @SerialName("tokenCount") val tokenCount: Int? = null
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
        data class SafetyRating(
            @SerialName("category") val category: String,
            @SerialName("probability") val probability: String,
            @SerialName("blocked") val blocked: Boolean? = null
        )
    }

    @Serializable
    data class PromptFeedback(
        @SerialName("blockReason") val blockReason: String? = null,
        @SerialName("safetyRatings") val safetyRatings: List<SafetyRating>? = null
    ) {
        @Serializable
        data class SafetyRating(
            @SerialName("category") val category: String,
            @SerialName("probability") val probability: String,
            @SerialName("blocked") val blocked: Boolean? = null
        )
    }

    @Serializable
    data class GeminiError(
        @SerialName("code") val code: Int? = null,
        @SerialName("message") val message: String? = null,
        @SerialName("status") val status: String? = null
    )

    /**
     * Extract the generated text from the response
     */
    fun getText(): String? {
        return candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
    }
}

