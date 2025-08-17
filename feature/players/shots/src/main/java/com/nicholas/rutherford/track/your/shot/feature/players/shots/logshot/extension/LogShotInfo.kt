package com.nicholas.rutherford.track.your.shot.feature.players.shots.logshot.extension

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Data class representing the information needed for logging a shot in the app.
 *
 * This class encapsulates various states and identifiers related to shots and players,
 * which are used when navigating between different shot-related screens or performing
 * operations on shot data.
 *
 * Created by Nicholas Rutherford
 * Last edited on 2025-08-16
 *
 * @property isExistingPlayer Flag indicating if the player already exists in the system.
 * @property playerId Unique identifier for the player.
 * @property shotType Type of shot (represented as an integer, likely mapping to a defined set of shot types).
 * @property shotId Unique identifier for the shot.
 * @property viewCurrentExistingShot Flag indicating if the current existing shot should be viewed.
 * @property viewCurrentPendingShot Flag indicating if the current pending shot should be viewed.
 * @property fromShotList Flag indicating if the shot action originated from the shot list screen.
 */
data class LogShotInfo(
    val isExistingPlayer: Boolean = false,
    val playerId: Int = 0,
    val shotType: Int = 0,
    val shotId: Int = 0,
    val viewCurrentExistingShot: Boolean = false,
    val viewCurrentPendingShot: Boolean = false,
    val fromShotList: Boolean = false
)
