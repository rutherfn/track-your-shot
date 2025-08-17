package com.nicholas.rutherford.track.your.shot.feature.players.shots.selectshot

import android.app.Application
import androidx.lifecycle.SavedStateHandle
import com.nicholas.rutherford.track.your.shot.base.resources.StringsIds
import com.nicholas.rutherford.track.your.shot.base.vm.BaseViewModel
import com.nicholas.rutherford.track.your.shot.data.room.repository.DeclaredShotRepository
import com.nicholas.rutherford.track.your.shot.data.room.repository.PendingPlayerRepository
import com.nicholas.rutherford.track.your.shot.data.room.repository.PlayerRepository
import com.nicholas.rutherford.track.your.shot.data.room.response.DeclaredShot
import com.nicholas.rutherford.track.your.shot.data.room.response.Player
import com.nicholas.rutherford.track.your.shot.data.shared.alert.Alert
import com.nicholas.rutherford.track.your.shot.data.shared.alert.AlertConfirmAndDismissButton
import com.nicholas.rutherford.track.your.shot.helper.constants.Constants
import com.nicholas.rutherford.track.your.shot.helper.extensions.safeLet
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

const val UPDATING_DECLARED_SHOT_LIST_DELAY = 400L

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * ViewModel responsible for managing the selection of a declared shot when logging a shot for a player.
 *
 * This ViewModel handles loading declared shots from the repository, filtering shots based on search input,
 * managing state for the current player context (existing or pending player), and navigation events.
 *
 * @property savedStateHandle Provides access to navigation arguments.
 * @property application Application context to access resources such as string resources.
 * @property scope CoroutineScope used for launching asynchronous tasks.
 * @property navigation Interface for navigation actions related to the select shot screen.
 * @property declaredShotRepository Repository for accessing declared shot data.
 * @property playerRepository Repository for accessing existing player data.
 * @property pendingPlayerRepository Repository for accessing pending (not fully saved) player data.
 */
