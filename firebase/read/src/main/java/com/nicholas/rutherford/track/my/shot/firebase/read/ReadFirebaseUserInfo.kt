package com.nicholas.rutherford.track.my.shot.firebase.read

import com.nicholas.rutherford.track.my.shot.account.info.realtime.AccountInfoRealtimeResponse
import kotlinx.coroutines.flow.Flow

interface ReadFirebaseUserInfo {
    fun getLoggedInAccountEmail(): Flow<String?>
    fun getAccountInfoFlowByEmail(email: String): Flow<AccountInfoRealtimeResponse?>
    fun getAllAccountInfoFlow(): Flow<List<AccountInfoRealtimeResponse>?>
    fun isEmailVerifiedFlow(): Flow<Boolean>
    fun isLoggedInFlow(): Flow<Boolean>
}
