package com.nicholas.rutherford.track.your.shot.feature.shots

data class ShotsListScreenParams(
    val state: ShotsListState,
    val onToolbarMenuClicked: () -> Unit
)
