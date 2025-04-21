package com.nicholas.rutherford.track.your.shot.feature.settings.managedeclaredshots.declaredshotslist

data class DeclaredShotsListScreenParams(
    val state: DeclaredShotsListState,
    val onAddDeclaredShotClicked: () -> Unit,
    val onDeclaredShotClicked: (id: Int) -> Unit,
    val onToolbarMenuClicked: () -> Unit
)
