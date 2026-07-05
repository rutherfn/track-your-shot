package com.nicholas.rutherford.track.your.shot.compose.components.charts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.nicholas.rutherford.track.your.shot.AppColors
import com.nicholas.rutherford.track.your.shot.helper.ui.Padding
import com.nicholas.rutherford.track.your.shot.helper.ui.TextStyles
import ir.ehsannarmani.compose_charts.PieChart
import ir.ehsannarmani.compose_charts.models.LabelHelperProperties
import ir.ehsannarmani.compose_charts.models.Pie

/**
 * Displays a pie chart breaking down a player's made and missed shots.
 *
 * The built-in chart legend is disabled so callers can render a compact custom legend
 * with short labels that fit on one line.
 *
 * @param chartInfo Made and missed shot counts with display labels.
 * @param modifier Optional modifier applied to the chart container.
 * @param chartSize Size of the pie chart drawable area.
 */
@Composable
fun PlayerShotBreakdownPieChart(
    chartInfo: PlayerShotBreakdownChartInfo,
    modifier: Modifier = Modifier,
    chartSize: Dp = 120.dp
) {
    val totalShots = chartInfo.shotsMade + chartInfo.shotsMissed

    if (totalShots != 0) {
        PieChart(
            modifier = modifier.size(chartSize),
            data = listOf(
                Pie(
                    label = chartInfo.madeLabel,
                    data = chartInfo.shotsMade.toDouble(),
                    color = AppColors.Orange,
                    selectedColor = AppColors.OrangeVariant
                ),
                Pie(
                    label = chartInfo.missedLabel,
                    data = chartInfo.shotsMissed.toDouble(),
                    color = AppColors.Red,
                    selectedColor = AppColors.LightGray
                )
            ),
            labelHelperProperties = LabelHelperProperties(enabled = false),
            labelHelperPadding = 0.dp,
            style = Pie.Style.Fill
        )
    }
}

/**
 * Displays a compact legend for made and missed shot breakdown colors.
 *
 * @param madeLabel Short label for made shots.
 * @param missedLabel Short label for missed shots.
 * @param modifier Optional modifier applied to the legend row.
 */
@Composable
fun ShotBreakdownLegend(
    madeLabel: String,
    missedLabel: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(Padding.twelve),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ShotBreakdownLegendItem(color = AppColors.Orange, label = madeLabel)
        ShotBreakdownLegendItem(color = AppColors.Red, label = missedLabel)
    }
}

/**
 * Displays a single color dot and label within [ShotBreakdownLegend].
 */
@Composable
private fun ShotBreakdownLegendItem(
    color: Color,
    label: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Padding.four)
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .background(color = color, shape = CircleShape)
        )
        Text(
            text = label,
            style = TextStyles.smallBold,
            maxLines = 1
        )
    }
}

/**
 * Preview of [PlayerShotBreakdownPieChart] with a custom legend.
 */
@Preview(showBackground = true)
@Composable
fun PlayerShotBreakdownPieChartPreview() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        PlayerShotBreakdownPieChart(
            chartInfo = PlayerShotBreakdownChartInfo(
                madeLabel = "Make",
                missedLabel = "Miss",
                shotsMade = 35,
                shotsMissed = 15
            )
        )
        ShotBreakdownLegend(madeLabel = "Make", missedLabel = "Miss")
    }
}
