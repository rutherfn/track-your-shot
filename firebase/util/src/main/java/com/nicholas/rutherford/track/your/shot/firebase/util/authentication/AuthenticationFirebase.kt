package com.nicholas.rutherford.track.your.shot.firebase.util.authentication

import com.nicholas.rutherford.track.your.shot.firebase.AuthenticateUserViaEmailFirebaseResponse
import kotlinx.coroutines.flow.Flow

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Defines the contract for Firebase authentication operations, including
 * user deletion, email verification, and password reset flows.
 */
interface AuthenticationFirebase {

    /**
     * Attempts to delete the currently authenticated user.
     *
     * @return A [Flow] emitting true if deletion succeeded or no user exists, false otherwise.
     */
    fun attemptToDeleteCurrentUserFlow(): Flow<Boolean>

    /**
     * Attempts to send an email verification to the currently authenticated user.
     *
     * @return A [Flow] emitting [AuthenticateUserViaEmailFirebaseResponse] indicating
     * the result of the attempt, whether the user is already verified, or if no user exists.
     */
    fun attemptToSendEmailVerificationForCurrentUser(): Flow<AuthenticateUserViaEmailFirebaseResponse>

    /**
     * Attempts to send a password reset email to the given [email].
     *
     * @param email The email address to send the password reset to.
     * @return A [Flow] emitting true if the email was successfully sent, false otherwise.
     */
    fun attemptToSendPasswordResetFlow(email: String): Flow<Boolean>
}
