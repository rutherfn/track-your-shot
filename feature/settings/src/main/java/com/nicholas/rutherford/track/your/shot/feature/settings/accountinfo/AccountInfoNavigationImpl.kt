package com.nicholas.rutherford.track.your.shot.feature.settings.accountinfo

import com.nicholas.rutherford.track.your.shot.navigation.NavigationDestinations
import com.nicholas.rutherford.track.your.shot.navigation.Navigator

class AccountInfoNavigationImpl(private val navigator: Navigator) : AccountInfoNavigation {
    override fun pop() = navigator.pop(popRouteAction = NavigationDestinations.SETTINGS_SCREEN)
}
