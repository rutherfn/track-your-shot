package com.nicholas.rutherford.track.your.shot.feature.settings.enabledpermissions

data class EnabledPermissionsParams(
    val onToolbarMenuClicked: () -> Unit,
    val onSwitchChangedToTurnOffPermission: () -> Unit,
    val permissionNotGrantedForCameraAlert: () -> Unit,
    val permissionNotGrantedForReadMediaOrExternalStorageAlert: () -> Unit
)
