package com.nicholas.rutherford.track.your.shot.data.room.repository

import com.nicholas.rutherford.track.your.shot.data.room.response.DeclaredShot

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Repository interface defining CRUD operations for DeclaredShot data.
 * Provides an abstraction layer over the underlying DeclaredShotDao,
 * allowing use of DeclaredShot domain models rather than database entities.
 */
interface DeclaredShotRepository {

    /** Inserts a new declared shot into the database. */
    suspend fun createNewDeclaredShot(declaredShot: DeclaredShot)

    /**
     * Creates multiple declared shots from a JSON source, excluding
     * any IDs present in [shotIdsToFilterOut] or already existing in the database.
     */
    suspend fun createDeclaredShots(shotIdsToFilterOut: List<Int>)

    /** Updates an existing declared shot in the database. */
    suspend fun updateDeclaredShot(declaredShot: DeclaredShot)

    /** Retrieves all declared shots from the database. */
    suspend fun fetchAllDeclaredShots(): List<DeclaredShot>

    /** Deletes all declared shots from the database. */
    suspend fun deleteAllDeclaredShots()

    /** Deletes a declared shot by its [id]. */
    suspend fun deleteShotById(id: Int)

    /** Fetches a declared shot by its [id], returning null if not found. */
    suspend fun fetchDeclaredShotFromId(id: Int): DeclaredShot?

    /** Fetches a declared shot by its [name/title], returning null if not found. */
    suspend fun fetchDeclaredShotFromName(name: String): DeclaredShot?

    /**
     * Searches for declared shots containing [searchQuery] in their title.
     * Returns a list of matching DeclaredShot objects.
     */
    suspend fun fetchDeclaredShotsBySearchQuery(searchQuery: String): List<DeclaredShot>

    /** Retrieves the maximum declared shot ID in the database, or a default value if empty. */
    suspend fun fetchMaxId(): Int
}
