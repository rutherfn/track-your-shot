package com.nicholas.rutherford.track.your.shot.data.test.firebase.realtime

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Utility object for creating test instances of [com.nicholas.rutherford.track.your.shot.firebase.realtime.ShotLoggedRealtimeResponse].
 * Provides predefined test values for all shot properties.
 */
object TestShotLoggedRealtimeResponse {

    /**
     * Builds a test [com.nicholas.rutherford.track.your.shot.firebase.realtime.ShotLoggedRealtimeResponse] instance with predefined values.
     *
     * @return a [com.nicholas.rutherford.track.your.shot.firebase.realtime.ShotLoggedRealtimeResponse] populated with test data.
     */
    fun build(): com.nicholas.rutherford.track.your.shot.firebase.realtime.ShotLoggedRealtimeResponse {
        return _root_ide_package_.com.nicholas.rutherford.track.your.shot.firebase.realtime.ShotLoggedRealtimeResponse(
            id = SHOT_ID,
            shotName = SHOT_NAME,
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

    /** Predefined test ID for the shot. */
    private const val SHOT_ID = 0

    /** Predefined test name for the shot. */
    private const val SHOT_NAME = "shotName"

    /** Predefined test type for the shot. */
    private const val SHOT_TYPE = 3

    /** Predefined test value for shots attempted. */
    private const val SHOTS_ATTEMPTED = 15

    /** Predefined test value for shots made. */
    private const val SHOTS_MADE = 5

    /** Predefined test value for shots missed. */
    private const val SHOTS_MISSED = 10

    /** Predefined test percentage value for shots made. */
    private const val SHOTS_MADE_PERCENT_VALUE = 33.33

    /** Predefined test percentage value for shots missed. */
    private const val SHOTS_MISSED_PERCENT_VALUE = 66.67

    /** Predefined test value for shot attempted timestamp in milliseconds. */
    private const val SHOTS_ATTEMPTED_MILLISECONDS_VALUE = 1000L

    /** Predefined test value for shot logged timestamp in milliseconds. */
    private const val SHOTS_LOGGED_MILLISECONDS_VALUE = 2000L

    /** Predefined test flag for whether the shot is pending. */
    private const val IS_PENDING = false
}
