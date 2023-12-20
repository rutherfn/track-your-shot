package com.nicholas.rutherford.track.your.shot.feature.players

import com.nicholas.rutherford.track.your.shot.data.room.response.Player
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class PlayersAdditionUpdatesImpl : PlayersAdditionUpdates {

    internal val newPlayerHasBeenAddedMutableSharedFlow = MutableSharedFlow<Boolean>(replay = Int.MAX_VALUE)

    override val newPlayerHasBeenAddedSharedFlow: SharedFlow<Boolean>
        get() = newPlayerHasBeenAddedMutableSharedFlow

    override suspend fun updateNewPlayerHasBeenAddedSharedFlow(hasBeenAdded: Boolean) {
        newPlayerHasBeenAddedMutableSharedFlow.emit(hasBeenAdded)
    }
}
