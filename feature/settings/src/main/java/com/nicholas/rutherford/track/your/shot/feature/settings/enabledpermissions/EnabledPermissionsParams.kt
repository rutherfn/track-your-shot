package com.nicholas.rutherford.track.your.shot.feature.settings.enabledpermissions

/**
 * Parameters used to render the EnabledPermissionsScreen UI.
 *
 * @property onToolbarMenuClicked Callback function triggered when the user interacts with the toolbar menu.
 * @property onSwitchChangedToTurnOffPermission Callback function triggered when the switch state changes.
 * @property permissionNotGrantedForCameraAlert Callback function triggered when the user denies camera permission.
 */
data class EnabledPermissionsParams(
    val onToolbarMenuClicked: () -> Unit,
    val onSwitchChangedToTurnOffPermission: () -> Unit,
    val permissionNotGrantedForCameraAlert: () -> Unit
)
