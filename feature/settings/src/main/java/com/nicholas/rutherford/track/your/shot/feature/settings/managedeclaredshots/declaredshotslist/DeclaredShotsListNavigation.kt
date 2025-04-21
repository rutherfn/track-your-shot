package com.nicholas.rutherford.track.your.shot.feature.settings.managedeclaredshots.declaredshotslist

import com.nicholas.rutherford.track.your.shot.data.shared.progress.Progress

interface DeclaredShotsListNavigation {
    fun disableProgress()
    fun enableProgress(progress: Progress)
    fun createEditDeclaredShot()
    fun pop()
}
