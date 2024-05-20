package com.nicholas.rutherford.track.your.shot.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.nicholas.rutherford.track.your.shot.data.room.entities.PendingPlayerEntity

@Dao
interface PendingPlayerDao {

    @Insert
    suspend fun insert(pendingPlayerEntity: PendingPlayerEntity)

    @Update
    suspend fun update(pendingPlayerEntity: PendingPlayerEntity)

    @Query("DELETE FROM pendingPlayers")
    suspend fun deleteAllPendingPlayers()

    @Query("SELECT * FROM pendingPlayers WHERE id = :id")
    suspend fun getPlayerById(id: Int): PendingPlayerEntity?

    @Query("SELECT * FROM pendingPlayers")
    suspend fun getAllPendingPlayers(): List<PendingPlayerEntity>?

    @Query("SELECT * FROM pendingPlayers WHERE firstName = :firstName AND lastName = :lastName")
    suspend fun getPendingPlayerByName(firstName: String, lastName: String): PendingPlayerEntity?

    @Query("SELECT id FROM pendingPlayers WHERE firstName = :firstName AND lastName = :lastName")
    suspend fun getPendingPlayerIdByName(firstName: String, lastName: String): Int
}
