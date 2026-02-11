package com.nicholas.rutherford.track.your.shot.data.room.repository

import android.app.Application
import com.nicholas.rutherford.track.your.shot.base.resources.StringsIds
import com.nicholas.rutherford.track.your.shot.data.room.dao.PlayerDao
import com.nicholas.rutherford.track.your.shot.data.room.dao.PlayerFilterDao
import com.nicholas.rutherford.track.your.shot.data.room.entities.PlayerEntity
import com.nicholas.rutherford.track.your.shot.data.room.entities.toPlayer
import com.nicholas.rutherford.track.your.shot.data.room.response.HasShotsLoggedFilter
import com.nicholas.rutherford.track.your.shot.data.room.response.Player
import com.nicholas.rutherford.track.your.shot.data.room.response.PlayerFilter
import com.nicholas.rutherford.track.your.shot.data.room.response.PlayerPositions.Center.toPlayerPositionValue
import com.nicholas.rutherford.track.your.shot.data.room.response.toPlayerEntity
import com.nicholas.rutherford.track.your.shot.data.room.response.toPlayerFilter

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Repository implementation for managing Player data.
 * Provides CRUD operations and abstracts the underlying PlayerDao.
 */
class PlayerRepositoryImpl(
    private val playerDao: PlayerDao,
    private val playerFilterDao: PlayerFilterDao,
    private val application: Application
) : PlayerRepository {

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
     * Both firstName and lastName must be non-empty to fetch a player.
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

    override suspend fun fetchAllPlayersWithFilters(): List<Player> {
        val currentActiveFilter = playerFilterDao.getActiveFilter()?.toPlayerFilter(positions = playerFilterDao.getActiveFilterPositions()) ?: PlayerFilter()
        return fetchAllPlayersWithFilter(filter = currentActiveFilter)
    }

    /**
     * Fetches all Players and then will filter which players to show based on [playerFilterDao].
     * That filter gets set and what gets return back is based on those user set filters
     *
     * @param filter The active filter to apply to the player list.
     * @return A list of filtered players given by the selected user filters
     */
    override suspend fun fetchAllPlayersWithFilter(filter: PlayerFilter): List<Player> {
        val allPlayers = playerDao.getAllPlayers()?.map { playerEntity -> playerEntity.toPlayer() } ?: emptyList()

        return allPlayers.filter { player ->
            matchesPositionFilter(player = player, filter = filter) &&
                matchesHasShotsLoggedFilter(player = player, filter = filter) &&
                matchesShotCountRangeFilter(player = player, filter = filter)
        }
    }

    /**
     * Checks if a player matches the position filter criteria.
     *
     * @param player The player to check.
     * @param filter The active filter containing position criteria.
     * @return true if the player matches the position filter, false otherwise.
     * Also if the [PlayerFilter.selectedPositions] is empty or if it contains All then it should return all filtered players
     */
    internal fun matchesPositionFilter(player: Player, filter: PlayerFilter): Boolean {
        if (filter.selectedPositions.isEmpty() || filter.selectedPositions.contains(application.getString(StringsIds.all))) {
            return true
        }

        return filter.selectedPositions.contains(player.position.toPlayerPositionValue(application))
    }

    /**
     * Checks if a player matches the hasShotsLogged filter criteria.
     *
     * @param player The player to check.
     * @param filter The active filter containing hasShotsLogged criteria.
     * @return true if the player matches the hasShotsLogged filter, false otherwise.
     */
    internal fun matchesHasShotsLoggedFilter(player: Player, filter: PlayerFilter): Boolean {
        return when (filter.hasShotsLogged) {
            is HasShotsLoggedFilter.HasShots -> player.shotsLoggedList.isNotEmpty()
            is HasShotsLoggedFilter.NoShots -> player.shotsLoggedList.isEmpty()
            is HasShotsLoggedFilter.Both -> true
            is HasShotsLoggedFilter.None -> true
            null -> true
        }
    }

    /**
     * Checks if a player matches the shot count range filter criteria (minShots and maxShots).
     *
     * @param player The player to check.
     * @param filter The active filter containing minShots and maxShots criteria.
     * @return true if the player matches the shot count range filter, false otherwise.
     */
    internal fun matchesShotCountRangeFilter(player: Player, filter: PlayerFilter): Boolean {
        val totalShots = player.shotsLoggedList.size

        val matchesMinShots = filter.minShots?.let { min ->
            totalShots >= min
        } ?: true

        val matchesMaxShots = filter.maxShots?.let { max ->
            totalShots <= max
        } ?: true

        return matchesMinShots && matchesMaxShots
    }

    /** Returns the total count of players in the database. */
    override suspend fun fetchPlayerCount(): Int = playerDao.getPlayerCount()

    /** Returns all players from the database from the [query]. It will then filtered by [PlayerFilter]. This will give results based on the first or last name partial match, this also goes in and converts [PlayerEntity] to a given [Player] */
    override suspend fun fetchPlayerByQuery(query: String, playerFilter: PlayerFilter): List<Player> {
        val searchedPlayers = playerDao.searchPlayers(query = query).map { playerEntity -> playerEntity.toPlayer() }

        return searchedPlayers.filter { player ->
            matchesPositionFilter(player = player, filter = playerFilter) &&
                matchesHasShotsLoggedFilter(player = player, filter = playerFilter) &&
                matchesShotCountRangeFilter(player = player, filter = playerFilter)
        }
    }
}
