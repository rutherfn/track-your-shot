package com.nicholas.rutherford.track.your.shot.data.test.room

import com.nicholas.rutherford.track.your.shot.data.room.entities.PlayerEntity
import com.nicholas.rutherford.track.your.shot.data.room.response.PlayerPositions

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Utility class for creating test instances of [PlayerEntity] objects.
 * Provides predefined test data for use in unit tests or mock scenarios.
 */
class TestPlayerEntity {

    /**
     * Creates a test [PlayerEntity] instance with predefined values.
     *
     * @return a new [PlayerEntity] containing test data including a sample logged shot.
     */
    fun create(): PlayerEntity {
        return PlayerEntity(
            id = PLAYER_ID,
            firstName = FIRST_NAME,
            lastName = LAST_NAME,
            position = position,
            firebaseKey = firebaseKey,
            imageUrl = IMAGE_URL,
            shotsLoggedList = listOf(TestShotLogged.build())
        )
    }
}

/** Predefined test player ID. */
const val PLAYER_ID = 1

/** Predefined test first name. */
const val FIRST_NAME = "first"

/** Predefined test last name. */
const val LAST_NAME = "last"

/** Predefined test player position. */
val position = PlayerPositions.Center

/** Predefined test firebase key. */
const val firebaseKey = "firebaseKey"

/** Predefined test image URL. */
const val IMAGE_URL = "url"
