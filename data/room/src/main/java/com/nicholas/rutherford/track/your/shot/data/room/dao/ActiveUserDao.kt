package com.nicholas.rutherford.track.your.shot.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.nicholas.rutherford.track.your.shot.data.room.entities.ActiveUserEntity

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Data Access Object (DAO) for performing CRUD operations on the [ActiveUserEntity] table.
 * Provides methods to insert, update, delete, and retrieve the active user from the database.
 */
@Dao
interface ActiveUserDao {

    /**
     * Inserts a single active user into the database.
     *
     * @param activeUserEntity The active user entity to insert.
     */
    @Insert
    suspend fun insert(activeUserEntity: ActiveUserEntity)

    /**
     * Updates an existing active user in the database.
     *
     * @param activeUserEntity The active user entity to update.
     */
    @Update
    suspend fun update(activeUserEntity: ActiveUserEntity)

    /**
     * Deletes all active users from the database.
     */
    @Query("DELETE FROM activeUsers")
    suspend fun delete()

    /**
     * Retrieves the active user from the database.
     *
     * @return The active user entity if one exists, otherwise null.
     */
    @Query("SELECT * FROM activeUsers")
    suspend fun getActiveUser(): ActiveUserEntity?
}
