package com.nicholas.rutherford.track.your.shot.data.test.room

import com.nicholas.rutherford.track.your.shot.data.room.entities.UserEntity

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Utility class for creating test instances of [UserEntity].
 * Provides a simple way to generate a [UserEntity] with predefined test values.
 */
class TestUserEntity {

    /**
     * Creates a test [UserEntity] instance with predefined test data.
     *
     * @return a [UserEntity] object containing test data.
     */
    fun create(): UserEntity {
        return UserEntity(
            id = ID,
            email = EMAIL,
            username = USERNAME
        )
    }
}
