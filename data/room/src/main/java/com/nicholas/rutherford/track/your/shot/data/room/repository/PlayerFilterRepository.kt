package com.nicholas.rutherford.track.your.shot.data.room.repository

import com.nicholas.rutherford.track.your.shot.data.room.response.PlayerFilter

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Repository interface defining operations for Player Filter data.
 * Provides an abstraction layer over the underlying PlayerFilterDao,
 * allowing use of PlayerFilter domain models rather than database entities.
 */
interface PlayerFilterRepository {

    /**
     * Fetches the active player filter.
     *
     * @return The active [PlayerFilter], or null if no filter exists.
     */
    suspend fun fetchActiveFilter(): PlayerFilter?

    /**
     * Saves the active player filter.
     *
     * @param filter The [PlayerFilter] to save.
     */
    suspend fun saveActiveFilter(filter: PlayerFilter)

    /**
     * Clears the active player filter and all associated positions.
     */
    suspend fun clearActiveFilter()

    /**
     * Checks if an active filter exists.
     *
     * @return true if an active filter exists, false otherwise.
     */
    suspend fun hasActiveFilter(): Boolean

    /**
     * Gets the count of active filters.
     * Count includes: each selected position, hasShotsLogged (if set), minShots (if set), maxShots (if set).
     *
     * @return The total count of active filters, or 0 if no filter exists.
     */
    suspend fun getActiveFilterCount(): Int
}
