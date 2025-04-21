package com.nicholas.rutherford.track.your.shot.feature.settings.managedeclaredshots.createeditdeclaredshot

import com.nicholas.rutherford.track.your.shot.data.shared.alert.Alert
import com.nicholas.rutherford.track.your.shot.data.shared.progress.Progress

interface CreateEditDeclaredShotNavigation {
    fun alert(alert: Alert)
    fun disableProgress()
    fun enableProgress(progress: Progress)
    fun pop()
}
