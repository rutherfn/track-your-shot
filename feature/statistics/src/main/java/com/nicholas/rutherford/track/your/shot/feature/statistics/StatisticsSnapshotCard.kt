package com.nicholas.rutherford.track.your.shot.feature.statistics

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
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
import com.nicholas.rutherford.track.your.shot.compose.components.BaseRow
import com.nicholas.rutherford.track.your.shot.compose.components.charts.PlayerShootingChartEntry
import com.nicholas.rutherford.track.your.shot.compose.components.charts.PlayerShootingPercentageChart
import com.nicholas.rutherford.track.your.shot.compose.components.charts.PlayerShotBreakdownChartData
import com.nicholas.rutherford.track.your.shot.compose.components.charts.PlayerShotBreakdownPieChart
import com.nicholas.rutherford.track.your.shot.compose.components.charts.ShotBreakdownLegend
import com.nicholas.rutherford.track.your.shot.helper.extensions.formatPercentageValue
import com.nicholas.rutherford.track.your.shot.helper.ui.Padding
import com.nicholas.rutherford.track.your.shot.helper.ui.TextStyles

/**
 * Created by Nicholas Rutherford, last edited on 2026-07-03
 *
 * Displays the team-level statistics snapshot shown when all players are selected.
 *
 * @param teamOverview Aggregated team statistics for the snapshot header.
 * @param playerStatistics Player statistics used to render the comparison chart.
 * @param formatPercentage Formats a numeric percentage value into a display string.
 * @param modifier Optional modifier applied to the card container.
 */
@Composable
fun TeamStatisticsSnapshotCard(
    teamOverview: StatisticsOverview,
    playerStatistics: List<PlayerStatisticsSummary>,
    formatPercentage: (value: Double) -> String,
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
                .padding(Padding.sixteen)
        ) {
            Text(
                text = stringResource(id = StringsIds.stats),
                style = TextStyles.bodyBold
            )

            Spacer(modifier = Modifier.height(Padding.twelve))

            TeamSnapshotSummaryRow(
                teamOverview = teamOverview,
                formatPercentage = formatPercentage
            )

            Spacer(modifier = Modifier.height(Padding.sixteen))

            PlayerShootingPercentageChart(
                entries = playerStatistics.map { playerStatisticsSummary ->
                    PlayerShootingChartEntry(
                        playerName = playerStatisticsSummary.playerName,
                        shootingPercentage = playerStatisticsSummary.overallMadePercentage
                    )
                }
            )
        }
    }
}

/**
 * Displays a compact player statistics snapshot card with a CTA for detailed stats.
 *
 * @param playerStatisticsSummary Aggregated statistics for the player.
 * @param formatPercentage Formats a numeric percentage value into a display string.
 * @param onViewDetailedStatsClicked Callback invoked when the detailed stats CTA is tapped.
 * @param isExpanded When true, renders a larger hero layout for a single filtered player.
 * @param modifier Optional modifier applied to the card container.
 */
@Composable
fun PlayerStatisticsSnapshotCard(
    playerStatisticsSummary: PlayerStatisticsSummary,
    formatPercentage: (value: Double) -> String,
    onViewDetailedStatsClicked: () -> Unit,
    modifier: Modifier = Modifier,
    isExpanded: Boolean = false
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
                .padding(Padding.sixteen)
        ) {
            PlayerSnapshotHeader(
                playerStatisticsSummary = playerStatisticsSummary,
                formatPercentage = formatPercentage,
                isExpanded = isExpanded
            )

            Spacer(modifier = Modifier.height(Padding.twelve))

            PlayerSnapshotBody(
                playerStatisticsSummary = playerStatisticsSummary,
                isExpanded = isExpanded
            )

            Spacer(modifier = Modifier.height(Padding.eight))

            HorizontalDivider(color = AppColors.LightGray.copy(alpha = 0.35f))

            BaseRow(
                title = stringResource(id = StringsIds.tapForMore),
                onClicked = onViewDetailedStatsClicked,
                titleStyle = TextStyles.bodyBold,
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                iconTint = AppColors.Orange
            )
        }
    }
}

/**
 * Displays the header row for a player snapshot card including name and shooting percentage.
 */
@Composable
private fun PlayerSnapshotHeader(
    playerStatisticsSummary: PlayerStatisticsSummary,
    formatPercentage: (value: Double) -> String,
    isExpanded: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = playerStatisticsSummary.playerName,
                style = if (isExpanded) {
                    TextStyles.medium
                } else {
                    TextStyles.bodyBold
                },
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            if (isExpanded) {
                Spacer(modifier = Modifier.height(Padding.four))
                Text(
                    text = stringResource(
                        id = StringsIds.statsAccuracyX,
                        formatPercentageValue(playerStatisticsSummary.overallMadePercentage)
                    ),
                    style = TextStyles.bodySmall,
                    color = AppColors.LightGray
                )
            }
        }

        PercentageBadge(
            percentage = formatPercentage(playerStatisticsSummary.overallMadePercentage)
        )
    }
}

/**
 * Displays the body content for a player snapshot card including chart and stat metrics.
 */
