package com.nicholas.rutherford.track.your.shot.feature.players

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

class PlayersAdditionUpdatesImpl : PlayersAdditionUpdates {

    internal val newPlayerHasBeenAddedMutableSharedFlow = MutableSharedFlow<Boolean>(replay = Int.MAX_VALUE)

    override val newPlayerHasBeenAddedSharedFlow: SharedFlow<Boolean>
        get() = newPlayerHasBeenAddedMutableSharedFlow

    override suspend fun updateNewPlayerHasBeenAddedSharedFlow(hasBeenAdded: Boolean) {
        newPlayerHasBeenAddedMutableSharedFlow.emit(hasBeenAdded)
    }
}
