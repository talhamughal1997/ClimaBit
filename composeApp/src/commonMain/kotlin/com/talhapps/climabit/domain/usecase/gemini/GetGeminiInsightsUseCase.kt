package com.talhapps.climabit.domain.usecase.gemini

import com.talhapps.climabit.core.domain.UseCase
import com.talhapps.climabit.domain.model.gemini.GeminiInsightRequest
import com.talhapps.climabit.domain.model.gemini.GeminiResponse
import com.talhapps.climabit.domain.repository.GeminiRepository
import kotlinx.coroutines.flow.flowOf

class GetGeminiInsightsUseCase(
    private val geminiRepository: GeminiRepository
) : UseCase<GeminiInsightRequest, GeminiResponse> {

    override fun invoke(params: GeminiInsightRequest?) =
        if (params == null) {
            flowOf(
                GeminiResponse(
                    error = GeminiResponse.GeminiError(
                        code = 400,
                        message = "Request parameters are required",
                        status = "INVALID_ARGUMENT"
                    )
                )
            )
        } else {
            geminiRepository.generateInsights(
                prompt = params.prompt,
                model = params.model
            )
        }
}


