package com.nicholas.rutherford.track.your.shot.firebase.realtime

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Data class representing the result of creating a new account
 * in Firebase Realtime Database. This is typically returned
 * after a successful write operation to the database.
 *
 * @property username The username of the newly created account.
 * @property email The email address of the newly created account.
 */
data class CreateAccountFirebaseRealtimeDatabaseResult(
    val username: String,
    val email: String
)
