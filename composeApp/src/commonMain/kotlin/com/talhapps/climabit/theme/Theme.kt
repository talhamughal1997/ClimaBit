package com.talhapps.climabit.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

/**
 * ClimaBit Material 3 Theme with dark/light mode support and Poppins font.
 *
 * @param darkTheme Whether to use dark theme. If null, follows system preference.
 * @param dynamicColor Dynamic color is available on Android 12+ (currently disabled)
 * @param content The composable content to apply the theme to.
 */
@Composable
fun ClimaBitTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        /* dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
             val context = LocalContext.current
             if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
         }*/

        darkTheme -> darkScheme
        else -> lightScheme
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
