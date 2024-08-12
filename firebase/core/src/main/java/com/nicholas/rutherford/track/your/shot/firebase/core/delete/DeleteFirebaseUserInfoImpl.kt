package com.nicholas.rutherford.track.your.shot.firebase.core.delete

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.nicholas.rutherford.track.your.shot.helper.constants.Constants
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import timber.log.Timber

class DeleteFirebaseUserInfoImpl(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseDatabase: FirebaseDatabase
) : DeleteFirebaseUserInfo {

    override fun deletePlayer(playerKey: String): Flow<Boolean> {
        return callbackFlow {
            val uid = firebaseAuth.currentUser?.uid ?: ""
            val path = "${Constants.USERS}/$uid/${Constants.PLAYERS}/$playerKey"

            firebaseDatabase.getReference(path)
                .removeValue()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        trySend(element = true)
                    }
                }
                .addOnFailureListener { exception ->
                    Timber.e(message = "Error(deletePlayer) -> Was not able to delete current player from given account. With following stack trace $exception")
                    trySend(element = false)
                }
            awaitClose()
        }
    }
}
