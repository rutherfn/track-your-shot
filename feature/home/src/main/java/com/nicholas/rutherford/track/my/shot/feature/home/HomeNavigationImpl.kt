package com.nicholas.rutherford.track.my.shot.feature.home

import com.nicholas.rutherford.track.my.shot.navigation.NavigationActions
import com.nicholas.rutherford.track.my.shot.navigation.Navigator

class HomeNavigationImpl(private val navigator: Navigator) : HomeNavigation {

    override fun navigateToLogin() = navigator.navigate(navigationAction = NavigationActions.HomeScreen.login())
}
