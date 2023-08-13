package com.nicholas.rutherford.track.my.shot.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.nicholas.rutherford.track.my.shot.data.room.entities.ActiveUserEntity

@Dao
interface ActiveUserDao {
    @Insert
    suspend fun insert(activeUserEntity: ActiveUserEntity)

    @Update
    suspend fun update(activeUserEntity: ActiveUserEntity)

    @Query("DELETE FROM activeUsers")
    suspend fun delete()

    @Query("SELECT * FROM activeUsers")
    suspend fun getActiveUser(): ActiveUserEntity?
}
