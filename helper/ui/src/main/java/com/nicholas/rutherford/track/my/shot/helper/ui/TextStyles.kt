package com.nicholas.rutherford.track.my.shot.helper.ui

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.sp
import com.nicholas.rutherford.track.my.shot.feature.splash.Colors

object TextStyles {
    val HyperLink = TextStyle(
        fontSize = 14.sp,
        fontFamily = FontFamily.SansSerif,
        textDecoration = TextDecoration.Underline,
        color = Colors.secondaryColor
    )
}