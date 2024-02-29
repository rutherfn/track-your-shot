package com.nicholas.rutherford.track.your.shot.data.room.repository

import com.nicholas.rutherford.track.your.shot.data.room.dao.PendingPlayerDao
import com.nicholas.rutherford.track.your.shot.data.room.entities.toPlayer
import com.nicholas.rutherford.track.your.shot.data.room.response.Player
import com.nicholas.rutherford.track.your.shot.data.room.response.toPendingPlayerEntity

class PendingPlayerRepositoryImpl(private val pendingPlayerDao: PendingPlayerDao) : PendingPlayerRepository {
    override suspend fun createPendingPlayer(player: Player) = pendingPlayerDao.insert(pendingPlayerEntity = player.toPendingPlayerEntity())

    override suspend fun updatePendingPlayer(currentPendingPlayer: Player, newPendingPlayer: Player) {
        val playerId = pendingPlayerDao.getPendingPlayerIdByName(firstName = currentPendingPlayer.firstName, lastName = currentPendingPlayer.lastName)

        pendingPlayerDao.update(pendingPlayerEntity = newPendingPlayer.toPendingPlayerEntity().copy(id = playerId))
    }

    override suspend fun deleteAllPendingPlayers() = pendingPlayerDao.deleteAllPendingPlayers()

    override suspend fun fetchPlayerById(id: Int): Player? = pendingPlayerDao.getPlayerById(id = id)?.toPlayer()

    override suspend fun fetchPendingPlayerIdByName(firstName: String, lastName: String): Int =
        pendingPlayerDao.getPendingPlayerIdByName(firstName = firstName, lastName = lastName)

    override suspend fun fetchAllPendingPlayers(): List<Player> = pendingPlayerDao.getAllPendingPlayers()?.map { pendingPlayer -> pendingPlayer.toPlayer() } ?: emptyList()
}
