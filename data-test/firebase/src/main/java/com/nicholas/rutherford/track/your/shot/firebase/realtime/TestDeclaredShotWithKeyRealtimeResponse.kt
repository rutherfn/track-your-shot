package com.nicholas.rutherford.track.your.shot.firebase.realtime

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Utility object for creating test instances of [DeclaredShotWithKeyRealtimeResponse].
 * Provides a predefined Firebase key and a nested test [DeclaredShotRealtimeResponse].
 */
object TestDeclaredShotWithKeyRealtimeResponse {

    /**
     * Creates a test [DeclaredShotWithKeyRealtimeResponse] instance with a predefined
     * Firebase key and a test [DeclaredShotRealtimeResponse].
     *
     * @return a [DeclaredShotWithKeyRealtimeResponse] containing a Firebase key and a
     *         test declared shot response.
     */
    fun create(): DeclaredShotWithKeyRealtimeResponse {
        return DeclaredShotWithKeyRealtimeResponse(
            declaredShotFirebaseKey = DECLARED_SHOT_FIREBASE_KEY,
            declaredShotRealtimeResponse = TestDeclaredShotRealtimeResponse.create()
        )
    }
}

/** Predefined test Firebase key for [DeclaredShotWithKeyRealtimeResponse]. */
const val DECLARED_SHOT_FIREBASE_KEY = "-j0P5J2LcXmXF"
