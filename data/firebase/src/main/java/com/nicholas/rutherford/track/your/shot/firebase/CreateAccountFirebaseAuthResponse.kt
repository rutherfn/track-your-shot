package com.nicholas.rutherford.track.your.shot.firebase

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Represents the response received after attempting to create a new account via Firebase Authentication.
 *
 * @property isSuccessful Indicates whether the account creation was successful.
 * @property username The username of the newly created account, if available.
 * @property isNewUser Indicates whether the account belongs to a new user.
 * @property exception The exception encountered during account creation, if any.
 */
data class CreateAccountFirebaseAuthResponse(
    val isSuccessful: Boolean = false,
    val username: String? = null,
    val isNewUser: Boolean? = null,
    val exception: Exception? = null
)
