package com.nicholas.rutherford.track.my.shot.feature.create.account.createaccount

import com.nicholas.rutherford.track.my.shot.data.shared.alert.Alert
import com.nicholas.rutherford.track.my.shot.data.shared.progress.Progress
import com.nicholas.rutherford.track.my.shot.navigation.NavigationDestinations
import com.nicholas.rutherford.track.my.shot.navigation.Navigator

class CreateAccountNavigationImpl(private val navigator: Navigator) : CreateAccountNavigation {

    override fun alert(alert: Alert) = navigator.alert(alertAction = alert)

    override fun enableProgress(progress: Progress) = navigator.progress(progressAction = progress)

    override fun disableProgress() = navigator.progress(progressAction = null)

    override fun pop() = navigator.pop(popRouteAction = NavigationDestinations.LOGIN_SCREEN)
}
