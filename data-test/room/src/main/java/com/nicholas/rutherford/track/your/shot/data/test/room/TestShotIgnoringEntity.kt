package com.nicholas.rutherford.track.your.shot.data.test.room

import com.nicholas.rutherford.track.your.shot.data.room.entities.ShotIgnoringEntity

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Utility object for creating test instances of [ShotIgnoringEntity].
 * Provides predefined test data for unit tests or mock scenarios.
 */
object TestShotIgnoringEntity {

    /**
     * Builds a test [ShotIgnoringEntity] instance with predefined values.
     *
     * @return a new [ShotIgnoringEntity] containing test ID and shot ID.
     */
    fun build(): ShotIgnoringEntity {
        return ShotIgnoringEntity(
            id = SHOT_IGNORING_ID,
            shotId = SHOT_IGNORING_SHOT_ID
        )
    }
}

/** Predefined test ID for [ShotIgnoringEntity]. */
const val SHOT_IGNORING_ID = 1

/** Predefined test shot ID for [ShotIgnoringEntity]. */
const val SHOT_IGNORING_SHOT_ID = 22
