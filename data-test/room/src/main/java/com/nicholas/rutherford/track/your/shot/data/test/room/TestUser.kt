package com.nicholas.rutherford.track.your.shot.data.test.room

import com.nicholas.rutherford.track.your.shot.data.room.entities.toUser
import com.nicholas.rutherford.track.your.shot.data.room.response.User

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Utility class for creating test instances of [User].
 * Converts a [TestUserEntity] into a [User] object for testing or mock scenarios.
 */
class TestUser {

    /**
     * Creates a test [User] instance by converting a [TestUserEntity] to [User].
     *
     * @return a [User] object containing test data.
     */
    fun create(): User {
        return TestUserEntity().create().toUser()
    }
}
