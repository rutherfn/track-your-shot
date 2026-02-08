package com.nicholas.rutherford.track.your.shot.feature.players.playerfilters

data class PlayerFiltersScreenParams(
    val state: PlayerFiltersState,
    val onToolbarMenuClicked: () -> Unit,
    val onPositionToggled: (title: String) -> Unit,
    val onShotLogsToggled: (title: String) -> Unit,
    val onMinShotsChanged: (value: Int) -> Unit,
    val onMaxShotsChanged: (value: Int) -> Unit,
    val onSeeResultsClicked: () -> Unit,
    val onClearShotRangeClicked: () -> Unit,
    val onResetFiltersClicked: () -> Unit
)
