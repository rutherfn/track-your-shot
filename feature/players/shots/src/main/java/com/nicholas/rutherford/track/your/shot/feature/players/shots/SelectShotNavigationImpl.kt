package com.nicholas.rutherford.track.your.shot.feature.players.shots

import com.nicholas.rutherford.track.your.shot.navigation.NavigationDestinations
import com.nicholas.rutherford.track.your.shot.navigation.Navigator

class SelectShotNavigationImpl(private val navigator: Navigator) : SelectShotNavigation {
    override fun popFromCreatePlayer() = navigator.pop(popRouteAction = NavigationDestinations.CREATE_EDIT_PLAYER_SCREEN)

    override fun popFromEditPlayer() = navigator.pop(popRouteAction = NavigationDestinations.CREATE_EDIT_PLAYER_SCREEN_WITH_PARAMS)

}
