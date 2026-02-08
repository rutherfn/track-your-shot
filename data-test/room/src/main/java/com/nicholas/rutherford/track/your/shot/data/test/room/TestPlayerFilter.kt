package com.nicholas.rutherford.track.your.shot.data.test.room

import com.nicholas.rutherford.track.your.shot.data.room.response.PlayerFilter
import com.nicholas.rutherford.track.your.shot.data.room.response.toPlayerFilter

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Utility class for creating test instances of [PlayerFilter] objects.
 * Converts a [TestPlayerFilterEntity] into a [PlayerFilter] for testing purposes.
 */
class TestPlayerFilter {

    /**
     * Creates a test [PlayerFilter] instance.
     *
     * @return a new [PlayerFilter] converted from [TestPlayerFilterEntity] with predefined test data.
     */
    fun create(): PlayerFilter {
        return TestPlayerFilterEntity().create().toPlayerFilter(positions = SELECTED_POSITIONS)
    }
}

val SELECTED_POSITIONS = listOf("Point Guard", "Shooting Guard")
