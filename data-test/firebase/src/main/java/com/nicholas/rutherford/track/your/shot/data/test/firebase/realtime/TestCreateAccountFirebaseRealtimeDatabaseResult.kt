package com.nicholas.rutherford.track.your.shot.data.test.firebase.realtime

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Utility class for creating test instances of [com.nicholas.rutherford.track.your.shot.firebase.realtime.CreateAccountFirebaseRealtimeDatabaseResult].
 * Provides predefined test data for the username and email.
 */
class TestCreateAccountFirebaseRealtimeDatabaseResult {

    /**
     * Creates a test [com.nicholas.rutherford.track.your.shot.firebase.realtime.CreateAccountFirebaseRealtimeDatabaseResult] instance with predefined values.
     *
     * @return a [com.nicholas.rutherford.track.your.shot.firebase.realtime.CreateAccountFirebaseRealtimeDatabaseResult] containing test username and email.
     */
    fun create(): com.nicholas.rutherford.track.your.shot.firebase.realtime.CreateAccountFirebaseRealtimeDatabaseResult {
        return _root_ide_package_.com.nicholas.rutherford.track.your.shot.firebase.realtime.CreateAccountFirebaseRealtimeDatabaseResult(
            username = USER_NAME,
            email = EMAIL
        )
    }
}

/** Predefined test username for [com.nicholas.rutherford.track.your.shot.firebase.realtime.CreateAccountFirebaseRealtimeDatabaseResult]. */
const val USER_NAME = "boomyNicholasR"

/** Predefined test email for [com.nicholas.rutherford.track.your.shot.firebase.realtime.CreateAccountFirebaseRealtimeDatabaseResult]. */
const val EMAIL = "testEmail@yahoo.com"
