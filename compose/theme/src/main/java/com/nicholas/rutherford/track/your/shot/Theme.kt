package com.nicholas.rutherford.track.your.shot

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController

/**
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
    background = AppColors.LightGray,
    onBackground = AppColors.Black,
    surface = AppColors.White,
    onSurface = AppColors.Black,
    error = AppColors.Red,
    onError = AppColors.Black
)

@Composable
fun TrackMyShotTheme(
    content: @Composable () -> Unit
) {
    val systemUiController = rememberSystemUiController()

    systemUiController.setStatusBarColor(color = lightColorScheme.primary)
    systemUiController.setNavigationBarColor(color = Color.Black)

    MaterialTheme(
        colorScheme = lightColorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
