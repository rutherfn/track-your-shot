package com.nicholas.rutherford.track.your.shot.firebase.util.existinguser

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class ExistingUserFirebaseImpl(private val firebaseAuth: FirebaseAuth) : ExistingUserFirebase {

    override fun isLoggedIn() = firebaseAuth.currentUser != null

    override fun logout() = firebaseAuth.signOut()

    override fun loginFlow(email: String, password: String): Flow<Boolean> {
        return callbackFlow {
            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    trySend(task.isSuccessful)
                }
            awaitClose()
        }
    }
}
