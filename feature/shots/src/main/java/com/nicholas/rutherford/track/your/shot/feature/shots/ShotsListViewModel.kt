package com.nicholas.rutherford.track.your.shot.feature.shots

import com.nicholas.rutherford.track.your.shot.base.vm.BaseViewModel
import com.nicholas.rutherford.track.your.shot.data.room.repository.PlayerRepository
import com.nicholas.rutherford.track.your.shot.data.room.response.fullName
import com.nicholas.rutherford.track.your.shot.helper.extensions.dataadditionupdates.DataAdditionUpdates
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ShotsListViewModel(
    private val scope: CoroutineScope,
    private val navigation: ShotsListNavigation,
    private val dataAdditionUpdates: DataAdditionUpdates,
    private val playerRepository: PlayerRepository
) : BaseViewModel() {

    internal var currentShotArrayList: ArrayList<ShotLoggedWithPlayer> = arrayListOf()

    internal val shotListMutableStateFlow = MutableStateFlow(value = ShotsListState())
    val shotListStateFlow = shotListMutableStateFlow.asStateFlow()

    init {
        scope.launch { collectShotHasBeenUpdatedSharedFlow() }
    }

    override fun onNavigatedTo() {
        super.onNavigatedTo()
        updateShotListState()
    }

    private suspend fun collectShotHasBeenUpdatedSharedFlow() {
        dataAdditionUpdates.shotHasBeenUpdatedSharedFlow.collectLatest { hasBeenUpdated ->
            println("here is has been updated $hasBeenUpdated")
            if (hasBeenUpdated) {
                updateShotListState()
            }
        }
    }

    private fun updateShotListState() {
        scope.launch {
            currentShotArrayList.clear()

            playerRepository.fetchAllPlayers().flatMap { player ->
                player.shotsLoggedList.map { shotLogged ->
                    ShotLoggedWithPlayer(
                        shotLogged = shotLogged,
                        playerId = playerRepository.fetchPlayerIdByName(firstName = player.firstName, lastName = player.lastName) ?: 0,
                        playerName = player.fullName()
                    )
                }
            }.let { updatedShotList ->
                currentShotArrayList.addAll(updatedShotList)
            }
            shotListMutableStateFlow.update { it.copy(shotList = currentShotArrayList.toList()) }
        }
    }

    fun onToolbarMenuClicked() = navigation.openNavigationDrawer()

    fun onShotItemClicked(shotLoggedWithPlayer: ShotLoggedWithPlayer) {
        navigation.navigateToLogShot(
            isExistingPlayer = true,
            playerId = shotLoggedWithPlayer.playerId,
            shotType = shotLoggedWithPlayer.shotLogged.shotType,
            shotId = shotLoggedWithPlayer.shotLogged.id,
            viewCurrentExistingShot = true,
            viewCurrentPendingShot = false,
            fromShotList = true
        )
    }
}
