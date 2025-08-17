package com.nicholas.rutherford.track.your.shot.firebase.util.existinguser

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Implementation of [ExistingUserFirebase] using Firebase Authentication.
 * Handles checking login state, logging out, and performing login with email/password.
 */
class ExistingUserFirebaseImpl(private val firebaseAuth: FirebaseAuth) : ExistingUserFirebase {

    /**
     * Checks if a user is currently logged in.
     *
     * @return True if there is a logged-in user, false otherwise.
     */
    override fun isLoggedIn() = firebaseAuth.currentUser != null

    /**
     * Logs out the currently authenticated user.
     */
    override fun logout() = firebaseAuth.signOut()

    /**
     * Performs login with the given email and password, returning a [Flow] that emits
     * the success state once the Firebase sign-in task completes.
     *
     * @param email The email address of the user.
     * @param password The password of the user.
     * @return A [Flow] emitting true if login succeeds, false otherwise.
     */
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
