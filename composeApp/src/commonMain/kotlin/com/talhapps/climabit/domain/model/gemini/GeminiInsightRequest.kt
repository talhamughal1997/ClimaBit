package com.talhapps.climabit.domain.model.gemini

/**
 * Request model for generating weather insights
 */
data class GeminiInsightRequest(
    val prompt: String,
    val model: String = "gemini-3-flash-preview",
    val temperature: Double = 0.7,
    val maxOutputTokens: Int = 2048
)

