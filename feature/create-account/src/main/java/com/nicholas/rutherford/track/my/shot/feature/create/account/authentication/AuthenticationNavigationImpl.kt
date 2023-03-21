package com.nicholas.rutherford.track.my.shot.feature.create.account.authentication

import com.nicholas.rutherford.track.my.shot.data.shared.alert.Alert
import com.nicholas.rutherford.track.my.shot.data.shared.dialogtextfield.DialogTextField
import com.nicholas.rutherford.track.my.shot.data.shared.progress.Progress
import com.nicholas.rutherford.track.my.shot.navigation.NavigationActions
import com.nicholas.rutherford.track.my.shot.navigation.Navigator

class AuthenticationNavigationImpl(private val navigator: Navigator) : AuthenticationNavigation {

    override fun alert(alert: Alert) = navigator.alert(alertAction = alert)
    override fun dialogWithTextField(dialogTextField: DialogTextField) = navigator.dialogWithTextField(dialogWithTextFieldAction = dialogTextField)
    override fun disableProgress() = navigator.progress(progressAction = null)
    override fun enableProgress(progress: Progress) = navigator.progress(progressAction = progress)
    override fun navigateToHome() = navigator.navigate(navigationAction = NavigationActions.AuthenticationScreen.home())
    override fun openEmail() = navigator.emailAction(emailAction = true)
    override fun finish() = navigator.finish(finishAction = true)
}
