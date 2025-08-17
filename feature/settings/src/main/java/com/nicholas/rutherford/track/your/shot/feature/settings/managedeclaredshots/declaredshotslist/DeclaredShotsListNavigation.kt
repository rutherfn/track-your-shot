package com.nicholas.rutherford.track.your.shot.feature.settings.managedeclaredshots.declaredshotslist

import com.nicholas.rutherford.track.your.shot.data.shared.progress.Progress

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Defines navigation actions available from the DeclaredShotsList screen.
 */
interface DeclaredShotsListNavigation {
    fun disableProgress()
    fun enableProgress(progress: Progress)
    fun createEditDeclaredShot(shotName: String)
    fun pop()
}
