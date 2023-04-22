package com.nicholas.rutherford.track.my.shot.feature.home

import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.nicholas.rutherford.track.my.shot.compose.components.Content

@Composable
fun HomeScreen(viewModel: HomeViewModel) {

    Content(
        ui = {
            CreateAccountContent(viewModel = viewModel)
        }
    )
}

@Composable
fun CreateAccountContent(viewModel: HomeViewModel) {
    Button(onClick = { viewModel.navigateTest() }, content = { Text("Log Out") })
}
