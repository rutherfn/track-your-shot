package com.nicholas.rutherford.track.my.shot.fakes

import com.nicholas.rutherford.track.my.shot.account.info.realtime.AccountInfoRealtimeResponse
import com.nicholas.rutherford.track.my.shot.firebase.read.ReadFirebaseUserInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeReadFirebaseUserInfo(
    private val loggedInAccountEmail: String? = null,
    private val accountInfoByEmail: AccountInfoRealtimeResponse? = null,
    private val accountInfoList: List<AccountInfoRealtimeResponse>? = null,
    private val isEmailVerified: Boolean = false,
    private val isLoggedIn: Boolean = false
) : ReadFirebaseUserInfo {

    override fun getLoggedInAccountEmail(): Flow<String?> = flowOf(loggedInAccountEmail)

    override fun getAccountInfoFlowByEmail(email: String): Flow<AccountInfoRealtimeResponse?> =
        flowOf(accountInfoByEmail)

    override fun getAccountInfoListFlow(): Flow<List<AccountInfoRealtimeResponse>?> =
        flowOf(accountInfoList)

    override fun isEmailVerifiedFlow(): Flow<Boolean> = flowOf(isEmailVerified)

    override fun isLoggedInFlow(): Flow<Boolean> = flowOf(isLoggedIn)
}
