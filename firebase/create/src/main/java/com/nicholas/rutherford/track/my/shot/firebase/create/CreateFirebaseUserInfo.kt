package com.nicholas.rutherford.track.my.shot.firebase.create

import com.nicholas.rutherford.track.my.shot.firebase.CreateAccountFirebaseAuthResponse
import kotlinx.coroutines.flow.Flow

interface CreateFirebaseUserInfo {
    fun attemptToCreateAccountFirebaseAuthResponseFlow(email: String, password: String): Flow<CreateAccountFirebaseAuthResponse>
    fun attemptToCreateAccountFirebaseRealTimeDatabaseResponseFlow(userName: String, email: String): Flow<Pair<Boolean, String?>>
}
