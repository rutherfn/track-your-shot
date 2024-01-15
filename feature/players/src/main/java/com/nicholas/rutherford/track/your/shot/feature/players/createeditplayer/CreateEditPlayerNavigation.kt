package com.nicholas.rutherford.track.your.shot.feature.players.createeditplayer

import com.nicholas.rutherford.track.your.shot.data.shared.alert.Alert
import com.nicholas.rutherford.track.your.shot.data.shared.progress.Progress

interface CreateEditPlayerNavigation {
    fun alert(alert: Alert)
    fun appSettings()
    fun navigateToSelectShot()
    fun disableProgress()
    fun enableProgress(progress: Progress)
    fun pop()
}
