package com.nicholas.rutherford.track.your.shot.firebase.util.existinguser

import kotlinx.coroutines.flow.Flow

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Defines the contract for handling existing user authentication using Firebase.
 */
interface ExistingUserFirebase {

    /**
     * Checks if a user is currently logged in.
     *
     * @return True if a user is logged in, false otherwise.
     */
    fun isLoggedIn(): Boolean

    /**
     * Logs out the currently authenticated user.
     */
    fun logout()

    /**
     * Performs login with the given email and password, returning a [Flow] that emits
     * the result of the login attempt.
     *
     * @param email The email address of the user.
     * @param password The password of the user.
     * @return A [Flow] emitting true if login succeeds, false otherwise.
     */
    fun loginFlow(email: String, password: String): Flow<Boolean>
}
