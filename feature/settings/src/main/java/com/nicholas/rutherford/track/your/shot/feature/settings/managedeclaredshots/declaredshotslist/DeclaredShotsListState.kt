package com.nicholas.rutherford.track.your.shot.feature.settings.managedeclaredshots.declaredshotslist

import com.nicholas.rutherford.track.your.shot.data.room.response.DeclaredShot

/**
 * Holds the UI state for the Declared Shots List screen.
 *
 * @property declaredShotsList The current state of the declared shot.
 */
data class DeclaredShotsListState(
    val declaredShotsList: List<DeclaredShot> = emptyList()
)
