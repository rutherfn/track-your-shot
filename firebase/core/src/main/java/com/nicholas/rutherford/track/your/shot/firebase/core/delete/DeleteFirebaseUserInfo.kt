package com.nicholas.rutherford.track.your.shot.firebase.core.delete

import kotlinx.coroutines.flow.Flow

interface DeleteFirebaseUserInfo {
    val hasDeletedShotFlow: Flow<Boolean>

    fun updateHasDeletedShotFlow(hasDeletedShot: Boolean)
    fun deletePlayer(playerKey: String): Flow<Boolean>
    fun deleteShot(playerKey: String, index: Int): Flow<Boolean>
    fun deleteDeclaredShot(shotKey: String): Flow<Boolean>
    fun deleteReport(reportKey: String): Flow<Boolean>
}
