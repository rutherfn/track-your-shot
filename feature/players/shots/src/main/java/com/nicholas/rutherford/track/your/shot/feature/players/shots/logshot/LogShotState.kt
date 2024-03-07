package com.nicholas.rutherford.track.your.shot.feature.players.shots.logshot

data class LogShotState(
    val shotName: String = "",
    val shotsLoggedDateValue: String = "",
    val shotsTakenDateValue: String = "",
    val shotsMade: Int = 0,
    val shotsMissed: Int = 0,
    val shotsAttempted: Int = 0,
    val shotsMadePercentValue: String = "",
    val shotsMissedPercentValue: String = ""
)
