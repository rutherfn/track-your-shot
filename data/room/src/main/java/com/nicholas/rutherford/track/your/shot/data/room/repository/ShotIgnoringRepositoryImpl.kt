package com.nicholas.rutherford.track.your.shot.data.room.repository

import com.nicholas.rutherford.track.your.shot.data.room.dao.ShotIgnoringDao
import com.nicholas.rutherford.track.your.shot.data.room.entities.ShotIgnoringEntity
import com.nicholas.rutherford.track.your.shot.data.room.entities.toShotIgnoring
import com.nicholas.rutherford.track.your.shot.data.room.response.ShotIgnoring
import com.nicholas.rutherford.track.your.shot.data.room.response.toShotIgnoringEntity

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Repository implementation for managing shots to ignore in the database.
 * Provides CRUD operations on [ShotIgnoring] objects while abstracting
 * the underlying [ShotIgnoringDao] and database entities.
 */
class ShotIgnoringRepositoryImpl(private val shotIgnoringDao: ShotIgnoringDao) : ShotIgnoringRepository {

    /**
     * Creates a new [ShotIgnoring] entry in the database for the given shotId.
     * Automatically determines the next available ID.
     */
    override suspend fun createShotIgnoring(shotId: Int) {
        val entity = ShotIgnoringEntity(
            id = shotIgnoringDao.getAllShots()
                .maxByOrNull { it.id }
                ?.id
                ?.plus(1)
                ?: 1,
            shotId = shotId
        )
        shotIgnoringDao.insert(entity)
    }

    /**
     * Updates an existing [ShotIgnoring] entry in the database.
     */
    override suspend fun updateShotIgnoring(shotIgnoring: ShotIgnoring) =
        shotIgnoringDao.update(shotIgnoringEntity = shotIgnoring.toShotIgnoringEntity())

    /**
     * Inserts a list of [ShotIgnoring] entries into the database.
     */
    override suspend fun createListOfShotIgnoring(shotIgnoringList: List<ShotIgnoring>) =
        shotIgnoringDao.insertAll(shotsIgnoringEntity = shotIgnoringList.map { it.toShotIgnoringEntity() })

    /**
     * Fetches all [ShotIgnoring] entries from the database.
     */
    override suspend fun fetchAllIgnoringShots(): List<ShotIgnoring> =
        shotIgnoringDao.getAllShots().map { it.toShotIgnoring() }

    /**
     * Deletes all [ShotIgnoring] entries from the database.
     */
    override suspend fun deleteAllShotsIgnoring() = shotIgnoringDao.deleteAll()

    /**
     * Deletes a [ShotIgnoring] entry by its associated shotId.
     */
    override suspend fun deleteShotIgnoringByShotId(shotId: Int) =
        shotIgnoringDao.deleteByShotId(shotId = shotId)
}
