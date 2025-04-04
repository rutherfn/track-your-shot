package com.nicholas.rutherford.track.your.shot.feature.settings.managedeclaredshots

data class DeclaredShotsListScreenParams(
    val state: DeclaredShotsListState,
    val onDeclaredShotClicked: (id: Int) -> Unit
)
