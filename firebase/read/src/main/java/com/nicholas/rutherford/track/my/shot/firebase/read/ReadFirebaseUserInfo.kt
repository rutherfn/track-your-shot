package com.nicholas.rutherford.track.my.shot.firebase.read

import com.nicholas.rutherford.track.my.shot.data.firebase.AccountInfoRealtimeResponse
import kotlinx.coroutines.flow.Flow
import java.util.Date

interface ReadFirebaseUserInfo {
    fun getLoggedInAccountEmail(): Flow<String?>
    fun getAccountInfoFlowByEmail(email: String): Flow<AccountInfoRealtimeResponse?>
    // fun getPlayersInfoFlowByEmail(email: String): Flow<Acc>
    fun getAccountInfoListFlow(): Flow<List<AccountInfoRealtimeResponse>?>
    fun getAccountInfoKeyByEmail(email: String): Flow<String?>
    fun getLastUpdatedDateFlow(): Flow<Date?>
    fun isEmailVerifiedFlow(): Flow<Boolean>
    fun isLoggedInFlow(): Flow<Boolean>
}
