package com.nicholas.rutherford.track.your.shot.feature.players.createeditplayer

import android.net.Uri

/**
 * Defines all state and callback parameters required by the Create/Edit Player screen.
 *
 * This class is passed down to composables to handle UI state, user interactions,
 * and side effects in a consistent and centralized way.
 *
 * @property state The current UI state of the player creation or editing flow.
 * @property updateImageUriState Callback to update the image URI when a new image is selected.
 * @property onClearImageState Callback to clear the current image selection and state.
 * @property onToolbarMenuClicked Called when the user interacts with the top app bar menu (e.g., Save or Close).
 * @property onLogShotsClicked Called when the user taps the "Log Shots" button.
 * @property onFirstNameValueChanged Called when the first name input is changed.
 * @property onLastNameValueChanged Called when the last name input is changed.
 * @property onPlayerPositionStringChanged Called when the user selects a new player position.
 * @property onImageUploadClicked Called when the user taps the image upload area.
 * @property onCreatePlayerClicked Called when the user attempts to create or update a player.
 * @property permissionNotGrantedForCameraAlert Called when camera permissions are not granted and the user must be alerted.
 * @property onSelectedCreateEditImageOption Converts a selected image option URI string into a [CreateEditImageOption].
 * @property onViewShotClicked Called when an existing shot is tapped for viewing.
 * @property onViewPendingShotClicked Called when a pending (unlogged) shot is tapped for viewing.
 */
data class CreateEditPlayerParams(
    val state: CreateEditPlayerState,
    val updateImageUriState: (uri: Uri?) -> Unit,
    val onClearImageState: () -> Unit,
    val onToolbarMenuClicked: () -> Unit,
    val onLogShotsClicked: () -> Unit,
    val onFirstNameValueChanged: (newFirstName: String) -> Unit,
    val onLastNameValueChanged: (newFirstName: String) -> Unit,
    val onPlayerPositionStringChanged: (newPosition: String) -> Unit,
    val onImageUploadClicked: (uri: Uri?) -> Unit,
    val onCreatePlayerClicked: () -> Unit,
    val permissionNotGrantedForCameraAlert: () -> Unit,
    val onSelectedCreateEditImageOption: (uri: String) -> CreateEditImageOption,
    val onViewShotClicked: (shotType: Int, shotId: Int) -> Unit,
    val onViewPendingShotClicked: (shotType: Int, shotId: Int) -> Unit
)

