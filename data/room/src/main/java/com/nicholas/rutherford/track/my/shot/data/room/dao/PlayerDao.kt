package com.nicholas.rutherford.track.my.shot.data.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.nicholas.rutherford.track.my.shot.data.room.entities.PlayerEntity

@Dao
interface PlayerDao {

    @Insert
    suspend fun insert(playerEntity: PlayerEntity)

    @Update
    suspend fun update(playerEntity: PlayerEntity)

    @Delete
    suspend fun delete(playerEntity: PlayerEntity)

    @Query("DELETE FROM players")
    suspend fun deleteAllPlayers()

    @Query("SELECT * FROM PLAYERS WHERE firstName = :firstName AND lastName = :lastName")
    suspend fun getPlayersByName(firstName: String, lastName: String): PlayerEntity?

    @Query("SELECT * FROM players")
    suspend fun getAllPlayers(): List<PlayerEntity>?

    @Query("SELECT COUNT(*) FROM players")
    suspend fun getPlayerCount(): Int
}
