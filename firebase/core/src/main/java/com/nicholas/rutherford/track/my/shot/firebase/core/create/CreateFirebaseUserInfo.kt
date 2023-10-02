package com.nicholas.rutherford.track.my.shot.firebase.core.create

import com.nicholas.rutherford.track.my.shot.firebase.CreateAccountFirebaseAuthResponse
import com.nicholas.rutherford.track.my.shot.firebase.realtime.PlayerInfoRealtimeResponse
import kotlinx.coroutines.flow.Flow

interface CreateFirebaseUserInfo {
    fun attemptToCreateAccountFirebaseAuthResponseFlow(email: String, password: String): Flow<CreateAccountFirebaseAuthResponse>
    fun attemptToCreateAccountFirebaseRealTimeDatabaseResponseFlow(userName: String, email: String): Flow<Pair<Boolean, String?>>
    fun attemptToCreatePlayerFirebaseRealtimeDatabaseResponseFlow(key: String, playerInfoRealtimeResponse: PlayerInfoRealtimeResponse): Flow<Boolean>
}
