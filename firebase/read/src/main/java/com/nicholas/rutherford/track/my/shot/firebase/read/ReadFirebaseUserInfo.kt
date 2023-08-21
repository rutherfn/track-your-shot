package com.nicholas.rutherford.track.my.shot.firebase.read

import com.nicholas.rutherford.track.my.shot.account.info.realtime.AccountInfoRealtimeResponse
import kotlinx.coroutines.flow.Flow
import java.util.Date

interface ReadFirebaseUserInfo {
    fun getLoggedInAccountEmail(): Flow<String?>
    fun getAccountInfoFlowByEmail(email: String): Flow<AccountInfoRealtimeResponse?>
    fun getAccountInfoListFlow(): Flow<List<AccountInfoRealtimeResponse>?>
    fun getLastUpdatedDateFlow(): Flow<Date?>
    fun isEmailVerifiedFlow(): Flow<Boolean>
    fun isLoggedInFlow(): Flow<Boolean>
}
