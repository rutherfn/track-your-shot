package com.nicholas.rutherford.track.your.shot.feature.players.shots.logshot.pendingshot

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class CurrentPendingShotImpl : CurrentPendingShot {

    internal val shotsMutableStateFlow: MutableStateFlow<List<PendingShot>> = MutableStateFlow(value = emptyList())
    override val shotsStateFlow: Flow<List<PendingShot>> = shotsMutableStateFlow.asStateFlow()

    override fun fetchPendingShots(): List<PendingShot> = shotsMutableStateFlow.value

    override fun createShot(shotLogged: PendingShot) {
        val currentShotList = shotsMutableStateFlow.value
        shotsMutableStateFlow.value = currentShotList + listOf(shotLogged)
    }

    override fun deleteShot(shotLogged: PendingShot) {
        val filterShotArrayList: ArrayList<PendingShot> = arrayListOf()

        shotsMutableStateFlow.value.forEach { shot ->
            if (shot != shotLogged) {
                filterShotArrayList.add(shot)
            }
        }

        shotsMutableStateFlow.value = filterShotArrayList.toList()
    }

    override fun clearShotList() {
        shotsMutableStateFlow.value = emptyList()
    }
}
