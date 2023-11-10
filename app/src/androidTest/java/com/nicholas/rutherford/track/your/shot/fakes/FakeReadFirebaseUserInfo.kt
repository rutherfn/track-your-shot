package com.nicholas.rutherford.track.your.shot.fakes

import com.nicholas.rutherford.track.your.shot.firebase.core.read.ReadFirebaseUserInfo
import com.nicholas.rutherford.track.your.shot.firebase.realtime.AccountInfoRealtimeResponse
import com.nicholas.rutherford.track.your.shot.firebase.realtime.PlayerInfoRealtimeWithKeyResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import java.util.Date

class FakeReadFirebaseUserInfo(
    private val loggedInAccountEmail: String? = null,
    private val accountInfoByEmail: AccountInfoRealtimeResponse? = null,
    private val accountInfoList: List<AccountInfoRealtimeResponse>? = null,
    private val accountInfoKey: String = "",
    private val playerInfoList: List<PlayerInfoRealtimeWithKeyResponse> = emptyList(),
    private val lastUpdatedDate: Date? = null,
    private val isEmailVerified: Boolean = false,
    private val isLoggedIn: Boolean = false
) : ReadFirebaseUserInfo {

    override fun getLoggedInAccountEmail(): Flow<String?> = flowOf(loggedInAccountEmail)

    override fun getAccountInfoFlowByEmail(email: String): Flow<AccountInfoRealtimeResponse?> =
        flowOf(accountInfoByEmail)

    override fun getAccountInfoListFlow(): Flow<List<AccountInfoRealtimeResponse>?> =
        flowOf(accountInfoList)

    override fun getAccountInfoKeyFlowByEmail(email: String): Flow<String?> =
        flowOf(accountInfoKey)

    override fun getPlayerInfoList(accountKey: String): Flow<List<PlayerInfoRealtimeWithKeyResponse>> =
        flowOf(playerInfoList)

    override fun getLastUpdatedDateFlow(): Flow<Date?> = flowOf(lastUpdatedDate)

    override fun isEmailVerifiedFlow(): Flow<Boolean> = flowOf(isEmailVerified)

    override fun isLoggedInFlow(): Flow<Boolean> = flowOf(isLoggedIn)
}
