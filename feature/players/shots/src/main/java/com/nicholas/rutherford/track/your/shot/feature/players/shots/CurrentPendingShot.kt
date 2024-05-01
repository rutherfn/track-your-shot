package com.nicholas.rutherford.track.your.shot.feature.players.shots

import com.nicholas.rutherford.track.your.shot.data.room.response.ShotLogged
import kotlinx.coroutines.flow.Flow

interface CurrentPendingShot {
    val shotsStateFlow: Flow<List<ShotLogged>>

    fun createShot(shotLogged: ShotLogged)
    fun clearShotList()
}