@Composable
private fun PlayerSnapshotBody(
    playerStatisticsSummary: PlayerStatisticsSummary,
    isExpanded: Boolean
) {
    val madeLabel = stringResource(id = StringsIds.make)
    val missedLabel = stringResource(id = StringsIds.miss)
    val chartSize = if (isExpanded) 140.dp else 110.dp

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(Padding.twelve),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Padding.eight)
        ) {
            PlayerShotBreakdownPieChart(
                chartData = PlayerShotBreakdownChartData(
                    madeLabel = madeLabel,
                    missedLabel = missedLabel,
                    shotsMade = playerStatisticsSummary.totalShotsMade,
                    shotsMissed = playerStatisticsSummary.totalShotsMissed
                ),
                chartSize = chartSize
            )
            ShotBreakdownLegend(
                madeLabel = madeLabel,
                missedLabel = missedLabel
            )
        }

        PlayerSnapshotStatGrid(
            playerStatisticsSummary = playerStatisticsSummary,
            modifier = Modifier.weight(1f)
        )
    }
}

/**
 * Displays compact stat metrics for a player snapshot card.
 */
@Composable
private fun PlayerSnapshotStatGrid(
    playerStatisticsSummary: PlayerStatisticsSummary,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(Padding.eight)
    ) {
        PlayerSnapshotStatLine(
            label = stringResource(id = StringsIds.statsAttemptsShort),
            value = playerStatisticsSummary.totalShotsAttempted.toString()
        )
        PlayerSnapshotStatLine(
            label = stringResource(id = StringsIds.make),
            value = playerStatisticsSummary.totalShotsMade.toString()
        )
        PlayerSnapshotStatLine(
            label = stringResource(id = StringsIds.miss),
            value = playerStatisticsSummary.totalShotsMissed.toString()
        )
    }
}

/**
 * Displays a single stat line with a label and value for a player snapshot card.
 */
@Composable
private fun PlayerSnapshotStatLine(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = AppColors.OffWhite,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(vertical = Padding.eight, horizontal = Padding.twelve),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = TextStyles.bodySmall,
            color = AppColors.LightGray,
            maxLines = 1
        )
        Text(
            text = value,
            style = TextStyles.bodyBold,
            color = AppColors.Orange
        )
    }
}

/**
 * Displays a single stat metric with a value and short label.
 */
@Composable
private fun SnapshotStatMetricCell(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(
                color = AppColors.OffWhite,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(vertical = Padding.eight, horizontal = Padding.four),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Padding.four)
    ) {
        Text(
            text = value,
            style = TextStyles.bodyBold,
            color = AppColors.Orange,
            textAlign = TextAlign.Center,
            maxLines = 1
        )
        Text(
            text = label,
            style = TextStyles.bodySmall,
            color = AppColors.LightGray,
            textAlign = TextAlign.Center,
            maxLines = 2,
            softWrap = true
        )
    }
}

/**
 * Displays a highlighted shooting percentage badge.
 */
@Composable
private fun PercentageBadge(percentage: String) {
    Text(
        text = percentage,
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

/**
 * Displays high-level team summary metrics above the comparison chart.
 */
@Composable
private fun TeamSnapshotSummaryRow(
    teamOverview: StatisticsOverview,
    formatPercentage: (value: Double) -> String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(Padding.eight)
    ) {
        SnapshotStatMetricCell(
            label = stringResource(id = StringsIds.players),
            value = teamOverview.totalPlayers.toString(),
            modifier = Modifier.weight(1f)
        )
        SnapshotStatMetricCell(
            label = stringResource(id = StringsIds.statsAttemptsShort),
            value = teamOverview.totalShotsAttempted.toString(),
            modifier = Modifier.weight(1f)
        )
        SnapshotStatMetricCell(
            label = stringResource(id = StringsIds.statsAccuracyShort),
            value = formatPercentage(teamOverview.averageMadePercentage),
            modifier = Modifier.weight(1f)
        )
    }
}

/**
 * Preview of [TeamStatisticsSnapshotCard] with sample team data.
 */
@Preview(showBackground = true)
@Composable
fun TeamStatisticsSnapshotCardPreview() {
    TeamStatisticsSnapshotCard(
        modifier = Modifier
            .background(AppColors.White)
            .padding(Padding.sixteen),
        teamOverview = StatisticsOverview(
            totalPlayers = 2,
            totalShotsAttempted = 90,
            totalShotsMade = 57,
            totalShotsMissed = 33,
            averageMadePercentage = 63.3
        ),
        playerStatistics = listOf(
            PlayerStatisticsSummary(
                playerName = "John Doe",
                totalShotsAttempted = 50,
                totalShotsMade = 35,
                totalShotsMissed = 15,
                overallMadePercentage = 70.0,
                loggedShotsCount = 3
            )
        ),
        formatPercentage = { value -> "$value%" }
    )
}

/**
 * Preview of [PlayerStatisticsSnapshotCard] in compact snapshot mode.
 */
@Preview(showBackground = true)
@Composable
fun PlayerStatisticsSnapshotCardPreview() {
    PlayerStatisticsSnapshotCard(
        modifier = Modifier
            .background(AppColors.White)
            .padding(Padding.sixteen),
        playerStatisticsSummary = PlayerStatisticsSummary(
            playerName = "John Doe",
            totalShotsAttempted = 50,
            totalShotsMade = 35,
            totalShotsMissed = 15,
            overallMadePercentage = 70.0,
            loggedShotsCount = 3
        ),
        formatPercentage = { value -> "$value%" },
        onViewDetailedStatsClicked = {}
    )
}
