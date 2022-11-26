package com.nicholas.rutherford.track.my.shot.helper.ui

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.sp
import com.nicholas.rutherford.track.my.shot.feature.splash.Colors

object TextStyles {

    val Large = TextStyle(
        fontSize = 40.sp,
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold, color = Colors.primaryColor
    )

    val SubLarge = TextStyle(
        fontSize = 32.sp, fontFamily = FontFamily.Default, fontWeight = FontWeight.Bold, color = Colors.primaryColor
    )

    val Medium = TextStyle(
        fontSize = 24.sp, fontFamily = FontFamily.Default, fontWeight = FontWeight.Medium, color = Colors.primaryColor
    )

    val Body = TextStyle(
        fontSize = 14.sp, fontFamily = FontFamily.Default, color = Colors.primaryColor
    )

    val HyperLink = TextStyle(
        fontSize = 14.sp,
        fontFamily = FontFamily.Default,
        textDecoration = TextDecoration.Underline,
        color = Colors.primaryColor
    )
}
