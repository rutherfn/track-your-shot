package com.nicholas.rutherford.track.your.shot.feature.players.playerlist

import com.nicholas.rutherford.track.your.shot.data.room.response.Player
import com.nicholas.rutherford.track.your.shot.data.room.response.PlayerPositions

data class PlayersListState(
    val playerList: List<Player> = emptyList(),
    val selectedPlayer: Player = Player(
        firstName = "",
        lastName = "",
        position = PlayerPositions.None,
        firebaseKey = "",
        imageUrl = "",
        shotsLoggedList = emptyList()
    )
)
