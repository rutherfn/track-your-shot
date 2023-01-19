package com.nicholas.rutherford.track.my.shot.firebase.create

import com.nicholas.rutherford.track.my.shot.account.info.CreateAccountFirebaseAuthResponse
import kotlinx.coroutines.flow.Flow

interface CreateFirebaseUserInfo {
    fun attemptToCreateAccountFirebaseAuthResponseFlow(email: String, password: String): Flow<CreateAccountFirebaseAuthResponse>
    fun attemptToCreateAccountFirebaseRealTimeDatabaseResponseFlow(userName: String, email: String): Flow<Boolean>
}
