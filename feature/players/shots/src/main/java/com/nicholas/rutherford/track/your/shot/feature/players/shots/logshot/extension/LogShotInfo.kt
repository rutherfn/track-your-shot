package com.nicholas.rutherford.track.your.shot.feature.players.shots.logshot.extension

data class LogShotInfo(
    val isExistingPlayer: Boolean = false,
    val playerId: Int = 0,
    val shotType: Int = 0,
    val shotId: Int = 0,
    val viewCurrentExistingShot: Boolean = false,
    val viewCurrentPendingShot: Boolean = false,
    val fromShotList: Boolean = false
)
