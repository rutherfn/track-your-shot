package com.nicholas.rutherford.track.my.shot.firebase.core.read

import com.nicholas.rutherford.track.my.shot.firebase.realtime.AccountInfoRealtimeResponse
import com.nicholas.rutherford.track.my.shot.firebase.realtime.PlayerInfoRealtimeResponse
import kotlinx.coroutines.flow.Flow
import java.util.Date

interface ReadFirebaseUserInfo {
    fun getLoggedInAccountEmail(): Flow<String?>
    fun getAccountInfoFlowByEmail(email: String): Flow<AccountInfoRealtimeResponse?>
    fun getAccountInfoListFlow(): Flow<List<AccountInfoRealtimeResponse>?>
    fun getAccountInfoKeyFlowByEmail(email: String): Flow<String?>
    fun getPlayerInfoList(accountKey: String): Flow<List<PlayerInfoRealtimeResponse>>
    fun getLastUpdatedDateFlow(): Flow<Date?>
    fun isEmailVerifiedFlow(): Flow<Boolean>
    fun isLoggedInFlow(): Flow<Boolean>
}
