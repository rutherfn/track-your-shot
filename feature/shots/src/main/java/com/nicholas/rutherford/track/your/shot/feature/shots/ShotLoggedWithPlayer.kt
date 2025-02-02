package com.nicholas.rutherford.track.your.shot.feature.shots

import com.nicholas.rutherford.track.your.shot.data.room.response.ShotLogged

data class ShotLoggedWithPlayer(
    val shotLogged: ShotLogged,
    val playerId: Int,
    val playerName: String
)
