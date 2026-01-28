package com.talhapps.climabit.presentation.dashboard.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.talhapps.climabit.core.ui.components.weather.getWeatherDescription
import com.talhapps.climabit.core.ui.components.weather.getWeatherIcon
import com.talhapps.climabit.domain.model.weather.OpenMeteoResponse

@Composable
fun MainWeatherCard(
    weather: OpenMeteoResponse,
    oneCallData: OpenMeteoResponse? = null,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLowest
        ),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                val weatherCode = weather.current?.weatherCode
                val weatherDescription = getWeatherDescription(weatherCode)
                val weatherIcon =
                    getWeatherIcon(weatherCode, weather.hourly?.isDay?.getOrNull(0) ?: 1)

                AsyncImage(
                    model = "https://openweathermap.org/img/wn/${weatherIcon}@4x.png",
                    contentDescription = weatherDescription,
                    modifier = Modifier.size(100.dp)
                )


                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    weather.current?.temperature2m?.let { temp ->
                        Text(
                            text = "${temp.toInt()}°C",
                            style = MaterialTheme.typography.displayLarge,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }



                    weather.current?.apparentTemperature?.let { feelsLike ->
                        Text(
                            text = "Feels like ${feelsLike.toInt()}°",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))


                    Row(
                        modifier = Modifier.align(Alignment.End),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        oneCallData?.daily?.temperature2mMin?.getOrNull(0)?.let { minTemp ->
                            Text(
                                modifier = Modifier.background(
                                    shape = MaterialTheme.shapes.medium,
                                    color = MaterialTheme.colorScheme.surfaceContainerLow
                                ).padding(vertical = 8.dp, horizontal = 12.dp),
                                text = buildAnnotatedString {
                                    append("L ↓ ")
                                    withStyle(style = SpanStyle(fontWeight = FontWeight.SemiBold)) {
                                        append("${minTemp.toInt()}°")
                                    }
                                },
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                            )
                        }
                        oneCallData?.daily?.temperature2mMax?.getOrNull(0)?.let { maxTemp ->
                            Text(
                                modifier = Modifier.background(
                                    shape = MaterialTheme.shapes.medium,
                                    color = MaterialTheme.colorScheme.surfaceContainerLow
                                ).padding(vertical = 8.dp, horizontal = 12.dp),
                                text = buildAnnotatedString {
                                    append("H ↑ ")
                                    withStyle(style = SpanStyle(fontWeight = FontWeight.SemiBold)) {
                                        append("${maxTemp.toInt()}°")
                                    }
                                },
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                            )
                        }
                    }

                }


            }

        }
    }
}

