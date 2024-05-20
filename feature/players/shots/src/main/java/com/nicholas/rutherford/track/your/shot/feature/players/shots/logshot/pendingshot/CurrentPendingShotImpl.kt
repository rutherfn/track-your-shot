package com.nicholas.rutherford.track.your.shot.feature.players.shots.logshot.pendingshot

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class CurrentPendingShotImpl : CurrentPendingShot {

    private val shotsMutableStateFlow: MutableStateFlow<List<PendingShot>> = MutableStateFlow(value = emptyList())
    override val shotsStateFlow: Flow<List<PendingShot>> = shotsMutableStateFlow.asStateFlow()

    override fun createShot(shotLogged: PendingShot) {
        val currentShotList = shotsMutableStateFlow.value
        shotsMutableStateFlow.value = currentShotList + listOf(shotLogged)
    }

    override fun clearShotList() {
        shotsMutableStateFlow.value = emptyList()
    }
}
