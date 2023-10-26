package com.nicholas.rutherford.track.my.shot.fakes

import com.nicholas.rutherford.track.my.shot.firebase.core.read.ReadFirebaseUserInfo
import com.nicholas.rutherford.track.my.shot.firebase.realtime.AccountInfoRealtimeResponse
import com.nicholas.rutherford.track.my.shot.firebase.realtime.PlayerInfoRealtimeWithKeyResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import java.util.Date

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

    override fun getAccountInfoKeyFlowByEmail(email: String): Flow<String?> {
        TODO("Not yet implemented")
    }

    override fun getPlayerInfoList(accountKey: String): Flow<List<PlayerInfoRealtimeWithKeyResponse>> {
        TODO("Not yet implemented")
    }

    override fun getLastUpdatedDateFlow(): Flow<Date?> {
        TODO("Not yet implemented")
    }

    override fun isEmailVerifiedFlow(): Flow<Boolean> = flowOf(isEmailVerified)

    override fun isLoggedInFlow(): Flow<Boolean> = flowOf(isLoggedIn)
}
