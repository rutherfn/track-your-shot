package com.nicholas.rutherford.track.your.shot.feature.players.shots.logshot

data class LogShotParams(
    val state: LogShotState,
    val onBackButtonClicked: () -> Unit,
    val onDateShotsTakenClicked: () -> Unit,
    val updateIsExistingPlayerAndPlayerId: () -> Unit,
    val onShotsMadeUpwardClicked: (value: Int) -> Unit,
    val onShotsMadeDownwardClicked: (value: Int) -> Unit,
    val onShotsMissedUpwardClicked: (value: Int) -> Unit,
    val onShotsMissedDownwardClicked: (value: Int) -> Unit,
    val onSaveClicked: () -> Unit,
    val onDeleteShotClicked: () -> Unit
)
