package com.nicholas.rutherford.track.your.shot.firebase.core.read

import com.nicholas.rutherford.track.your.shot.firebase.realtime.AccountInfoRealtimeResponse
import com.nicholas.rutherford.track.your.shot.firebase.realtime.PlayerInfoRealtimeWithKeyResponse
import kotlinx.coroutines.flow.Flow
import java.util.Date

interface ReadFirebaseUserInfo {
    fun getLoggedInAccountEmail(): Flow<String?>
    fun getAccountInfoFlow(): Flow<AccountInfoRealtimeResponse?>
    fun getAccountInfoKeyFlow(): Flow<String?>
    fun getPlayerInfoList(accountKey: String): Flow<List<PlayerInfoRealtimeWithKeyResponse>>
    fun getLastUpdatedDateFlow(): Flow<Date?>
    fun isEmailVerifiedFlow(): Flow<Boolean>
    fun isLoggedInFlow(): Flow<Boolean>
}
