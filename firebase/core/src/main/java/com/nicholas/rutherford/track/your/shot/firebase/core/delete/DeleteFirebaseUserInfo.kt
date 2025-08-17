package com.nicholas.rutherford.track.your.shot.firebase.core.delete

import kotlinx.coroutines.flow.Flow

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Interface defining the contract for deleting Firebase user data such as players, shots,
 * declared shots, and individual player reports. Also exposes a flow to track the deletion
 * state of a shot.
 */
interface DeleteFirebaseUserInfo {

    /**
     * A flow that emits the current state of whether a shot has been deleted.
     * This allows reactive updates to the UI or other components observing deletion events.
     */
    val hasDeletedShotFlow: Flow<Boolean>

    /**
     * Updates the state of [hasDeletedShotFlow].
     *
     * @param hasDeletedShot Boolean indicating whether a shot has been deleted.
     */
    fun updateHasDeletedShotFlow(hasDeletedShot: Boolean)

    /**
     * Deletes a player from Firebase Realtime Database by their unique player key.
     *
     * @param playerKey The unique key of the player to delete.
     * @return [Flow] emitting true if the deletion was successful, false otherwise.
     */
    fun deletePlayer(playerKey: String): Flow<Boolean>

    /**
     * Deletes a specific shot for a given player in Firebase Realtime Database.
     * Updates [hasDeletedShotFlow] to reflect the deletion state.
     *
     * @param playerKey The key of the player whose shot is being deleted.
     * @param index The index of the shot to delete.
     * @return [Flow] emitting true if the deletion was successful, false otherwise.
     */
    fun deleteShot(playerKey: String, index: Int): Flow<Boolean>

    /**
     * Deletes a declared shot from Firebase Realtime Database by its unique shot key.
     *
     * @param shotKey The unique key of the declared shot to delete.
     * @return [Flow] emitting true if the deletion was successful, false otherwise.
     */
    fun deleteDeclaredShot(shotKey: String): Flow<Boolean>

    /**
     * Deletes an individual player report from Firebase Realtime Database by its report key.
     *
     * @param reportKey The unique key of the report to delete.
     * @return [Flow] emitting true if the deletion was successful, false otherwise.
     */
    fun deleteReport(reportKey: String): Flow<Boolean>
}
