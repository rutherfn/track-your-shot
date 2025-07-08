package com.nicholas.rutherford.track.your.shot.feature.login

import com.nicholas.rutherford.track.your.shot.data.shared.alert.Alert
import com.nicholas.rutherford.track.your.shot.data.shared.progress.Progress
import com.nicholas.rutherford.track.your.shot.navigation.NavigationActions
import com.nicholas.rutherford.track.your.shot.navigation.Navigator

/**
 * Implementation of [LoginNavigation] using a [Navigator] to perform
 * navigation via predefined [NavigationActions].
 */
class LoginNavigationImpl(private val navigator: Navigator) : LoginNavigation {
    override fun alert(alert: Alert) = navigator.alert(alertAction = alert)

    override fun disableProgress() = navigator.progress(progressAction = null)

    override fun enableProgress(progress: Progress) = navigator.progress(progressAction = progress)

    override fun navigateToPlayersList() = navigator.navigate(navigationAction = NavigationActions.LoginScreen.playersList())

    override fun navigateToCreateAccount() = navigator.navigate(navigationAction = NavigationActions.LoginScreen.createAccount())

    override fun navigateToForgotPassword() = navigator.navigate(navigationAction = NavigationActions.LoginScreen.forgotPassword())
}
