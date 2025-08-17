package com.nicholas.rutherford.track.your.shot.data.test.firebase.realtime

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Utility object for creating test instances of [com.nicholas.rutherford.track.your.shot.firebase.realtime.DeclaredShotWithKeyRealtimeResponse].
 * Provides a predefined Firebase key and a nested test [com.nicholas.rutherford.track.your.shot.firebase.realtime.DeclaredShotRealtimeResponse].
 */
object TestDeclaredShotWithKeyRealtimeResponse {

    /**
     * Creates a test [com.nicholas.rutherford.track.your.shot.firebase.realtime.DeclaredShotWithKeyRealtimeResponse] instance with a predefined
     * Firebase key and a test [com.nicholas.rutherford.track.your.shot.firebase.realtime.DeclaredShotRealtimeResponse].
     *
     * @return a [com.nicholas.rutherford.track.your.shot.firebase.realtime.DeclaredShotWithKeyRealtimeResponse] containing a Firebase key and a
     *         test declared shot response.
     */
    fun create(): com.nicholas.rutherford.track.your.shot.firebase.realtime.DeclaredShotWithKeyRealtimeResponse {
        return _root_ide_package_.com.nicholas.rutherford.track.your.shot.firebase.realtime.DeclaredShotWithKeyRealtimeResponse(
            declaredShotFirebaseKey = DECLARED_SHOT_FIREBASE_KEY,
            declaredShotRealtimeResponse = TestDeclaredShotRealtimeResponse.create()
        )
    }
}

/** Predefined test Firebase key for [com.nicholas.rutherford.track.your.shot.firebase.realtime.DeclaredShotWithKeyRealtimeResponse]. */
const val DECLARED_SHOT_FIREBASE_KEY = "-j0P5J2LcXmXF"
