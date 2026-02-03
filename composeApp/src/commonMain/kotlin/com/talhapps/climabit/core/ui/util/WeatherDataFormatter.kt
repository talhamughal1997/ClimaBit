package com.talhapps.climabit.core.ui.util

import com.talhapps.climabit.domain.model.weather.AirQualityResponse
import com.talhapps.climabit.domain.model.weather.OpenMeteoResponse

object WeatherDataFormatter {

    fun formatDashboardPrompt(
        weather: OpenMeteoResponse,
        oneCallData: OpenMeteoResponse?,
        locationName: String?
    ): String {
        val current = weather.current
        val location = locationName ?: "${weather.latitude}, ${weather.longitude}"

        val prompt = buildString {
            appendLine("Provide concise and actionable weather insights for the current conditions at $location.")
            appendLine()
            appendLine("Current Weather Data:")

            current?.temperature2m?.let {
                appendLine("- Temperature: ${it.toInt()}°C")
            }
            current?.apparentTemperature?.let {
                appendLine("- Feels like: ${it.toInt()}°C")
            }
            current?.relativeHumidity2m?.let {
                appendLine("- Humidity: $it%")
            }
            current?.windSpeed10m?.let {
                appendLine("- Wind Speed: ${it.toInt()} km/h")
            }
            current?.windDirection10m?.let {
                appendLine("- Wind Direction: $it°")
            }
            current?.pressureMsl?.let {
                appendLine("- Pressure: ${it.toInt()} hPa")
            }
            current?.weatherCode?.let {
                appendLine("- Weather Condition Code: $it")
            }

            oneCallData?.hourly?.precipitationProbability?.firstOrNull()?.let {
                appendLine("- Precipitation Probability: $it%")
            }

            oneCallData?.daily?.temperature2mMax?.firstOrNull()?.let { max ->
                oneCallData.daily?.temperature2mMin?.firstOrNull()?.let { min ->
                    appendLine("- Today's Range: ${min.toInt()}°C to ${max.toInt()}°C")
                }
            }

            appendLine()
            appendLine("Please provide:")
            appendLine("1. A brief summary of current conditions")
            appendLine("2. What to expect for the rest of the day")
            appendLine("3. Any recommendations (clothing, activities, precautions)")
            appendLine("4. Notable weather patterns or changes")
            appendLine()
            appendLine("Keep the response concise, friendly, and practical (max 200 words).")
        }

        return prompt
    }

    fun formatDetailsPrompt(
        weather: OpenMeteoResponse,
        airQuality: AirQualityResponse?,
        locationName: String?
    ): String {
        val current = weather.current
        val location = locationName ?: "${weather.latitude}, ${weather.longitude}"

        val prompt = buildString {
            appendLine("Provide detailed weather insights and analysis for $location.")
            appendLine()
            appendLine("Current Weather Data:")

            current?.temperature2m?.let {
                appendLine("- Temperature: ${it.toInt()}°C")
            }
            current?.apparentTemperature?.let {
                appendLine("- Feels like: ${it.toInt()}°C")
            }
            current?.relativeHumidity2m?.let {
                appendLine("- Humidity: $it%")
            }
            current?.windSpeed10m?.let {
                appendLine("- Wind Speed: ${it.toInt()} km/h")
            }
            current?.windGusts10m?.let {
                appendLine("- Wind Gusts: ${it.toInt()} km/h")
            }
            current?.windDirection10m?.let {
                appendLine("- Wind Direction: $it°")
            }
            current?.pressureMsl?.let {
                appendLine("- Pressure: ${it.toInt()} hPa")
            }
            current?.cloudCover?.let {
                appendLine("- Cloud Cover: $it%")
            }
            current?.weatherCode?.let {
                appendLine("- Weather Condition Code: $it")
            }

            weather.daily?.uvIndexMax?.firstOrNull()?.let {
                appendLine("- UV Index: ${it.toInt()}")
            }

            weather.daily?.sunrise?.firstOrNull()?.let {
                appendLine("- Sunrise: $it")
            }
            weather.daily?.sunset?.firstOrNull()?.let {
                appendLine("- Sunset: $it")
            }

            airQuality?.current?.let { aq ->
                val aqi = aq.europeanAqi ?: aq.usAqi ?: 0
                appendLine("- Air Quality Index: $aqi")
                aq.pm25?.let {
                    appendLine("- PM2.5: ${it.toInt()} μg/m³")
                }
                aq.pm10?.let {
                    appendLine("- PM10: ${it.toInt()} μg/m³")
                }
            }

            weather.daily?.let { daily ->
                appendLine()
                appendLine("7-Day Forecast Summary:")
                daily.time.take(7).forEachIndexed { index, date ->
                    val max = daily.temperature2mMax.getOrNull(index)
                    val min = daily.temperature2mMin.getOrNull(index)
                    val precip = daily.precipitationProbabilityMax.getOrNull(index)
                    val code = daily.weatherCode.getOrNull(index)

                    if (max != null && min != null) {
                        append("$date: ${min.toInt()}°C - ${max.toInt()}°C")
                        precip?.let { append(", $it% rain") }
                        code?.let { append(", condition code: $it") }
                        appendLine()
                    }
                }
            }

            appendLine()
            appendLine("Please provide:")
            appendLine("1. Comprehensive weather analysis for today")
            appendLine("2. Detailed forecast insights for the week ahead")
            appendLine("3. Health and safety recommendations based on air quality and UV index")
            appendLine("4. Activity suggestions based on weather conditions")
            appendLine("5. Any notable weather patterns, trends, or warnings")
            appendLine()
            appendLine("Keep the response detailed but readable (max 300 words).")
        }

        return prompt
    }
}

