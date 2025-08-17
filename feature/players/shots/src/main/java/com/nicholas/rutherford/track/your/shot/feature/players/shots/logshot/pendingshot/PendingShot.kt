package com.nicholas.rutherford.track.your.shot.feature.players.shots.logshot.pendingshot

import com.nicholas.rutherford.track.your.shot.data.room.response.Player
import com.nicholas.rutherford.track.your.shot.data.room.response.ShotLogged
data class PendingShot(
    val player: Player,
    val shotLogged: ShotLogged,
    val isPendingPlayer: Boolean
)
