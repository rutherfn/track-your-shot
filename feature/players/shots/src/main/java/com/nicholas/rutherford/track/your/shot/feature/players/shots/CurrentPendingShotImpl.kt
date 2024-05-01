package com.nicholas.rutherford.track.your.shot.feature.players.shots

import com.nicholas.rutherford.track.your.shot.data.room.response.ShotLogged
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class CurrentPendingShotImpl : CurrentPendingShot {

    private val shotsMutableStateFlow: MutableStateFlow<List<ShotLogged>> = MutableStateFlow(value = emptyList())
    override val shotsStateFlow: Flow<List<ShotLogged>> = shotsMutableStateFlow.asStateFlow()

    override fun createShot(shotLogged: ShotLogged) {
        val currentShotList = shotsMutableStateFlow.value
        shotsMutableStateFlow.value = currentShotList + listOf(shotLogged)
    }

    override fun clearShotList() {
        shotsMutableStateFlow.value = emptyList()
    }
}