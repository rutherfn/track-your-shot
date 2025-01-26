package com.nicholas.rutherford.track.your.shot.feature.players.playerlist

import com.nicholas.rutherford.track.your.shot.data.room.response.Player

data class PlayersListScreenParams(
    val state: PlayersListState,
    val onToolbarMenuClicked: () -> Unit,
    val updatePlayerListState: () -> Unit,
    val onAddPlayerClicked: () -> Unit,
    val onPlayerClicked: (player: Player) -> Unit,
    val onSheetItemClicked: (index: Int) -> Unit
)
