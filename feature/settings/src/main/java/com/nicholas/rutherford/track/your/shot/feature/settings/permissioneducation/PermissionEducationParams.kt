package com.nicholas.rutherford.track.your.shot.feature.settings.permissioneducation

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Parameters used to render the PermissionEducationScreen UI.
 *
 * @property onGotItButtonClicked Callback function triggered when the user clicks the "Got It" button.
 * @property onMoreInfoClicked Callback function triggered when the user clicks the "More Info" button.
 * @property state The current state of the permission education screen.
 */
data class PermissionEducationParams(
    val onGotItButtonClicked: () -> Unit,
    val onMoreInfoClicked: () -> Unit,
    val state: PermissionEducationState
)
