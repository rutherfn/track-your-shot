package com.nicholas.rutherford.track.your.shot.feature.players.createeditplayer.helper

import android.app.Application
import com.nicholas.rutherford.track.your.shot.base.resources.StringsIds
import com.nicholas.rutherford.track.your.shot.data.room.response.ShotLogged
import com.nicholas.rutherford.track.your.shot.data.shared.alert.Alert
import com.nicholas.rutherford.track.your.shot.data.shared.alert.AlertConfirmAndDismissButton
import com.nicholas.rutherford.track.your.shot.data.shared.sheet.Sheet
import com.nicholas.rutherford.track.your.shot.feature.players.createeditplayer.CreateEditPlayerState
import com.nicholas.rutherford.track.your.shot.feature.players.shots.logshot.pendingshot.PendingShot
import com.nicholas.rutherford.track.your.shot.firebase.realtime.ShotLoggedRealtimeResponse
import com.nicholas.rutherford.track.your.shot.helper.extensions.safeLet

/**
 * Helper class containing shared logic used by both create and edit player operations.
 *
 * This class provides utility functions that are common to both creating new players
 * and editing existing players, such as:
 * - Generating hint text for shot logging
 * - Processing pending shots
 * - Converting shot data between formats
 * - Creating alert dialogs
 * - Creating bottom sheets
 * - Validating player input
 *
 * All functions are documented with their use cases and expected behavior.
 */
class CreateEditPlayerSharedHelper(private val application: Application) {

    /**
     * Generates the hint text for logging a new shot based on the player's name.
     *
     * The hint text dynamically changes based on what player information is available:
     * - If both first and last name are provided: "Press the \"Log Shots\" button to record shots for first name and last name"
     * - If only first name is provided: "Press the \"Log Shots\" button to record shots for first name"
     * - If only last name is provided: "Press the \"Log Shots\" button to record shots for last name"
     * - If neither is provided: "Press the \"Log Shots\" button to record shots for the player."
     *
     * Use cases:
     * - When creating a new player and the user has entered a name
     * - When editing an existing player and displaying the shot logging hint
     * - When the player name changes and the hint needs to be updated
     *
     * @param firstName Optional first name of the player. Can be null or empty.
     * @param lastName Optional last name of the player. Can be null or empty.
     * @param currentHintText The current hint text from the state, used as a fallback.
     * @return The generated hint text string based on the provided names.
     */
    fun hintLogNewShotText(
        firstName: String? = null,
        lastName: String? = null,
        currentHintText: String = ""
    ): String {
        var hintLogNewShotText: String = currentHintText

        safeLet(firstName, lastName) { first, last ->
            hintLogNewShotText = when {
                first.isNotEmpty() && last.isNotEmpty() -> {
                    application.getString(StringsIds.hintLogNewShotsForPlayer) + " $first $last"
                }
                first.isNotEmpty() -> {
                    application.getString(StringsIds.hintLogNewShotsForPlayer) + " $first"
                }
                last.isNotEmpty() -> {
                    application.getString(StringsIds.hintLogNewShotsForPlayer) + " $last"
                }
                else -> {
                    application.getString(StringsIds.hintLogNewShots)
                }
            }
        } ?: run {
            hintLogNewShotText = application.getString(StringsIds.hintLogNewShots)
        }

        return hintLogNewShotText
    }

    /**
     * Validates the player input before creating or editing.
     *
     * This function checks if the required fields are filled in:
     * - First name must not be empty (required field)
     *
     * Use cases:
     * - Before creating a new player
     * - Before updating an existing player
     * - When the user clicks the save/create button
     *
     * @param state The current screen state containing player information.
     * @return true if validation passes (first name is not empty), false otherwise.
     */
    fun validatePlayer(state: CreateEditPlayerState): Boolean {
        return state.firstName.isNotEmpty()
    }

