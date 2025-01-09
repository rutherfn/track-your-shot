package com.nicholas.rutherford.track.your.shot.firebase.realtime

class TestPlayerInfoRealtimeWithKeyResponse {

    fun create(): PlayerInfoRealtimeWithKeyResponse {
        return PlayerInfoRealtimeWithKeyResponse(
            playerFirebaseKey = PLAYER_FIREBASE_KEY,
            playerInfo = TestPlayerInfoRealtimeResponse().create()
        )
    }
}

const val PLAYER_FIREBASE_KEY = "-j0P5J2LcXmXF"
