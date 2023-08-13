package com.nicholas.rutherford.track.my.shot.feature.login

import com.nicholas.rutherford.track.my.shot.data.shared.alert.Alert
import com.nicholas.rutherford.track.my.shot.data.shared.progress.Progress
import com.nicholas.rutherford.track.my.shot.navigation.NavigationActions
import com.nicholas.rutherford.track.my.shot.navigation.Navigator

class LoginNavigationImpl(private val navigator: Navigator) : LoginNavigation {
    override fun alert(alert: Alert) = navigator.alert(alertAction = alert)

    override fun disableProgress() = navigator.progress(progressAction = null)

    override fun enableProgress(progress: Progress) = navigator.progress(progressAction = progress)

    override fun navigateToHome() = navigator.navigate(navigationAction = NavigationActions.LoginScreen.home())

    override fun navigateToCreateAccount() = navigator.navigate(navigationAction = NavigationActions.LoginScreen.createAccount())

    override fun navigateToForgotPassword() = navigator.navigate(navigationAction = NavigationActions.LoginScreen.forgotPassword())
}
