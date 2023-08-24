package com.nicholas.rutherford.track.my.shot.feature.players

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import com.nicholas.rutherford.track.my.shot.data.room.response.Player


@Composable
fun PlayersListScreen(playerList: List<Player>) {
    if (playerList.isNotEmpty()) {
        LazyColumn {
            items(playerList) { player ->
                PlayerItem(player = player)
            }
        }
    } else {
        AddNewPlayerEmptyState()
    }
}

@Composable
fun PlayerItem(player: Player) {

}

@Composable
fun AddNewPlayerEmptyState() {

}
