package com.nicholas.rutherford.track.your.shot.firebase.core.update

import com.nicholas.rutherford.track.your.shot.firebase.realtime.PlayerInfoRealtimeWithKeyResponse
import kotlinx.coroutines.flow.Flow

interface UpdateFirebaseUserInfo {
    fun updatePlayer(playerInfoRealtimeWithKeyResponse: PlayerInfoRealtimeWithKeyResponse): Flow<Boolean>
}
