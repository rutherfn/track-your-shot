package com.nicholas.rutherford.track.your.shot.feature.players.createeditplayer

/**
 * Represents the available image options when editing or creating a player.
 *
 * Used to display image-related actions in a dialog or bottom sheet UI.
 *
 * Options include:
 * - [CHOOSE_IMAGE_FROM_GALLERY]: Open the device's image gallery to select an existing photo.
 * - [TAKE_A_PICTURE]: Launch the camera to capture a new photo.
 * - [REMOVE_IMAGE]: Remove the currently selected or uploaded image.
 * - [CANCEL]: Dismiss the image option selection without making any changes.
 */
enum class CreateEditImageOption {
    CHOOSE_IMAGE_FROM_GALLERY,
    TAKE_A_PICTURE,
    REMOVE_IMAGE,
    CANCEL
}

