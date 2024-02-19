package com.nicholas.rutherford.track.your.shot.feature.players.shots.logshot

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.res.stringResource
import com.nicholas.rutherford.track.your.shot.compose.components.Content
import com.nicholas.rutherford.track.your.shot.data.shared.appbar.AppBar
import com.nicholas.rutherford.track.your.shot.feature.splash.StringsIds

@Composable
fun LogShotScreen(logShotParams: LogShotParams) {

    BackHandler(true) {
        logShotParams.onBackButtonClicked.invoke()
    }

    LaunchedEffect(Unit) {
        logShotParams.updateIsExistingPlayerAndPlayerId.invoke()
    }

    Content(
        ui = {

        },
        appBar = AppBar(
            toolbarTitle = stringResource(id = StringsIds.logShot),
            shouldShowMiddleContentAppBar = false,
            shouldIncludeSpaceAfterDeclaration = false,
            shouldShowSecondaryButton = false,
            onIconButtonClicked = { logShotParams.onBackButtonClicked.invoke() }
        )
    )
}