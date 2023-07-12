package com.nicholas.rutherford.track.my.shot.data.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.nicholas.rutherford.track.my.shot.data.room.PendingUser

@Dao
interface PendingUserDao {
    @Insert
    fun insert(pendingUser: PendingUser)

    @Update
    fun update(pendingUser: PendingUser)

    @Delete
    fun delete(pendingUser: PendingUser)

    @Query("SELECT * FROM pendingUser")
    fun getPendingUser(): PendingUser?
}
