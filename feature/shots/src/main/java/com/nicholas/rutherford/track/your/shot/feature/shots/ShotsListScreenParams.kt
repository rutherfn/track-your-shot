package com.nicholas.rutherford.track.your.shot.feature.shots

data class ShotsListScreenParams(
    val state: ShotsListState,
    val onHelpClicked: () -> Unit,
    val onToolbarMenuClicked: () -> Unit,
    val onShotItemClicked: (ShotLoggedWithPlayer) -> Unit,
    val shouldShowAllPlayerShots: Boolean
)
