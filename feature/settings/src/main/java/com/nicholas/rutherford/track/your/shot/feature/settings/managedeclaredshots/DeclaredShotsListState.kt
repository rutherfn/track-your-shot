package com.nicholas.rutherford.track.your.shot.feature.settings.managedeclaredshots

import com.nicholas.rutherford.track.your.shot.data.room.response.DeclaredShot

data class DeclaredShotsListState(
    val declaredShotsList: List<DeclaredShot> = emptyList()
)
