package com.nicholas.rutherford.track.your.shot.feature.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun SettingsScreen() {
    Text("Hello World")
}

@Preview
@Composable
fun SettingsScreenPreview() {
    Column(modifier = Modifier.background(Color.White)) {
        SettingsScreen()
    }
}