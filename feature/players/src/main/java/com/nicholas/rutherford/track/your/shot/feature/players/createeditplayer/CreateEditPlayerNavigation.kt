package com.nicholas.rutherford.track.your.shot.feature.players.createeditplayer

import com.nicholas.rutherford.track.your.shot.data.shared.alert.Alert
import com.nicholas.rutherford.track.your.shot.data.shared.progress.Progress

interface CreateEditPlayerNavigation {
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
