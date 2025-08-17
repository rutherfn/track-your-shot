package com.nicholas.rutherford.track.your.shot.feature.create.account.createaccount

import com.nicholas.rutherford.track.your.shot.data.shared.alert.Alert
import com.nicholas.rutherford.track.your.shot.data.shared.progress.Progress
import com.nicholas.rutherford.track.your.shot.navigation.NavigationActions
import com.nicholas.rutherford.track.your.shot.navigation.NavigationDestinations
import com.nicholas.rutherford.track.your.shot.navigation.Navigator

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Implementation of [CreateAccountNavigation] using a [Navigator] to perform
 * navigation via predefined [NavigationActions].
 */
class CreateAccountNavigationImpl(private val navigator: Navigator) : CreateAccountNavigation {

    override fun alert(alert: Alert) = navigator.alert(alertAction = alert)
    override fun disableProgress() = navigator.progress(progressAction = null)
    override fun enableProgress(progress: Progress) = navigator.progress(progressAction = progress)
    override fun navigateToAuthentication(email: String, username: String) = navigator.navigate(navigationAction = NavigationActions.CreateAccountScreen.authentication(username = username, email = email))
    override fun navigateToTermsAndConditions() = navigator.navigate(navigationAction = NavigationActions.CreateAccountScreen.termsConditions(shouldAcceptTerms = true))
    override fun pop() = navigator.pop(popRouteAction = NavigationDestinations.LOGIN_SCREEN)
}
