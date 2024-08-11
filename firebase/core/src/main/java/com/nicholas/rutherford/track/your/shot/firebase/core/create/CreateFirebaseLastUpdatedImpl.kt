package com.nicholas.rutherford.track.your.shot.firebase.core.create

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.nicholas.rutherford.track.your.shot.helper.constants.Constants
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import timber.log.Timber
import java.util.Date

class CreateFirebaseLastUpdatedImpl(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseDatabase: FirebaseDatabase
) : CreateFirebaseLastUpdated {

    override fun attemptToCreateLastUpdatedFlow(date: Date): Flow<Boolean> {
        return callbackFlow {
            val uid = firebaseAuth.currentUser?.uid ?: ""
            val values = hashMapOf<String, Long>()

            values[Constants.LAST_UPDATED] = date.time

            firebaseDatabase.getReference("${Constants.CONTENT_LAST_UPDATED_PATH}/$uid").setValue(values)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        trySend(element = true)
                    }
                }
                .addOnFailureListener { exception ->
                    Timber.e(message = "Error(attemptToCreateLastUpdatedFlow) -> Creating last updated time failed to create in Firebase Realtime Database following stack trace ${exception.stackTrace}")
                    trySend(element = false)
                }
            awaitClose()
        }
    }
}
