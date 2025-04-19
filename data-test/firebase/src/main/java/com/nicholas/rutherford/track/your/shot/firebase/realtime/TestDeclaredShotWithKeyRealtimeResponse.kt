package com.nicholas.rutherford.track.your.shot.firebase.realtime

object TestDeclaredShotWithKeyRealtimeResponse {

    fun create(): DeclaredShotWithKeyRealtimeResponse {
        return DeclaredShotWithKeyRealtimeResponse(
            declaredShotFirebaseKey = DECLARED_SHOT_FIREBASE_KEY,
            declaredShotRealtimeResponse = TestDeclaredShotRealtimeResponse.create()
        )
    }
}

const val DECLARED_SHOT_FIREBASE_KEY = "-j0P5J2LcXmXF"
