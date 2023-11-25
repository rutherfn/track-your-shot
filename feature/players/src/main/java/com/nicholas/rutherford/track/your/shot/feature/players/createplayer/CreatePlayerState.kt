package com.nicholas.rutherford.track.your.shot.feature.players.createplayer

data class CreatePlayerState (
    val firstName: String = "",
    val lastName: String = "",
    val playerPositionValue: Int = 0,
    val imagePath: String? = null
)