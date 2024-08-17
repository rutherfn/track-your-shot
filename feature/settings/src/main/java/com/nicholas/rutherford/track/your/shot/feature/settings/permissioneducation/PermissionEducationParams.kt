package com.nicholas.rutherford.track.your.shot.feature.settings.permissioneducation

data class PermissionEducationParams(
    val onGotItButtonClicked: () -> Unit,
    val state: PermissionEducationState
)
