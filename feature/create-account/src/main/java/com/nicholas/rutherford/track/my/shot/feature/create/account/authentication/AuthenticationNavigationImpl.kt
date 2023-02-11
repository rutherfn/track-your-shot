package com.nicholas.rutherford.track.my.shot.feature.create.account.authentication

import com.nicholas.rutherford.track.my.shot.data.shared.alert.Alert
import com.nicholas.rutherford.track.my.shot.navigation.Navigator

class AuthenticationNavigationImpl(private val navigator: Navigator) : AuthenticationNavigation {

    override fun alert(alert: Alert) = navigator.alert(alertAction = alert)

    override fun openEmail() = navigator.emailAction(emailAction = true)

    override fun finish() = navigator.finish(finishAction = true)
}
