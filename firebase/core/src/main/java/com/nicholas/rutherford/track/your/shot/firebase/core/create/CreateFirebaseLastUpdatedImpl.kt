package com.nicholas.rutherford.track.your.shot.firebase.core.create

import com.google.firebase.database.FirebaseDatabase
import com.nicholas.rutherford.track.your.shot.helper.constants.Constants
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.util.Date

class CreateFirebaseLastUpdatedImpl(firebaseDatabase: FirebaseDatabase) :
    CreateFirebaseLastUpdated {

    private val reference = firebaseDatabase.reference.child(Constants.CONTENT_LAST_UPDATED_PATH)

    override fun attemptToCreateLastUpdatedFlow(date: Date): Flow<Boolean> {
        return callbackFlow {
            val values = hashMapOf<String, Long>()

            values[Constants.LAST_UPDATED] = date.time

            reference.setValue(values)
                .addOnCompleteListener { task ->
                    trySend(task.isSuccessful)
                }
            awaitClose()
        }
    }
}
