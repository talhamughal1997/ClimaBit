package com.talhapps.climabit.domain.model.gemini

data class GeminiInsightRequest(
    val prompt: String,
    val model: String,
    val temperature: Double = 0.7,
    val maxOutputTokens: Int = 2048
)


