package com.nicholas.rutherford.track.your.shot.firebase.core.create

import android.net.Uri
import com.nicholas.rutherford.track.your.shot.firebase.CreateAccountFirebaseAuthResponse
import com.nicholas.rutherford.track.your.shot.firebase.realtime.IndividualPlayerReportRealtimeResponse
import com.nicholas.rutherford.track.your.shot.firebase.realtime.PlayerInfoRealtimeResponse
import kotlinx.coroutines.flow.Flow

interface CreateFirebaseUserInfo {
    fun attemptToCreateAccountFirebaseAuthResponseFlow(email: String, password: String): Flow<CreateAccountFirebaseAuthResponse>
    fun attemptToCreateAccountFirebaseRealTimeDatabaseResponseFlow(userName: String, email: String): Flow<Pair<Boolean, String?>>
    fun attemptToCreateDefaultShotIdsToIgnoreFirebaseRealTimeDatabaseResponseFlow(defaultShotIdsToIgnore: List<Int>): Flow<Pair<Boolean, List<Int>?>>
    fun attemptToCreatePlayerFirebaseRealtimeDatabaseResponseFlow(playerInfoRealtimeResponse: PlayerInfoRealtimeResponse): Flow<Pair<Boolean, String?>>
    fun attemptToCreateIndividualPlayerReportFirebaseRealtimeDatabaseResponseFlow(individualPlayerReportRealtimeResponse: IndividualPlayerReportRealtimeResponse): Flow<Pair<Boolean, String?>>
    fun attemptToCreateImageFirebaseStorageResponseFlow(uri: Uri): Flow<String?>
    fun attemptToCreatePdfFirebaseStorageResponseFlow(uri: Uri): Flow<String?>
}
