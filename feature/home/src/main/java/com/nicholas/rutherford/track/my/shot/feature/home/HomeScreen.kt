package com.nicholas.rutherford.track.my.shot.feature.home

import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable

@Composable
fun HomeScreen(viewModel: HomeViewModel) {
    Button(onClick = { viewModel.navigateTest() }, content = { Text("Log Out") })
}
