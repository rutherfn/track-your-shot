package com.nicholas.rutherford.track.your.shot.data.test.room

import com.nicholas.rutherford.track.your.shot.data.room.entities.ActiveUserEntity
import com.nicholas.rutherford.track.your.shot.helper.constants.Constants

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Utility class for creating test instances of [ActiveUserEntity].
 * Provides a simple method to generate a default active user for testing purposes.
 */
class TestActiveUserEntity {

    /**
     * Creates a test [ActiveUserEntity] with default values.
     *
     * @return a new [ActiveUserEntity] instance with predefined test values.
     */
    fun create(): ActiveUserEntity {
        return ActiveUserEntity(
            id = ID,
            accountHasBeenCreated = ACCOUNT_HAS_BEEN_CREATED,
            email = EMAIL,
            username = USERNAME,
            firebaseAccountInfoKey = FIREBASE_ACCOUNT_INFO_KEY
        )
    }
}

/** Default test ID for the active user entity */
const val ID = Constants.ACTIVE_USER_ID

/** Default flag indicating if the account has been created */
const val ACCOUNT_HAS_BEEN_CREATED = false

/** Default test email for the active user entity */
const val EMAIL = "unverifiedemail@test.org"

/** Default test username for the active user entity */
const val USERNAME = "unverifiedUsername"

/** Default Firebase account info key for the active user entity */
const val FIREBASE_ACCOUNT_INFO_KEY = "-Ne4L6shsEBIH-zX9wld"
