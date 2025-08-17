package com.nicholas.rutherford.track.your.shot.feature.players.shots.selectshot

import com.nicholas.rutherford.track.your.shot.data.shared.alert.Alert

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Defines navigation actions available from the select shot screen.
 */
interface SelectShotNavigation {
    fun alert(alert: Alert)
    fun popFromCreatePlayer()
    fun popFromEditPlayer()
    fun navigateToLogShot(
        isExistingPlayer: Boolean,
        playerId: Int,
        shotType: Int,
        shotId: Int,
        viewCurrentExistingShot: Boolean,
        viewCurrentPendingShot: Boolean,
        fromShotList: Boolean
    )
}
