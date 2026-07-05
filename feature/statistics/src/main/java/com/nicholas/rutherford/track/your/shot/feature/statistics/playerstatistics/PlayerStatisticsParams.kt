package com.nicholas.rutherford.track.your.shot.feature.statistics.playerstatistics

data class PlayerStatisticsParams(
    val state: PlayerStatisticsState,
    val onToolbarMenuClicked: () -> Unit
)
