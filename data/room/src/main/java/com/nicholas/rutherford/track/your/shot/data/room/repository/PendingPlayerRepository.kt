package com.nicholas.rutherford.track.your.shot.data.room.repository

import com.nicholas.rutherford.track.your.shot.data.room.response.Player

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Repository interface defining CRUD operations for pending Player data.
 * Provides an abstraction layer over PendingPlayerDao, allowing
 * use of Player domain models rather than database entities.
 */
interface PendingPlayerRepository {

    /** Inserts a new pending [Player] into the database. */
    suspend fun createPendingPlayer(player: Player)

    /**
     * Updates an existing pending player in the database.
     *
     * @param currentPendingPlayer The player used to identify which record to update.
     * @param newPendingPlayer The new player data to replace the existing record.
     */
    suspend fun updatePendingPlayer(currentPendingPlayer: Player, newPendingPlayer: Player)

    /** Deletes all pending players from the database. */
    suspend fun deleteAllPendingPlayers()

    /**
     * Fetches a pending [Player] by its database ID.
     *
     * @param id The ID of the pending player.
     * @return The corresponding [Player], or null if not found.
     */
    suspend fun fetchPlayerById(id: Int): Player?

    /**
     * Fetches a pending player's database ID by first and last name.
     *
     * @param firstName The first name of the pending player.
     * @param lastName The last name of the pending player.
     * @return The ID of the pending player.
     */
    suspend fun fetchPendingPlayerIdByName(firstName: String, lastName: String): Int

    /** Fetches all pending players from the database. Returns an empty list if none exist. */
    suspend fun fetchAllPendingPlayers(): List<Player>
}
