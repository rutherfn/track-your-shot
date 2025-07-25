package com.nicholas.rutherford.track.your.shot.feature.shots

import com.nicholas.rutherford.track.your.shot.data.room.response.ShotLogged

/**
 * Represents a logged shot along with associated player information.
 *
 * @property shotLogged The [ShotLogged] data containing details of the shot (e.g., made/missed, timestamp).
 * @property playerId The unique ID of the player who took the shot.
 * @property playerName The name of the player who took the shot.
 */
data class ShotLoggedWithPlayer(
    val shotLogged: ShotLogged,
    val playerId: Int,
    val playerName: String
)
