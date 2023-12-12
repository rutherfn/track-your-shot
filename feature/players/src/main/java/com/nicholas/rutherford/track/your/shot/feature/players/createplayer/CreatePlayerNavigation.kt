package com.nicholas.rutherford.track.your.shot.feature.players.createplayer

import com.nicholas.rutherford.track.your.shot.data.shared.alert.Alert
import com.nicholas.rutherford.track.your.shot.data.shared.progress.Progress

interface CreatePlayerNavigation {
    fun alert(alert: Alert)
    fun appSettings()
    fun disableProgress()
    fun enableProgress(progress: Progress)
    fun navigateToPlayersList()
    fun pop()
}
