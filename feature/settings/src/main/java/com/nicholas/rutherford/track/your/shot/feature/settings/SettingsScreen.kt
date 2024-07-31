package com.nicholas.rutherford.track.your.shot.feature.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Help
import androidx.compose.runtime.Composable
import com.nicholas.rutherford.track.your.shot.base.resources.R
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.nicholas.rutherford.track.your.shot.compose.components.Content
import com.nicholas.rutherford.track.your.shot.data.shared.appbar.AppBar

@Composable
fun SettingsScreen(params: SettingsParams) {
    Content(
        ui = {
            Text("Hello World")
        },
        appBar = AppBar(
            toolbarTitle = stringResource(id = R.string.settings),
            shouldShowMiddleContentAppBar = true,
            onIconButtonClicked = {
                params.onToolbarMenuClicked.invoke()
            },
        ),
        secondaryImageVector = Icons.Filled.Help
    )
}

@Preview
@Composable
fun SettingsScreenPreview() {
    Column(modifier = Modifier.background(Color.White)) {
        SettingsScreen(
            params = SettingsParams(
                onToolbarMenuClicked = {}
            )
        )
    }
}