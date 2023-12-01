package com.nicholas.rutherford.track.your.shot.feature.players.createplayer

data class CreatePlayerParams(
    val state: CreatePlayerState,
    val onToolbarMenuClicked: () -> Unit,
    val onFirstNameValueChanged: (newFirstName: String) -> Unit,
    val onLastNameValueChanged: (newFirstName: String) -> Unit,
    val onImageUploadClicked: () -> Unit,
    val onCreatePlayerClicked: () -> Unit
)
