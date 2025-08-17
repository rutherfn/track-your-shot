package com.nicholas.rutherford.track.your.shot.data.room.repository

import com.nicholas.rutherford.track.your.shot.data.room.dao.PlayerDao
import com.nicholas.rutherford.track.your.shot.data.room.entities.toPlayer
import com.nicholas.rutherford.track.your.shot.data.room.response.Player
import com.nicholas.rutherford.track.your.shot.data.room.response.toPlayerEntity

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Repository implementation for managing Player data.
 * Provides CRUD operations and abstracts the underlying PlayerDao.
 */
class PlayerRepositoryImpl(private val playerDao: PlayerDao) : PlayerRepository {

    /** Inserts a single [Player] into the database. */
    override suspend fun createPlayer(player: Player) =
        playerDao.insert(playerEntity = player.toPlayerEntity())

    /** Inserts a list of [Player] objects into the database. */
    override suspend fun createListOfPlayers(playerList: List<Player>) =
        playerDao.insertAll(players = playerList.map { it.toPlayerEntity() })

    /**
     * Updates an existing player in the database.
     *
     * @param currentPlayer The player to identify which record to update.
     * @param newPlayer The new player data to replace the existing record.
     */
    override suspend fun updatePlayer(currentPlayer: Player, newPlayer: Player) {
        val playerId = playerDao.getPlayerIdByName(firstName = currentPlayer.firstName, lastName = currentPlayer.lastName)
        playerDao.update(playerEntity = newPlayer.toPlayerEntity().copy(id = playerId))
    }

    /**
     * Deletes a player from the database by first and last name.
     *
     * @param firstName The first name of the player to delete.
     * @param lastName The last name of the player to delete.
     */
    override suspend fun deletePlayerByName(firstName: String, lastName: String) =
        playerDao.deletePlayerByName(firstName = firstName, lastName = lastName)

    /** Deletes all players from the database. */
    override suspend fun deleteAllPlayers() = playerDao.deleteAllPlayers()

    /**
     * Fetches a player's database ID by first and last name.
     *
     * @param firstName The first name of the player.
     * @param lastName The last name of the player.
     * @return The ID of the player.
     */
    override suspend fun fetchPlayerIdByName(firstName: String, lastName: String): Int =
        playerDao.getPlayerIdByName(firstName = firstName, lastName = lastName)

    /**
     * Fetches a [Player] by its database ID.
     *
     * @param id The ID of the player.
     * @return The corresponding [Player], or null if not found.
     */
    override suspend fun fetchPlayerById(id: Int): Player? =
        playerDao.getPlayerById(id = id).toPlayer()

    /**
     * Fetches a [Player] by first and last name.
     *
     * @param firstName The first name of the player.
     * @param lastName The last name of the player.
     * @return The matching [Player] or null if names are empty or player not found.
     */
    override suspend fun fetchPlayerByName(firstName: String, lastName: String): Player? {
        return if (firstName.isNotEmpty() && lastName.isNotEmpty()) {
            playerDao.getPlayersByName(firstName = firstName, lastName = lastName)?.toPlayer()
        } else {
            null
        }
    }

    /** Fetches all players from the database. Returns an empty list if none exist. */
    override suspend fun fetchAllPlayers(): List<Player> =
        playerDao.getAllPlayers()?.map { it.toPlayer() } ?: emptyList()

    /** Returns the total count of players in the database. */
    override suspend fun fetchPlayerCount(): Int = playerDao.getPlayerCount()
}
