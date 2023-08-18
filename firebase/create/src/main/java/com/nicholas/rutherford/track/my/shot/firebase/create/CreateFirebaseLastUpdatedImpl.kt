package com.nicholas.rutherford.track.my.shot.firebase.create

import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.Date

const val LAST_UPDATED = "lastUpdated"

class CreateFirebaseLastUpdatedImpl(
    private val createFirebaseLastUpdated: CreateFirebaseLastUpdated,
    firebaseDatabase: FirebaseDatabase
) : CreateFirebaseLastUpdated {

    private val reference = firebaseDatabase.reference

    override fun attemptToCreateLastUpdatedFlow(date: Date): Flow<Boolean> {
        return callbackFlow {
            val values = hashMapOf<String, Long>()

            values[LAST_UPDATED] = date.time

            reference.push().setValue(values)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val currentDate = Date()
                        launch { createFirebaseLastUpdated.attemptToCreateLastUpdatedFlow(date = currentDate).collect() }
                    }
                    trySend(task.isSuccessful)
                }
            awaitClose()
        }
    }
}
