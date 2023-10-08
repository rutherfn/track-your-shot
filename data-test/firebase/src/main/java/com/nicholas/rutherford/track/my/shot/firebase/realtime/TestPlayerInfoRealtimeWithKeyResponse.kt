package com.nicholas.rutherford.track.my.shot.firebase.realtime

const val PLAYER_FIREBASE_KEY = "-j0P5J2LcXmXF"
class TestPlayerInfoRealtimeWithKeyResponse {

    fun create(): PlayerInfoRealtimeWithKeyResponse {
        return PlayerInfoRealtimeWithKeyResponse(
            playerFirebaseKey = PLAYER_FIREBASE_KEY,
            playerInfo = TestPlayerInfoRealtimeResponse().create()
        )
    }
}
