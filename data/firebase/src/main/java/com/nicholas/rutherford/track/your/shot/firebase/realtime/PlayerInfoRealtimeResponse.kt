package com.nicholas.rutherford.track.your.shot.firebase.realtime

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Represents a player's information stored in Firebase Realtime Database.
 *
 * @property firstName The player's first name.
 * @property lastName The player's last name.
 * @property positionValue The player's position represented as an integer value.
 * @property imageUrl URL of the player's profile image.
 * @property shotsLogged List of shots logged for this player in realtime.
 */
data class PlayerInfoRealtimeResponse(
    val firstName: String = "",
    val lastName: String = "",
    val positionValue: Int = 0,
    val imageUrl: String = "",
    val shotsLogged: List<ShotLoggedRealtimeResponse> = emptyList()
)
