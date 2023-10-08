package com.nicholas.rutherford.track.my.shot.firebase.core.update

import com.nicholas.rutherford.track.my.shot.firebase.realtime.PlayerInfoRealtimeWithKeyResponse
import kotlinx.coroutines.flow.Flow

interface UpdateFirebaseUserInfo {
    fun updatePlayer(accountKey: String, playerInfoRealtimeWithKeyResponse: PlayerInfoRealtimeWithKeyResponse): Flow<Boolean>
}
