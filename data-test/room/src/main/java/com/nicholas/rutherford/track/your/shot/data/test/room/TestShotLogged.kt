package com.nicholas.rutherford.track.your.shot.data.test.room

import com.nicholas.rutherford.track.your.shot.data.room.response.ShotLogged

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Utility object for creating test instances of [ShotLogged].
 * Provides predefined test data for unit tests or mock scenarios.
 */
object TestShotLogged {

    /**
     * Builds a test [ShotLogged] instance with predefined values.
     *
     * @return a new [ShotLogged] containing test shot information including attempts, made/missed stats,
     *         percentages, timestamps, and pending status.
     */
    fun build(): ShotLogged {
        return ShotLogged(
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
    const val SHOT_ID = 1

    /** Predefined test name for the shot. */
    private const val SHOT_NAME = "shotName"

    /** Predefined test type for the shot. */
    private const val SHOT_TYPE = 3

    /** Predefined number of shots attempted in the test data. */
    private const val SHOTS_ATTEMPTED = 15

    /** Predefined number of shots made in the test data. */
    private const val SHOTS_MADE = 5

    /** Predefined number of shots missed in the test data. */
    private const val SHOTS_MISSED = 10

    /** Predefined shots made percentage value for test data. */
    private const val SHOTS_MADE_PERCENT_VALUE = 33.33

    /** Predefined shots missed percentage value for test data. */
    private const val SHOTS_MISSED_PERCENT_VALUE = 66.67

    /** Predefined milliseconds value for shots attempted in test data. */
    private const val SHOTS_ATTEMPTED_MILLISECONDS_VALUE = 1000L

    /** Predefined milliseconds value for shots logged in test data. */
    private const val SHOTS_LOGGED_MILLISECONDS_VALUE = 2000L

    /** Predefined pending status for the test shot. */
    private const val IS_PENDING = false
}
