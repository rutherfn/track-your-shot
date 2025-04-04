package com.nicholas.rutherford.track.your.shot.feature.settings.managedeclaredshots.createeditdeclaredshot

data class CreateEditDeclaredShotScreenParams(
    val state: CreateEditDeclaredShotState,
    val onToolbarMenuClicked: () -> Unit
)