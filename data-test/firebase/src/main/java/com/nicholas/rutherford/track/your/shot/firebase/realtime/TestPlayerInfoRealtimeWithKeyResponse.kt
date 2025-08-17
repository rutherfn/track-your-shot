package com.nicholas.rutherford.track.your.shot.firebase.realtime

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Utility class for creating test instances of [PlayerInfoRealtimeWithKeyResponse].
 * Provides a predefined Firebase key and a test [PlayerInfoRealtimeResponse].
 */
class TestPlayerInfoRealtimeWithKeyResponse {

    /**
     * Creates a test [PlayerInfoRealtimeWithKeyResponse] instance with a predefined
     * Firebase key and a test player info response.
     *
     * @return a [PlayerInfoRealtimeWithKeyResponse] populated with test values.
     */
    fun create(): PlayerInfoRealtimeWithKeyResponse {
        return PlayerInfoRealtimeWithKeyResponse(
            playerFirebaseKey = PLAYER_FIREBASE_KEY,
            playerInfo = TestPlayerInfoRealtimeResponse().create()
        )
    }
}

/** Predefined test Firebase key for [PlayerInfoRealtimeWithKeyResponse]. */
const val PLAYER_FIREBASE_KEY = "-j0P5J2LcXmXF"
