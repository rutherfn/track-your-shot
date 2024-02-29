package com.nicholas.rutherford.track.your.shot.feature.players.shots.logshot

data class LogShotParams(
    val state: LogShotState,
    val onBackButtonClicked: () -> Unit,
    val onDateShotsTakenClicked: () -> Unit,
    val updateIsExistingPlayerAndPlayerId: () -> Unit
)
