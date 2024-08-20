package com.nicholas.rutherford.track.your.shot.feature.settings.enabledpermissions

data class EnabledPermissionsParams(
    val onBackButtonClicked: () -> Unit,
    val state: EnabledPermissionsState
)