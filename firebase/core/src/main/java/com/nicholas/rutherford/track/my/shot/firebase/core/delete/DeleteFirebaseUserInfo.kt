package com.nicholas.rutherford.track.my.shot.firebase.core.delete

import kotlinx.coroutines.flow.Flow

interface DeleteFirebaseUserInfo {
    fun deletePlayer(accountKey: String, playerKey: String): Flow<Boolean>
}
