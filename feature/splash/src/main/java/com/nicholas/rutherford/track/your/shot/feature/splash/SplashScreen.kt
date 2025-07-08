package com.nicholas.rutherford.track.your.shot.feature.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

/**
 * Entry point composable for the splash screen UI.
 */
@Composable
fun SplashScreen() = SplashScreenContent()

/**
 * Basic splash screen layout displaying a full-screen transparent background
 * with content centered.
 * todo -> Expand on this to make it nicer in a future ticket.
 */
@Composable
private fun SplashScreenContent() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {}
}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    SplashScreen()
}
