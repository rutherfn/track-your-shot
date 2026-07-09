package com.nicholas.rutherford.track.your.shot.feature.statistics.playerstatistics

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nicholas.rutherford.track.your.shot.AppColors
import com.nicholas.rutherford.track.your.shot.base.resources.StringsIds
import com.nicholas.rutherford.track.your.shot.compose.components.charts.PlayerShotsBreakdownLineChart
import com.nicholas.rutherford.track.your.shot.compose.components.charts.PlayerShotsBreakdownLineChartEntry
import com.nicholas.rutherford.track.your.shot.compose.components.charts.PlayerShotsBreakdownLineChartInfo
import com.nicholas.rutherford.track.your.shot.compose.components.charts.ShotBreakdownLegend
import com.nicholas.rutherford.track.your.shot.feature.statistics.PlayerStatisticsSummary
import com.nicholas.rutherford.track.your.shot.feature.statistics.StatisticsPlayerFilterRow
import com.nicholas.rutherford.track.your.shot.helper.ui.Padding
import com.nicholas.rutherford.track.your.shot.helper.ui.TextStyles

/**
 * Created by Nicholas Rutherford, last edited on 2026-07-05
 *
 * Displays detailed player statistics including a line chart and date filter options.
 *
 * @param params Contains the state and callback handlers for this screen.
 */
@Composable
fun PlayerStatisticsScreen(params: PlayerStatisticsParams) {
    BackHandler { params.onToolbarMenuClicked.invoke() }

    if (params.state.hasNoLoggedShots) {
        PlayerStatisticsEmptyState()
    } else {
        PlayerStatisticsContent(params = params)
    }
}

/**
 * Displays the main player statistics content including filters and the line chart.
 */
@Composable
private fun PlayerStatisticsContent(params: PlayerStatisticsParams) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColors.OffWhite)
    ) {
        item {
            params.state.playerStatisticsSummary?.let { playerStatisticsSummary ->
                PlayerStatisticsHeader(
                    playerStatisticsSummary = playerStatisticsSummary,
                    formatPercentage = params.formatPercentage
                )
            }
        }

        if (params.state.dateFilterOptions.size > 1) {
            item {
                Spacer(modifier = Modifier.height(Padding.twelve))

                Text(
                    text = stringResource(id = StringsIds.statsFilterByDate),
                    modifier = Modifier.padding(horizontal = Padding.sixteen),
                    style = TextStyles.smallBold
                )

                Spacer(modifier = Modifier.height(Padding.eight))

                StatisticsPlayerFilterRow(
                    filterOptions = params.state.dateFilterOptions,
                    selectedFilter = params.state.selectedDateFilter,
                    onFilterSelected = params.onDateFilterSelected
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(Padding.sixteen))

            PlayerStatisticsChartCard(
                modifier = Modifier.padding(horizontal = Padding.sixteen),
                chartInfo = params.state.chartInfo,
                madeLabel = stringResource(id = StringsIds.make),
                missedLabel = stringResource(id = StringsIds.miss)
            )
        }

        item {
            Spacer(modifier = Modifier.height(Padding.sixteen))
        }
    }
}

/**
 * Displays the player header with name and overall shooting percentage.
 */
@Composable
private fun PlayerStatisticsHeader(
    playerStatisticsSummary: PlayerStatisticsSummary,
    formatPercentage: (value: Double) -> String
) {
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
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = playerStatisticsSummary.playerName,
                style = TextStyles.medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )

            Text(
                text = formatPercentage(playerStatisticsSummary.overallMadePercentage),
                style = TextStyles.bodyBold,
                color = AppColors.Orange,
                modifier = Modifier
                    .background(
                        color = AppColors.Orange.copy(alpha = 0.12f),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(horizontal = Padding.twelve, vertical = Padding.eight)
            )
        }

        Spacer(modifier = Modifier.height(Padding.four))

        Text(
            text = stringResource(id = StringsIds.playerShots),
            style = TextStyles.bodySmall,
            color = AppColors.LightGray
        )
    }
}

/**
 * Displays the player statistics line chart card.
 */
@Composable
private fun PlayerStatisticsChartCard(
    chartInfo: PlayerShotsBreakdownLineChartInfo?,
    madeLabel: String,
    missedLabel: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = AppColors.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Padding.sixteen),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(id = StringsIds.stats),
                style = TextStyles.bodyBold,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(Padding.twelve))

            chartInfo?.let { lineChartInfo ->
                ShotBreakdownLegend(
                    madeLabel = madeLabel,
                    missedLabel = missedLabel,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(Padding.twelve))

                PlayerShotsBreakdownLineChart(chartInfo = lineChartInfo)
            }
        }
    }
}

/**
 * Displays an empty state when the player has no finalized logged shots.
 */
@Composable
private fun PlayerStatisticsEmptyState() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColors.White),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(id = StringsIds.trackYourProgressDescription),
            style = TextStyles.bodySmall,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(Padding.sixteen)
        )
    }
}

/**
 * Preview of [PlayerStatisticsScreen] with sample chart data and date filters.
 */
@Preview(showBackground = true)
@Composable
fun PlayerStatisticsScreenPreview() {
    PlayerStatisticsScreen(
        params = PlayerStatisticsParams(
            state = PlayerStatisticsState(
                playerStatisticsSummary = PlayerStatisticsSummary(
                    playerId = 1,
                    playerName = "John Doe",
                    totalShotsAttempted = 50,
                    totalShotsMade = 35,
                    totalShotsMissed = 15,
                    overallMadePercentage = 70.0,
                    loggedShotsCount = 3
                ),
                dateFilterOptions = listOf("All", "July 01, 2026", "July 03, 2026"),
                selectedDateFilter = "All",
                allDatesFilterLabel = "All",
                chartInfo = PlayerShotsBreakdownLineChartInfo(
                    madeLabel = "Make",
                    missedLabel = "Miss",
                    entries = listOf(
                        PlayerShotsBreakdownLineChartEntry(
                            sessionLabel = "Baby Hook",
                            madePercentage = 45.0,
                            missedPercentage = 55.0
                        ),
                        PlayerShotsBreakdownLineChartEntry(
                            sessionLabel = "Catch and Shoot Three",
                            madePercentage = 62.0,
                            missedPercentage = 38.0
                        ),
                        PlayerShotsBreakdownLineChartEntry(
                            sessionLabel = "Deep Three",
                            madePercentage = 71.0,
                            missedPercentage = 29.0
                        )
                    )
                ),
                hasNoLoggedShots = false
            ),
            onToolbarMenuClicked = {},
            onDateFilterSelected = {},
            formatPercentage = { value -> "$value%" }
        )
    )
}
