package com.nicholas.rutherford.track.your.shot.feature.players.shots.logshot.pendingshot

import kotlinx.coroutines.flow.Flow

interface CurrentPendingShot {
    val shotsStateFlow: Flow<List<PendingShot>>

    fun fetchPendingShots(): List<PendingShot>
    fun createShot(shotLogged: PendingShot)
    fun deleteShot(shotLogged: PendingShot)
    fun clearShotList()
}