    /**
     * Returns a list of shots that are not pending from the current edited player.
     *
     * This function filters out any shots that are currently in the pending shot list,
     * ensuring only confirmed shots are returned.
     *
     * Use cases:
     * - When displaying the list of confirmed shots
     * - When updating the UI after a shot is confirmed
     * - When processing pending shots and need to separate confirmed from pending
     *
     * @param editedPlayerShots List of shots from the edited player.
     * @param pendingShotLoggedList List of pending shots that should be excluded.
     * @return List of confirmed shots (not in the pending list).
     */
    fun currentShotsNotPending(
        editedPlayerShots: List<ShotLogged>,
        pendingShotLoggedList: List<PendingShot>
    ): List<ShotLogged> {
        val currentShotsArrayList: ArrayList<ShotLogged> = arrayListOf()

        if (editedPlayerShots.isNotEmpty()) {
            val pendingShotIds = pendingShotLoggedList.map { it.shotLogged.id }

            editedPlayerShots.forEach { shot ->
                if (!pendingShotIds.contains(shot.id)) {
                    currentShotsArrayList.add(shot)
                }
            }
        }

        return currentShotsArrayList.toList()
    }

    /**
     * Combines current shots with any pending shots (not yet confirmed).
     *
     * This function merges confirmed shots with pending shots, marking pending shots
     * as not pending in the realtime response format for Firebase upload.
     *
     * Use cases:
     * - When creating a player in Firebase and need to include pending shots
     * - When updating a player in Firebase and need to include pending shots
     * - When syncing shot data to Firebase
     *
     * @param currentShotList List of confirmed shots already converted to realtime response format.
     * @param pendingShotLoggedList List of pending shots to include.
     * @return Combined list including pending shots marked as not pending.
     */
    fun currentShotLoggedRealtimeResponseList(
        currentShotList: List<ShotLoggedRealtimeResponse>,
        pendingShotLoggedList: List<PendingShot>
    ): List<ShotLoggedRealtimeResponse> {
        if (pendingShotLoggedList.isNotEmpty()) {
            val shotLoggedRealtimeResponseArrayList: ArrayList<ShotLoggedRealtimeResponse> =
                arrayListOf()

            pendingShotLoggedList.forEach { pendingShot ->
                shotLoggedRealtimeResponseArrayList.add(
                    ShotLoggedRealtimeResponse(
                        id = pendingShot.shotLogged.id,
                        shotName = pendingShot.shotLogged.shotName,
                        shotType = pendingShot.shotLogged.shotType,
                        shotsAttempted = pendingShot.shotLogged.shotsAttempted,
                        shotsMade = pendingShot.shotLogged.shotsMade,
                        shotsMissed = pendingShot.shotLogged.shotsMissed,
                        shotsMadePercentValue = pendingShot.shotLogged.shotsMadePercentValue,
                        shotsMissedPercentValue = pendingShot.shotLogged.shotsMissedPercentValue,
                        shotsAttemptedMillisecondsValue = pendingShot.shotLogged.shotsAttemptedMillisecondsValue,
                        shotsLoggedMillisecondsValue = pendingShot.shotLogged.shotsLoggedMillisecondsValue,
                        isPending = false
                    )
                )
            }
            return currentShotList + shotLoggedRealtimeResponseArrayList.toList()
        } else {
            return currentShotList
        }
    }

    /**
     * Combines current shots with pending shots (marked as not pending).
     *
     * This function merges confirmed shots with pending shots, converting pending shots
     * to confirmed shots by setting isPending to false.
     *
     * Use cases:
     * - When saving a player locally and need to include pending shots
     * - When updating the player's shot list after confirmation
     * - When preparing player data for local storage
     *
     * @param currentShotLoggedList List of confirmed shots.
     * @param pendingShotLoggedList List of pending shots to include.
     * @return Combined list including pending shots marked as not pending.
     */
    fun currentShotLoggedList(
        currentShotLoggedList: List<ShotLogged>,
        pendingShotLoggedList: List<PendingShot>
    ): List<ShotLogged> {
        return if (pendingShotLoggedList.isNotEmpty()) {
            currentShotLoggedList + pendingShotLoggedList.map { pendingShot ->
                pendingShot.shotLogged.copy(
                    isPending = false
                )
            }
        } else {
            currentShotLoggedList
        }
    }

