package com.nicholas.rutherford.track.your.shot.data.room.repository

import com.nicholas.rutherford.track.your.shot.data.room.response.Player

interface PlayerRepository {
    suspend fun createPlayer(player: Player)
    suspend fun createListOfPlayers(playerList: List<Player>)
    suspend fun updatePlayer(currentPlayer: Player, newPlayer: Player)
    suspend fun deletePlayerByName(firstName: String, lastName: String)
    suspend fun deleteAllPlayers()
    suspend fun fetchPlayerIdByName(firstName: String, lastName: String): Int?
    suspend fun fetchPlayerByName(firstName: String, lastName: String): Player?
    suspend fun fetchPlayerById(id: Int): Player?
    suspend fun fetchAllPlayers(): List<Player>
    suspend fun fetchPlayerCount(): Int
}
