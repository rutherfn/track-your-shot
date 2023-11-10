package com.nicholas.rutherford.track.your.shot.feature.players

data class PlayersListScreenParams(
    val state: PlayersListState,
    val onToolbarMenuClicked: () -> Unit
)
