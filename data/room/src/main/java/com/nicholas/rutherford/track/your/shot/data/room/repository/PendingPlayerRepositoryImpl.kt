package com.nicholas.rutherford.track.your.shot.data.room.repository

import com.nicholas.rutherford.track.your.shot.data.room.dao.PendingPlayerDao
import com.nicholas.rutherford.track.your.shot.data.room.entities.toPlayer
import com.nicholas.rutherford.track.your.shot.data.room.response.Player
import com.nicholas.rutherford.track.your.shot.data.room.response.toPendingPlayerEntity

class PendingPlayerRepositoryImpl(private val pendingPlayerDao: PendingPlayerDao) : PendingPlayerRepository {
    override suspend fun createPendingPlayer(player: Player) = pendingPlayerDao.insert(pendingPlayerEntity = player.toPendingPlayerEntity())

    override suspend fun updatePendingPlayer(player: Player) = pendingPlayerDao.update(pendingPlayerEntity = player.toPendingPlayerEntity())

    override suspend fun deleteAllPendingPlayers() = pendingPlayerDao.deleteAllPendingPlayers()

    override suspend fun fetchAllPendingPlayers(): List<Player> = pendingPlayerDao.getAllPendingPlayers()?.map { pendingPlayer -> pendingPlayer.toPlayer() } ?: emptyList()
}