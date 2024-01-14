package com.nicholas.rutherford.track.your.shot.data.room.repository

import com.nicholas.rutherford.track.your.shot.data.room.response.DeclaredShot

interface DeclaredShotRepository {

    suspend fun createDeclaredShots()

    suspend fun updateDeclaredShot(declaredShot: DeclaredShot)

    suspend fun fetchAllDeclaredShots(): List<DeclaredShot>

    suspend fun deleteAllDeclaredShots()
}
