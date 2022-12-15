package com.nicholas.rutherford.track.my.shot.feature.create.account

import com.nicholas.rutherford.track.my.shot.navigation.NavigationDestinations
import com.nicholas.rutherford.track.my.shot.navigation.Navigator

class CreateAccountNavigationImpl(private val navigator: Navigator) : CreateAccountNavigation {
    override fun pop() = navigator.pop(popRouteAction = NavigationDestinations.LOGIN_SCREEN)
}
