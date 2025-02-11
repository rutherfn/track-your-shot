package com.nicholas.rutherford.track.your.shot.helper.extensions.dataadditionupdates

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

class DataAdditionUpdatesImpl : DataAdditionUpdates {
    internal val newPlayerHasBeenAddedMutableSharedFlow = MutableSharedFlow<Boolean>(replay = Int.MAX_VALUE)
    internal val newReportHasBeenAddedMutableSharedFlow = MutableSharedFlow<Boolean>(replay = Int.MAX_VALUE)
    internal val shotHasBeenUpdatedMutableSharedFlow =  MutableSharedFlow<Boolean>(replay = Int.MAX_VALUE)

    override val newPlayerHasBeenAddedSharedFlow: SharedFlow<Boolean>
        get() = newPlayerHasBeenAddedMutableSharedFlow

    override val newReportHasBeenAddedSharedFlow: SharedFlow<Boolean>
        get() = newReportHasBeenAddedMutableSharedFlow

    override val shotHasBeenUpdatedSharedFlow: SharedFlow<Boolean>
        get() = shotHasBeenUpdatedMutableSharedFlow

    override suspend fun updateNewPlayerHasBeenAddedSharedFlow(hasBeenAdded: Boolean) {
        newPlayerHasBeenAddedMutableSharedFlow.emit(hasBeenAdded)
    }

    override suspend fun updateNewReportHasBeenAddedSharedFlow(hasBeenAdded: Boolean) {
        newReportHasBeenAddedMutableSharedFlow.emit(hasBeenAdded)
    }

    override suspend fun updateShotHasBeenUpdatedSharedFlow(hasShotBeenUpdated: Boolean) {
        shotHasBeenUpdatedMutableSharedFlow.emit(hasShotBeenUpdated)
    }
}
