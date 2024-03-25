package com.nicholas.rutherford.track.your.shot.data.room.repository

import com.nicholas.rutherford.track.your.shot.data.room.dao.PlayerDao
import com.nicholas.rutherford.track.your.shot.data.room.entities.toPlayer
import com.nicholas.rutherford.track.your.shot.data.room.response.Player
import com.nicholas.rutherford.track.your.shot.data.room.response.toPlayerEntity

class PlayerRepositoryImpl(private val playerDao: PlayerDao) : PlayerRepository {

    override suspend fun createPlayer(player: Player) = playerDao.insert(playerEntity = player.toPlayerEntity())

    override suspend fun createListOfPlayers(playerList: List<Player>) = playerDao.insertAll(players = playerList.map { it.toPlayerEntity() })

    override suspend fun updatePlayer(currentPlayer: Player, newPlayer: Player) {
        val playerId = playerDao.getPlayerIdByName(firstName = currentPlayer.firstName, lastName = currentPlayer.lastName)

        playerDao.update(playerEntity = newPlayer.toPlayerEntity().copy(id = playerId))
    }

    override suspend fun deletePlayerByName(firstName: String, lastName: String) = playerDao.deletePlayerByName(firstName = firstName, lastName = lastName)

    override suspend fun deleteAllPlayers() = playerDao.deleteAllPlayers()

    override suspend fun fetchPlayerIdByName(firstName: String, lastName: String): Int = playerDao.getPlayerIdByName(firstName = firstName, lastName = lastName)

    override suspend fun fetchPlayerById(id: Int): Player? = playerDao.getPlayerById(id = id).toPlayer()

    override suspend fun fetchPlayerByName(firstName: String, lastName: String): Player? {
        return if (firstName.isNotEmpty() && lastName.isNotEmpty()) {
            playerDao.getPlayersByName(firstName = firstName, lastName = lastName)?.toPlayer()
        } else {
            null
        }
    }

    override suspend fun fetchAllPlayers(): List<Player> = playerDao.getAllPlayers()?.map { it.toPlayer() } ?: emptyList()

    override suspend fun fetchPlayerCount(): Int = playerDao.getPlayerCount()
}
