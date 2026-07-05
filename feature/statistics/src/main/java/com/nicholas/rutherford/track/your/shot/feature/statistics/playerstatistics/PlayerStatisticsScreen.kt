package com.nicholas.rutherford.track.your.shot.feature.statistics.playerstatistics

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable

@Composable
fun PlayerStatisticsScreen(params: PlayerStatisticsParams) {
    BackHandler { params.onToolbarMenuClicked.invoke() }
}
