package com.nicholas.rutherford.track.your.shot.feature.players.shots.logshot.pendingshot

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Implementation of [CurrentPendingShot] that manages the current list of pending shots.
 * Provides functionality to fetch, create, delete, and clear pending shots,
 * while exposing a [Flow] to observe changes to the shot list.
 */
class CurrentPendingShotImpl : CurrentPendingShot {

    /** Internal mutable state flow holding the current list of pending shots */
    internal val shotsMutableStateFlow: MutableStateFlow<List<PendingShot>> = MutableStateFlow(value = emptyList())

    /** Public read-only [Flow] of the current pending shots */
    override val shotsStateFlow: Flow<List<PendingShot>> = shotsMutableStateFlow.asStateFlow()

    /**
     * Returns the current list of pending shots.
     *
     * @return list of [PendingShot].
     */
    override fun fetchPendingShots(): List<PendingShot> = shotsMutableStateFlow.value

    /**
     * Adds a new pending shot to the list.
     *
     * @param shotLogged the [PendingShot] to add.
     */
    override fun createShot(shotLogged: PendingShot) {
        val currentShotList = shotsMutableStateFlow.value
        shotsMutableStateFlow.value = currentShotList + listOf(shotLogged)
    }

    /**
     * Deletes a specific pending shot from the list.
     *
     * @param shotLogged the [PendingShot] to remove.
     */
    override fun deleteShot(shotLogged: PendingShot) {
        val filterShotArrayList: ArrayList<PendingShot> = arrayListOf()

        shotsMutableStateFlow.value.forEach { shot ->
            if (shot != shotLogged) {
                filterShotArrayList.add(shot)
            }
        }

        shotsMutableStateFlow.value = filterShotArrayList.toList()
    }

    /**
     * Clears all pending shots from the list.
     */
    override fun clearShotList() {
        shotsMutableStateFlow.value = emptyList()
    }
}
