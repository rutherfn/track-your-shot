package com.nicholas.rutherford.track.your.shot.feature.shots

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.nicholas.rutherford.track.your.shot.base.resources.R
import com.nicholas.rutherford.track.your.shot.compose.components.Content
import com.nicholas.rutherford.track.your.shot.data.shared.appbar.AppBar

@Composable
fun ShotsListScreen(params: ShotsListScreenParams) {
    val isShotListEmpty = params.state.shotList.isEmpty()

    Content(
        ui = {

        },
        appBar = AppBar(
            toolbarTitle = stringResource(id = R.string.shots),
            shouldShowMiddleContentAppBar = true,
            onIconButtonClicked = {
               // playerListScreenParams.onToolbarMenuClicked.invoke()
            },
            onSecondaryIconButtonClicked = {
                //playerListScreenParams.onAddPlayerClicked.invoke()
            }
        )
    )
}