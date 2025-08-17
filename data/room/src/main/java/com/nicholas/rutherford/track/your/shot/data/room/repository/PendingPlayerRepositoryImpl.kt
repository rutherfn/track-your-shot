package com.nicholas.rutherford.track.your.shot.data.room.repository

import com.nicholas.rutherford.track.your.shot.data.room.dao.PendingPlayerDao
import com.nicholas.rutherford.track.your.shot.data.room.entities.toPlayer
import com.nicholas.rutherford.track.your.shot.data.room.response.Player
import com.nicholas.rutherford.track.your.shot.data.room.response.toPendingPlayerEntity

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Repository implementation for managing pending Player data.
 * Provides an abstraction layer over PendingPlayerDao, allowing
 * CRUD operations using Player domain models rather than database entities.
 */
class PendingPlayerRepositoryImpl(private val pendingPlayerDao: PendingPlayerDao) : PendingPlayerRepository {

    /** Inserts a new pending [Player] into the database. */
    override suspend fun createPendingPlayer(player: Player) =
        pendingPlayerDao.insert(pendingPlayerEntity = player.toPendingPlayerEntity())

    /**
     * Updates an existing pending player in the database.
     *
     * @param currentPendingPlayer The player used to identify which record to update.
     * @param newPendingPlayer The new player data to replace the existing record.
     */
    override suspend fun updatePendingPlayer(currentPendingPlayer: Player, newPendingPlayer: Player) {
        val playerId = pendingPlayerDao.getPendingPlayerIdByName(
            firstName = currentPendingPlayer.firstName,
            lastName = currentPendingPlayer.lastName
        )

        pendingPlayerDao.update(
            pendingPlayerEntity = newPendingPlayer.toPendingPlayerEntity().copy(id = playerId)
        )
    }

    /** Deletes all pending players from the database. */
    override suspend fun deleteAllPendingPlayers() = pendingPlayerDao.deleteAllPendingPlayers()

    /**
     * Fetches a pending [Player] by its database ID.
     *
     * @param id The ID of the pending player.
     * @return The corresponding [Player], or null if not found.
     */
    override suspend fun fetchPlayerById(id: Int): Player? =
        pendingPlayerDao.getPlayerById(id = id)?.toPlayer()

    /**
     * Fetches a pending player's database ID by first and last name.
     *
     * @param firstName The first name of the pending player.
     * @param lastName The last name of the pending player.
     * @return The ID of the pending player.
     */
    override suspend fun fetchPendingPlayerIdByName(firstName: String, lastName: String): Int =
        pendingPlayerDao.getPendingPlayerIdByName(firstName = firstName, lastName = lastName)

    /** Fetches all pending players from the database. Returns an empty list if none exist. */
    override suspend fun fetchAllPendingPlayers(): List<Player> =
        pendingPlayerDao.getAllPendingPlayers()?.map { pendingPlayer -> pendingPlayer.toPlayer() } ?: emptyList()
}
