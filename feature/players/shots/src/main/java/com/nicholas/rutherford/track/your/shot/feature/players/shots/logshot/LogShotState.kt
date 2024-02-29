package com.nicholas.rutherford.track.your.shot.feature.players.shots.logshot

data class LogShotState(
    val shotName: String = "",
    val shotsAttempted: Int = 0,
    val shotsMade: Int = 0,
    val shotsMissed: Int = 0,
    val shotsMadePercentValue: String = "",
    val shotsMissedPercentValue: String = "",
    val shotsAttemptedPercentValue: String = " "
)
