package com.nicholas.rutherford.track.your.shot.helper.ui

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.sp

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16.
 *
 * Object containing predefined [TextStyle]s for consistent typography across the app.
 *
 * This centralizes font sizes, weights, and decorations to make it easier to maintain
 * a uniform design system and update styles in one place.
 */
object TextStyles {

    /** Large, bold text suitable for subtitles or section headers. */
    val subLarge = TextStyle(
        fontSize = 28.sp,
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold
    )

    /** Medium text with medium weight, suitable for headings or prominent labels. */
    val medium = TextStyle(
        fontSize = 24.sp,
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium
    )

    /** Small text with medium weight, suitable for standard labels or content. */
    val small = TextStyle(
        fontSize = 18.sp,
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium
    )

    /** Small, bold text for emphasis within small labels or content. */
    val smallBold = TextStyle(
        fontSize = 18.sp,
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold
    )

    /** Bold body text for emphasizing content. */
    val bodyBold = TextStyle(
        fontSize = 14.sp,
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold
    )

    /** Standard body text. */
    val body = TextStyle(
        fontSize = 14.sp,
        fontFamily = FontFamily.Default
    )

    /** Small, bold body text, suitable for footnotes or minor emphasis. */
    val bodySmall = TextStyle(
        fontSize = 12.sp,
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold
    )

    /** Text style for hyperlinks, underlined to indicate interactivity. */
    val hyperLink = TextStyle(
        fontSize = 14.sp,
        fontFamily = FontFamily.Default,
        textDecoration = TextDecoration.Underline
    )

    /** Bold text style for toolbar titles or primary navigation headers. */
    val toolbar = TextStyle(
        fontSize = 22.sp,
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold
    )
}
