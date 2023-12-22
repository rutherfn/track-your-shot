package com.nicholas.rutherford.track.your.shot.firebase.realtime

data class ShotLoggedRealtimeResponse(
    val shotType: Int = 0,
    val shotsAttempted: Int = 0,
    val shotsMade: Int = 0,
    val shotsMissed: Int = 0,
    val shotsMadePercentValue: Double = 0.0,
    val shotsMissedPercentValue: Double = 0.0,
    val shotsAttemptedMillisecondsValue: Long = 0L,
    val shotsLoggedMillisecondsValue: Long = 0L
)
