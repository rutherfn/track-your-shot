package com.nicholas.rutherford.track.my.shot.feature.splash

import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable

@Composable
fun SplashScreen(viewModel: SplashViewModel) {
    Button(onClick = { viewModel.navigateSomewhere() }, content = { Text("dada")})
}