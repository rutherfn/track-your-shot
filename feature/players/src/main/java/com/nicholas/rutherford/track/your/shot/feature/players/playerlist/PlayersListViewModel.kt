package com.nicholas.rutherford.track.your.shot.feature.players.playerlist

import android.app.Application
import com.nicholas.rutherford.track.your.shot.base.resources.StringsIds
import com.nicholas.rutherford.track.your.shot.base.vm.BaseViewModel
import com.nicholas.rutherford.track.your.shot.data.room.repository.PendingPlayerRepository
import com.nicholas.rutherford.track.your.shot.data.room.repository.PlayerRepository
import com.nicholas.rutherford.track.your.shot.data.room.response.Player
import com.nicholas.rutherford.track.your.shot.data.room.response.PlayerPositions
import com.nicholas.rutherford.track.your.shot.data.room.response.fullName
import com.nicholas.rutherford.track.your.shot.data.shared.alert.Alert
import com.nicholas.rutherford.track.your.shot.data.shared.alert.AlertConfirmAndDismissButton
import com.nicholas.rutherford.track.your.shot.data.shared.progress.Progress
import com.nicholas.rutherford.track.your.shot.firebase.core.delete.DeleteFirebaseUserInfo
import com.nicholas.rutherford.track.your.shot.shared.preference.create.CreateSharedPreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

const val DELETE_PLAYER_DELAY_IN_MILLIS = 2000L

/**
 * ViewModel for managing the state and business logic of the Players List screen.
 *
 * This ViewModel handles data operations related to the player list such as loading players,
 * managing the selection of players, building sheet options for each player, handling deletion logic,
 * and coordinating navigation to other screens like shot list and player creation/editing.
 *
 * @property application Provides access to application-level resources.
 * @property scope The coroutine scope used for asynchronous operations.
 * @property navigation Defines navigation actions for the Players List screen.
 * @property deleteFirebaseUserInfo Handles deletion of player data from Firebase.
 * @property playerRepository Repository for accessing and modifying player data.
 * @property pendingPlayerRepository Repository for managing temporary/pending players.
 * @property createSharedPreferences Used to store and retrieve shared preference values.
 */
