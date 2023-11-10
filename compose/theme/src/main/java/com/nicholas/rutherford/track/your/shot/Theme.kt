package com.nicholas.rutherford.track.your.shot

import androidx.compose.material.MaterialTheme
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController

/**
 * Defined theme */
private val lightColorPalette = lightColors(
    primary = AppColors.Orange,
    primaryVariant = AppColors.OrangeVariant,
    secondary = AppColors.Black,
    secondaryVariant = AppColors.BlackVariant,
    surface = AppColors.White,
    onSurface = AppColors.Black,
    background = AppColors.White,
    onBackground = AppColors.Black,
    error = AppColors.Red,
    onError = AppColors.Black
)

@Composable
fun TrackMyShotTheme(
    content: @Composable () -> Unit
) {
    val systemUiController = rememberSystemUiController()

    systemUiController.setStatusBarColor(color = lightColorPalette.primary)
    systemUiController.setNavigationBarColor(color = Color.Black)

    MaterialTheme(
        colors = lightColorPalette,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
