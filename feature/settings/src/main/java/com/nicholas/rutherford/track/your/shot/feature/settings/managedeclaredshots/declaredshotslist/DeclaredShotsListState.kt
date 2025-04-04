package com.nicholas.rutherford.track.your.shot.feature.settings.managedeclaredshots.declaredshotslist

import com.nicholas.rutherford.track.your.shot.data.room.response.DeclaredShot

data class DeclaredShotsListState(
    val declaredShotsList: List<DeclaredShot> = emptyList()
)
