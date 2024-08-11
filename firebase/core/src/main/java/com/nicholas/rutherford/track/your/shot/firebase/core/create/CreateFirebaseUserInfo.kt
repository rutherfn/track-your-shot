package com.nicholas.rutherford.track.your.shot.firebase.core.create

import android.net.Uri
import com.nicholas.rutherford.track.your.shot.firebase.CreateAccountFirebaseAuthResponse
import com.nicholas.rutherford.track.your.shot.firebase.realtime.PlayerInfoRealtimeResponse
import kotlinx.coroutines.flow.Flow

interface CreateFirebaseUserInfo {
    fun attemptToCreateAccountFirebaseAuthResponseFlow(email: String, password: String): Flow<CreateAccountFirebaseAuthResponse>
    fun attemptToCreateAccountFirebaseRealTimeDatabaseResponseFlow(userName: String, email: String): Flow<Pair<Boolean, String?>>
    fun attemptToCreatePlayerFirebaseRealtimeDatabaseResponseFlow(playerInfoRealtimeResponse: PlayerInfoRealtimeResponse): Flow<Pair<Boolean, String?>>
    fun attemptToCreateImageFirebaseStorageResponseFlow(uri: Uri): Flow<String?>
}
