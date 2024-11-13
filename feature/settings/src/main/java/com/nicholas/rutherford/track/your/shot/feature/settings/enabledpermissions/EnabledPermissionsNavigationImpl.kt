package com.nicholas.rutherford.track.your.shot.feature.settings.enabledpermissions

import com.nicholas.rutherford.track.your.shot.helper.constants.Constants
import com.nicholas.rutherford.track.your.shot.navigation.Navigator

class EnabledPermissionsNavigationImpl(private val navigator: Navigator) : EnabledPermissionsNavigation {
    override fun pop() = navigator.pop(popRouteAction = Constants.POP_DEFAULT_ACTION)
}