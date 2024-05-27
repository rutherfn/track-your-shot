package com.nicholas.rutherford.track.your.shot.data.room.repository

import com.nicholas.rutherford.track.your.shot.data.room.response.DeclaredShot

interface DeclaredShotRepository {

    suspend fun createDeclaredShots()

    suspend fun updateDeclaredShot(declaredShot: DeclaredShot)

    suspend fun fetchAllDeclaredShots(): List<DeclaredShot>

    suspend fun deleteAllDeclaredShots()
    suspend fun fetchDeclaredShotFromId(id: Int): DeclaredShot?

    suspend fun fetchDeclaredShotsBySearchQuery(searchQuery: String): List<DeclaredShot>
}
