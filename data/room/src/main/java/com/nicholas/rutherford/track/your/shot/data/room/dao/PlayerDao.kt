package com.nicholas.rutherford.track.your.shot.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.nicholas.rutherford.track.your.shot.data.room.entities.PlayerEntity

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Data Access Object (DAO) for performing CRUD operations on the [PlayerEntity] table.
 * Handles insertion, updates, deletions, and queries for players in the database.
 */
@Dao
interface PlayerDao {

    /**
     * Inserts a single player entity into the database.
     *
     * @param playerEntity The player to insert.
     */
    @Insert
    suspend fun insert(playerEntity: PlayerEntity)

    /**
     * Inserts a list of player entities into the database.
     *
     * @param players The list of players to insert.
     */
    @Insert
    suspend fun insertAll(players: List<PlayerEntity>)

    /**
     * Updates an existing player entity in the database.
     *
     * @param playerEntity The player entity to update.
     */
    @Update
    suspend fun update(playerEntity: PlayerEntity)

    /**
     * Deletes a player by matching first and last name.
     *
     * @param firstName The first name of the player.
     * @param lastName The last name of the player.
     */
    @Query("DELETE FROM players WHERE firstName = :firstName AND lastName = :lastName")
    suspend fun deletePlayerByName(firstName: String, lastName: String)

    /**
     * Deletes all players from the database.
     */
    @Query("DELETE FROM players")
    suspend fun deleteAllPlayers()

    /**
     * Retrieves a player by their ID.
     *
     * @param id The ID of the player.
     * @return The player entity with the specified ID.
     */
    @Query("SELECT * FROM PLAYERS WHERE id = :id")
    suspend fun getPlayerById(id: Int): PlayerEntity

    /**
     * Retrieves a player's ID by their first and last name.
     *
     * @param firstName The player's first name.
     * @param lastName The player's last name.
     * @return The player's ID.
     */
    @Query("SELECT id FROM players WHERE firstName = :firstName AND lastName = :lastName")
    suspend fun getPlayerIdByName(firstName: String, lastName: String): Int

    /**
     * Retrieves a player by their first and last name.
     *
     * @param firstName The player's first name.
     * @param lastName The player's last name.
     * @return The player entity or null if not found.
     */
    @Query("SELECT * FROM PLAYERS WHERE firstName = :firstName AND lastName = :lastName")
    suspend fun getPlayersByName(firstName: String, lastName: String): PlayerEntity?

    /**
     * Retrieves all players from the database.
     *
     * @return A list of all players or null if none exist.
     */
    @Query("SELECT * FROM players")
    suspend fun getAllPlayers(): List<PlayerEntity>?

    /**
     * Returns the total number of players in the database.
     *
     * @return The count of players.
     */
    @Query("SELECT COUNT(*) FROM players")
    suspend fun getPlayerCount(): Int
}
