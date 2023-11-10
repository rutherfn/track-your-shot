package com.nicholas.rutherford.track.your.shot.firebase.core.delete

import com.google.firebase.database.FirebaseDatabase
import com.nicholas.rutherford.track.your.shot.helper.constants.Constants
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import timber.log.Timber

class DeleteFirebaseUserInfoImpl(private val firebaseDatabase: FirebaseDatabase) : DeleteFirebaseUserInfo {

    override fun deletePlayer(
        accountKey: String,
        playerKey: String
    ): Flow<Boolean> {
        return callbackFlow {
            firebaseDatabase.getReference(Constants.USERS)
                .child(Constants.ACCOUNT_INFO)
                .child(accountKey)
                .child(Constants.PLAYERS)
                .child(playerKey)
                .removeValue()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        trySend(element = true)
                    } else {
                        Timber.w(message = "Warning(deletePlayer) -> Was not able to delete current player from given account.")
                        trySend(element = false)
                    }
                }
            awaitClose()
        }
    }
}
