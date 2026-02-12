package com.nicholas.rutherford.track.your.shot.feature.shots

import com.nicholas.rutherford.track.your.shot.base.vm.BaseViewModel
import com.nicholas.rutherford.track.your.shot.data.room.repository.PlayerRepository
import com.nicholas.rutherford.track.your.shot.data.room.response.fullName
import com.nicholas.rutherford.track.your.shot.data.shared.alert.Alert
import com.nicholas.rutherford.track.your.shot.data.shared.alert.AlertConfirmAndDismissButton
import com.nicholas.rutherford.track.your.shot.data.store.reader.DataStorePreferencesReader
import com.nicholas.rutherford.track.your.shot.data.store.writer.DataStorePreferencesWriter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * ViewModel for the Shots List screen, responsible for managing and presenting the list of shots
 * logged by players. It handles filtering by player name and searching by shot name, collecting updates, and navigating between screens.
 *
 * @property scope CoroutineScope used for asynchronous operations.
 * @property navigation Interface for handling navigation events from the Shots List screen.
 * @property playerRepository Repository used to fetch player and shot information from local storage.
 * @property dataStorePreferencesWriter Writer for writing data to DataStore preferences.
 * @property dataStorePreferencesReader Reader for reading data from DataStore preferences.
 */
class ShotsListViewModel(
    private val scope: CoroutineScope,
    private val navigation: ShotsListNavigation,
    private val playerRepository: PlayerRepository,
    private val dataStorePreferencesWriter: DataStorePreferencesWriter,
    private val dataStorePreferencesReader: DataStorePreferencesReader
) : BaseViewModel() {

    internal val shotListMutableStateFlow = MutableStateFlow(value = ShotsListState())

    /** State flow representing the current state of the shots list. */
    val shotListStateFlow = shotListMutableStateFlow.asStateFlow()

    init {
        scope.launch { checkToCreatePlayerFilterName() }
        scope.launch { updateShotListState() }
    }

    /**
     * Checks to see if a player filter name exists.
     * If it does, it updates the filter and re-filters the shot list.
     */
    internal suspend fun checkToCreatePlayerFilterName() {
        dataStorePreferencesReader.readPlayerFilterNameFlow().collectLatest { filterName ->
            shotListMutableStateFlow.update { state ->
                state.copy(playerFilteredName = filterName)
            }
            if (filterName.isNotEmpty()) {
                dataStorePreferencesWriter.savePlayerFilterName(value = "")
                updateShotListState()
            }
        }
    }

    /**
     * Filters the list of shots by the filtered player name.
     *
     * @param shotList The complete list of shots.
     * @param playerFilteredName The name of the player to filter by.
     * @return A filtered list of shots that belong to the player with the filtered name.
     */
    internal fun filterShotList(shotList: List<ShotLoggedWithPlayer>, playerFilteredName: String): List<ShotLoggedWithPlayer> {
        return shotList.filterNot { it.playerName != playerFilteredName }
    }

    /**
     * Updates the internal state with the current list of shots for all players.
     * Applies filtering based on the player's name if applicable.
     */
    internal suspend fun updateShotListState() {
        val allShots = playerRepository.fetchAllPlayers().flatMap { player ->
            player.shotsLoggedList.map { shotLogged ->
                ShotLoggedWithPlayer(
                    shotLogged = shotLogged,
                    playerId = playerRepository.fetchPlayerIdByName(firstName = player.firstName, lastName = player.lastName) ?: 0,
                    playerName = player.fullName()
                )
            }
        }

        val currentState = shotListMutableStateFlow.value

        val filteredShots = if (currentState.playerFilteredName.isEmpty()) {
            allShots
        } else {
            filterShotList(shotList = allShots, playerFilteredName = currentState.playerFilteredName)
        }

        shotListMutableStateFlow.update { state ->
            state.copy(shotList = filteredShots)
        }
    }

    /** Navigation event to open the shot filters screen */
    fun onFilterChipClicked() {
    }

    /**
     * Handles search text changes from the search field.
     * Filters the shot list by shot name based on the search query.
     *
     * @param searchQuery The search query entered by the user.
     */
    fun onSearchTextChanged(searchQuery: String) {
        scope.launch {
            if (searchQuery.isEmpty()) {
                updateShotListState()
                shotListMutableStateFlow.update { state -> state.copy(searchQuery = searchQuery) }
            } else {
                val queriedPlayers = playerRepository.fetchPlayersByShotNameQuery(query = searchQuery)
                val queriedShots = queriedPlayers.flatMap { player ->
                    player.shotsLoggedList
                        .filter { shotLogged -> shotLogged.shotName.lowercase().contains(searchQuery.lowercase().trim()) }
                        .map { shotLogged ->
                            ShotLoggedWithPlayer(
                                shotLogged = shotLogged,
                                playerId = playerRepository.fetchPlayerIdByName(firstName = player.firstName, lastName = player.lastName) ?: 0,
                                playerName = player.fullName()
                            )
                        }
                }

                val currentState = shotListMutableStateFlow.value
                val filteredShots = if (currentState.playerFilteredName.isEmpty()) {
                    queriedShots
                } else {
                    filterShotList(shotList = queriedShots, playerFilteredName = currentState.playerFilteredName)
                }

                shotListMutableStateFlow.update { state ->
                    state.copy(
                        searchQuery = searchQuery,
                        shotList = filteredShots
                    )
                }
            }
        }
    }

    /**
     * Handles the click event on the toolbar menu button.
     * If filtering is not applied, it opens the navigation drawer; otherwise, it returns to the player list.
     */
    fun onToolbarMenuClicked(shouldShowAllPlayerShots: Boolean) {
        val currentState = shotListMutableStateFlow.value

        if (currentState.playerFilteredName.isEmpty() && shouldShowAllPlayerShots) {
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
    private fun buildHelpAlert(): Alert {
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
