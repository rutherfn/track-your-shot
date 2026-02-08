package com.nicholas.rutherford.track.your.shot.data.test.room

import com.nicholas.rutherford.track.your.shot.data.room.entities.PlayerFilterEntity
import com.nicholas.rutherford.track.your.shot.helper.constants.Constants

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Utility class for creating test instances of [PlayerFilterEntity] objects.
 * Provides predefined test data for use in unit tests or mock scenarios.
 */
class TestPlayerFilterEntity {

    /**
     * Creates a test [PlayerFilterEntity] instance with predefined values.
     *
     * @return a new [PlayerFilterEntity] containing test data with default filter values.
     */
    fun create(): PlayerFilterEntity {
        return PlayerFilterEntity(
            id = FILTER_ID,
            hasShotsLogged = HAS_SHOTS_LOGGED,
            minShots = MIN_SHOTS,
            maxShots = MAX_SHOTS,
            lastUpdated = LAST_UPDATED
        )
    }
}

const val FILTER_ID = 1
const val HAS_SHOTS_LOGGED = Constants.BOTH_SHOTS_LOGGED_VALUE
const val MIN_SHOTS = 4

const val MAX_SHOTS = 9

const val LAST_UPDATED = 1000000L
