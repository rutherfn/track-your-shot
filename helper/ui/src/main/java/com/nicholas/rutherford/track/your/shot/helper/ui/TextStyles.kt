package com.nicholas.rutherford.track.your.shot.helper.ui

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.sp
import com.nicholas.rutherford.track.your.shot.feature.splash.Colors

object TextStyles {

    val large = TextStyle(
        fontSize = 40.sp,
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        color = Colors.primaryColor
    )

    val subLarge = TextStyle(
        fontSize = 28.sp,
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold
    )

    val medium = TextStyle(
        fontSize = 24.sp,
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium
    )

    val small = TextStyle(
        fontSize = 18.sp,
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium
    )

    val smallBold = TextStyle(
        fontSize = 18.sp,
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold
    )

    val bodyBold = TextStyle(
        fontSize = 14.sp,
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold
    )

    val body = TextStyle(
        fontSize = 14.sp,
        fontFamily = FontFamily.Default
    )

    val hyperLink = TextStyle(
        fontSize = 14.sp,
        fontFamily = FontFamily.Default,
        textDecoration = TextDecoration.Underline
    )

    val toolbar = TextStyle(
        fontSize = 22.sp,
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold
    )
}
