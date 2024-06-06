package com.nicholas.rutherford.track.your.shot.feature.players.playerlist

import android.app.Application
import androidx.lifecycle.ViewModel
import com.nicholas.rutherford.track.your.shot.data.room.repository.ActiveUserRepository
import com.nicholas.rutherford.track.your.shot.data.room.repository.PendingPlayerRepository
import com.nicholas.rutherford.track.your.shot.data.room.repository.PlayerRepository
import com.nicholas.rutherford.track.your.shot.data.room.response.Player
import com.nicholas.rutherford.track.your.shot.data.room.response.fullName
import com.nicholas.rutherford.track.your.shot.data.shared.alert.Alert
import com.nicholas.rutherford.track.your.shot.data.shared.alert.AlertConfirmAndDismissButton
import com.nicholas.rutherford.track.your.shot.data.shared.progress.Progress
import com.nicholas.rutherford.track.your.shot.feature.players.PlayersAdditionUpdates
import com.nicholas.rutherford.track.your.shot.feature.splash.StringsIds
import com.nicholas.rutherford.track.your.shot.firebase.core.delete.DeleteFirebaseUserInfo
import com.nicholas.rutherford.track.your.shot.helper.account.AccountManager
import com.nicholas.rutherford.track.your.shot.helper.network.Network
import com.nicholas.rutherford.track.your.shot.shared.preference.create.CreateSharedPreferences
import com.nicholas.rutherford.track.your.shot.shared.preference.read.ReadSharedPreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

const val DELETE_PLAYER_DELAY_IN_MILLIS = 2000L

class PlayersListViewModel(
    private val application: Application,
    private val scope: CoroutineScope,
    private val navigation: PlayersListNavigation,
    private val network: Network,
    private val accountManager: AccountManager,
    private val deleteFirebaseUserInfo: DeleteFirebaseUserInfo,
    private val activeUserRepository: ActiveUserRepository,
    private val playersAdditionUpdates: PlayersAdditionUpdates,
    private val playerRepository: PlayerRepository,
    private val pendingPlayerRepository: PendingPlayerRepository,
    private val createSharedPreferences: CreateSharedPreferences,
    private val readSharedPreferences: ReadSharedPreferences
) : ViewModel() {

    internal var currentPlayerArrayList: ArrayList<Player> = arrayListOf()

    internal val playerListMutableStateFlow = MutableStateFlow(value = PlayersListState())
    val playerListStateFlow = playerListMutableStateFlow.asStateFlow()

    init {
        updatePlayerListState()
        collectPlayerAdditionUpdates()
        collectLoggedInPlayerListStateFlow()
        deleteAllNonEmptyPendingPlayers()
    }

    fun updatePlayerListState() {
        scope.launch {
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
            playersAdditionUpdates.newPlayerHasBeenAddedSharedFlow.collectLatest { hasBeenAdded ->
                handlePlayerAdded(hasBeenAdded = hasBeenAdded)
            }
        }
    }

    private fun collectLoggedInPlayerListStateFlow() {
        scope.launch {
            accountManager.loggedInPlayerListStateFlow.collectLatest { loggedInPlayerList ->
                if (shouldUpdateFromUserLoggedIn(loggedInPlayerList = loggedInPlayerList, shouldUpdateLoggedInPlayerListState = readSharedPreferences.shouldUpdateLoggedInPlayerListState())) {
                    handleLoggedInPlayerList(playerList = loggedInPlayerList)
                    createSharedPreferences.createShouldUpdateLoggedInPlayerListPreference(value = false)
                }
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

    suspend fun onYesDeletePlayerClicked(player: Player) {
        enableProgressAndDelay()
        deletePlayer(player = player)
    }

    internal suspend fun enableProgressAndDelay() {
        navigation.enableProgress(progress = Progress())
        delay(DELETE_PLAYER_DELAY_IN_MILLIS)
    }

    internal suspend fun deletePlayer(player: Player) {
        if (network.isDeviceConnectedToInternet()) {
            activeUserRepository.fetchActiveUser()?.firebaseAccountInfoKey?.let { accountKey ->
                deleteFirebaseUserInfo.deletePlayer(
                    accountKey = accountKey,
                    playerKey = player.firebaseKey
                )
                    .collectLatest { isSuccessful ->
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
            } ?: run {
                navigation.disableProgress()
                navigation.alert(alert = weHaveDetectedAProblemWithYourAccountAlert())
            }
        } else {
            navigation.disableProgress()
            navigation.alert(alert = notConnectedToInternetAlert())
        }
    }

    fun onEditPlayerClicked(player: Player) = navigation.navigateToCreateEditPlayer(firstName = player.firstName, lastName = player.lastName)

    fun onDeletePlayerClicked(player: Player) = navigation.alert(alert = deletePlayerAlert(player = player))

    private fun deletePlayerAlert(player: Player): Alert {
        return Alert(
            title = application.getString(StringsIds.deleteX, player.fullName()),
            confirmButton = AlertConfirmAndDismissButton(
                buttonText = application.getString(StringsIds.yes),
                onButtonClicked = { scope.launch { onYesDeletePlayerClicked(player = player) } }
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
            dismissButton = AlertConfirmAndDismissButton(buttonText = application.getString(StringsIds.gotIt))
        )
    }

    private fun weHaveDetectedAProblemWithYourAccountAlert(): Alert {
        return Alert(
            title = application.getString(StringsIds.empty),
            description = application.getString(StringsIds.weHaveDetectedAProblemWithYourAccountPleaseContactSupportToResolveIssue),
            dismissButton = AlertConfirmAndDismissButton(buttonText = application.getString(StringsIds.gotIt))
        )
    }

    private fun unableToDeletePlayerAlert(): Alert {
        return Alert(
            title = application.getString(StringsIds.empty),
            description = application.getString(StringsIds.unableToDeletePlayerPleaseContactSupport),
            dismissButton = AlertConfirmAndDismissButton(buttonText = application.getString(StringsIds.gotIt))
        )
    }
}
