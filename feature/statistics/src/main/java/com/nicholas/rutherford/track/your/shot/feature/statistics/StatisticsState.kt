package com.nicholas.rutherford.track.your.shot.feature.statistics

/**
 * Created by Nicholas Rutherford, last edited on 2026-07-03
 *
 * Holds the UI state for the Statistics screen.
 *
 * @property allPlayersFilterLabel Localized label used for the "All players" filter option.
 * @property playerFilterOptions Available player filter options including [allPlayersFilterLabel].
 * @property selectedPlayerFilter Currently selected player filter option.
 * @property playerStatistics Full list of aggregated statistics for each player with logged shots.
 * @property displayedPlayerStatistics Player statistics filtered by [selectedPlayerFilter].
 * @property teamOverview Team-level snapshot shown when all players are selected.
 * @property hasNoStatistics Whether there are no player statistics available to display.
 */
data class StatisticsState(
    val allPlayersFilterLabel: String = "",
    val playerFilterOptions: List<String> = emptyList(),
    val selectedPlayerFilter: String = "",
    val playerStatistics: List<PlayerStatisticsSummary> = emptyList(),
    val displayedPlayerStatistics: List<PlayerStatisticsSummary> = emptyList(),
    val teamOverview: StatisticsOverview? = null,
    val hasNoStatistics: Boolean = true
) {

    /** Whether the snapshot is showing all players rather than a single filtered player. */
    val isShowingAllPlayers: Boolean = selectedPlayerFilter.isEmpty() || selectedPlayerFilter == allPlayersFilterLabel
}
