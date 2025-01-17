package com.nicholas.rutherford.track.your.shot.helper.extensions.dataadditionupdates

import kotlinx.coroutines.flow.SharedFlow

interface DataAdditionUpdates {
    val newPlayerHasBeenAddedSharedFlow: SharedFlow<Boolean>
    val newReportHasBeenAddedSharedFlow: SharedFlow<Boolean>

    suspend fun updateNewPlayerHasBeenAddedSharedFlow(hasBeenAdded: Boolean)
    suspend fun updateNewReportHasBeenAddedSharedFlow(hasBeenAdded: Boolean)
}
