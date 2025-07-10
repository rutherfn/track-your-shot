package com.nicholas.rutherford.track.your.shot.feature.shots

import com.nicholas.rutherford.track.your.shot.base.vm.BaseViewModel
import com.nicholas.rutherford.track.your.shot.data.room.repository.PlayerRepository
import com.nicholas.rutherford.track.your.shot.data.room.response.fullName
import com.nicholas.rutherford.track.your.shot.data.shared.alert.Alert
import com.nicholas.rutherford.track.your.shot.data.shared.alert.AlertConfirmAndDismissButton
import com.nicholas.rutherford.track.your.shot.helper.extensions.dataadditionupdates.DataAdditionUpdates
import com.nicholas.rutherford.track.your.shot.shared.preference.create.CreateSharedPreferences
import com.nicholas.rutherford.track.your.shot.shared.preference.read.ReadSharedPreferences
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
    private val playerRepository: PlayerRepository,
    private val createSharedPreferences: CreateSharedPreferences,
    private val readSharedPreferences: ReadSharedPreferences
) : BaseViewModel() {

    var playerFilteredName = ""
    internal var currentShotArrayList: ArrayList<ShotLoggedWithPlayer> = arrayListOf()

    internal val shotListMutableStateFlow = MutableStateFlow(value = ShotsListState())
    val shotListStateFlow = shotListMutableStateFlow.asStateFlow()

    init {
        collectShotHasBeenUpdatedSharedFlow()
    }

    override fun onNavigatedTo() {
        super.onNavigatedTo()
        playerFilteredName = readSharedPreferences.playerFilterName()
        if (playerFilteredName.isNotEmpty()) {
            createSharedPreferences.createPlayerFilterName(value = "")
        }
        scope.launch { updateShotListState() }
    }

    internal fun collectShotHasBeenUpdatedSharedFlow() {
        scope.launch {
            dataAdditionUpdates.shotHasBeenUpdatedSharedFlow.collectLatest { hasBeenUpdated ->
                if (hasBeenUpdated) {
                    updateShotListState()
                }
                if (currentShotArrayList.isEmpty() && playerFilteredName.isNotEmpty()) {
                    navigation.popToPlayerList()
                }
            }
        }
    }

    internal fun filterShotList(shotList: List<ShotLoggedWithPlayer>): List<ShotLoggedWithPlayer> {
        return shotList.filterNot { it.playerName != playerFilteredName }
    }

    internal suspend fun updateShotListState() {
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
            currentShotArrayList.addAll(
                if (playerFilteredName.isEmpty()) {
                    updatedShotList
                } else {
                    filterShotList(shotList = updatedShotList)
                }
            )
        }
        shotListMutableStateFlow.update { it.copy(shotList = currentShotArrayList.toList()) }
    }

    fun onToolbarMenuClicked() {
        if (playerFilteredName.isEmpty()) {
            navigation.openNavigationDrawer()
        } else {
            navigation.popToPlayerList()
        }
    }

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

    // todo - remove this once we add filter functionality
    fun buildHelpAlert(): Alert {
        return Alert(
            title = "View Shots",
            description = "Access and manage player shot logs with options to create, edit, or delete entries.",
            confirmButton = AlertConfirmAndDismissButton(
                buttonText = "Got It",
                onButtonClicked = {}
            )
        )
    }

    // todo - remove this once we add filter functionality
    fun onHelpClicked() {
        navigation.alert(alert = buildHelpAlert())
    }
}
