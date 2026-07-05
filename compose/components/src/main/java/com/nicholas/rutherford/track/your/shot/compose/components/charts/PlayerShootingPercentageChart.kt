package com.nicholas.rutherford.track.your.shot.compose.components.charts

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nicholas.rutherford.track.your.shot.AppColors
import ir.ehsannarmani.compose_charts.ColumnChart
import ir.ehsannarmani.compose_charts.models.BarProperties
import ir.ehsannarmani.compose_charts.models.Bars
import ir.ehsannarmani.compose_charts.models.LabelHelperProperties

/**
 * Created by Nicholas Rutherford, last edited on 2026-07-02
 *
 * Represents a single player's shooting percentage entry for chart display.
 *
 * @property playerName Display label for the player.
 * @property shootingPercentage Overall shooting percentage value (0–100).
 */
data class PlayerShootingChartEntry(
    val playerName: String,
    val shootingPercentage: Double
)

/**
 * Displays a column chart comparing shooting percentages across players.
 *
 * @param entries List of player shooting percentage values to plot.
 * @param modifier Optional modifier applied to the chart container.
 */
@Composable
fun PlayerShootingPercentageChart(
    entries: List<PlayerShootingChartEntry>,
    modifier: Modifier = Modifier
) {
    ColumnChart(
        modifier = modifier
            .fillMaxWidth()
            .height(220.dp),
        data = entries.map { entry ->
            Bars(
                label = entry.playerName,
                values = listOf(
                    Bars.Data(
                        value = entry.shootingPercentage,
                        color = SolidColor(AppColors.Orange)
                    )
                )
            )
        },
        maxValue = MAX_SHOOTING_PERCENTAGE,
        minValue = MIN_SHOOTING_PERCENTAGE,
        barProperties = BarProperties(
            thickness = 24.dp,
            spacing = 8.dp,
            cornerRadius = Bars.Data.Radius.Rectangle(topLeft = 6.dp, topRight = 6.dp)
        ),
        labelHelperProperties = LabelHelperProperties(enabled = false)
    )
}

/**
 * Preview of [PlayerShootingPercentageChart] with sample player data.
 */
@Preview(showBackground = true)
@Composable
fun PlayerShootingPercentageChartPreview() {
    PlayerShootingPercentageChart(
        entries = listOf(
            PlayerShootingChartEntry(playerName = "John", shootingPercentage = 70.0),
            PlayerShootingChartEntry(playerName = "Jane", shootingPercentage = 55.0),
            PlayerShootingChartEntry(playerName = "Alex", shootingPercentage = 82.0)
        )
    )
}

private const val MAX_SHOOTING_PERCENTAGE = 100.0
private const val MIN_SHOOTING_PERCENTAGE = 0.0
