package com.nicholas.rutherford.track.your.shot.firebase.realtime

data class PlayerInfoRealtimeResponse(
    val firstName: String = "",
    val lastName: String = "",
    val positionValue: Int = 0,
    val imageUrl: String = ""
)