    /**
     * Converts a [ShotLogged] instance to a [ShotLoggedRealtimeResponse].
     *
     * Maps all relevant shot properties and preserves the pending state.
     *
     * Use cases:
     * - When uploading shot data to Firebase
     * - When converting local shot data to Firebase format
     * - When syncing shots between local and remote storage
     *
     * @param shotLogged The [ShotLogged] instance to convert.
     * @return The corresponding [ShotLoggedRealtimeResponse] instance.
     */
    fun shotLoggedToRealtimeResponse(shotLogged: ShotLogged): ShotLoggedRealtimeResponse {
        return ShotLoggedRealtimeResponse(
            id = shotLogged.id,
            shotName = shotLogged.shotName,
            shotType = shotLogged.shotType,
            shotsAttempted = shotLogged.shotsAttempted,
            shotsMade = shotLogged.shotsMade,
            shotsMissed = shotLogged.shotsMissed,
            shotsMadePercentValue = shotLogged.shotsMadePercentValue,
            shotsMissedPercentValue = shotLogged.shotsMissedPercentValue,
            shotsAttemptedMillisecondsValue = shotLogged.shotsAttemptedMillisecondsValue,
            shotsLoggedMillisecondsValue = shotLogged.shotsLoggedMillisecondsValue,
            isPending = shotLogged.isPending
        )
    }

    /**
     * Creates an Alert to inform user a shot was updated.
     *
     * Use cases:
     * - When a pending shot is updated and needs confirmation
     * - When displaying notification about shot changes
     *
     * @return Alert instance with shot updated message.
     */
    fun showUpdatedAlert(): Alert {
        return Alert(
            title = application.getString(StringsIds.shotUpdated),
            dismissButton = AlertConfirmAndDismissButton(buttonText = application.getString(StringsIds.gotIt)),
            description = application.getString(StringsIds.currentShotHasBeenUpdatedDescription)
        )
    }

    /**
     * Creates an alert dialog informing the user that camera permission has been declined.
     *
     * Provides options to navigate to app settings or dismiss the alert.
     *
     * Use cases:
     * - When user denies camera permission for taking a picture
     * - When camera permission is required but not granted
     *
     * @param onNavigateToSettings Callback to navigate to app settings.
     * @return Alert configured for camera permission denial.
     */
    fun cameraPermissionNotGrantedAlert(onNavigateToSettings: () -> Unit): Alert {
        return Alert(
            title = application.getString(StringsIds.permissionHasBeenDeclined),
            confirmButton = AlertConfirmAndDismissButton(buttonText = application.getString(StringsIds.settings), onButtonClicked = onNavigateToSettings),
            dismissButton = AlertConfirmAndDismissButton(buttonText = application.getString(StringsIds.notNow)),
            description = application.getString(StringsIds.cameraPermissionHasBeenDeniedDescription)
        )
    }

    /**
     * Creates an alert dialog informing the user that media or external storage permission has been declined.
     *
     * The description changes depending on whether the permission should be requested again.
     *
     * Use cases:
     * - When user denies media/external storage permission for gallery access
     * - When permission is required but not granted
     *
     * @param shouldAskForPermission Indicates if permission should be requested again; affects description text.
     * @param onNavigateToSettings Callback to navigate to app settings.
     * @return Alert configured for media or external storage permission denial.
     */
    fun mediaOrExternalStorageNotGrantedAlert(
        shouldAskForPermission: Boolean,
        onNavigateToSettings: () -> Unit
    ): Alert {
        return Alert(
            title = application.getString(StringsIds.permissionHasBeenDeclined),
            confirmButton = AlertConfirmAndDismissButton(buttonText = application.getString(StringsIds.settings), onButtonClicked = onNavigateToSettings),
            dismissButton = AlertConfirmAndDismissButton(buttonText = application.getString(StringsIds.notNow)),
            description = if (!shouldAskForPermission) {
                application.getString(StringsIds.readExternalStorageDescription)
            } else {
                application.getString(StringsIds.readMediaImagesDescription)
            }
        )
    }

    /**
     * Creates an alert dialog indicating that no first name has been entered.
     *
     * Use cases:
     * - When user tries to save a player without a first name
     * - When validation fails for required first name field
     *
     * @return Alert for empty first name input.
     */
    fun firstNameEmptyAlert(): Alert {
        return Alert(
            title = application.getString(StringsIds.noFirstNameEntered),
            dismissButton = AlertConfirmAndDismissButton(buttonText = application.getString(StringsIds.gotIt)),
            description = application.getString(StringsIds.playersFirstNameEmptyDescription)
        )
    }

