package com.nicholas.rutherford.track.my.shot.firebase.create

import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.util.Date

const val LAST_UPDATED = "lastUpdated"
const val CONTENT_LAST_UPDATED_PATH = "contentLastUpdated"

class CreateFirebaseLastUpdatedImpl(firebaseDatabase: FirebaseDatabase) : CreateFirebaseLastUpdated {

    private val reference = firebaseDatabase.reference.child(CONTENT_LAST_UPDATED_PATH)

    override fun attemptToCreateLastUpdatedFlow(date: Date): Flow<Boolean> {
        return callbackFlow {
            val values = hashMapOf<String, Long>()

            values[LAST_UPDATED] = date.time

            reference.setValue(values)
                .addOnCompleteListener { task ->
                    trySend(task.isSuccessful)
                }
            awaitClose()
        }
    }
}
