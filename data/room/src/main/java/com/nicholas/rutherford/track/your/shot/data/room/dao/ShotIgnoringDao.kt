package com.nicholas.rutherford.track.your.shot.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.nicholas.rutherford.track.your.shot.data.room.entities.ShotIgnoringEntity

@Dao
interface ShotIgnoringDao {

    @Insert
    suspend fun insert(shotIgnoringEntity: ShotIgnoringEntity)

    @Update
    suspend fun update(shotIgnoringEntity: ShotIgnoringEntity)

    @Insert
    suspend fun insertAll(shotsIgnoringEntity: List<ShotIgnoringEntity>)

    @Query("SELECT * FROM shotsIgnoring")
    suspend fun getAllShots(): List<ShotIgnoringEntity>

    @Query("DELETE FROM shotsIgnoring")
    suspend fun deleteAll()

    @Query("DELETE FROM shotsIgnoring WHERE shotId = :shotId")
    suspend fun deleteByShotId(shotId: Int)
}
