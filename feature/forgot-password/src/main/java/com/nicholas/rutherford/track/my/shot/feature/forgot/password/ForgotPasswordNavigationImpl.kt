package com.nicholas.rutherford.track.my.shot.feature.forgot.password

import com.nicholas.rutherford.track.my.shot.navigation.NavigationDestinations
import com.nicholas.rutherford.track.my.shot.navigation.Navigator

class ForgotPasswordNavigationImpl(private val navigator: Navigator) : ForgotPasswordNavigation {

    override fun pop() = navigator.pop(NavigationDestinations.LOGIN_SCREEN)
}
