package com.nicholas.rutherford.track.your.shot.feature.shots

import com.nicholas.rutherford.track.your.shot.navigation.Navigator

class ShotsListNavigationImpl(private val navigator: Navigator) : ShotsListNavigation {
    override fun openNavigationDrawer() = navigator.showNavigationDrawer(navigationDrawerAction = true)
}
