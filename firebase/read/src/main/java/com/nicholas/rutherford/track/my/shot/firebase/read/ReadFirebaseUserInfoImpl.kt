package com.nicholas.rutherford.track.my.shot.firebase.read

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class ReadFirebaseUserInfoImpl(private val firebaseAuth: FirebaseAuth) : ReadFirebaseUserInfo {

    override fun isEmailVerified(): Flow<Boolean> {
        return callbackFlow {
            firebaseAuth.currentUser?.let { firebaseUser ->
                firebaseUser.reload().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        trySend(element = firebaseUser.isEmailVerified)
                    } else {
                        trySend(element = false)
                    }
                }
            } ?: run {
                trySend(element = false)
            }
            awaitClose()
        }
    }

    override fun isLoggedIn(): Flow<Boolean> {
        return callbackFlow {
            trySend(element = firebaseAuth.currentUser != null)
            awaitClose()
        }
    }
}
