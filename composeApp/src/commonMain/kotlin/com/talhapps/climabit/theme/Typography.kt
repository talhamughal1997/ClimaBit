package com.talhapps.climabit.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable

/**
 * Material 3 Typography with Poppins font family.
 * Must be called from a @Composable context.
 */
@Composable
fun AppTypography(): Typography {
    val baseline = Typography()
    val bodyFont = bodyFontFamily()
    val displayFont = displayFontFamily()

    return Typography(
        displayLarge = baseline.displayLarge.copy(fontFamily = displayFont),
        displayMedium = baseline.displayMedium.copy(fontFamily = displayFont),
        displaySmall = baseline.displaySmall.copy(fontFamily = displayFont),
        headlineLarge = baseline.headlineLarge.copy(fontFamily = displayFont),
        headlineMedium = baseline.headlineMedium.copy(fontFamily = displayFont),
        headlineSmall = baseline.headlineSmall.copy(fontFamily = displayFont),
        titleLarge = baseline.titleLarge.copy(fontFamily = displayFont),
        titleMedium = baseline.titleMedium.copy(fontFamily = displayFont),
        titleSmall = baseline.titleSmall.copy(fontFamily = displayFont),
        bodyLarge = baseline.bodyLarge.copy(fontFamily = bodyFont),
        bodyMedium = baseline.bodyMedium.copy(fontFamily = bodyFont),
        bodySmall = baseline.bodySmall.copy(fontFamily = bodyFont),
        labelLarge = baseline.labelLarge.copy(fontFamily = bodyFont),
        labelMedium = baseline.labelMedium.copy(fontFamily = bodyFont),
        labelSmall = baseline.labelSmall.copy(fontFamily = bodyFont),
    )
}
