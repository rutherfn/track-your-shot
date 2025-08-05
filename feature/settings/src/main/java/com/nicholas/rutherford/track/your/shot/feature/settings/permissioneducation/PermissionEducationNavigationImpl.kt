package com.nicholas.rutherford.track.your.shot.feature.settings.permissioneducation

import com.nicholas.rutherford.track.your.shot.navigation.NavigationActions
import com.nicholas.rutherford.track.your.shot.navigation.NavigationDestinations
import com.nicholas.rutherford.track.your.shot.navigation.Navigator

/**
 * Implementation of [PermissionEducationNavigation] using a [Navigator] to perform
 * navigation via predefined [NavigationActions].
 */
class PermissionEducationNavigationImpl(private val navigator: Navigator) : PermissionEducationNavigation {

    override fun navigateToUrl(url: String) = navigator.url(url = url)

    override fun pop() = navigator.pop(popRouteAction = NavigationDestinations.SETTINGS_SCREEN)
}
