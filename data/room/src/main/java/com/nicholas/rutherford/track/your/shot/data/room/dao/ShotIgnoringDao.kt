package com.nicholas.rutherford.track.your.shot.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.nicholas.rutherford.track.your.shot.data.room.entities.ShotIgnoringEntity

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Data Access Object (DAO) for performing CRUD operations on the [ShotIgnoringEntity] table.
 * This table tracks shots that are marked to be ignored in the application.
 */
@Dao
interface ShotIgnoringDao {

    /**
     * Inserts a single shot ignoring entity into the database.
     *
     * @param shotIgnoringEntity The entity to insert.
     */
    @Insert
    suspend fun insert(shotIgnoringEntity: ShotIgnoringEntity)

    /**
     * Updates an existing shot ignoring entity in the database.
     *
     * @param shotIgnoringEntity The entity to update.
     */
    @Update
    suspend fun update(shotIgnoringEntity: ShotIgnoringEntity)

    /**
     * Inserts a list of shot ignoring entities into the database.
     *
     * @param shotsIgnoringEntity The list of entities to insert.
     */
    @Insert
    suspend fun insertAll(shotsIgnoringEntity: List<ShotIgnoringEntity>)

    /**
     * Retrieves all shot ignoring entities from the database.
     *
     * @return A list of all [ShotIgnoringEntity] objects.
     */
    @Query("SELECT * FROM shotsIgnoring")
    suspend fun getAllShots(): List<ShotIgnoringEntity>

    /**
     * Deletes all shot ignoring entities from the database.
     */
    @Query("DELETE FROM shotsIgnoring")
    suspend fun deleteAll()

    /**
     * Deletes a shot ignoring entity based on the shot ID.
     *
     * @param shotId The ID of the shot to delete.
     */
    @Query("DELETE FROM shotsIgnoring WHERE shotId = :shotId")
    suspend fun deleteByShotId(shotId: Int)
}
