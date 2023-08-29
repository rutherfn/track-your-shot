package com.nicholas.rutherford.track.my.shot.fakes

import com.nicholas.rutherford.track.my.shot.data.firebase.AccountInfoRealtimeResponse
import com.nicholas.rutherford.track.my.shot.firebase.read.ReadFirebaseUserInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeReadFirebaseUserInfo(
    private val loggedInAccountEmail: String? = null,
    private val accountInfoByEmail: com.nicholas.rutherford.track.my.shot.data.firebase.AccountInfoRealtimeResponse? = null,
    private val accountInfoList: List<com.nicholas.rutherford.track.my.shot.data.firebase.AccountInfoRealtimeResponse>? = null,
    private val isEmailVerified: Boolean = false,
    private val isLoggedIn: Boolean = false
) : ReadFirebaseUserInfo {

    override fun getLoggedInAccountEmail(): Flow<String?> = flowOf(loggedInAccountEmail)

    override fun getAccountInfoFlowByEmail(email: String): Flow<com.nicholas.rutherford.track.my.shot.data.firebase.AccountInfoRealtimeResponse?> =
        flowOf(accountInfoByEmail)

    override fun getAccountInfoListFlow(): Flow<List<com.nicholas.rutherford.track.my.shot.data.firebase.AccountInfoRealtimeResponse>?> =
        flowOf(accountInfoList)

    override fun isEmailVerifiedFlow(): Flow<Boolean> = flowOf(isEmailVerified)

    override fun isLoggedInFlow(): Flow<Boolean> = flowOf(isLoggedIn)
}
