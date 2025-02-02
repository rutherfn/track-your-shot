package com.nicholas.rutherford.track.your.shot.feature.shots

interface ShotsListNavigation {
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
}
