package com.nicholas.rutherford.track.your.shot.firebase

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Represents the response received after attempting to authenticate a user via email using Firebase.
 *
 * @property isSuccessful Indicates whether the authentication attempt was successful.
 * @property isAlreadyAuthenticated Indicates whether the user was already authenticated before this attempt.
 * @property isUserExist Indicates whether the user exists in the Firebase system.
 */
data class AuthenticateUserViaEmailFirebaseResponse(
    val isSuccessful: Boolean,
    val isAlreadyAuthenticated: Boolean,
    val isUserExist: Boolean
)
