package com.nicholas.rutherford.track.your.shot.feature.settings.accountinfo

import com.nicholas.rutherford.track.your.shot.helper.constants.Constants
import com.nicholas.rutherford.track.your.shot.navigation.Navigator

class AccountInfoNavigationImpl(private val navigator: Navigator) : AccountInfoNavigation {
    override fun pop() = navigator.pop(popRouteAction = Constants.POP_DEFAULT_ACTION)
}
