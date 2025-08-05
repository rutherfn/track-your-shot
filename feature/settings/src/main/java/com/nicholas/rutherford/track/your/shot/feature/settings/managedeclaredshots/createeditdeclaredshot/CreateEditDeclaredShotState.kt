package com.nicholas.rutherford.track.your.shot.feature.settings.managedeclaredshots.createeditdeclaredshot

import com.nicholas.rutherford.track.your.shot.data.room.response.DeclaredShot

/**
 * Holds the UI state for the Create Edit Declared Shot screen.
 *
 * @property currentDeclaredShot The currently selected declared shot.
 * @property declaredShotState The current state of the declared shot.
 */
data class CreateEditDeclaredShotState(
    val currentDeclaredShot: DeclaredShot? = null,
    val declaredShotState: DeclaredShotState = DeclaredShotState.NONE
)
