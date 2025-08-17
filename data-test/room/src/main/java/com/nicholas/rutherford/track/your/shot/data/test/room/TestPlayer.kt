package com.nicholas.rutherford.track.your.shot.data.test.room

import com.nicholas.rutherford.track.your.shot.data.room.entities.toPlayer
import com.nicholas.rutherford.track.your.shot.data.room.response.Player

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Utility class for creating test instances of [Player] objects.
 * Converts a [TestPlayerEntity] into a [Player] for testing purposes.
 */
class TestPlayer {

    /**
     * Creates a test [Player] instance.
     *
     * @return a new [Player] converted from [TestPlayerEntity] with predefined test data.
     */
    fun create(): Player {
        return TestPlayerEntity().create().toPlayer()
    }
}
