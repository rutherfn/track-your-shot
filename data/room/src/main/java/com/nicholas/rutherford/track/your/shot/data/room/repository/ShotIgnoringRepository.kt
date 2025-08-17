package com.nicholas.rutherford.track.your.shot.data.room.repository

import com.nicholas.rutherford.track.your.shot.data.room.response.ShotIgnoring

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Repository interface defining CRUD operations for managing shots to ignore.
 * Provides an abstraction over the underlying data source for [ShotIgnoring] objects.
 */
interface ShotIgnoringRepository {

    /**
     * Creates a new [ShotIgnoring] entry for the given shot ID.
     *
     * @param shotId The ID of the shot to ignore.
     */
    suspend fun createShotIgnoring(shotId: Int)

    /**
     * Updates an existing [ShotIgnoring] entry.
     *
     * @param shotIgnoring The [ShotIgnoring] object containing updated data.
     */
    suspend fun updateShotIgnoring(shotIgnoring: ShotIgnoring)

    /**
     * Inserts a list of [ShotIgnoring] entries into the data source.
     *
     * @param shotIgnoringList The list of [ShotIgnoring] objects to insert.
     */
    suspend fun createListOfShotIgnoring(shotIgnoringList: List<ShotIgnoring>)

    /**
     * Fetches all [ShotIgnoring] entries from the data source.
     *
     * @return A list of all [ShotIgnoring] objects.
     */
    suspend fun fetchAllIgnoringShots(): List<ShotIgnoring>

    /**
     * Deletes all [ShotIgnoring] entries from the data source.
     */
    suspend fun deleteAllShotsIgnoring()

    /**
     * Deletes a [ShotIgnoring] entry by its associated shot ID.
     *
     * @param shotId The ID of the shot to remove from ignored shots.
     */
    suspend fun deleteShotIgnoringByShotId(shotId: Int)
}
