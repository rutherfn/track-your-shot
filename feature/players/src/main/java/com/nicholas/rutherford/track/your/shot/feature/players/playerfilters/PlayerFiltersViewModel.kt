package com.nicholas.rutherford.track.your.shot.feature.players.playerfilters

import android.app.Application
import com.nicholas.rutherford.track.your.shot.base.resources.StringsIds
import com.nicholas.rutherford.track.your.shot.base.vm.BaseViewModel
import com.nicholas.rutherford.track.your.shot.data.room.repository.PlayerFilterRepository
import com.nicholas.rutherford.track.your.shot.data.room.repository.PlayerRepository
import com.nicholas.rutherford.track.your.shot.data.room.response.HasShotsLoggedFilter
import com.nicholas.rutherford.track.your.shot.data.room.response.PlayerFilter
import com.nicholas.rutherford.track.your.shot.data.room.response.getFilterCount
import com.nicholas.rutherford.track.your.shot.data.room.response.toHasShotsLoggedFilter
import com.nicholas.rutherford.track.your.shot.data.shared.alert.Alert
import com.nicholas.rutherford.track.your.shot.data.shared.alert.AlertConfirmAndDismissButton
import com.nicholas.rutherford.track.your.shot.data.shared.progress.Progress
import com.nicholas.rutherford.track.your.shot.helper.extensions.date.DateExt
import com.nicholas.rutherford.track.your.shot.helper.extensions.toDateWithTimestampString
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Represents the UI state for the Player Filters Screen.
 *
 * ViewModel responsible for handling the players filter screen logic.
 *
 * Manages state for Player filters which includes filtering of:
 * - Choosing different basketball player positions
 * - Choosing if the shot hs or has not shot logged
 * - Number of shots logged via the min shots and max shots logged to give us a filtering for it
 *
 * @param scope CoroutineScope for launching asynchronous tasks.
 * @param application Application context used for resource access.
 * @param navigation Interface to perform navigation and UI alert actions.
 * @param playerFilterRepository Repository to access and update player filter data locally.
 * @param playerRepository Repository to access and update player data locally.
 * @param dateExt Extension functions for handling dates.
 *
 */
