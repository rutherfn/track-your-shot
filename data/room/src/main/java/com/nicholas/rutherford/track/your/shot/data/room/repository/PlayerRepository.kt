package com.nicholas.rutherford.track.your.shot.data.room.repository

import com.nicholas.rutherford.track.your.shot.data.room.response.Player

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Repository interface defining CRUD operations for Player data.
 * Provides an abstraction layer over the underlying PlayerDao,
 * allowing use of Player domain models rather than database entities.
 */
interface PlayerRepository {

    /** Inserts a single [Player] into the database. */
    suspend fun createPlayer(player: Player)

    /** Inserts a list of [Player] objects into the database. */
    suspend fun createListOfPlayers(playerList: List<Player>)

    /**
     * Updates an existing player in the database.
     *
     * @param currentPlayer The player to identify which record to update.
     * @param newPlayer The new player data to replace the existing record.
     */
    suspend fun updatePlayer(currentPlayer: Player, newPlayer: Player)

    /**
     * Deletes a player from the database by first and last name.
     *
     * @param firstName The first name of the player to delete.
     * @param lastName The last name of the player to delete.
     */
    suspend fun deletePlayerByName(firstName: String, lastName: String)

    /** Deletes all players from the database. */
    suspend fun deleteAllPlayers()

    /**
     * Fetches a player's database ID by first and last name.
     *
     * @param firstName The first name of the player.
     * @param lastName The last name of the player.
     * @return The ID of the player, or null if not found.
     */
    suspend fun fetchPlayerIdByName(firstName: String, lastName: String): Int?

    /**
     * Fetches a [Player] by first and last name.
     *
     * @param firstName The first name of the player.
     * @param lastName The last name of the player.
     * @return The matching [Player] or null if not found.
     */
    suspend fun fetchPlayerByName(firstName: String, lastName: String): Player?

    /**
     * Fetches a [Player] by its database ID.
     *
     * @param id The ID of the player.
     * @return The corresponding [Player], or null if not found.
     */
    suspend fun fetchPlayerById(id: Int): Player?

    /** Fetches all players from the database. Returns an empty list if none exist. */
    suspend fun fetchAllPlayers(): List<Player>

    /** Returns the total count of players in the database. */
    suspend fun fetchPlayerCount(): Int
}
