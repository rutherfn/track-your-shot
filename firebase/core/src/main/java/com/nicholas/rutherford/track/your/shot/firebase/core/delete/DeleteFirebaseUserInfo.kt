package com.nicholas.rutherford.track.your.shot.firebase.core.delete

import kotlinx.coroutines.flow.Flow

interface DeleteFirebaseUserInfo {
    fun deletePlayer(playerKey: String): Flow<Boolean>
}
