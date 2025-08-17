package com.nicholas.rutherford.track.your.shot.feature.players.shots.logshot.pendingshot

import kotlinx.coroutines.flow.Flow

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Interface defining the contract for managing the current list of pending shots.
 * Provides methods to fetch, create, delete, and clear pending shots,
 * and exposes a [Flow] to observe changes to the shot list.
 */
interface CurrentPendingShot {

    /** Public read-only [Flow] of the current pending shots */
    val shotsStateFlow: Flow<List<PendingShot>>

    /**
     * Returns the current list of pending shots.
     *
     * @return list of [PendingShot].
     */
    fun fetchPendingShots(): List<PendingShot>

    /**
     * Adds a new pending shot to the list.
     *
     * @param shotLogged the [PendingShot] to add.
     */
    fun createShot(shotLogged: PendingShot)

    /**
     * Deletes a specific pending shot from the list.
     *
     * @param shotLogged the [PendingShot] to remove.
     */
    fun deleteShot(shotLogged: PendingShot)

    /**
     * Clears all pending shots from the list.
     */
    fun clearShotList()
}
