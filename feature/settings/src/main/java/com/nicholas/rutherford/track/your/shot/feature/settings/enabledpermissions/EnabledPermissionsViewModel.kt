package com.nicholas.rutherford.track.your.shot.feature.settings.enabledpermissions

import android.app.Application
import com.nicholas.rutherford.track.your.shot.base.resources.StringsIds
import com.nicholas.rutherford.track.your.shot.base.vm.BaseViewModel
import com.nicholas.rutherford.track.your.shot.data.shared.alert.Alert
import com.nicholas.rutherford.track.your.shot.data.shared.alert.AlertConfirmAndDismissButton

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * ViewModel for the Enabled Permissions screen, responsible for handling permission-related actions
 * and presenting alerts when permissions are not granted.
 *
 * @property navigation Interface for handling navigation events from the Enabled Permissions screen.
 * @property application Application instance used for accessing resources like strings.
 */
class EnabledPermissionsViewModel(
    private val navigation: EnabledPermissionsNavigation,
    private val application: Application
) : BaseViewModel() {

    /**
     * Handles the click event on the toolbar menu button.
     * Navigates back to the previous screen.
     */
    fun onToolbarMenuClicked() = navigation.pop()

    /**
     * Handles the action when the user turns off a permission from the switch.
     * Navigates the user to the application settings screen.
     */
    fun onSwitchChangedToTurnOffPermission() = navigation.appSettings()

    /**
     * Builds an [Alert] notifying the user that the camera permission is not granted.
     * Provides an option to open the settings screen or dismiss the alert.
     *
     * @return An [Alert] configured with a title, description, and action buttons.
     */
    internal fun cameraPermissionNotGrantedAlert(): Alert {
        return Alert(
            title = application.getString(StringsIds.permissionHasBeenDeclined),
            confirmButton = AlertConfirmAndDismissButton(
                buttonText = application.getString(StringsIds.settings),
                onButtonClicked = { onSwitchChangedToTurnOffPermission() }
            ),
            dismissButton = AlertConfirmAndDismissButton(
                buttonText = application.getString(StringsIds.notNow)
            ),
            description = application.getString(StringsIds.cameraPermissionHasBeenDeniedDescription)
        )
    }

    /**
     * Displays the camera permission not granted alert.
     */
    fun permissionNotGrantedForCameraAlert() = navigation.alert(alert = cameraPermissionNotGrantedAlert())
}
