package com.nicholas.rutherford.track.your.shot.feature.players.createeditplayer

import com.nicholas.rutherford.track.your.shot.data.shared.alert.Alert
import com.nicholas.rutherford.track.your.shot.data.shared.progress.Progress

/**
 * Represents the UI state for the Create/Edit Player screen.
 *
 * Defines navigation actions available from the create edit player screen.
 */
interface CreateEditPlayerNavigation {
    fun navigateToPlayersList()
    fun alert(alert: Alert)
    fun appSettings()
    fun navigateToSelectShot(isExistingPlayer: Boolean, playerId: Int)
    fun navigateToLogShot(
        isExistingPlayer: Boolean,
        playerId: Int,
        shotType: Int,
        shotId: Int,
        viewCurrentExistingShot: Boolean,
        viewCurrentPendingShot: Boolean,
        fromShotList: Boolean
    )
    fun disableProgress()
    fun enableProgress(progress: Progress)
    fun pop()
}
