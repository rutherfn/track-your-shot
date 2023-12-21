package com.nicholas.rutherford.track.your.shot.feature.players

import kotlinx.coroutines.flow.SharedFlow

interface PlayersAdditionUpdates {
    val newPlayerHasBeenAddedSharedFlow: SharedFlow<Boolean>

    suspend fun updateNewPlayerHasBeenAddedSharedFlow(hasBeenAdded: Boolean)
}
