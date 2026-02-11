package com.nicholas.rutherford.track.your.shot.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nicholas.rutherford.track.your.shot.data.room.entities.PlayerFilterEntity
import com.nicholas.rutherford.track.your.shot.data.room.entities.PlayerFilterPositionEntity

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Data Access Object (DAO) for performing CRUD operations on player filter tables.
 * Handles operations for the active player filter and its associated positions.
 */
@Dao
interface PlayerFilterDao {

    /**
     * Retrieves the active player filter.
     *
     * @return The active filter entity, or null if no filter exists.
     */
    @Query("SELECT * FROM playerFilters WHERE id = 1")
    suspend fun getActiveFilter(): PlayerFilterEntity?

    /**
     * Saves or updates the active player filter.
     * Uses REPLACE strategy to update if filter already exists.
     *
     * @param filter The filter entity to save.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveActiveFilter(filter: PlayerFilterEntity)

    /**
     * Deletes the active player filter.
     * This will also cascade delete associated positions due to foreign key constraint.
     */
    @Query("DELETE FROM playerFilters WHERE id = 1")
    suspend fun clearActiveFilter()

    /**
     * Retrieves all positions associated with the active filter.
     *
     * @return List of position strings for the active filter, sorted alphabetically.
     */
    @Query("SELECT position FROM playerFilterPositions WHERE filterId = 1 ORDER BY position ASC")
    suspend fun getActiveFilterPositions(): List<String>

    /**
     * Saves filter positions for the active filter.
     * Replaces any existing positions.
     *
     * @param positions List of position entities to save.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveFilterPositions(positions: List<PlayerFilterPositionEntity>)

    /**
     * Deletes all positions for the active filter.
     */
    @Query("DELETE FROM playerFilterPositions WHERE filterId = 1")
    suspend fun clearFilterPositions()

    /**
     * Checks if an active filter exists.
     *
     * @return true if an active filter exists, false otherwise.
     */
    @Query("SELECT EXISTS(SELECT 1 FROM playerFilters WHERE id = 1)")
    suspend fun hasActiveFilter(): Boolean
}
