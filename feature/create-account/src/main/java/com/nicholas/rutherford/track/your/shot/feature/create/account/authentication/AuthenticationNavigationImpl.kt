package com.nicholas.rutherford.track.your.shot.feature.create.account.authentication

import com.nicholas.rutherford.track.your.shot.data.shared.alert.Alert
import com.nicholas.rutherford.track.your.shot.data.shared.progress.Progress
import com.nicholas.rutherford.track.your.shot.navigation.NavigationActions
import com.nicholas.rutherford.track.your.shot.navigation.Navigator

class AuthenticationNavigationImpl(private val navigator: Navigator) : AuthenticationNavigation {

    override fun alert(alert: Alert) = navigator.alert(alertAction = alert)
    override fun disableProgress() = navigator.progress(progressAction = null)
    override fun enableProgress(progress: Progress) = navigator.progress(progressAction = progress)
    override fun navigateToTermsAndConditions() = navigator.navigate(navigationAction = NavigationActions.AuthenticationScreen.termsConditions(isAcknowledgeConditions = true))

    override fun navigateToLogin() = navigator.navigate(navigationAction = NavigationActions.AuthenticationScreen.login())
    override fun openEmail() = navigator.emailAction(emailAction = true)
    override fun finish() = navigator.finish(finishAction = true)
}
