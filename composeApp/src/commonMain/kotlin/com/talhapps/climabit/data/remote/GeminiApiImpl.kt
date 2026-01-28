package com.talhapps.climabit.data.remote

import com.talhapps.climabit.core.config.ApiKeys
import com.talhapps.climabit.data.core.executeAsFlow
import com.talhapps.climabit.domain.model.gemini.GeminiRequest
import com.talhapps.climabit.domain.model.gemini.GeminiResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.path
import io.ktor.http.takeFrom
import kotlinx.coroutines.flow.Flow

class GeminiApiImpl(
    private val client: HttpClient,
    private val baseUrl: String = DEFAULT_GEMINI_BASE_URL
) : GeminiApi {

    override fun generateContent(
        prompt: String,
        model: String
    ): Flow<GeminiResponse> {
        val request = GeminiRequest(
            contents = listOf(
                GeminiRequest.Content(
                    parts = listOf(
                        GeminiRequest.Content.Part(text = prompt)
                    ),
                    role = "user"
                )
            ),
            generationConfig = GeminiRequest.GenerationConfig(
                temperature = 0.7,
                topK = 40,
                topP = 0.95,
                maxOutputTokens = 2048
            )
        )
        return generateContent(request, model)
    }

    override fun generateContent(
        request: GeminiRequest,
        model: String
    ): Flow<GeminiResponse> = executeAsFlow {
        val apiKey = ApiKeys.GEMINI_API_KEY
        client.post {
            url {
                takeFrom(baseUrl)
                path("v1beta", "models", "$model:generateContent")
                parameter("key", apiKey)
            }
            contentType(ContentType.Application.Json)
            setBody(request)
        }
    }

    companion object {
        private const val DEFAULT_GEMINI_BASE_URL = "https://generativelanguage.googleapis.com"
    }
}


