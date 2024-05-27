package com.nicholas.rutherford.track.your.shot.data.room.repository

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.nicholas.rutherford.track.your.shot.data.room.dao.DeclaredShotDao
import com.nicholas.rutherford.track.your.shot.data.room.entities.toDeclaredShot
import com.nicholas.rutherford.track.your.shot.data.room.response.DeclaredShot
import com.nicholas.rutherford.track.your.shot.data.room.response.toDeclaredShotEntity
import com.nicholas.rutherford.track.your.shot.feature.splash.declaredshotsjson.DeclaredShotsJson

class DeclaredShotRepositoryImpl(
    private val declaredShotDao: DeclaredShotDao,
    private val declaredShotsJson: DeclaredShotsJson
) : DeclaredShotRepository {

    override suspend fun createDeclaredShots() {
        val declaredShots = declaredShotDao.getAllDeclaredShots()

        if (declaredShots.isEmpty()) {
            declaredShotDao.insert(declaredShotEntities = getDeclaredShotsFromJson().map { it.toDeclaredShotEntity() })
        }
    }
    override suspend fun updateDeclaredShot(declaredShot: DeclaredShot) =
        declaredShotDao.update(declaredShotEntity = declaredShot.toDeclaredShotEntity())

    override suspend fun fetchAllDeclaredShots(): List<DeclaredShot> =
        declaredShotDao.getAllDeclaredShots().map { it.toDeclaredShot() }

    override suspend fun deleteAllDeclaredShots() = declaredShotDao.deleteAll()

    override suspend fun fetchDeclaredShotFromId(id: Int): DeclaredShot? =
        declaredShotDao.getDeclaredShotFromId(id = id)?.toDeclaredShot()

    override suspend fun fetchDeclaredShotsBySearchQuery(searchQuery: String): List<DeclaredShot> =
        declaredShotDao.getDeclaredShotsBySearchQuery(searchQuery = searchQuery).map { it.toDeclaredShot() }

    private fun getDeclaredShotsFromJson(): List<DeclaredShot> {
        val json = declaredShotsJson.fetchJsonDeclaredShots()
        val typeToken = object : TypeToken<List<DeclaredShot>>() {}.type
        return Gson().fromJson(json, typeToken)
    }
}
