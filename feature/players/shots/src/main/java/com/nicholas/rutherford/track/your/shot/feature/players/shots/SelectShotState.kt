package com.nicholas.rutherford.track.your.shot.feature.players.shots

import com.nicholas.rutherford.track.your.shot.data.room.response.DeclaredShot

data class SelectShotState (
    val declaredShotList: List<DeclaredShot> = emptyList()
)