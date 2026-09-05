package com.nicholas.rutherford.track.your.shot

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Defined theme using Material 3
 */
private val lightColorScheme = lightColorScheme(
    primary = AppColors.Orange,
    onPrimary = AppColors.White,
    primaryContainer = AppColors.OrangeVariant,
    onPrimaryContainer = AppColors.Black,
    secondary = AppColors.Black,
    onSecondary = AppColors.White,
    secondaryContainer = AppColors.BlackVariant,
    onSecondaryContainer = AppColors.White,
    background = AppColors.OffWhite,
    onBackground = AppColors.Black,
    surface = AppColors.White,
    onSurface = AppColors.Black,
    error = AppColors.Red,
    onError = AppColors.Black
)

@Composable
fun TrackYourShotTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = lightColorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
