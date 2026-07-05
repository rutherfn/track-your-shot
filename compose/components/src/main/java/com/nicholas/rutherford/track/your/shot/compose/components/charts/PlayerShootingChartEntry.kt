package com.nicholas.rutherford.track.your.shot.compose.components.charts

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
