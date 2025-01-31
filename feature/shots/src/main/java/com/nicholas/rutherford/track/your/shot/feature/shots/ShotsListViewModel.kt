package com.nicholas.rutherford.track.your.shot.feature.shots

import com.nicholas.rutherford.track.your.shot.base.vm.BaseViewModel
import com.nicholas.rutherford.track.your.shot.data.room.repository.PlayerRepository
import com.nicholas.rutherford.track.your.shot.data.room.response.ShotLogged
import com.nicholas.rutherford.track.your.shot.data.room.response.fullName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ShotsListViewModel(
    private val scope: CoroutineScope,
    private val playerRepository: PlayerRepository,
) : BaseViewModel() {

    internal var currentShotArrayList: ArrayList<ShotLoggedWithPlayer> = arrayListOf()

    internal val shotListMutableStateFlow = MutableStateFlow(value = ShotsListState())
    val shotListStateFlow = shotListMutableStateFlow.asStateFlow()

    override fun onNavigatedTo() {
        super.onNavigatedTo()
        updateShotListState()
    }

    fun updateShotListState() {
        scope.launch {
            currentShotArrayList.clear()
            playerRepository.fetchAllPlayers().forEach { player ->
                player.shotsLoggedList.forEach { shotLogged ->
                    currentShotArrayList.add(ShotLoggedWithPlayer(shotLogged = shotLogged, playerName = player.fullName()))
                }
            }
            println("here is the current shot array list $currentShotArrayList")
            shotListMutableStateFlow.update { state -> state.copy(shotList = currentShotArrayList.toList()) }
        }
    }
}