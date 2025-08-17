package com.nicholas.rutherford.track.your.shot.firebase.realtime

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Utility class for creating test instances of [CreateAccountFirebaseRealtimeDatabaseResult].
 * Provides predefined test data for the username and email.
 */
class TestCreateAccountFirebaseRealtimeDatabaseResult {

    /**
     * Creates a test [CreateAccountFirebaseRealtimeDatabaseResult] instance with predefined values.
     *
     * @return a [CreateAccountFirebaseRealtimeDatabaseResult] containing test username and email.
     */
    fun create(): CreateAccountFirebaseRealtimeDatabaseResult {
        return CreateAccountFirebaseRealtimeDatabaseResult(
            username = USER_NAME,
            email = EMAIL
        )
    }
}

/** Predefined test username for [CreateAccountFirebaseRealtimeDatabaseResult]. */
const val USER_NAME = "boomyNicholasR"

/** Predefined test email for [CreateAccountFirebaseRealtimeDatabaseResult]. */
const val EMAIL = "testEmail@yahoo.com"
