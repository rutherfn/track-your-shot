package com.nicholas.rutherford.track.my.shot.feature.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.nicholas.rutherford.track.my.shot.compose.components.Content

@Composable
fun SplashScreen() {
    Content(
        ui = {
            SplashScreenContent()
        }
    )
}

@Composable
fun SplashScreenContent() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(Colors.primaryColor)
    ) {
        Image(
            painter = painterResource(id = DrawablesIds.splash),
            contentDescription = stringResource(id = StringsIds.splashIconDescription),
            modifier = Modifier.scale(scale = SPLASH_IMAGE_SCALE)
        )
    }
}
