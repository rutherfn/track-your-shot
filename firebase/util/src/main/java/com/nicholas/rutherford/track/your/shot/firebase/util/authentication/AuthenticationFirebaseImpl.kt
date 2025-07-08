package com.nicholas.rutherford.track.your.shot.firebase.util.authentication

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.nicholas.rutherford.track.your.shot.firebase.AuthenticateUserViaEmailFirebaseResponse
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class AuthenticationFirebaseImpl(private val firebaseAuth: FirebaseAuth) : AuthenticationFirebase {

    override fun attemptToDeleteCurrentUserFlow(): Flow<Boolean> {
        return callbackFlow {
            firebaseAuth.currentUser?.let { currentUser ->
                currentUser.delete()
                    .addOnCompleteListener { trySend(element = it.isSuccessful) }
            } ?: run { trySend(element = true) }
            awaitClose()
        }
    }

    override fun attemptToSendEmailVerificationForCurrentUser(): Flow<AuthenticateUserViaEmailFirebaseResponse> {
        return callbackFlow {
            firebaseAuth.currentUser?.let { firebaseUser ->
                if (firebaseUser.isEmailVerified) {
                    trySend(element = AuthenticateUserViaEmailFirebaseResponse(isSuccessful = false, isAlreadyAuthenticated = true, isUserExist = true))
                } else {
                    println("get here firebase user ${firebaseUser.email}")
                    firebaseUser.sendEmailVerification().addOnCompleteListener { task ->
                        if (!task.isSuccessful) {
                            Log.e("EmailVerification", "Failed to send verification email", task.exception)
                        }
                        trySend(element = AuthenticateUserViaEmailFirebaseResponse(isSuccessful = task.isSuccessful, isAlreadyAuthenticated = false, isUserExist = true))
                    }
                }
            } ?: run {
                trySend(element = AuthenticateUserViaEmailFirebaseResponse(isSuccessful = false, isAlreadyAuthenticated = false, isUserExist = false))
            }
            awaitClose()
        }
    }

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
