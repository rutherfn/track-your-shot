package com.nicholas.rutherford.track.your.shot.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.nicholas.rutherford.track.your.shot.data.room.entities.DeclaredShotEntity

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Data Access Object (DAO) for performing CRUD operations on the [DeclaredShotEntity] table.
 * Provides methods to insert, update, delete, and query declared shots in the database.
 */
@Dao
interface DeclaredShotDao {

    /**
     * Inserts a single declared shot into the database.
     *
     * @param declaredShotEntity The declared shot entity to insert.
     */
    @Insert
    suspend fun insert(declaredShotEntity: DeclaredShotEntity)

    /**
     * Inserts multiple declared shots into the database.
     *
     * @param declaredShotEntities A list of declared shot entities to insert.
     */
    @Insert
    suspend fun insert(declaredShotEntities: List<DeclaredShotEntity>)

    /**
     * Updates an existing declared shot in the database.
     *
     * @param declaredShotEntity The declared shot entity to update.
     */
    @Update
    suspend fun update(declaredShotEntity: DeclaredShotEntity)

    /**
     * Deletes all declared shots from the database.
     */
    @Query("DELETE FROM declaredShots")
    suspend fun deleteAll()

    /**
     * Deletes a declared shot by its ID.
     *
     * @param id The ID of the declared shot to delete.
     */
    @Query("DELETE FROM declaredShots WHERE id = :id")
    suspend fun deleteDeclaredShotById(id: Int)

    /**
     * Retrieves all declared shots, sorted alphabetically by title (case-insensitive).
     *
     * @return A list of all [DeclaredShotEntity] objects.
     */
    @Query("SELECT * FROM declaredShots ORDER BY title COLLATE NOCASE ASC")
    suspend fun getAllDeclaredShots(): List<DeclaredShotEntity>

    /**
     * Retrieves a declared shot by its ID.
     *
     * @param id The ID of the declared shot.
     * @return The declared shot entity if found, otherwise null.
     */
    @Query("SELECT * FROM declaredShots WHERE id = :id")
    suspend fun getDeclaredShotFromId(id: Int): DeclaredShotEntity?

    /**
     * Retrieves a declared shot by its title.
     *
     * @param title The title of the declared shot.
     * @return The declared shot entity if found, otherwise null.
     */
    @Query("SELECT * FROM declaredShots WHERE title = :title")
    suspend fun getDeclaredShotByTitle(title: String): DeclaredShotEntity?

    /**
     * Searches for declared shots by a query string, ignoring spaces.
     *
     * @param searchQuery The search query string.
     * @return A list of declared shots that match the search query.
     */
    @Query("SELECT * FROM declaredShots WHERE REPLACE(title, ' ', '') LIKE '%' || REPLACE(:searchQuery, ' ', '') || '%' ORDER BY title ASC")
    suspend fun getDeclaredShotsBySearchQuery(searchQuery: String): List<DeclaredShotEntity>

    /**
     * Retrieves the maximum ID of all declared shots, or returns 1 if none exist.
     *
     * @return The maximum declared shot ID, or 1 as default.
     */
    @Query("SELECT COALESCE(MAX(id), 1) FROM declaredShots")
    suspend fun getMaxIdOrDefault(): Int
}
