package com.nicholas.rutherford.track.your.shot.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.nicholas.rutherford.track.your.shot.data.room.entities.PlayerEntity

@Dao
interface PlayerDao {

    @Insert
    suspend fun insert(playerEntity: PlayerEntity)

    @Insert
    suspend fun insertAll(players: List<PlayerEntity>)

    @Update
    suspend fun update(playerEntity: PlayerEntity)

    @Query("DELETE FROM players WHERE firstName = :firstName AND lastName = :lastName")
    suspend fun deletePlayerByName(firstName: String, lastName: String)

    @Query("DELETE FROM players")
    suspend fun deleteAllPlayers()

    @Query("SELECT * FROM PLAYERS WHERE id = :id")
    suspend fun getPlayerById(id: Int): PlayerEntity

    @Query("SELECT id FROM players WHERE firstName = :firstName AND lastName = :lastName")
    suspend fun getPlayerIdByName(firstName: String, lastName: String): Int

    @Query("SELECT * FROM PLAYERS WHERE firstName = :firstName AND lastName = :lastName")
    suspend fun getPlayersByName(firstName: String, lastName: String): PlayerEntity?

    @Query("SELECT * FROM players")
    suspend fun getAllPlayers(): List<PlayerEntity>?

    @Query("SELECT COUNT(*) FROM players")
    suspend fun getPlayerCount(): Int
}