class PlayerFiltersViewModel(
    private val scope: CoroutineScope,
    private val application: Application,
    private val navigation: PlayerFiltersNavigation,
    private val playerFilterRepository: PlayerFilterRepository,
    private val playerRepository: PlayerRepository,
    private val dateExt: DateExt
) : BaseViewModel() {

    /**
     * Mutable state flow holding the UI state for player filters screen
     */
    internal val playerFiltersMutableStateFlow = MutableStateFlow(value = PlayerFiltersState())
    val playerFilterStateFlow = playerFiltersMutableStateFlow.asStateFlow()

    /** The initial state of the [PlayerFilter] pulled directly from the database*/
    internal var initialFilter = PlayerFilter()

    /** A copy of initially the [initialFilter] but will get updated based on UI interactions which then be used to override the current filter.*/
    internal var pendingFilter = PlayerFilter()

    init {
        initializePlayerFilterState()
    }

    /** Returns alert when a user is attempting to cancel there given player filters. */
    fun buildAreYouSureYouWantToCancelFilters(): Alert =
        Alert(
            title = application.getString(StringsIds.discardFilterChanges),
            description = application.getString(StringsIds.discardFilterChangesDescription),
            confirmButton = AlertConfirmAndDismissButton(
                buttonText = application.getString(StringsIds.yes),
                onButtonClicked = { navigation.pop() }
            ),
            dismissButton = AlertConfirmAndDismissButton(
                buttonText = application.getString(StringsIds.no),
                onButtonClicked = {}
            )
        )

    /** Returns alert when a user wants to clear the shot range filters. */
    fun buildClearShotRangeAlert(): Alert =
        Alert(
            title = application.getString(StringsIds.clearShotRange),
            description = application.getString(StringsIds.clearShotRangeDescription),
            confirmButton = AlertConfirmAndDismissButton(
                buttonText = application.getString(StringsIds.yes),
                onButtonClicked = { onClearShotRangeConfirmed() }
            ),
            dismissButton = AlertConfirmAndDismissButton(
                buttonText = application.getString(StringsIds.no),
                onButtonClicked = {}
            )
        )

    /**
     * Called when the user clicks the "Clear" button in the shot range section.
     * Shows an alert asking for confirmation before clearing the shot range.
     */
    fun onClearShotRangeClicked() = navigation.alert(alert = buildClearShotRangeAlert())

    /**
     * Clears the minimum and maximum shot values to their defaults (null/0).
     * Called when the user confirms the clear action in the alert.
     */
    internal fun onClearShotRangeConfirmed() {
        scope.launch {
            pendingFilter = pendingFilter.copy(
                minShots = null,
                maxShots = null
            )
            updatePlayerFilterState()
        }
    }

    /** Returns alert when a user wants to reset all filters to default state. */
    fun buildResetFiltersAlert(): Alert =
        Alert(
            title = application.getString(StringsIds.resetFilters),
            description = application.getString(StringsIds.resetFiltersDescription),
            confirmButton = AlertConfirmAndDismissButton(
                buttonText = application.getString(StringsIds.yes),
                onButtonClicked = { onResetFiltersConfirmed() }
            ),
            dismissButton = AlertConfirmAndDismissButton(
                buttonText = application.getString(StringsIds.no),
                onButtonClicked = {}
            )
        )

    /**
     * Called when the user clicks the "Reset Filters" button.
     * Shows an alert asking for confirmation before resetting all filters.
     */
    fun onResetFiltersClicked() = navigation.alert(alert = buildResetFiltersAlert())

    /**
     * Resets all filters to their default state:
     * - No player positions selected
     * - hasShotsLogged set to Both
     * - minShots and maxShots set to null
     * Saves the reset filter to the database and updates the UI state.
     */
    internal fun onResetFiltersConfirmed() {
        scope.launch {
            navigation.enableProgress(progress = Progress())

            pendingFilter = PlayerFilter(
                hasShotsLogged = HasShotsLoggedFilter.Both,
                minShots = null,
                maxShots = null,
                selectedPositions = emptyList(),
                lastUpdatedValue = dateExt.now.toDateWithTimestampString()
            )

            playerFilterRepository.saveActiveFilter(filter = pendingFilter)

            initialFilter = pendingFilter

            updatePlayerFilterState()

            navigation.disableProgress()
        }
    }

    /**
     * Triggers when the user clicks the toolbar X icon or the native back button.
     * Will check if the [initialFilter] and the [pendingFilter] are equal to each other
     * If so will call navigation.pop otherwise try to show the [buildAreYouSureYouWantToCancelFilters] alert.
     */
    fun onToolbarMenuClicked() {
        if (initialFilter == pendingFilter) {
            navigation.pop()
        } else {
            navigation.alert(alert = buildAreYouSureYouWantToCancelFilters())
        }
    }

    /**
     * initially loads the initial filters and set it equal tot he default pending filter
     * Default filter has "Both" as the hasShotsLogged filter (the default state)
     * If hasShotsLogged is null, treat it as "Both" since that's the default state
     */
    internal suspend fun loadInitialFilterFromDatabase() {
        val fetchedFilter = playerFilterRepository.fetchActiveFilter()
        initialFilter = fetchedFilter?.copy(
            hasShotsLogged = fetchedFilter.hasShotsLogged ?: HasShotsLoggedFilter.Both
        ) ?: PlayerFilter(hasShotsLogged = HasShotsLoggedFilter.Both)
        pendingFilter = initialFilter.copy()
    }

    /**
     * Gives us a list of all the selectable positions string values.
     */
    internal fun buildDefaultPositions(): List<String> =
        listOf(
            application.getString(StringsIds.pointGuard),
            application.getString(StringsIds.shootingGuard),
            application.getString(StringsIds.smallForward),
            application.getString(StringsIds.powerForward),
            application.getString(StringsIds.center),
            application.getString(StringsIds.all)
        )

    /**
     * Gives us a list of all the actual player positions which just doesn't include all
     */
    internal fun buildDefaultPositionNames(): List<String> =
        listOf(
            application.getString(StringsIds.pointGuard),
            application.getString(StringsIds.shootingGuard),
            application.getString(StringsIds.smallForward),
            application.getString(StringsIds.powerForward),
            application.getString(StringsIds.center)
        )

    /**
     * Returns true if the [title] passed in is equal to All.
     */
    internal fun isSelectedAllChoosePositionsOption(title: String): Boolean = title == application.getString(StringsIds.all)

    /**
     * Returns true if the [currentPositions] contains all the default positions and doesn't contain all.
     */
    internal fun shouldAddAllPosition(currentPositions: List<String>): Boolean = currentPositions.containsAll(buildDefaultPositionNames()) && !currentPositions.contains(application.getString(StringsIds.all))

    /**
     * Returns the list of strings for the default shot logs options.
     */
    internal fun buildDefaultShotLogsOptions(): List<String> =
        listOf(
            application.getString(StringsIds.hasShots),
            application.getString(StringsIds.noShots),
            application.getString(StringsIds.both)
        )

    /**
     * Returns the list of string ids for default selected shot log options based on [hasShotsLogged]
     * "Both" is the default state, so when null, we show "Both" as selected
     */
    internal fun buildDefaultSelectedShotLogOptions(hasShotsLogged: HasShotsLoggedFilter?): List<String> =
        when (hasShotsLogged) {
            is HasShotsLoggedFilter.HasShots -> listOf(application.getString(StringsIds.hasShots))
            is HasShotsLoggedFilter.NoShots -> listOf(application.getString(StringsIds.noShots))
            is HasShotsLoggedFilter.Both -> listOf(application.getString(StringsIds.both))
            is HasShotsLoggedFilter.None -> emptyList()
            null -> listOf(application.getString(StringsIds.both))
        }

    /**
     * Loads initial filter info fron the database and updates the filter state
     */
    internal fun initializePlayerFilterState() {
        scope.launch {
            loadInitialFilterFromDatabase()
            updatePlayerFilterState()
        }
    }

    /**
     * Updates the player filter state from mostly the [pendingFilter]
     */
    internal suspend fun updatePlayerFilterState() {
        playerFiltersMutableStateFlow.update { state ->
            state.copy(
                filterCount = pendingFilter.getFilterCount(),
                lastUpdatedFilterDateValue = pendingFilter.lastUpdatedValue,
                defaultPositions = buildDefaultPositions(),
                selectedPositions = pendingFilter.selectedPositions,
                defaultShotLogsOptions = buildDefaultShotLogsOptions(),
                selectedShotLogsOptions = buildDefaultSelectedShotLogOptions(hasShotsLogged = pendingFilter.hasShotsLogged),
                minShots = pendingFilter.minShots,
                maxShots = pendingFilter.maxShots,
                filteredPlayerCount = playerRepository.fetchAllPlayersWithFilter(filter = pendingFilter).size
            )
        }
    }

    /**
     * Function that gets called when a user toggles the different selectable positions.
     * These positions itself are the ones they can filter by so whatever they select or deselect
     * it will handle the entire filters
     *
     * @param title The title of the position that was toggled.
     */
    fun onPositionToggled(title: String) {
        scope.launch {
            val currentPositions = pendingFilter.selectedPositions.toMutableList()

            if (currentPositions.contains(title)) {
                handlePositionDeselection(
                    title = title,
                    currentPositions = currentPositions
                )
            } else {
                handlePositionSelection(
                    title = title,
                    currentPositions = currentPositions
                )
            }

            pendingFilter = pendingFilter.copy(selectedPositions = currentPositions)
            updatePlayerFilterState()
        }
    }

    /**
     * Function that handles the deselection of the player toggles.
     *
     * @param title The title of the position that was deselected.
     */
    internal fun handlePositionDeselection(title: String, currentPositions: MutableList<String>) {
        val all = application.getString(StringsIds.all)

        if (isSelectedAllChoosePositionsOption(title = title)) {
            currentPositions.clear()
        } else {
            if (currentPositions.contains(all)) {
                currentPositions.remove(all)
            }
            currentPositions.remove(title)
        }
    }

    /**
     * Function that handles the selection of the player toggles.
     *
     * @param title The title of the position that was deselected.
     * @param currentPositions The current list of positions that are selected.
     */
    internal fun handlePositionSelection(title: String, currentPositions: MutableList<String>) {
        if (isSelectedAllChoosePositionsOption(title = title)) {
            currentPositions.clear()
            currentPositions.addAll(buildDefaultPositions())
        } else {
            currentPositions.add(title)

            if (shouldAddAllPosition(currentPositions = currentPositions)) {
                currentPositions.add(application.getString(StringsIds.all))
            }
        }
    }

    /**
     * Function that handles when the shots are amount logs are toggled or not
     *
     * @param title The title of the shots logged by the title
     */
    fun onShotLogsToggled(title: String) {
        scope.launch {
            val newFilter = title.toHasShotsLoggedFilter(application)
            pendingFilter = pendingFilter.copy(
                hasShotsLogged = if (newFilter is HasShotsLoggedFilter.None) {
                    null
                } else {
                    newFilter
                }
            )
            updatePlayerFilterState()
        }
    }

    /**
     * Function to handle when we increment or decrement a minimum shot changed value
     * Will then go in and update the state and pending filter based on the value changed to
     *
     * @param value actual value that is being set by the user
     */
    fun onMinShotsChanged(value: Int) {
        scope.launch {
            val minShotsValue = if (value == 0) {
                null
            } else {
                value
            }
            val currentMaxShots = pendingFilter.maxShots

            pendingFilter = pendingFilter.copy(
                minShots = minShotsValue,
                maxShots = when {
                    minShotsValue == null -> currentMaxShots
                    currentMaxShots == null -> minShotsValue + 1
                    minShotsValue >= currentMaxShots -> minShotsValue + 1
                    else -> currentMaxShots
                }
            )
            updatePlayerFilterState()
        }
    }

    /**
     * Function to handle when we increment or decrement a max shot changed value
     * Will then go in and update the state and pending filter based on the value changed to
     *
     * @param value actual value that is being set by the user
     */
    fun onMaxShotsChanged(value: Int) {
        scope.launch {
            val maxShotsValue = if (value == 0) {
                null
            } else {
                value
            }
            val currentMinShots = pendingFilter.minShots

            pendingFilter = pendingFilter.copy(
                maxShots = when {
                    maxShotsValue == null -> null
                    currentMinShots == null -> maxShotsValue
                    maxShotsValue < currentMinShots -> currentMinShots + 1
                    maxShotsValue == currentMinShots -> currentMinShots + 1
                    else -> maxShotsValue
                }
            )

            updatePlayerFilterState()
        }
    }

    /**
     * Called when the user clicks "See Results" button.
     * Saves the pending filter to the database and updates the initial filter.
     */
    fun onSeeResultsClicked() {
        scope.launch {
            navigation.enableProgress(progress = Progress())
            pendingFilter = pendingFilter.copy(lastUpdatedValue = dateExt.now.toDateWithTimestampString())

            playerFilterRepository.saveActiveFilter(filter = pendingFilter)

            initialFilter = pendingFilter

            updatePlayerFilterState()

            navigation.pop()
            navigation.disableProgress()
        }
    }
}
