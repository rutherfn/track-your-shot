package com.nicholas.rutherford.track.your.shot.feature.statistics.main

import android.app.Application
import com.nicholas.rutherford.track.your.shot.base.resources.StringsIds
import com.nicholas.rutherford.track.your.shot.base.vm.BaseViewModel
import com.nicholas.rutherford.track.your.shot.data.room.repository.PlayerRepository
import com.nicholas.rutherford.track.your.shot.data.room.response.buildPlayersWithShots
import com.nicholas.rutherford.track.your.shot.data.room.response.sortedPlayers
import com.nicholas.rutherford.track.your.shot.data.shared.alert.Alert
import com.nicholas.rutherford.track.your.shot.data.shared.alert.AlertConfirmAndDismissButton
import com.nicholas.rutherford.track.your.shot.feature.statistics.PlayerStatisticsSummary
import com.nicholas.rutherford.track.your.shot.feature.statistics.StatisticsOverview
import com.nicholas.rutherford.track.your.shot.feature.statistics.toPlayerStatisticsSummary
import com.nicholas.rutherford.track.your.shot.helper.constants.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Created by Nicholas Rutherford, last edited on 2026-07-03
 *
 * ViewModel responsible for managing and exposing the state and user interactions
 * for the Statistics screen.
 *
 *
 * @param application Application context used to access string resources.
 * @param scope Coroutine scope used to launch background operations.
 * @param navigation Interface to handle navigation events and alerts.
 * @param playerRepository Repository used to fetch player and shot information from local storage.
 */
