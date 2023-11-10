package com.nicholas.rutherford.track.your.shot.firebase.core.delete

import kotlinx.coroutines.flow.Flow

interface DeleteFirebaseUserInfo {
    fun deletePlayer(accountKey: String, playerKey: String): Flow<Boolean>
}
