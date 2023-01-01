package com.nicholas.rutherford.track.my.shot.feature.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.nicholas.rutherford.track.my.shot.compose.components.Content

@Composable
fun SplashScreen(viewModel: SplashViewModel) {
    val state = viewModel.splashStateFlow.collectAsState().value

    Content(
        ui = {
            SplashScreenContent(state = state)
        }
    )
}

@Composable
fun SplashScreenContent(
    state: SplashState
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(state.backgroundColor)
    ) {
        Image(
            painter = painterResource(id = state.imageDrawableId),
            contentDescription = stringResource(id = StringsIds.splashIconDescription),
            modifier = Modifier.scale(scale = state.imageScale)
        )
    }
}
