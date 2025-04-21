package com.nicholas.rutherford.track.your.shot.data.room.repository

import com.nicholas.rutherford.track.your.shot.data.room.response.ShotIgnoring

interface ShotIgnoringRepository {
    suspend fun createShotIgnoring(shotId: Int)
    suspend fun updateShotIgnoring(shotIgnoring: ShotIgnoring)
    suspend fun createListOfShotIgnoring(shotIgnoringList: List<ShotIgnoring>)
    suspend fun fetchAllIgnoringShots(): List<ShotIgnoring>
    suspend fun deleteAllShotsIgnoring()
    suspend fun deleteShotIgnoringByShotId(shotId: Int)
}
