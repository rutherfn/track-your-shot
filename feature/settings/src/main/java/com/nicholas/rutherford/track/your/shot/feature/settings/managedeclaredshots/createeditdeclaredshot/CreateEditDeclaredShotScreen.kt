package com.nicholas.rutherford.track.your.shot.feature.settings.managedeclaredshots.createeditdeclaredshot

import androidx.compose.runtime.Composable
import com.nicholas.rutherford.track.your.shot.compose.components.Content
import com.nicholas.rutherford.track.your.shot.data.shared.appbar.AppBar

@Composable
fun CreateEditDeclaredShotScreen(params: CreateEditDeclaredShotScreenParams) {
    Content(
        ui = {

        },
        appBar = AppBar(
            toolbarTitle = params.state.toolbarTitle,
            onIconButtonClicked = { params.onToolbarMenuClicked.invoke() }
        )
    )
}