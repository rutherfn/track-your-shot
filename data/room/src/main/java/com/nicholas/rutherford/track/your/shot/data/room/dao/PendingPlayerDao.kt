package com.nicholas.rutherford.track.your.shot.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.nicholas.rutherford.track.your.shot.data.room.entities.PendingPlayerEntity

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Data Access Object (DAO) for performing CRUD operations on the [PendingPlayerEntity] table.
 * Handles insertion, updates, deletions, and queries for pending players in the database.
 */
@Dao
interface PendingPlayerDao {

    /**
     * Inserts a single pending player entity into the database.
     *
     * @param pendingPlayerEntity The pending player to insert.
     */
    @Insert
    suspend fun insert(pendingPlayerEntity: PendingPlayerEntity)

    /**
     * Updates an existing pending player entity in the database.
     *
     * @param pendingPlayerEntity The pending player entity to update.
     */
    @Update
    suspend fun update(pendingPlayerEntity: PendingPlayerEntity)

    /**
     * Deletes all pending players from the database.
     */
    @Query("DELETE FROM pendingPlayers")
    suspend fun deleteAllPendingPlayers()

    /**
     * Retrieves a pending player by their ID.
     *
     * @param id The ID of the pending player.
     * @return The pending player entity or null if not found.
     */
    @Query("SELECT * FROM pendingPlayers WHERE id = :id")
    suspend fun getPlayerById(id: Int): PendingPlayerEntity?

    /**
     * Retrieves all pending players from the database.
     *
     * @return A list of all pending players or null if none exist.
     */
    @Query("SELECT * FROM pendingPlayers")
    suspend fun getAllPendingPlayers(): List<PendingPlayerEntity>?

    /**
     * Retrieves a pending player's ID by their first and last name.
     *
     * @param firstName The player's first name.
     * @param lastName The player's last name.
     * @return The pending player's ID.
     */
    @Query("SELECT id FROM pendingPlayers WHERE firstName = :firstName AND lastName = :lastName")
    suspend fun getPendingPlayerIdByName(firstName: String, lastName: String): Int
}