class SelectShotViewModel(
    savedStateHandle: SavedStateHandle,
    private val application: Application,
    private val scope: CoroutineScope,
    private val navigation: SelectShotNavigation,
    private val declaredShotRepository: DeclaredShotRepository,
    private val playerRepository: PlayerRepository,
    private val pendingPlayerRepository: PendingPlayerRepository
) : BaseViewModel() {

    internal var currentDeclaredShotArrayList: ArrayList<DeclaredShot> = arrayListOf()

    internal val selectShotMutableStateFlow = MutableStateFlow(value = SelectShotState())

    val selectShotStateFlow = selectShotMutableStateFlow.asStateFlow()

    internal var isExistingPlayer: Boolean? = null

    internal var playerId: Int? = null

    /**
     * Navigation argument: whether the player is existing.
     */
    internal val isExistingPlayerArgument: Boolean = savedStateHandle.get<Boolean>("isExistingPlayer") ?: false

    /**
     * Navigation argument: the player ID.
     */
    internal val playerIdArgument: Int? = savedStateHandle.get<Int>("playerId")

    init {
        updateIsExistingPlayerAndPlayerId()
        fetchDeclaredShotsAndUpdateState()
    }

    /**
     * Updates internal variables to reflect whether the player is existing and their ID,
     * based on the navigation arguments.
     */
    fun updateIsExistingPlayerAndPlayerId() {
        this.isExistingPlayer = isExistingPlayerArgument
        this.playerId = playerIdArgument
    }

    /**
     * Fetches all declared shots from the repository and updates the UI state.
     *
     * @param shouldDelay If true, delays the fetch by [UPDATING_DECLARED_SHOT_LIST_DELAY] milliseconds.
     */
    internal fun fetchDeclaredShotsAndUpdateState(shouldDelay: Boolean = false) {
        scope.launch {
            currentDeclaredShotArrayList.clear()
            if (shouldDelay) {
                delay(UPDATING_DECLARED_SHOT_LIST_DELAY)
            }
            currentDeclaredShotArrayList.addAll(declaredShotRepository.fetchAllDeclaredShots())
            selectShotMutableStateFlow.update { state ->
                state.copy(declaredShotList = currentDeclaredShotArrayList)
            }
        }
    }

    /**
     * Handles changes in the search input query.
     * Filters the declared shot list by the search query and updates the UI state.
     *
     * @param newSearchQuery The new search text entered by the user.
     */
    fun onSearchValueChanged(newSearchQuery: String) {
        currentDeclaredShotArrayList.clear()

        scope.launch {
            declaredShotRepository.fetchDeclaredShotsBySearchQuery(searchQuery = newSearchQuery).forEach { declaredShot ->
                currentDeclaredShotArrayList.add(declaredShot)
            }
            selectShotMutableStateFlow.update { state ->
                state.copy(declaredShotList = currentDeclaredShotArrayList)
            }
        }
    }

    /**
     * Handles the cancel icon click in the search bar.
     * Resets the declared shot list to the full list if the search query is not empty.
     *
     * @param query The current search query.
     */
    fun onCancelIconClicked(query: String) {
        if (query.isNotEmpty()) {
            currentDeclaredShotArrayList.clear()
            fetchDeclaredShotsAndUpdateState()
        }
    }

    /**
     * Handles the system back button click.
     * Navigates back differently depending on whether the player is existing or new.
     */
    fun onBackButtonClicked() {
        if (isExistingPlayer == false) {
            navigation.popFromCreatePlayer()
        } else {
            navigation.popFromEditPlayer()
        }
    }

    /**
     * Constructs and returns an informational alert dialog explaining shot selection.
     *
     * @return An [Alert] dialog with title, description, and a dismiss button.
     */
    internal fun moreInfoAlert(): Alert {
        return Alert(
            title = application.getString(StringsIds.selectingAShot),
            dismissButton = AlertConfirmAndDismissButton(
                buttonText = application.getString(StringsIds.gotIt)
            ),
            description = application.getString(StringsIds.chooseAShotToLogInfoDescription)
        )
    }

    /**
     * Handles clicks on the help icon by showing the more info alert.
     */
    fun onHelpIconClicked() = navigation.alert(alert = moreInfoAlert())

    /**
     * Determines the next shot ID to assign when logging a new shot for the given player.
     *
     * @param player The player for whom the shot is being logged.
     * @return The next shot ID to use, or a default constant if no shots exist.
     */
    internal fun determineShotId(player: Player): Int {
        return if (player.shotsLoggedList.isNotEmpty()) {
            player.shotsLoggedList.size
        } else {
            Constants.DEFAULT_SHOT_ID
        }
    }

    /**
     * Retrieves the next shot ID for a player identified by the existing status and player ID.
     *
     * @param isExistingPlayer True if the player exists in the main repository, false if pending.
     * @param playerId The ID of the player.
     * @return The next shot ID for logging.
     */
    internal suspend fun loggedShotId(isExistingPlayer: Boolean, playerId: Int): Int {
        return if (isExistingPlayer) {
            playerRepository.fetchPlayerById(playerId)?.let { player ->
                determineShotId(player = player)
            } ?: Constants.DEFAULT_SHOT_ID
        } else {
            pendingPlayerRepository.fetchPlayerById(id = playerId)?.let { player ->
                determineShotId(player = player)
            } ?: Constants.DEFAULT_SHOT_ID
        }
    }

    /**
     * Handles the event when a declared shot is selected by the user.
     * Navigates to the Log Shot screen passing relevant player and shot details.
     *
     * @param shotType The type ID of the declared shot selected.
     */
    fun onDeclaredShotItemClicked(shotType: Int) {
        safeLet(isExistingPlayer, playerId) { isExisting, id ->
            scope.launch {
                navigation.navigateToLogShot(
                    isExistingPlayer = isExisting,
                    playerId = id,
                    shotType = shotType,
                    shotId = loggedShotId(isExistingPlayer = isExisting, playerId = id),
                    viewCurrentExistingShot = false,
                    viewCurrentPendingShot = false,
                    fromShotList = false
                )
            }
        }
    }
}
