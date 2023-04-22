package com.nicholas.rutherford.track.my.shot

import androidx.compose.material.MaterialTheme
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import com.google.accompanist.systemuicontroller.rememberSystemUiController

/**
 * Defined theme */
private val lightColorPalette = lightColors(
    primary = PrimaryOrange,
    primaryVariant = PrimaryOrange,
    secondary = PrimaryWhite
)

@Composable
fun TrackMyShotTheme(
    content: @Composable () -> Unit
) {
    val systemUiController = rememberSystemUiController()

    //  systemUiController.setStatusBarColor(color = lightColorPalette.primary)
    // systemUiController.setNavigationBarColor(color = Color.Black)

    MaterialTheme(
        colors = lightColorPalette,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
