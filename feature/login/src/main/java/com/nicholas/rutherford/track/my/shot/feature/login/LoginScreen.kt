package com.nicholas.rutherford.track.my.shot.feature.login

import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable

@Composable
fun LoginScreen(viewModel: LoginViewModel) {
    Button(onClick = { viewModel.onButtonClickedTest() }, content = { Text(text = "Login Button")})
}
