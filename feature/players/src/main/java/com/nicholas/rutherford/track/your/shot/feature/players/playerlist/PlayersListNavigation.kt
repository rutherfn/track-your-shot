package com.nicholas.rutherford.track.your.shot.feature.players.playerlist

import com.nicholas.rutherford.track.your.shot.data.shared.alert.Alert
import com.nicholas.rutherford.track.your.shot.data.shared.progress.Progress

interface PlayersListNavigation {
    fun alert(alert: Alert)
    fun disableProgress()
    fun enableProgress(progress: Progress)
    fun navigateToCreatePlayer()
    fun openNavigationDrawer()
}
