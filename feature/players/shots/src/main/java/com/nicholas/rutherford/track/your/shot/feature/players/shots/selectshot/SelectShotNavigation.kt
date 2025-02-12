package com.nicholas.rutherford.track.your.shot.feature.players.shots.selectshot

import com.nicholas.rutherford.track.your.shot.data.shared.alert.Alert

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
