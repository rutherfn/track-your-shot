package com.nicholas.rutherford.track.your.shot.data.test.room

import com.nicholas.rutherford.track.your.shot.data.room.entities.toActiveUser
import com.nicholas.rutherford.track.your.shot.data.room.response.ActiveUser

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Utility class for creating test instances of [ActiveUser].
 * Uses [TestActiveUserEntity] to generate a default active user entity
 * and converts it to the [ActiveUser] domain model.
 */
class TestActiveUser {

    /**
     * Creates a test [ActiveUser] instance.
     *
     * @return a new [ActiveUser] instance with predefined test values.
     */
    fun create(): ActiveUser {
        return TestActiveUserEntity().create().toActiveUser()
    }
}
