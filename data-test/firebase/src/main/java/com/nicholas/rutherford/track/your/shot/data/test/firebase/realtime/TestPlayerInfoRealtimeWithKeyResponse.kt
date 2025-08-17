package com.nicholas.rutherford.track.your.shot.data.test.firebase.realtime

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Utility class for creating test instances of [com.nicholas.rutherford.track.your.shot.firebase.realtime.PlayerInfoRealtimeWithKeyResponse].
 * Provides a predefined Firebase key and a test [com.nicholas.rutherford.track.your.shot.firebase.realtime.PlayerInfoRealtimeResponse].
 */
class TestPlayerInfoRealtimeWithKeyResponse {

    /**
     * Creates a test [com.nicholas.rutherford.track.your.shot.firebase.realtime.PlayerInfoRealtimeWithKeyResponse] instance with a predefined
     * Firebase key and a test player info response.
     *
     * @return a [com.nicholas.rutherford.track.your.shot.firebase.realtime.PlayerInfoRealtimeWithKeyResponse] populated with test values.
     */
    fun create(): com.nicholas.rutherford.track.your.shot.firebase.realtime.PlayerInfoRealtimeWithKeyResponse {
        return _root_ide_package_.com.nicholas.rutherford.track.your.shot.firebase.realtime.PlayerInfoRealtimeWithKeyResponse(
            playerFirebaseKey = PLAYER_FIREBASE_KEY,
            playerInfo = TestPlayerInfoRealtimeResponse().create()
        )
    }
}

/** Predefined test Firebase key for [com.nicholas.rutherford.track.your.shot.firebase.realtime.PlayerInfoRealtimeWithKeyResponse]. */
const val PLAYER_FIREBASE_KEY = "-j0P5J2LcXmXF"
