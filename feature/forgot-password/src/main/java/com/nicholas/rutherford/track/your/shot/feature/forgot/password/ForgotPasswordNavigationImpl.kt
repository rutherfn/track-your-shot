package com.nicholas.rutherford.track.your.shot.feature.forgot.password

import com.nicholas.rutherford.track.your.shot.data.shared.alert.Alert
import com.nicholas.rutherford.track.your.shot.data.shared.progress.Progress
import com.nicholas.rutherford.track.your.shot.navigation.NavigationActions
import com.nicholas.rutherford.track.your.shot.navigation.NavigationDestinations
import com.nicholas.rutherford.track.your.shot.navigation.Navigator

/**
 * Implementation of [ForgotPasswordNavigation] using a [Navigator] to perform
 * navigation via predefined [NavigationActions].
 */
class ForgotPasswordNavigationImpl(private val navigator: Navigator) : ForgotPasswordNavigation {
    override fun alert(alert: Alert) = navigator.alert(alertAction = alert)
    override fun disableProgress() = navigator.progress(progressAction = null)

    override fun enableProgress(progress: Progress) = navigator.progress(progressAction = progress)

    override fun pop() = navigator.pop(popRouteAction = NavigationDestinations.LOGIN_SCREEN)
}
