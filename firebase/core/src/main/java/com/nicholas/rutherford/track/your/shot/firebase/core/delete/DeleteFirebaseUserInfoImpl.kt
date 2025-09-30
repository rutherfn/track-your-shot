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

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Implementation class responsible for deleting users, players, shots, declared shots, and reports
 * from Firebase Realtime Database. Also provides a flow to track whether a shot has been deleted.
 *
 * @property firebaseAuth Instance of [FirebaseAuth] to retrieve the current authenticated user.
 * @property firebaseDatabase Instance of [FirebaseDatabase] to access Firebase Realtime Database.
 */
class DeleteFirebaseUserInfoImpl(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseDatabase: FirebaseDatabase
) : DeleteFirebaseUserInfo {

    /** Internal mutable state flow tracking whether a shot has been deleted. */
    private val hasDeletedShotMutableStateFlow: MutableStateFlow<Boolean> = MutableStateFlow(value = false)

    /** Public flow exposing whether a shot has been deleted. */
    override val hasDeletedShotFlow: Flow<Boolean> = hasDeletedShotMutableStateFlow.asStateFlow()

    /**
     * Updates the state of [hasDeletedShotFlow].
     *
     * @param hasDeletedShot Boolean indicating whether a shot has been deleted.
     */
    override fun updateHasDeletedShotFlow(hasDeletedShot: Boolean) {
        hasDeletedShotMutableStateFlow.value = hasDeletedShot
    }

    /**
     * Deletes a player from Firebase Realtime Database by player key.
     *
     * @param playerKey The key of the player to delete.
     * @return [Flow] emitting true if deletion succeeded, false otherwise.
     */
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

    /**
     * Deletes a specific shot from a player in Firebase Realtime Database.
     * Updates [hasDeletedShotFlow] to reflect success or failure.
     *
     * @param playerKey The key of the player whose shot is being deleted.
     * @param index The index of the shot in the player's shot list.
     * @return [Flow] emitting true if deletion succeeded, false otherwise.
     */
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

    /**
     * Deletes a declared shot from Firebase Realtime Database.
     *
     * @param shotKey The key of the declared shot to delete.
     * @return [Flow] emitting true if deletion succeeded, false otherwise.
     */
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

    /**
     * Deletes an individual player report from Firebase Realtime Database.
     *
     * @param reportKey The key of the report to delete.
     * @return [Flow] emitting true if deletion succeeded, false otherwise.
     */
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

    /**
     * Deletes a user from Firebase Realtime Database by their unique user ID.
     * This method is useful for deleting user data after account deletion.
     *
     * @param uid The unique identifier of the user to delete from the database.
     * @return [Flow] emitting true if deletion succeeded, false otherwise.
     */
    override fun deleteUser(uid: String): Flow<Boolean> {
        return callbackFlow {
            val path = "${Constants.USERS}/$uid"

            firebaseDatabase.getReference(path)
                .removeValue()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        trySend(element = true)
                    } else {
                        Timber.w(message = "Error(deleteUser) -> Was not able to delete current user from given account.")
                        trySend(element = false)
                    }
                }
                .addOnFailureListener { exception ->
                    Timber.e(message = "Error(deleteUser) -> Was not able to delete current user from given account. With following stack trace $exception")
                    trySend(element = false)
                }
            awaitClose()
        }
    }
}
