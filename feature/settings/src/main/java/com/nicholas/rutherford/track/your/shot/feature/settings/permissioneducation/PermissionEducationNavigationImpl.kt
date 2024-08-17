package com.nicholas.rutherford.track.your.shot.feature.settings.permissioneducation

import com.nicholas.rutherford.track.your.shot.helper.constants.Constants
import com.nicholas.rutherford.track.your.shot.navigation.NavigationDestinations
import com.nicholas.rutherford.track.your.shot.navigation.Navigator

class PermissionEducationNavigationImpl(private val navigator: Navigator) : PermissionEducationNavigation {
    override fun pop() = navigator.pop(popRouteAction = Constants.POP_DEFAULT_ACTION)
}