class StatisticsViewModel(
    private val application: Application,
    private val scope: CoroutineScope,
    private val navigation: StatisticsNavigation,
    private val playerRepository: PlayerRepository
) : BaseViewModel() {

    internal val statisticsMutableStateFlow = MutableStateFlow(value = StatisticsState())
    val statisticsStateFlow = statisticsMutableStateFlow.asStateFlow()

    init {
        updateStatisticsState(currentSelectedFilter = statisticsMutableStateFlow.value.selectedPlayerFilter)
    }

    /**
     * Fetches all players with logged shots and updates the statistics screen state.
     */
    internal fun updateStatisticsState(currentSelectedFilter: String) {
        scope.launch {
            val playerStatistics = playerRepository.fetchAllPlayers()
                .buildPlayersWithShots()
                .sortedPlayers()
                .map { player -> player.toPlayerStatisticsSummary() }
            val selectedPlayerFilter = resolveSelectedPlayerFilter(
                currentSelectedFilter = currentSelectedFilter,
                allPlayersFilterLabel = application.getString(StringsIds.all),
                playerStatistics = playerStatistics
            )

            statisticsMutableStateFlow.update { statisticsState ->
                statisticsState.copy(
                    allPlayersFilterLabel = application.getString(StringsIds.all),
                    playerFilterOptions = buildPlayerFilterOptions(
                        allPlayersFilterLabel = application.getString(StringsIds.all),
                        playerStatistics = playerStatistics
                    ),
                    selectedPlayerFilter = selectedPlayerFilter,
                    playerStatistics = playerStatistics,
                    displayedPlayerStatistics = filterPlayerStatistics(
                        allPlayersFilterLabel = application.getString(StringsIds.all),
                        selectedPlayerFilter = selectedPlayerFilter,
                        playerStatistics = playerStatistics
                    ),
                    teamOverview = buildTeamStatisticsOverview(playerStatistics = playerStatistics),
                    hasNoStatistics = playerStatistics.isEmpty()
                )
            }
        }
    }

    /**
     * Updates the selected player filter and refreshes the displayed player statistics.
     *
     * @param filter The selected filter option, either [StatisticsState.allPlayersFilterLabel] or a player name.
     */
    fun onPlayerFilterSelected(filter: String) {
        statisticsMutableStateFlow.update { statisticsState ->
            statisticsState.copy(
                selectedPlayerFilter = filter,
                displayedPlayerStatistics = filterPlayerStatistics(
                    allPlayersFilterLabel = statisticsState.allPlayersFilterLabel,
                    selectedPlayerFilter = filter,
                    playerStatistics = statisticsState.playerStatistics
                )
            )
        }
    }

    /**
     * Called when the user taps the CTA to view detailed statistics for a player.
     *
     * @param playerStatisticsSummary Aggregated statistics for the selected player.
     */
    fun onViewDetailedStatsClicked(playerStatisticsSummary: PlayerStatisticsSummary) = navigation.navigateToPlayerStatistics(playerId = playerStatisticsSummary.playerId)

    /**
     * Builds team-level snapshot statistics from the full player statistics list.
     */
    internal fun buildTeamStatisticsOverview(
        playerStatistics: List<PlayerStatisticsSummary>
    ): StatisticsOverview? {
        if (playerStatistics.isEmpty()) {
            return null
        } else {
            val totalAttempted = playerStatistics.sumOf { it.totalShotsAttempted }
            val totalMade = playerStatistics.sumOf { it.totalShotsMade }
            val averagePercentage = if (totalAttempted > 0) {
                (totalMade.toDouble() / totalAttempted.toDouble()) * Constants.PERCENTAGE_MULTIPLIER
            } else {
                0.0
            }

            return StatisticsOverview(
                totalPlayers = playerStatistics.size,
                totalShotsAttempted = totalAttempted,
                totalShotsMade = totalMade,
                totalShotsMissed = playerStatistics.sumOf { value -> value.totalShotsMissed },
                averageMadePercentage = averagePercentage
            )
        }
    }

    /**
     * Builds the list of player filter options shown on the statistics snapshot screen.
     */
    internal fun buildPlayerFilterOptions(
        allPlayersFilterLabel: String,
        playerStatistics: List<PlayerStatisticsSummary>
    ): List<String> = listOf(allPlayersFilterLabel) + playerStatistics.map { value -> value.playerName }

    /**
     * Filters player statistics based on the selected filter option.
     */
    internal fun filterPlayerStatistics(
        allPlayersFilterLabel: String,
        selectedPlayerFilter: String,
        playerStatistics: List<PlayerStatisticsSummary>
    ): List<PlayerStatisticsSummary> {
        return if (selectedPlayerFilter.isEmpty() || selectedPlayerFilter == allPlayersFilterLabel) {
            playerStatistics
        } else {
            playerStatistics.filter { it.playerName == selectedPlayerFilter }
        }
    }

    /**
     * Keeps the current filter when possible, otherwise falls back to the all-players filter.
     */
    internal fun resolveSelectedPlayerFilter(
        currentSelectedFilter: String,
        allPlayersFilterLabel: String,
        playerStatistics: List<PlayerStatisticsSummary>
    ): String {
        val availableFilters = buildPlayerFilterOptions(
            allPlayersFilterLabel = allPlayersFilterLabel,
            playerStatistics = playerStatistics
        )

        return if (currentSelectedFilter in availableFilters) {
            currentSelectedFilter
        } else {
            allPlayersFilterLabel
        }
    }

    /**
     * Called when the toolbar menu icon is clicked.
     * Triggers navigation to open the drawer menu.
     */
    fun onToolbarMenuClicked() = navigation.openNavigationDrawer()

    /**
     * Called when the help icon is clicked.
     * Triggers the display of the help alert dialog.
     */
    fun onHelpClicked() = navigation.alert(alert = statisticsHelpAlert())

    /**
     * Builds and returns the alert shown when the help icon is clicked in the statistics screen.
     */
    fun statisticsHelpAlert(): Alert {
        return Alert(
            title = application.getString(StringsIds.statistics),
            description = application.getString(StringsIds.trackYourProgressDescription),
            confirmButton = AlertConfirmAndDismissButton(
                buttonText = application.getString(StringsIds.gotIt)
            )
        )
    }
}
