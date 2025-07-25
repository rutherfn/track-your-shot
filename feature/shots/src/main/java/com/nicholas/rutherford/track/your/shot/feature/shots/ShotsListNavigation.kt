package com.nicholas.rutherford.track.your.shot.feature.shots

import com.nicholas.rutherford.track.your.shot.data.shared.alert.Alert

/**
 * Defines navigation actions available from the shots list screen.
 */
interface ShotsListNavigation {
    fun alert(alert: Alert)
    fun navigateToLogShot(
        isExistingPlayer: Boolean,
        playerId: Int,
        shotType: Int,
        shotId: Int,
        viewCurrentExistingShot: Boolean,
        viewCurrentPendingShot: Boolean,
        fromShotList: Boolean
    )
    fun openNavigationDrawer()
    fun popToPlayerList()
}
