package com.nicholas.rutherford.track.my.shot.firebase.util.authentication

import com.google.firebase.auth.FirebaseAuth
import com.nicholas.rutherford.track.my.shot.firebase.AuthenticateUserViaEmailFirebaseResponse
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class AuthenticationFirebaseImpl(private val firebaseAuth: FirebaseAuth) : AuthenticationFirebase {

    override fun attemptToSendEmailVerificationForCurrentUser(): Flow<AuthenticateUserViaEmailFirebaseResponse> {
        return callbackFlow {
            firebaseAuth.currentUser?.let { firebaseUser ->
                if (firebaseUser.isEmailVerified) {
                    trySend(element = AuthenticateUserViaEmailFirebaseResponse(isSuccessful = false, isAlreadyAuthenticated = true, isUserExist = true))
                } else {
                    firebaseUser.sendEmailVerification().addOnCompleteListener { task ->
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
