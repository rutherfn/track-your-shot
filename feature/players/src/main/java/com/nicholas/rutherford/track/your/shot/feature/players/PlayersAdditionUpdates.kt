package com.nicholas.rutherford.track.your.shot.feature.players

import com.nicholas.rutherford.track.your.shot.data.room.response.Player
import kotlinx.coroutines.flow.StateFlow

interface PlayersAdditionUpdates {
    val newPlayerAddedStateFlow: StateFlow<Player?>

    fun updateNewPlayerAddedFlow(player: Player)
}