package com.nicholas.rutherford.track.your.shot.feature.create.account.createaccount

import com.nicholas.rutherford.track.your.shot.data.shared.alert.Alert
import com.nicholas.rutherford.track.your.shot.data.shared.progress.Progress
import com.nicholas.rutherford.track.your.shot.navigation.NavigationActions
import com.nicholas.rutherford.track.your.shot.navigation.NavigationDestinations
import com.nicholas.rutherford.track.your.shot.navigation.Navigator

class CreateAccountNavigationImpl(private val navigator: Navigator) : CreateAccountNavigation {

    override fun alert(alert: Alert) = navigator.alert(alertAction = alert)

    override fun disableProgress() = navigator.progress(progressAction = null)

    override fun enableProgress(progress: Progress) = navigator.progress(progressAction = progress)

    override fun navigateToAuthentication(email: String, username: String) = navigator.navigate(navigationAction = NavigationActions.CreateAccountScreen.authentication(username = username, email = email))

    override fun pop() = navigator.pop(popRouteAction = NavigationDestinations.LOGIN_SCREEN)
}
