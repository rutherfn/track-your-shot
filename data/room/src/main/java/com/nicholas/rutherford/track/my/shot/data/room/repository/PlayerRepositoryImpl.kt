package com.nicholas.rutherford.track.my.shot.data.room.repository

import com.nicholas.rutherford.track.my.shot.data.room.dao.PlayerDao
import com.nicholas.rutherford.track.my.shot.data.room.entities.toPlayer
import com.nicholas.rutherford.track.my.shot.data.room.response.Player
import com.nicholas.rutherford.track.my.shot.data.room.response.toPlayerEntity

class PlayerRepositoryImpl(private val playerDao: PlayerDao) : PlayerRepository {

    override suspend fun createPlayer(player: Player) = playerDao.insert(playerEntity = player.toPlayerEntity())

    override suspend fun updatePlayer(player: Player) = playerDao.update(playerEntity = player.toPlayerEntity())

    override suspend fun deletePlayer(player: Player) = playerDao.delete(playerEntity = player.toPlayerEntity())

    override suspend fun deleteAllPlayers() = playerDao.deleteAllPlayers()

    override suspend fun fetchPlayerByName(firstName: String, lastName: String): Player? {
        return if (firstName.isNotEmpty() && lastName.isNotEmpty()) {
            playerDao.getPlayersByName(firstName = firstName, lastName = lastName)?.toPlayer()
        } else {
            null
        }
    }

    override suspend fun fetchAllPlayers(): List<Player> = playerDao.getAllPlayers()?.map { it.toPlayer() } ?: emptyList()
}
