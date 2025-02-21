package com.nicholas.rutherford.track.your.shot.feature.players.playerlist

import android.app.Application
import com.nicholas.rutherford.track.your.shot.base.resources.StringsIds
import com.nicholas.rutherford.track.your.shot.base.vm.BaseViewModel
import com.nicholas.rutherford.track.your.shot.data.room.repository.PendingPlayerRepository
import com.nicholas.rutherford.track.your.shot.data.room.repository.PlayerRepository
import com.nicholas.rutherford.track.your.shot.data.room.response.Player
import com.nicholas.rutherford.track.your.shot.data.room.response.fullName
import com.nicholas.rutherford.track.your.shot.data.shared.alert.Alert
import com.nicholas.rutherford.track.your.shot.data.shared.alert.AlertConfirmAndDismissButton
import com.nicholas.rutherford.track.your.shot.data.shared.progress.Progress
import com.nicholas.rutherford.track.your.shot.firebase.core.delete.DeleteFirebaseUserInfo
import com.nicholas.rutherford.track.your.shot.helper.extensions.dataadditionupdates.DataAdditionUpdates
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

const val DELETE_PLAYER_DELAY_IN_MILLIS = 2000L
const val EDIT_PLAYER_OPTION_INDEX = 0

class PlayersListViewModel(
    private val application: Application,
    private val scope: CoroutineScope,
    private val navigation: PlayersListNavigation,
    private val deleteFirebaseUserInfo: DeleteFirebaseUserInfo,
    private val dataAdditionUpdates: DataAdditionUpdates,
    private val playerRepository: PlayerRepository,
    private val pendingPlayerRepository: PendingPlayerRepository
) : BaseViewModel() {

    internal var currentPlayerArrayList: ArrayList<Player> = arrayListOf()

    internal val playerListMutableStateFlow = MutableStateFlow(value = PlayersListState())
    val playerListStateFlow = playerListMutableStateFlow.asStateFlow()

    override fun onNavigatedTo() {
        super.onNavigatedTo()
        updatePlayerListState()
    }

    init {
        updatePlayerListState()
        collectPlayerAdditionUpdates()
        deleteAllNonEmptyPendingPlayers()
    }

    fun updatePlayerListState() {
        scope.launch {
            currentPlayerArrayList.clear()
            playerRepository.fetchAllPlayers().forEach { player ->
                currentPlayerArrayList.add(player)
            }
            playerListMutableStateFlow.value =
                PlayersListState(playerList = currentPlayerArrayList.toList())
        }
    }

    private fun clearAndUpdatePlayerListState() {
        currentPlayerArrayList.clear()
        updatePlayerListState()
    }

    internal fun collectPlayerAdditionUpdates() {
        scope.launch {
            dataAdditionUpdates.newPlayerHasBeenAddedSharedFlow.collectLatest { hasBeenAdded ->
                handlePlayerAdded(hasBeenAdded = hasBeenAdded)
            }
        }
    }

    internal fun deleteAllNonEmptyPendingPlayers() {
        scope.launch {
            if (pendingPlayerRepository.fetchAllPendingPlayers().isNotEmpty()) {
                pendingPlayerRepository.deleteAllPendingPlayers()
            }
        }
    }

    internal fun shouldUpdateFromUserLoggedIn(loggedInPlayerList: List<Player>, shouldUpdateLoggedInPlayerListState: Boolean): Boolean {
        return loggedInPlayerList.isNotEmpty() && shouldUpdateLoggedInPlayerListState
    }

    private fun handlePlayerAdded(hasBeenAdded: Boolean) {
        if (hasBeenAdded) {
            clearAndUpdatePlayerListState()
        }
    }

    internal fun handleLoggedInPlayerList(playerList: List<Player>) {
        currentPlayerArrayList.clear()
        currentPlayerArrayList.addAll(playerList)
        playerListMutableStateFlow.value =
            PlayersListState(playerList = currentPlayerArrayList.toList())
    }

    fun onToolbarMenuClicked() = navigation.openNavigationDrawer()

    fun onAddPlayerClicked() {
        navigation.navigateToCreateEditPlayer(
            firstName = null,
            lastName = null
        )
    }

    suspend fun onYesDeletePlayerClicked(isConnectedToInternet: Boolean, player: Player) {
        enableProgressAndDelay()
        deletePlayer(isConnectedToInternet = isConnectedToInternet, player = player)
    }

    internal suspend fun enableProgressAndDelay() {
        navigation.enableProgress(progress = Progress())
        delay(DELETE_PLAYER_DELAY_IN_MILLIS)
    }

    internal suspend fun deletePlayer(isConnectedToInternet: Boolean, player: Player) {
        if (isConnectedToInternet) {
            deleteFirebaseUserInfo.deletePlayer(playerKey = player.firebaseKey).collectLatest { isSuccessful ->
                if (isSuccessful) {
                    playerRepository.deletePlayerByName(
                        firstName = player.firstName,
                        lastName = player.lastName
                    )
                    currentPlayerArrayList.remove(player)
                    playerListMutableStateFlow.value = PlayersListState(playerList = currentPlayerArrayList.toList())
                    navigation.disableProgress()
                } else {
                    navigation.disableProgress()
                    navigation.alert(alert = unableToDeletePlayerAlert())
                }
            }
        } else {
            navigation.disableProgress()
            navigation.alert(alert = notConnectedToInternetAlert())
        }
    }

    fun onPlayerClicked(player: Player) {
        playerListMutableStateFlow.update { it.copy(selectedPlayer = player) }
    }

    fun onSheetItemClicked(isConnectedToInternet: Boolean, index: Int) {
        val player = playerListMutableStateFlow.value.selectedPlayer

        if (index == EDIT_PLAYER_OPTION_INDEX) {
            onEditPlayerClicked(player = player)
        } else {
            onDeletePlayerClicked(isConnectedToInternet = isConnectedToInternet, player = player)
        }
    }

    internal fun onEditPlayerClicked(player: Player) = navigation.navigateToCreateEditPlayer(firstName = player.firstName, lastName = player.lastName)

    internal fun onDeletePlayerClicked(isConnectedToInternet: Boolean, player: Player) = navigation.alert(alert = deletePlayerAlert(isConnectedToInternet = isConnectedToInternet, player = player))

    private fun deletePlayerAlert(isConnectedToInternet: Boolean, player: Player): Alert {
        return Alert(
            title = application.getString(StringsIds.deleteX, player.fullName()),
            confirmButton = AlertConfirmAndDismissButton(
                buttonText = application.getString(StringsIds.yes),
                onButtonClicked = { scope.launch { onYesDeletePlayerClicked(isConnectedToInternet = isConnectedToInternet, player = player) } }
            ),
            dismissButton = AlertConfirmAndDismissButton(
                buttonText = application.getString(StringsIds.no)
            ),
            description = application.getString(StringsIds.areYouCertainYouWishToRemoveX, player.fullName())
        )
    }

    private fun notConnectedToInternetAlert(): Alert {
        return Alert(
            title = application.getString(StringsIds.notConnectedToInternet),
            description = application.getString(StringsIds.weHaveDetectedCurrentlyNotConnectedToInternetDescription),
            dismissButton = AlertConfirmAndDismissButton(
                buttonText = application.getString(
                    StringsIds.gotIt
                )
            )
        )
    }

    private fun unableToDeletePlayerAlert(): Alert {
        return Alert(
            title = application.getString(StringsIds.empty),
            description = application.getString(StringsIds.unableToDeletePlayerPleaseContactSupport),
            dismissButton = AlertConfirmAndDismissButton(
                buttonText = application.getString(
                    StringsIds.gotIt
                )
            )
        )
    }
}
