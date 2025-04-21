package com.nicholas.rutherford.track.your.shot.data.room.repository

import com.nicholas.rutherford.track.your.shot.data.room.dao.ShotIgnoringDao
import com.nicholas.rutherford.track.your.shot.data.room.entities.ShotIgnoringEntity
import com.nicholas.rutherford.track.your.shot.data.room.entities.toShotIgnoring
import com.nicholas.rutherford.track.your.shot.data.room.response.ShotIgnoring
import com.nicholas.rutherford.track.your.shot.data.room.response.toShotIgnoringEntity

class ShotIgnoringRepositoryImpl(private val shotIgnoringDao: ShotIgnoringDao) : ShotIgnoringRepository {
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

    override suspend fun updateShotIgnoring(shotIgnoring: ShotIgnoring) =
        shotIgnoringDao.update(shotIgnoringEntity = shotIgnoring.toShotIgnoringEntity())

    override suspend fun createListOfShotIgnoring(shotIgnoringList: List<ShotIgnoring>) =
        shotIgnoringDao.insertAll(shotsIgnoringEntity = shotIgnoringList.map { it.toShotIgnoringEntity() })

    override suspend fun fetchAllIgnoringShots(): List<ShotIgnoring> =
        shotIgnoringDao.getAllShots().map { it.toShotIgnoring() }

    override suspend fun deleteAllShotsIgnoring() = shotIgnoringDao.deleteAll()

    override suspend fun deleteShotIgnoringByShotId(shotId: Int) =
        shotIgnoringDao.deleteByShotId(shotId = shotId)
}
