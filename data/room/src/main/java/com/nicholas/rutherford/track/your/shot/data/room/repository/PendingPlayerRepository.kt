package com.nicholas.rutherford.track.your.shot.data.room.repository

import com.nicholas.rutherford.track.your.shot.data.room.response.Player

interface PendingPlayerRepository {

    suspend fun createPendingPlayer(player: Player)

    suspend fun updatePendingPlayer(currentPendingPlayer: Player, newPendingPlayer: Player)

    suspend fun deleteAllPendingPlayers()

    suspend fun fetchAllPendingPlayers(): List<Player>
}