    /**
     * Creates an alert dialog informing the user that there is no internet connection.
     *
     * Use cases:
     * - When user tries to create/edit player without internet
     * - When network operation fails due to no connectivity
     *
     * @return Alert for lack of internet connectivity.
     */
    fun notConnectedToInternetAlert(): Alert {
        return Alert(
            title = application.getString(StringsIds.notConnectedToInternet),
            description = application.getString(StringsIds.weHaveDetectedCurrentlyNotConnectedToInternetDescription),
            dismissButton = AlertConfirmAndDismissButton(buttonText = application.getString(StringsIds.gotIt))
        )
    }

    /**
     * Creates an alert dialog indicating that image upload was unsuccessful.
     *
     * Use cases:
     * - When Firebase image upload fails
     * - When image cannot be processed or uploaded
     *
     * @return Alert for failed image upload.
     */
    fun notAbleToUploadImageAlert(): Alert {
        return Alert(
            title = application.getString(StringsIds.unableToUploadImage),
            description = application.getString(StringsIds.theImageUploadWasUnsuccessful),
            dismissButton = AlertConfirmAndDismissButton(buttonText = application.getString(StringsIds.ok))
        )
    }

    /**
     * Creates an alert dialog indicating that a problem with the user's account was detected.
     *
     * Use cases:
     * - When Firebase operations fail due to account issues
     * - When user authentication or account data is invalid
     *
     * @return Alert for detected account issues.
     */
    fun weHaveDetectedAProblemWithYourAccountAlert(): Alert {
        return Alert(
            title = application.getString(StringsIds.issueOccurred),
            description = application.getString(StringsIds.weHaveDetectedAProblemWithYourAccountPleaseContactSupportToResolveIssue),
            dismissButton = AlertConfirmAndDismissButton(buttonText = application.getString(StringsIds.gotIt))
        )
    }

    /**
     * Creates an alert dialog indicating that the player has already been added.
     *
     * Use cases:
     * - When user tries to create a player with a name that already exists
     * - When duplicate player name is detected
     *
     * @return Alert for duplicate player addition.
     */
    fun playerAlreadyHasBeenAddedAlert(): Alert {
        return Alert(
            title = application.getString(StringsIds.issueOccurred),
            description = application.getString(StringsIds.playerAlreadyHasBeenAddedDescription),
            dismissButton = AlertConfirmAndDismissButton(buttonText = application.getString(StringsIds.gotIt))
        )
    }

    /**
     * Creates an alert dialog asking the user to confirm proceeding with unsaved player changes.
     *
     * Use cases:
     * - When user tries to navigate away with unsaved changes
     * - When pending players or shots exist and user wants to leave
     *
     * @param onConfirm Callback when user confirms to proceed.
     * @return Alert prompting confirmation for unsaved changes.
     */
    fun unsavedPlayerChangesAlert(onConfirm: () -> Unit): Alert {
        return Alert(
            title = application.getString(StringsIds.unsavedPlayerChanges),
            description = application.getString(StringsIds.doYouWishToProceedDescription),
            confirmButton = AlertConfirmAndDismissButton(buttonText = application.getString(StringsIds.yes), onButtonClicked = onConfirm),
            dismissButton = AlertConfirmAndDismissButton(buttonText = application.getString(StringsIds.no), onButtonClicked = {})
        )
    }

    /**
     * Creates a [Sheet] UI model representing an option sheet to remove the player image.
     *
     * Use cases:
     * - When user clicks on an existing image to remove it
     * - When editing a player with an existing image
     *
     * @return A [Sheet] instance with a title and a single option to remove the image.
     */
    fun removeImageSheet(): Sheet {
        return Sheet(
            title = application.getString(StringsIds.chooseOption),
            values = listOf(application.getString(StringsIds.removeImage))
        )
    }

    /**
     * Creates a [Sheet] UI model representing an option sheet for selecting an image source.
     *
     * Provides options to either choose an image from the gallery or take a new picture.
     *
     * Use cases:
     * - When user wants to add an image to a new player
     * - When user wants to change an existing player's image
     *
     * @return A [Sheet] instance with a title and two options: choose from gallery or take a picture.
     */
    fun chooseFromGalleryOrTakePictureSheet(): Sheet {
        return Sheet(
            title = application.getString(StringsIds.chooseOption),
            values = listOf(
                application.getString(StringsIds.chooseImageFromGallery),
                application.getString(StringsIds.takeAPicture)
            )
        )
    }
}

