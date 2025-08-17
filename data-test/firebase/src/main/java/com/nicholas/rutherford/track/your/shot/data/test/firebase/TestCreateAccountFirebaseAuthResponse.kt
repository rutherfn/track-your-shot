package com.nicholas.rutherford.track.your.shot.data.test.firebase

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Utility class for creating test instances of [com.nicholas.rutherford.track.your.shot.firebase.CreateAccountFirebaseAuthResponse].
 * Provides predefined test values for account creation results.
 */
class TestCreateAccountFirebaseAuthResponse {

    /**
     * Creates a test [com.nicholas.rutherford.track.your.shot.firebase.CreateAccountFirebaseAuthResponse] instance with predefined values.
     *
     * @return a [com.nicholas.rutherford.track.your.shot.firebase.CreateAccountFirebaseAuthResponse] populated with test account creation data.
     */
    fun create(): com.nicholas.rutherford.track.your.shot.firebase.CreateAccountFirebaseAuthResponse {
        return _root_ide_package_.com.nicholas.rutherford.track.your.shot.firebase.CreateAccountFirebaseAuthResponse(
            isSuccessful = IS_SUCCESSFUL,
            username = USERNAME,
            isNewUser = IS_NEW_USER,
            exception = EXCEPTION
        )
    }
}

/** Predefined test value indicating whether the account creation was successful. */
const val IS_SUCCESSFUL = true

/** Predefined test username for the created account. */
const val USERNAME = "boomyNicholasR"

/** Predefined test value indicating if the created account is a new user. */
const val IS_NEW_USER = true

/** Predefined test exception for account creation errors. */
val EXCEPTION = Exception("Something went wrong")
