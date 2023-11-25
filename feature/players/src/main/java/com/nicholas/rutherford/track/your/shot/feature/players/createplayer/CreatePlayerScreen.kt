package com.nicholas.rutherford.track.your.shot.feature.players.createplayer

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.nicholas.rutherford.track.your.shot.base.resources.R
import com.nicholas.rutherford.track.your.shot.compose.components.Content
import com.nicholas.rutherford.track.your.shot.data.shared.appbar.AppBar

@Composable
fun CreatePlayerScreen(createPlayerParams: CreatePlayerParams) {
    Content(
        ui = {},
        appBar = AppBar(
                toolbarTitle = stringResource(id = R.string.create_player),
                shouldShowMiddleContentAppBar = false,
                onIconButtonClicked = {
                    createPlayerParams.onToolbarMenuClicked.invoke()
                }
            )
    )
}