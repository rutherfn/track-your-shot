package com.nicholas.rutherford.track.my.shot.data.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.nicholas.rutherford.track.my.shot.data.room.entities.ActiveUserEntity

@Dao
interface ActiveUserDao {
    @Insert
    fun insert(activeUserEntity: ActiveUserEntity)

    @Update
    fun update(activeUserEntity: ActiveUserEntity)

    @Delete
    fun delete(activeUserEntity: ActiveUserEntity)

    @Query("SELECT * FROM ActiveUserEntity")
    fun getActiveUser(): ActiveUserEntity?
}
