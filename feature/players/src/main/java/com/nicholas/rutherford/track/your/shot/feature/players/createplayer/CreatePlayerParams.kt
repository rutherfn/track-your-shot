package com.nicholas.rutherford.track.your.shot.feature.players.createplayer

import android.net.Uri

data class CreatePlayerParams(
    val state: CreatePlayerState,
    val onToolbarMenuClicked: () -> Unit,
    val onFirstNameValueChanged: (newFirstName: String) -> Unit,
    val onLastNameValueChanged: (newFirstName: String) -> Unit,
    val onPlayerPositionStringResIdValueChanged: (newPositionStringResId: Int) -> Unit,
    val onImageUploadClicked: (uri: Uri?) -> Unit,
    val onCreatePlayerClicked: (uri: Uri?) -> Unit,
    val permissionNotGrantedForCameraAlert: () -> Unit,
    val permissionNotGrantedForReadMediaOrExternalStorageAlert: () -> Unit,
    val onSelectedCreateEditImageOption: (uri: String) -> CreateEditImageOption
)
