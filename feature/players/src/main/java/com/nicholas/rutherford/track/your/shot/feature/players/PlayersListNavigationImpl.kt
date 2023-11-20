package com.nicholas.rutherford.track.your.shot.feature.players

import com.nicholas.rutherford.track.your.shot.data.shared.alert.Alert
import com.nicholas.rutherford.track.your.shot.navigation.Navigator

class PlayersListNavigationImpl(
    private val navigator: Navigator
) : PlayersListNavigation {
    override fun alert(alert: Alert) = navigator.alert(alertAction = alert)
    override fun openNavigationDrawer() = navigator.showNavigationDrawer(navigationDrawerAction = true)
}
