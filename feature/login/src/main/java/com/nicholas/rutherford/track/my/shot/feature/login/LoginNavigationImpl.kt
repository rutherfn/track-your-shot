package com.nicholas.rutherford.track.my.shot.feature.login

import com.nicholas.rutherford.track.my.shot.navigation.NavigationActions
import com.nicholas.rutherford.track.my.shot.navigation.Navigator

class LoginNavigationImpl(private val navigator: Navigator) : LoginNavigation {

    override fun navigateToHome() = navigator.navigate(navigationAction = NavigationActions.LoginScreen.home())

    override fun navigateToCreateAccount() = navigator.navigate(navigationAction = NavigationActions.LoginScreen.createAccount())

    override fun navigateToForgotPassword() = navigator.navigate(navigationAction = NavigationActions.LoginScreen.forgotPassword())
}