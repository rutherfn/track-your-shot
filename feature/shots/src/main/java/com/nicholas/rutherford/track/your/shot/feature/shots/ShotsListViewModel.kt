package com.nicholas.rutherford.track.your.shot.feature.shots

import com.nicholas.rutherford.track.your.shot.base.vm.BaseViewModel
import com.nicholas.rutherford.track.your.shot.data.room.repository.PlayerRepository
import com.nicholas.rutherford.track.your.shot.data.room.response.fullName
import com.nicholas.rutherford.track.your.shot.data.shared.alert.Alert
import com.nicholas.rutherford.track.your.shot.data.shared.alert.AlertConfirmAndDismissButton
import com.nicholas.rutherford.track.your.shot.shared.preference.create.CreateSharedPreferences
import com.nicholas.rutherford.track.your.shot.shared.preference.read.ReadSharedPreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel for the Shots List screen, responsible for managing and presenting the list of shots
 * logged by players. It handles filtering by player name, collecting updates, and navigating between screens.
 *
 * @property scope CoroutineScope used for asynchronous operations.
 * @property navigation Interface for handling navigation events from the Shots List screen.
 * @property playerRepository Repository used to fetch player and shot information from local storage.
 * @property createSharedPreferences Interface for writing to shared preferences.
 * @property readSharedPreferences Interface for reading from shared preferences.
 */
class ShotsListViewModel(
    private val scope: CoroutineScope,
    private val navigation: ShotsListNavigation,
    private val playerRepository: PlayerRepository,
    private val createSharedPreferences: CreateSharedPreferences,
    private val readSharedPreferences: ReadSharedPreferences
) : BaseViewModel() {

    /** Name of the player currently being filtered. */
    var playerFilteredName = ""

    /** List of shots logged by players. */
    internal var currentShotArrayList: ArrayList<ShotLoggedWithPlayer> = arrayListOf()

    internal val shotListMutableStateFlow = MutableStateFlow(value = ShotsListState())

    /** State flow representing the current state of the shots list. */
    val shotListStateFlow = shotListMutableStateFlow.asStateFlow()

    init {
        checkToCreatePlayerFilterName()
        scope.launch { updateShotListState() }
    }


    /**
     * Checks to see if a player filter name exists.
     * If it does, it creates a new one.
     */
    internal fun checkToCreatePlayerFilterName() {
        playerFilteredName = readSharedPreferences.playerFilterName()
        if (playerFilteredName.isNotEmpty()) {
            createSharedPreferences.createPlayerFilterName(value = "")
        }
    }

    /**
     * Filters the list of shots by the filtered player name.
     *
     * @param shotList The complete list of shots.
     * @return A filtered list of shots that belong to the player with the filtered name.
     */
    internal fun filterShotList(shotList: List<ShotLoggedWithPlayer>): List<ShotLoggedWithPlayer> {
        return shotList.filterNot { it.playerName != playerFilteredName }
    }

    /**
     * Updates the internal state with the current list of shots for all players.
     * Applies filtering based on the player's name if applicable.
     */
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

    /**
     * Handles the click event on the toolbar menu button.
     * If filtering is not applied, it opens the navigation drawer; otherwise, it returns to the player list.
     */
    fun onToolbarMenuClicked() {
        if (playerFilteredName.isEmpty()) {
            navigation.openNavigationDrawer()
        } else {
            navigation.popToPlayerList()
        }
    }

    /**
     * Handles the event when a shot item is clicked.
     * Navigates to the Log Shot screen with the relevant shot and player information.
     *
     * @param shotLoggedWithPlayer The selected shot and associated player information.
     */
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

    /**
     * Builds a static informational alert about the shot list screen.
     *
     * @return An [Alert] containing a title, description, and dismiss button.
     *
     * todo -> Get rid of this once we add filter functionality
     *
     * Not testing since it will be removed in the future once we add filter functionality
     */
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

    /**
     * Called when the help icon is clicked.
     * Displays an informational alert about the screen's purpose.
     *
     * todo -> Get rid of this once we add filter functionality
     */
    fun onHelpClicked() {
        navigation.alert(alert = buildHelpAlert())
    }
}