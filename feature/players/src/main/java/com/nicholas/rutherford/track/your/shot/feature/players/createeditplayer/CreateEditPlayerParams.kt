package com.nicholas.rutherford.track.your.shot.feature.players.createeditplayer

import android.net.Uri

data class CreateEditPlayerParams(
    val state: CreateEditPlayerState,
    val checkForExistingPlayer: () -> Unit,
    val onToolbarMenuClicked: () -> Unit,
    val onFirstNameValueChanged: (newFirstName: String) -> Unit,
    val onLastNameValueChanged: (newFirstName: String) -> Unit,
    val onPlayerPositionStringChanged: (newPosition: String) -> Unit,
    val onImageUploadClicked: (uri: Uri?) -> Unit,
    val onCreatePlayerClicked: (uri: Uri?) -> Unit,
    val permissionNotGrantedForCameraAlert: () -> Unit,
    val permissionNotGrantedForReadMediaOrExternalStorageAlert: () -> Unit,
    val onSelectedCreateEditImageOption: (uri: String) -> CreateEditImageOption
)
