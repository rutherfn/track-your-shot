package com.nicholas.rutherford.track.your.shot.firebase.realtime

object TestShotLoggedRealtimeResponse {

    fun build(): ShotLoggedRealtimeResponse {
        return ShotLoggedRealtimeResponse(
            shotName = "",
            shotType = SHOT_TYPE,
            shotsAttempted = SHOTS_ATTEMPTED,
            shotsMade = SHOTS_MADE,
            shotsMissed = SHOTS_MISSED,
            shotsMadePercentValue = SHOTS_MADE_PERCENT_VALUE,
            shotsMissedPercentValue = SHOTS_MISSED_PERCENT_VALUE,
            shotsAttemptedMillisecondsValue = SHOTS_ATTEMPTED_MILLISECONDS_VALUE,
            shotsLoggedMillisecondsValue = SHOTS_LOGGED_MILLISECONDS_VALUE,
            isPending = IS_PENDING
        )
    }

    private const val SHOT_NAME = "shotName"
    private const val SHOT_TYPE = 3
    private const val SHOTS_ATTEMPTED = 15
    private const val SHOTS_MADE = 5
    private const val SHOTS_MISSED = 10
    private const val SHOTS_MADE_PERCENT_VALUE = 33.33
    private const val SHOTS_MISSED_PERCENT_VALUE = 66.67
    private const val SHOTS_ATTEMPTED_MILLISECONDS_VALUE = 1000L
    private const val SHOTS_LOGGED_MILLISECONDS_VALUE = 2000L
    private const val IS_PENDING = false
}
