package com.nicholas.rutherford.track.your.shot.feature.players.createplayer

data class CreatePlayerParams (
    val state: CreatePlayerState,
    val onImageUploadClicked: () -> Unit,
    val onCreatePlayerClicked: () -> Unit
)