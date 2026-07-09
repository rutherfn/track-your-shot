package com.nicholas.rutherford.track.your.shot.feature.statistics.main

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.nicholas.rutherford.track.your.shot.AppColors
import com.nicholas.rutherford.track.your.shot.base.resources.StringsIds
import com.nicholas.rutherford.track.your.shot.feature.statistics.PlayerStatisticsSummary
import com.nicholas.rutherford.track.your.shot.feature.statistics.StatisticsOverview
import com.nicholas.rutherford.track.your.shot.feature.statistics.StatisticsPlayerFilterRow
import com.nicholas.rutherford.track.your.shot.helper.ui.Padding
import com.nicholas.rutherford.track.your.shot.helper.ui.TextStyles

/**
 * Created by Nicholas Rutherford, last edited on 2026-07-03
 *
 * Displays the Statistics screen as a snapshot overview of player shooting performance.
 *
 * Users can filter by all players or an individual player, review snapshot cards with charts,
 * and tap a CTA on each card to navigate to detailed statistics in a future screen.
 *
 * If no statistics are available, an empty state is shown encouraging users to log shots.
 *
 * @param params Contains the state and callback handlers for this screen.
 */
@Composable
fun StatisticsScreen(params: StatisticsParams) {
    BackHandler { params.onToolbarMenuClicked.invoke() }

    when {
        params.state.isLoading -> StatisticsLoadingState()
        params.state.hasNoStatistics -> StatisticsEmptyState()
        else -> StatisticsSnapshotContent(params = params)
    }
}

/**
 * Displays the snapshot overview content including filters, team summary, and player cards.
 *
 * @param params Contains the state and callback handlers for this screen.
 */
@Composable
private fun StatisticsSnapshotContent(params: StatisticsParams) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(color = AppColors.OffWhite)
    ) {
        item { StatisticsSnapshotHeader(isShowingAllPlayers = params.state.isShowingAllPlayers) }

        item {
            Spacer(modifier = Modifier.height(Padding.twelve))

            StatisticsPlayerFilterRow(
                filterOptions = params.state.playerFilterOptions,
                selectedFilter = params.state.selectedPlayerFilter,
                onFilterSelected = params.onPlayerFilterSelected
            )
        }

        if (params.state.isShowingAllPlayers) {
            allPlayersContent(params = params)
        } else {
            selectedPlayerContent(params = params)
        }

        item {
            Spacer(modifier = Modifier.height(Padding.sixteen))
        }
    }
}

/**
 * Displays all the players view when the user clicks on the filter row being All.
 *
 * @param params Contains the state and callback handlers for this screen.
 */
private fun LazyListScope.allPlayersContent(params: StatisticsParams) {
    params.state.teamOverview?.let { teamOverview ->
        item {
            Spacer(modifier = Modifier.height(Padding.sixteen))

            TeamStatisticsSnapshotCard(
                modifier = Modifier.padding(horizontal = Padding.sixteen),
                teamOverview = teamOverview,
                playerStatistics = params.state.displayedPlayerStatistics,
                formatPercentage = params.formatPercentage
            )
        }
    }

    item {
        Spacer(modifier = Modifier.height(Padding.sixteen))

        Text(
            text = stringResource(id = StringsIds.players),
            modifier = Modifier.padding(horizontal = Padding.sixteen),
            style = TextStyles.smallBold
        )
    }

    items(
        items = params.state.displayedPlayerStatistics,
        key = { playerStatisticsSummary ->
            "${playerStatisticsSummary.playerId}_${playerStatisticsSummary.playerName}"
        }
    ) { playerStatisticsSummary ->
        PlayerStatisticsSnapshotCard(
            modifier = Modifier.padding(
                horizontal = Padding.sixteen,
                vertical = Padding.eight
            ),
            playerStatisticsSummary = playerStatisticsSummary,
            formatPercentage = params.formatPercentage,
            onViewDetailedStatsClicked = { params.onViewDetailedStatsClicked(playerStatisticsSummary) }
        )
    }
}

/**
 * Displays the selected player stats when the user clicks on a specific person to filter stats by.
 *
 * @param params Contains the state and callback handlers for this screen.
 */
private fun LazyListScope.selectedPlayerContent(params: StatisticsParams) {
    params.state.displayedPlayerStatistics.firstOrNull()?.let { playerStatisticsSummary ->
        item {
            Spacer(modifier = Modifier.height(Padding.sixteen))

            PlayerStatisticsSnapshotCard(
                modifier = Modifier.padding(horizontal = Padding.sixteen),
                playerStatisticsSummary = playerStatisticsSummary,
                formatPercentage = params.formatPercentage,
                onViewDetailedStatsClicked = {
                    params.onViewDetailedStatsClicked(playerStatisticsSummary)
                },
                isExpanded = true
            )
        }
    }
}

/**
 * Displays the snapshot screen header describing the current statistics view.
 *
 * @param isShowingAllPlayers Whether the all-players snapshot is currently displayed.
 */
