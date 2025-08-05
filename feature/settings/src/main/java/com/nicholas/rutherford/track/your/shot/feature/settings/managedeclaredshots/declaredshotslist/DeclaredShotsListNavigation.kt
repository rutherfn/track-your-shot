package com.nicholas.rutherford.track.your.shot.feature.settings.managedeclaredshots.declaredshotslist

import com.nicholas.rutherford.track.your.shot.data.shared.progress.Progress


/**
 * Defines navigation actions available from the DeclaredShotsList screen.
 */
interface DeclaredShotsListNavigation {
    fun disableProgress()
    fun enableProgress(progress: Progress)
    fun createEditDeclaredShot(shotName: String)
    fun pop()
}
