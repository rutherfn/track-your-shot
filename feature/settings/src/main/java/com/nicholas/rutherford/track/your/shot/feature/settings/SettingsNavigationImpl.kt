package com.nicholas.rutherford.track.your.shot.feature.settings

import com.nicholas.rutherford.track.your.shot.navigation.Navigator

class SettingsNavigationImpl(private val navigator: Navigator): SettingsNavigation {
    override fun openNavigationDrawer() = navigator.showNavigationDrawer(navigationDrawerAction = true)


}