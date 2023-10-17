package com.nicholas.rutherford.track.my.shot.data.room.repository

import com.nicholas.rutherford.track.my.shot.data.room.response.Player

interface PlayerRepository {
    suspend fun createPlayer(player: Player)
    suspend fun updatePlayer(player: Player)
    suspend fun deletePlayer(player: Player)
    suspend fun deleteAllPlayers()
    suspend fun fetchPlayerByName(firstName: String, lastName: String): Player?
    suspend fun fetchAllPlayers(): List<Player>
}
