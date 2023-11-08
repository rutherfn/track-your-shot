package com.nicholas.rutherford.track.my.shot.feature.players

import com.nicholas.rutherford.track.my.shot.navigation.Navigator

class PlayersListNavigationImpl(
    private val navigator: Navigator
) : PlayersListNavigation {
    override fun openNavigationDrawer() = navigator.showNavigationDrawer(navigationDrawerAction = true)
}
