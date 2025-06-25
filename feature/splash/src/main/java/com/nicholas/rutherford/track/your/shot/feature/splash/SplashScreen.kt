package com.nicholas.rutherford.track.your.shot.feature.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.nicholas.rutherford.track.your.shot.compose.components.Content

const val SPLASH_IMAGE_SCALE = 1f

@Composable
fun SplashScreen(navigateToPlayersListLoginOrAuthentication: () -> Unit) {
    LaunchedEffect(Unit) {
        navigateToPlayersListLoginOrAuthentication.invoke()
    }

    Content(ui = { SplashScreenContent() })
}

@Composable
fun SplashScreenContent() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent)
    ) {
    }
}
