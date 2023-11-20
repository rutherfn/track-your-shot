package com.nicholas.rutherford.track.your.shot.feature.players

import com.nicholas.rutherford.track.your.shot.data.shared.alert.Alert

interface PlayersListNavigation {
    fun alert(alert: Alert)
    fun openNavigationDrawer()
}
