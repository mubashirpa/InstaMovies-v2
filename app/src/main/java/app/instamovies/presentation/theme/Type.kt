package app.instamovies.presentation.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import app.instamovies.R.font as Fonts

val Karla =
    FontFamily(
        Font(Fonts.karla_light, FontWeight.Light),
        Font(Fonts.karla_regular, FontWeight.Normal),
        Font(Fonts.karla_italic, FontWeight.Normal, FontStyle.Italic),
        Font(Fonts.karla_medium, FontWeight.Medium),
        Font(Fonts.karla_bold, FontWeight.Bold),
    )

private val Montserrat =
    FontFamily(
        Font(Fonts.montserrat_light, FontWeight.Light),
        Font(Fonts.montserrat_regular, FontWeight.Normal),
        Font(Fonts.montserrat_italic, FontWeight.Normal, FontStyle.Italic),
        Font(Fonts.montserrat_medium, FontWeight.Medium),
        Font(Fonts.montserrat_bold, FontWeight.Bold),
    )

val Typography =
    Typography(
        displayLarge =
            TextStyle(
                fontFamily = Karla,
                fontWeight = FontWeight.Normal,
                letterSpacing = (-0.25).sp,
                lineHeight = 64.sp,
                fontSize = 57.sp,
            ),
        displayMedium =
            TextStyle(
                fontFamily = Karla,
                fontWeight = FontWeight.Normal,
                letterSpacing = 0.sp,
                lineHeight = 52.sp,
                fontSize = 45.sp,
            ),
        displaySmall =
            TextStyle(
                fontFamily = Karla,
                fontWeight = FontWeight.Normal,
                letterSpacing = 0.sp,
                lineHeight = 44.sp,
                fontSize = 36.sp,
            ),
        headlineLarge =
            TextStyle(
                fontFamily = Karla,
                fontWeight = FontWeight.Normal,
                letterSpacing = 0.sp,
                lineHeight = 40.sp,
                fontSize = 32.sp,
            ),
        headlineMedium =
            TextStyle(
                fontFamily = Karla,
                fontWeight = FontWeight.Normal,
                letterSpacing = 0.sp,
                lineHeight = 36.sp,
                fontSize = 28.sp,
            ),
        headlineSmall =
            TextStyle(
                fontFamily = Karla,
                fontWeight = FontWeight.Normal,
                letterSpacing = 0.sp,
                lineHeight = 32.sp,
                fontSize = 24.sp,
            ),
        titleLarge =
            TextStyle(
                fontFamily = Karla,
                fontWeight = FontWeight.Normal,
                letterSpacing = 0.sp,
                lineHeight = 28.sp,
                fontSize = 22.sp,
            ),
        titleMedium =
            TextStyle(
                fontFamily = Montserrat,
                fontWeight = FontWeight.Medium,
                letterSpacing = 0.15.sp,
                lineHeight = 24.sp,
                fontSize = 16.sp,
            ),
        titleSmall =
            TextStyle(
                fontFamily = Montserrat,
                fontWeight = FontWeight.Medium,
                letterSpacing = 0.1.sp,
                lineHeight = 20.sp,
                fontSize = 14.sp,
            ),
        bodyLarge =
            TextStyle(
                fontFamily = Karla,
                fontWeight = FontWeight.Normal,
                letterSpacing = 0.5.sp,
                lineHeight = 24.sp,
                fontSize = 16.sp,
            ),
        bodyMedium =
            TextStyle(
                fontFamily = Karla,
                fontWeight = FontWeight.Normal,
                letterSpacing = 0.25.sp,
                lineHeight = 20.sp,
                fontSize = 14.sp,
            ),
        bodySmall =
            TextStyle(
                fontFamily = Karla,
                fontWeight = FontWeight.Normal,
                letterSpacing = 0.4.sp,
                lineHeight = 16.sp,
                fontSize = 12.sp,
            ),
        labelLarge =
            TextStyle(
                fontFamily = Montserrat,
                fontWeight = FontWeight.Medium,
                letterSpacing = 0.1.sp,
                lineHeight = 20.sp,
                fontSize = 14.sp,
            ),
        labelMedium =
            TextStyle(
                fontFamily = Montserrat,
                fontWeight = FontWeight.Medium,
                letterSpacing = 0.5.sp,
                lineHeight = 16.sp,
                fontSize = 12.sp,
            ),
        labelSmall =
            TextStyle(
                fontFamily = Montserrat,
                fontWeight = FontWeight.Medium,
                letterSpacing = 0.5.sp,
                lineHeight = 16.sp,
                fontSize = 11.sp,
            ),
    )
