package com.talhapps.climabit.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

@Composable
fun ClimaBitTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        /* dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
             val context = LocalContext.current
             if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
         }*/

        darkTheme -> highContrastDarkColorScheme
        else -> highContrastLightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography(),
        content = content
    )
}

@Immutable
data class ColorFamily(
    val color: Color,
    val onColor: Color,
    val colorContainer: Color,
    val onColorContainer: Color
)

val unspecified_scheme = ColorFamily(
    Color.Unspecified, Color.Unspecified, Color.Unspecified, Color.Unspecified
)
