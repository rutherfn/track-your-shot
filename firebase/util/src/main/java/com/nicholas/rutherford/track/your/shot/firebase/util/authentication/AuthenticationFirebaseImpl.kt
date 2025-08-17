package com.nicholas.rutherford.track.your.shot.firebase.util.authentication

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.nicholas.rutherford.track.your.shot.firebase.AuthenticateUserViaEmailFirebaseResponse
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Implementation of [AuthenticationFirebase] using Firebase Authentication.
 * Handles user deletion, email verification, and password reset flows.
 */
class AuthenticationFirebaseImpl(private val firebaseAuth: FirebaseAuth) : AuthenticationFirebase {

    /**
     * Attempts to delete the currently authenticated user.
     *
     * @return A [Flow] emitting true if deletion succeeded or no user exists, false otherwise.
     */
    override fun attemptToDeleteCurrentUserFlow(): Flow<Boolean> {
        return callbackFlow {
            firebaseAuth.currentUser?.let { currentUser ->
                currentUser.delete()
                    .addOnCompleteListener { trySend(element = it.isSuccessful) }
            } ?: run { trySend(element = true) }
            awaitClose()
        }
    }

    /**
     * Attempts to send an email verification to the currently authenticated user.
     *
     * @return A [Flow] emitting [AuthenticateUserViaEmailFirebaseResponse] indicating
     * the result of the attempt, whether the user is already verified, or if no user exists.
     */
    override fun attemptToSendEmailVerificationForCurrentUser(): Flow<AuthenticateUserViaEmailFirebaseResponse> {
        return callbackFlow {
            firebaseAuth.currentUser?.let { firebaseUser ->
                if (firebaseUser.isEmailVerified) {
                    trySend(
                        element = AuthenticateUserViaEmailFirebaseResponse(
                            isSuccessful = false,
                            isAlreadyAuthenticated = true,
                            isUserExist = true
                        )
                    )
                } else {
                    firebaseUser.sendEmailVerification().addOnCompleteListener { task ->
                        if (!task.isSuccessful) {
                            Log.e("EmailVerification", "Failed to send verification email", task.exception)
                        }
                        trySend(
                            element = AuthenticateUserViaEmailFirebaseResponse(
                                isSuccessful = task.isSuccessful,
                                isAlreadyAuthenticated = false,
                                isUserExist = true
                            )
                        )
                    }
                }
            } ?: run {
                trySend(
                    element = AuthenticateUserViaEmailFirebaseResponse(
                        isSuccessful = false,
                        isAlreadyAuthenticated = false,
                        isUserExist = false
                    )
                )
            }
            awaitClose()
        }
    }

    /**
     * Attempts to send a password reset email to the given [email].
     *
     * @param email The email address to send the password reset to.
     * @return A [Flow] emitting true if the email was successfully sent, false otherwise.
     */
    override fun attemptToSendPasswordResetFlow(email: String): Flow<Boolean> {
        return callbackFlow {
            firebaseAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    trySend(element = task.isSuccessful)
                }
            awaitClose()
        }
    }
}
