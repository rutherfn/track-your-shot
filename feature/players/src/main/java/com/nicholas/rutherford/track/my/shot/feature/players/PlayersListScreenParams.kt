package com.nicholas.rutherford.track.my.shot.feature.players

data class PlayersListScreenParams(
    val state: PlayersListState,
    val onToolbarMenuClicked: () -> Unit
)
