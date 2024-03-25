package com.nicholas.rutherford.track.your.shot.feature.players.shots.selectshot

interface SelectShotNavigation {
    fun popFromCreatePlayer()
    fun popFromEditPlayer()
    fun navigateToLogShot(
        isExistingPlayer: Boolean,
        playerId: Int,
        shotId: Int
    )
}
