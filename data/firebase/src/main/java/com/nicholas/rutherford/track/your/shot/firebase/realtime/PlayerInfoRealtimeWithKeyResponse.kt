package com.nicholas.rutherford.track.your.shot.firebase.realtime

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Represents a player's information in Firebase Realtime Database along with its unique Firebase key.
 *
 * @property playerFirebaseKey The unique Firebase key associated with this player.
 * @property playerInfo The actual player information stored in Firebase.
 */
data class PlayerInfoRealtimeWithKeyResponse(
    val playerFirebaseKey: String,
    val playerInfo: PlayerInfoRealtimeResponse
)