@Composable
private fun StatisticsSnapshotHeader(isShowingAllPlayers: Boolean) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(AppColors.White)
            .padding(
                start = Padding.sixteen,
                end = Padding.sixteen,
                top = Padding.sixteen,
                bottom = Padding.eight
            )
    ) {
        Text(
            text = stringResource(id = StringsIds.trackYourProgress),
            style = TextStyles.medium
        )

        Spacer(modifier = Modifier.height(Padding.four))

        Text(
            text = if (isShowingAllPlayers) {
                stringResource(id = StringsIds.stats)
            } else {
                stringResource(id = StringsIds.playerShots)
            },
            style = TextStyles.bodySmall,
            color = AppColors.LightGray
        )
    }
}

/**
 * Displays a loading state while statistics data is being fetched.
 */
@Composable
private fun StatisticsLoadingState() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColors.OffWhite),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(id = StringsIds.stats),
            style = TextStyles.bodySmall,
            color = AppColors.LightGray
        )
    }
}

/**
 * Displays an empty state UI when there are no player statistics to show.
 */
@Composable
private fun StatisticsEmptyState() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColors.White),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(Padding.sixteen)
        ) {
            Text(
                text = stringResource(id = StringsIds.trackYourProgress),
                style = TextStyles.medium,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(vertical = Padding.four)
            )

            Text(
                text = stringResource(id = StringsIds.trackYourProgressDescription),
                style = TextStyles.smallBold,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(vertical = Padding.four)
            )
        }
    }
}

/**
 * Preview of the [StatisticsScreen] when there are no statistics to display.
 */
@Preview(showBackground = true)
@Composable
fun StatisticsScreenEmptyStatePreview() {
    StatisticsScreen(
        params = StatisticsParams(
            state = StatisticsState(
                playerStatistics = emptyList(),
                hasNoStatistics = true
            ),
            onToolbarMenuClicked = {},
            onHelpClicked = {},
            onPlayerFilterSelected = {},
            onViewDetailedStatsClicked = {},
            formatPercentage = { value -> "$value%" }
        )
    )
}

/**
 * Preview of the [StatisticsScreen] with an all-players snapshot layout.
 */
@Preview(showBackground = true)
@Composable
fun StatisticsScreenAllPlayersPreview() {
    val samplePlayers = listOf(
        PlayerStatisticsSummary(
            playerId = 0,
            playerName = "John Doe",
            totalShotsAttempted = 50,
            totalShotsMade = 35,
            totalShotsMissed = 15,
            overallMadePercentage = 70.0,
            loggedShotsCount = 3
        ),
        PlayerStatisticsSummary(
            playerId = 1,
            playerName = "Jane Smith",
            totalShotsAttempted = 40,
            totalShotsMade = 22,
            totalShotsMissed = 18,
            overallMadePercentage = 55.0,
            loggedShotsCount = 2
        )
    )

    StatisticsScreen(
        params = StatisticsParams(
            state = StatisticsState(
                allPlayersFilterLabel = "All",
                playerFilterOptions = listOf("All", "John Doe", "Jane Smith"),
                selectedPlayerFilter = "All",
                playerStatistics = samplePlayers,
                displayedPlayerStatistics = samplePlayers,
                teamOverview = StatisticsOverview(
                    totalPlayers = 2,
                    totalShotsAttempted = 90,
                    totalShotsMade = 57,
                    totalShotsMissed = 33,
                    averageMadePercentage = 63.3
                ),
                hasNoStatistics = false
            ),
            onToolbarMenuClicked = {},
            onHelpClicked = {},
            onPlayerFilterSelected = {},
            onViewDetailedStatsClicked = {},
            formatPercentage = { value -> "$value%" }
        )
    )
}

/**
 * Preview of the [StatisticsScreen] with a single filtered player snapshot layout.
 */
@Preview(showBackground = true)
@Composable
fun StatisticsScreenFilteredPlayerPreview() {
    val samplePlayer = PlayerStatisticsSummary(
        playerId = 2,
        playerName = "John Doe",
        totalShotsAttempted = 50,
        totalShotsMade = 35,
        totalShotsMissed = 15,
        overallMadePercentage = 70.0,
        loggedShotsCount = 3
    )

    StatisticsScreen(
        params = StatisticsParams(
            state = StatisticsState(
                allPlayersFilterLabel = "All",
                playerFilterOptions = listOf("All", "John Doe"),
                selectedPlayerFilter = "John Doe",
                playerStatistics = listOf(samplePlayer),
                displayedPlayerStatistics = listOf(samplePlayer),
                teamOverview = StatisticsOverview(
                    totalPlayers = 1,
                    totalShotsAttempted = 50,
                    totalShotsMade = 35,
                    totalShotsMissed = 15,
                    averageMadePercentage = 70.0
                ),
                hasNoStatistics = false
            ),
            onToolbarMenuClicked = {},
            onHelpClicked = {},
            onPlayerFilterSelected = {},
            onViewDetailedStatsClicked = {},
            formatPercentage = { value -> "$value%" }
        )
    )
}
