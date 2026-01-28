package com.talhapps.climabit.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import climabit.composeapp.generated.resources.Poppins_Bold
import climabit.composeapp.generated.resources.Poppins_Light
import climabit.composeapp.generated.resources.Poppins_Medium
import climabit.composeapp.generated.resources.Poppins_Regular
import climabit.composeapp.generated.resources.Poppins_SemiBold
import climabit.composeapp.generated.resources.Res
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.Font

@OptIn(ExperimentalResourceApi::class)
@Composable
fun PoppinsFontFamily(): FontFamily {
    return FontFamily(
        Font(Res.font.Poppins_Light, FontWeight.Light),
        Font(Res.font.Poppins_Regular, FontWeight.Normal),
        Font(Res.font.Poppins_Medium, FontWeight.Medium),
        Font(Res.font.Poppins_SemiBold, FontWeight.SemiBold),
        Font(Res.font.Poppins_Bold, FontWeight.Bold)
    )
}

@Composable
fun bodyFontFamily(): FontFamily = PoppinsFontFamily()

@Composable
fun displayFontFamily(): FontFamily = PoppinsFontFamily()

val CenturyGothicFontFamily: FontFamily = FontFamily.SansSerif
