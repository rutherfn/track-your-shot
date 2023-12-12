package com.nicholas.rutherford.track.your.shot.feature.players

import com.nicholas.rutherford.track.your.shot.data.room.response.Player
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class PlayersAdditionUpdatesImpl : PlayersAdditionUpdates {

    private val _newPlayerAddedStateFlow: MutableStateFlow<Player?> = MutableStateFlow(value = null)

    override val newPlayerAddedStateFlow: StateFlow<Player?> = _newPlayerAddedStateFlow.asStateFlow()

    override fun updateNewPlayerAddedFlow(player: Player) {
        _newPlayerAddedStateFlow.value = player
    }
}