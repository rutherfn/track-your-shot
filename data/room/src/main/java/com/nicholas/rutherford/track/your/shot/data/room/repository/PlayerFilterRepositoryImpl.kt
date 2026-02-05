package com.nicholas.rutherford.track.your.shot.data.room.repository

import com.nicholas.rutherford.track.your.shot.data.room.dao.PlayerFilterDao
import com.nicholas.rutherford.track.your.shot.data.room.response.PlayerFilter
import com.nicholas.rutherford.track.your.shot.data.room.response.getFilterCount
import com.nicholas.rutherford.track.your.shot.data.room.response.toPlayerFilter
import com.nicholas.rutherford.track.your.shot.data.room.response.toPlayerFilterEntities

const val DEFAULT_FILTER_COUNT = 0

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Repository implementation for managing Player Filter data.
 * Provides CRUD operations and abstracts the underlying PlayerFilterDao.
 */
class PlayerFilterRepositoryImpl(private val playerFilterDao: PlayerFilterDao) : PlayerFilterRepository {

    /**
     * Fetches the active player filter.
     *
     * @return The active [PlayerFilter], or null if no filter exists.
     */
    override suspend fun fetchActiveFilter(): PlayerFilter? {
        val filterEntity = playerFilterDao.getActiveFilter() ?: return null
        val positions = playerFilterDao.getActiveFilterPositions()
        return filterEntity.toPlayerFilter(positions)
    }

    /**
     * Saves the active player filter.
     *
     * @param filter The [PlayerFilter] to save.
     */
    override suspend fun saveActiveFilter(filter: PlayerFilter) {
        val (filterEntity, positionEntities) = filter.toPlayerFilterEntities()
        playerFilterDao.saveActiveFilter(filterEntity)
        playerFilterDao.clearFilterPositions()
        if (positionEntities.isNotEmpty()) {
            playerFilterDao.saveFilterPositions(positionEntities)
        }
    }

    /**
     * Clears the active player filter and all associated positions.
     */
    override suspend fun clearActiveFilter() = playerFilterDao.clearActiveFilter()

    /**
     * Checks if an active filter exists.
     *
     * @return true if an active filter exists, false otherwise.
     */
    override suspend fun hasActiveFilter(): Boolean = playerFilterDao.hasActiveFilter()

    /**
     * Gets the count of active filters.
     * Count includes: each selected position, hasShotsLogged (if set), minShots (if set), maxShots (if set).
     *
     * @return The total count of active filters, or 0 if no filter exists.
     */
    override suspend fun getActiveFilterCount(): Int = fetchActiveFilter()?.getFilterCount() ?: DEFAULT_FILTER_COUNT
}
