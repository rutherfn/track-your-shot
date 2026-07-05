package com.nicholas.rutherford.track.your.shot.compose.components.charts

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nicholas.rutherford.track.your.shot.AppColors
import com.nicholas.rutherford.track.your.shot.helper.constants.Constants
import com.nicholas.rutherford.track.your.shot.helper.ui.Padding
import ir.ehsannarmani.compose_charts.LineChart
import ir.ehsannarmani.compose_charts.models.DotProperties
import ir.ehsannarmani.compose_charts.models.LabelHelperProperties
import ir.ehsannarmani.compose_charts.models.LabelProperties
import ir.ehsannarmani.compose_charts.models.Line

/**
 * Created by Nicholas Rutherford, last edited on 2026-07-05
 *
 * Displays a line chart showing made and missed shot percentages across logged sessions.
 *
 * @param chartInfo Made and missed percentages per logged session with display labels.
 * @param modifier Optional modifier applied to the chart container.
 */
@Composable
fun PlayerShotsBreakdownLineChart(
    chartInfo: PlayerShotsBreakdownLineChartInfo,
    modifier: Modifier = Modifier
) {
    if (chartInfo.entries.isEmpty()) {
        return
    }

    val sessionLabels = chartInfo.entries.map { entry -> entry.sessionLabel }

    LineChart(
        modifier = modifier
            .fillMaxWidth()
            .height(220.dp),
        data = listOf(
            Line(
                label = chartInfo.madeLabel,
                values = chartInfo.entries.map { entry -> entry.madePercentage },
                color = SolidColor(AppColors.Orange),
                firstGradientFillColor = AppColors.Orange.copy(alpha = 0.2f),
                secondGradientFillColor = Color.Transparent,
                curvedEdges = true,
                dotProperties = DotProperties(
                    enabled = true,
                    radius = 4.dp,
                    color = SolidColor(AppColors.White),
                    strokeWidth = 2.dp,
                    strokeColor = SolidColor(AppColors.Orange)
                )
            ),
            Line(
                label = chartInfo.missedLabel,
                values = chartInfo.entries.map { entry -> entry.missedPercentage },
                color = SolidColor(AppColors.Red),
                firstGradientFillColor = AppColors.Red.copy(alpha = 0.15f),
                secondGradientFillColor = Color.Transparent,
                curvedEdges = true,
                dotProperties = DotProperties(
                    enabled = true,
                    radius = 4.dp,
                    color = SolidColor(AppColors.White),
                    strokeWidth = 2.dp,
                    strokeColor = SolidColor(AppColors.Red)
                )
            )
        ),
        maxValue = Constants.MAX_SHOOTING_PERCENTAGE,
        minValue = Constants.MIN_SHOOTING_PERCENTAGE,
        labelProperties = LabelProperties(
            enabled = true,
            labels = sessionLabels,
            rotation = LabelProperties.Rotation(mode = LabelProperties.Rotation.Mode.IfNecessary)
        ),
        labelHelperProperties = LabelHelperProperties(enabled = true)
    )
}

/**
 * Preview of [PlayerShotsBreakdownLineChart] with sample session data.
 */
@Preview(showBackground = true)
@Composable
fun PlayerShotsBreakdownLineChartPreview() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Padding.sixteen),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PlayerShotsBreakdownLineChart(
            chartInfo = PlayerShotsBreakdownLineChartInfo(
                madeLabel = "Make",
                missedLabel = "Miss",
                entries = listOf(
                    PlayerShotsBreakdownLineChartEntry(
                        sessionLabel = "Session 1",
                        madePercentage = 45.0,
                        missedPercentage = 55.0
                    ),
                    PlayerShotsBreakdownLineChartEntry(
                        sessionLabel = "Session 2",
                        madePercentage = 62.0,
                        missedPercentage = 38.0
                    ),
                    PlayerShotsBreakdownLineChartEntry(
                        sessionLabel = "Session 3",
                        madePercentage = 58.0,
                        missedPercentage = 42.0
                    ),
                    PlayerShotsBreakdownLineChartEntry(
                        sessionLabel = "Session 4",
                        madePercentage = 71.0,
                        missedPercentage = 29.0
                    ),
                    PlayerShotsBreakdownLineChartEntry(
                        sessionLabel = "Session 5",
                        madePercentage = 66.0,
                        missedPercentage = 34.0
                    )
                )
            )
        )

        Spacer(modifier = Modifier.height(Padding.twelve))

        ShotBreakdownLegend(madeLabel = "Make", missedLabel = "Miss")
    }
}
