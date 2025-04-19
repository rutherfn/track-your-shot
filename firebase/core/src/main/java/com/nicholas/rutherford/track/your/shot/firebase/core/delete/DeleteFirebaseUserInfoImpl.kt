package com.nicholas.rutherford.track.your.shot.firebase.core.delete

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.nicholas.rutherford.track.your.shot.helper.constants.Constants
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.callbackFlow
import timber.log.Timber

class DeleteFirebaseUserInfoImpl(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseDatabase: FirebaseDatabase
) : DeleteFirebaseUserInfo {

    private val hasDeletedShotMutableStateFlow: MutableStateFlow<Boolean> = MutableStateFlow(value = false)
    override val hasDeletedShotFlow: Flow<Boolean> = hasDeletedShotMutableStateFlow.asStateFlow()

    override fun updateHasDeletedShotFlow(hasDeletedShot: Boolean) {
        hasDeletedShotMutableStateFlow.value = hasDeletedShot
    }

    override fun deletePlayer(playerKey: String): Flow<Boolean> {
        return callbackFlow {
            val uid = firebaseAuth.currentUser?.uid ?: ""
            val path = "${Constants.USERS}/$uid/${Constants.PLAYERS}/$playerKey"

            firebaseDatabase.getReference(path)
                .removeValue()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        trySend(element = true)
                    } else {
                        Timber.w(message = "Error(deletePlayer) -> Was not able to delete current player from given account.")
                        trySend(element = false)
                    }
                }
                .addOnFailureListener { exception ->
                    Timber.e(message = "Error(deletePlayer) -> Was not able to delete current player from given account. With following stack trace $exception")
                    trySend(element = false)
                }
            awaitClose()
        }
    }

    override fun deleteShot(playerKey: String, index: Int): Flow<Boolean> {
        return callbackFlow {
            val uid = firebaseAuth.currentUser?.uid ?: ""
            val path = "${Constants.USERS}/$uid/${Constants.PLAYERS}/$playerKey/${Constants.SHOTS_LOGGED}/$index"

            firebaseDatabase.getReference(path)
                .removeValue()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        hasDeletedShotMutableStateFlow.value = true
                        trySend(element = true)
                    } else {
                        hasDeletedShotMutableStateFlow.value = false
                        Timber.w(message = "Error(deletePlayer) -> Was not able to delete current player shot from given account.")
                        trySend(element = false)
                    }
                }
                .addOnFailureListener { exception ->
                    hasDeletedShotMutableStateFlow.value = false
                    Timber.e(message = "Error(deletePlayer) -> Was not able to delete current player shot from given account. With following stack trace $exception")
                    trySend(element = false)
                }
            awaitClose()
        }
    }

    override fun deleteDeclaredShot(shotKey: String): Flow<Boolean> {
        return callbackFlow {
            val uid = firebaseAuth.currentUser?.uid ?: ""
            val path = "${Constants.USERS}/$uid/${Constants.CREATED_SHOTS}/$shotKey"

            firebaseDatabase.getReference(path)
                .removeValue()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        trySend(element = true)
                    } else {
                        Timber.w(message = "Error(deleteShot) -> Was not able to delete current shot from given account.")
                        trySend(element = false)
                    }
                }
                .addOnFailureListener { exception ->
                    Timber.e(message = "Error(deleteShot) -> Was not able to delete current shot from given account. With following stack trace $exception")
                    trySend(element = false)
                }
            awaitClose()
        }
    }

    override fun deleteReport(reportKey: String): Flow<Boolean> {
        return callbackFlow {
            val uid = firebaseAuth.currentUser?.uid ?: ""
            val path = "${Constants.USERS}/$uid/${Constants.PLAYERS_INDIVIDUAL_REPORTS}/$reportKey"

            firebaseDatabase.getReference(path)
                .removeValue()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        trySend(element = true)
                    } else {
                        Timber.w(message = "Error(deleteReport) -> Was not able to delete current report from given account.")
                        trySend(element = false)
                    }
                }
                .addOnFailureListener { exception ->
                    Timber.e(message = "Error(deleteReport) -> Was not able to delete current report from given account. With following stack trace $exception")
                    trySend(element = false)
                }
            awaitClose()
        }
    }
}
