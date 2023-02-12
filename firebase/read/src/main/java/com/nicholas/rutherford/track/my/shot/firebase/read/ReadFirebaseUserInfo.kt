package com.nicholas.rutherford.track.my.shot.firebase.read

import kotlinx.coroutines.flow.Flow

interface ReadFirebaseUserInfo {
    fun isEmailVerified(): Flow<Boolean>
    fun isLoggedIn(): Flow<Boolean>
}