class PlayersListViewModel(
    private val application: Application,
    private val scope: CoroutineScope,
    private val navigation: PlayersListNavigation,
    private val deleteFirebaseUserInfo: DeleteFirebaseUserInfo,
    private val playerRepository: PlayerRepository,
    private val pendingPlayerRepository: PendingPlayerRepository,
    private val createSharedPreferences: CreateSharedPreferences
) : BaseViewModel() {

    internal var selectedPlayer: Player = Player(
        firstName = "",
        lastName = "",
        position = PlayerPositions.None,
        firebaseKey = "",
        imageUrl = "",
        shotsLoggedList = emptyList()
    )

    internal var currentPlayerArrayList: ArrayList<Player> = arrayListOf()

    internal val playerListMutableStateFlow = MutableStateFlow(value = PlayersListState())

    val playerListStateFlow = playerListMutableStateFlow.asStateFlow()

    init {
        updatePlayerListState()
        deleteAllNonEmptyPendingPlayers()
    }

    /** Loads all players from the repository and updates the state */
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

    /** Builds sheet options based on whether the selected player has logged shots or not */
    internal fun buildSheetOptions(selectedPlayer: Player): List<String> {
        val selectedPlayerFullName = selectedPlayer.fullName()
        val baseSheetOptions = buildBaseSheetOptions(selectedPlayerFullName)

        return if (selectedPlayer.shotsLoggedList.isEmpty()) {
            baseSheetOptions
        } else {
            buildAddViewShotsOption(selectedPlayerFullName)
        }
    }

    /** Base edit/delete options for a player */
    private fun buildBaseSheetOptions(selectedPlayerFullName: String): List<String> = listOf(
        application.getString(StringsIds.editX, selectedPlayerFullName),
        application.getString(StringsIds.deleteX, selectedPlayerFullName)
    )

    /** Adds the "View Shots" option if the player has logged any shots */
    private fun buildAddViewShotsOption(selectedPlayerFullName: String): List<String> =
        listOf(application.getString(StringsIds.viewXShots, selectedPlayerFullName)) +
                buildBaseSheetOptions(selectedPlayerFullName)

    /** Deletes all non-empty pending players from local storage */
    internal fun deleteAllNonEmptyPendingPlayers() {
        scope.launch {
            if (pendingPlayerRepository.fetchAllPendingPlayers().isNotEmpty()) {
                pendingPlayerRepository.deleteAllPendingPlayers()
            }
        }
    }

    /** Checks whether to update the player list state from login */
    internal fun shouldUpdateFromUserLoggedIn(
        loggedInPlayerList: List<Player>,
        shouldUpdateLoggedInPlayerListState: Boolean
    ): Boolean {
        return loggedInPlayerList.isNotEmpty() && shouldUpdateLoggedInPlayerListState
    }

    /** Updates the UI state with players from login */
    internal fun handleLoggedInPlayerList(playerList: List<Player>) {
        currentPlayerArrayList.clear()
        currentPlayerArrayList.addAll(playerList)
        playerListMutableStateFlow.value =
            PlayersListState(playerList = currentPlayerArrayList.toList())
    }

    /** Navigation event to open the drawer menu */
    fun onToolbarMenuClicked() = navigation.openNavigationDrawer()

    /** Navigation event to open the create/edit player screen */
    fun onAddPlayerClicked() {
        navigation.navigateToCreateEditPlayer(firstName = null, lastName = null)
    }

    /** Deletes the player after confirming and showing progress */
    suspend fun onYesDeletePlayerClicked(isConnectedToInternet: Boolean, player: Player) {
        enableProgressAndDelay()
        deletePlayer(isConnectedToInternet, player)
    }

    /** Enables a progress indicator and waits before deletion */
    internal suspend fun enableProgressAndDelay() {
        navigation.enableProgress(progress = Progress())
        delay(DELETE_PLAYER_DELAY_IN_MILLIS)
    }

    /** Deletes the player either locally or remotely depending on internet status */
    internal suspend fun deletePlayer(isConnectedToInternet: Boolean, player: Player) {
        if (isConnectedToInternet) {
            deleteFirebaseUserInfo.deletePlayer(player.firebaseKey).collectLatest { isSuccessful ->
                if (isSuccessful) {
                    playerRepository.deletePlayerByName(player.firstName, player.lastName)
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

    /** Updates the selected player and shows sheet options */
    fun onPlayerClicked(player: Player) {
        selectedPlayer = player
        playerListMutableStateFlow.update {
            it.copy(
                selectedPlayer = player,
                sheetOptions = buildSheetOptions(player)
            )
        }
    }

    /** Handles sheet item actions based on index and player shot count */
    fun onSheetItemClicked(isConnectedToInternet: Boolean, index: Int) {
        if (selectedPlayer.shotsLoggedList.isEmpty()) {
            handleSheetItemClickForEmptyPlayerList(isConnectedToInternet, index)
        } else {
            handleSheetItemClickForPlayerList(isConnectedToInternet, index)
        }
    }

    private fun handleSheetItemClickForEmptyPlayerList(isConnectedToInternet: Boolean, index: Int) {
        if (index == 0) {
            onEditPlayerClicked(player = selectedPlayer)
        } else {
            onDeletePlayerClicked(isConnectedToInternet, selectedPlayer)
        }
    }

    private fun handleSheetItemClickForPlayerList(isConnectedToInternet: Boolean, index: Int) {
        when (index) {
            0 -> onShotListClicked(selectedPlayer.fullName())
            1 -> onEditPlayerClicked(selectedPlayer)
            else -> onDeletePlayerClicked(isConnectedToInternet, selectedPlayer)
        }
    }

    /** Navigates to shot list after saving selected player name */
    internal fun onShotListClicked(playerName: String) {
        createSharedPreferences.createPlayerFilterName(value = playerName)
        navigation.navigateToShotList()
    }

    /** Navigates to the create/edit screen for the specified player */
    internal fun onEditPlayerClicked(player: Player) {
        navigation.navigateToCreateEditPlayer(firstName = player.firstName, lastName = player.lastName)
    }

    /** Triggers alert before confirming deletion of player */
    internal fun onDeletePlayerClicked(isConnectedToInternet: Boolean, player: Player) {
        navigation.alert(alert = deletePlayerAlert(isConnectedToInternet, player))
    }

    /** Alert for confirming deletion of a player */
    private fun deletePlayerAlert(isConnectedToInternet: Boolean, player: Player): Alert {
        return Alert(
            title = application.getString(StringsIds.deleteX, player.fullName()),
            confirmButton = AlertConfirmAndDismissButton(
                buttonText = application.getString(StringsIds.yes),
                onButtonClicked = {
                    scope.launch {
                        onYesDeletePlayerClicked(isConnectedToInternet, player)
                    }
                }
            ),
            dismissButton = AlertConfirmAndDismissButton(buttonText = application.getString(StringsIds.no)),
            description = application.getString(StringsIds.areYouCertainYouWishToRemoveX, player.fullName())
        )
    }

    /** Alert shown when not connected to the internet */
    private fun notConnectedToInternetAlert(): Alert {
        return Alert(
            title = application.getString(StringsIds.notConnectedToInternet),
            description = application.getString(StringsIds.weHaveDetectedCurrentlyNotConnectedToInternetDescription),
            dismissButton = AlertConfirmAndDismissButton(
                buttonText = application.getString(StringsIds.gotIt)
            )
        )
    }

    /** Alert shown when player deletion fails due to internal error */
    private fun unableToDeletePlayerAlert(): Alert {
        return Alert(
            title = application.getString(StringsIds.empty),
            description = application.getString(StringsIds.unableToDeletePlayerPleaseContactSupport),
            dismissButton = AlertConfirmAndDismissButton(
                buttonText = application.getString(StringsIds.gotIt)
            )
        )
    }
}
