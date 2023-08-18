package com.nicholas.rutherford.track.my.shot.firebase.create

import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.util.Date

const val LAST_UPDATED = "lastUpdated"

class CreateLastUpdatedImpl(firebaseDatabase: FirebaseDatabase) : CreateLastUpdated {

    private val reference = firebaseDatabase.reference

    override fun attemptToCreateLastUpdatedFlow(date: Date): Flow<Boolean> {
        return callbackFlow {
            val values = hashMapOf<String, Long>()

            values[LAST_UPDATED] = date.time

            reference.push().setValue(values)
                .addOnCompleteListener { task ->
                    trySend(task.isSuccessful)
                }
            awaitClose()
        }
    }
}
