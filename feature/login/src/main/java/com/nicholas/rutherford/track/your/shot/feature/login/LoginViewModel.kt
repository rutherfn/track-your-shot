package com.nicholas.rutherford.track.your.shot.feature.login

import android.app.Application
import androidx.lifecycle.ViewModel
import com.nicholas.rutherford.track.your.shot.build.type.BuildType
import com.nicholas.rutherford.track.your.shot.data.shared.alert.Alert
import com.nicholas.rutherford.track.your.shot.data.shared.alert.AlertConfirmAndDismissButton
import com.nicholas.rutherford.track.your.shot.feature.splash.DrawablesIds
import com.nicholas.rutherford.track.your.shot.feature.splash.StringsIds
import com.nicholas.rutherford.track.your.shot.helper.account.AccountManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class LoginViewModel(
    private val application: Application,
    private val navigation: LoginNavigation,
    private val buildType: BuildType,
    private val accountManager: AccountManager
) : ViewModel() {

    internal val loginMutableStateFlow = MutableStateFlow(LoginState())
    val loginStateFlow = loginMutableStateFlow.asStateFlow()

    init {
        updateLauncherDrawableIdState()
    }

    internal fun updateLauncherDrawableIdState() {
        if (buildType.isDebug()) {
            loginMutableStateFlow.value = loginMutableStateFlow.value.copy(launcherDrawableId = DrawablesIds.launcherRoundTest)
        } else if (buildType.isStage()) {
            loginMutableStateFlow.value = loginMutableStateFlow.value.copy(launcherDrawableId = DrawablesIds.launcherRoundStage)
        } else if (buildType.isRelease()) {
            loginMutableStateFlow.value = loginMutableStateFlow.value.copy(launcherDrawableId = DrawablesIds.launcherRound)
        }
    }

    internal fun fieldsErrorAlert(email: String?, password: String?): Alert? {
        if (email.isNullOrEmpty()) {
            return emailEmptyAlert()
        }

        if (password.isNullOrEmpty()) {
            return passwordEmptyAlert()
        }

        return null
    }

    fun onLoginButtonClicked() {
        val email = loginStateFlow.value.email
        val password = loginStateFlow.value.password

        fieldsErrorAlert(email = email, password = password)?.let { alert ->
            navigation.alert(alert = alert)
        } ?: run {
            attemptToLoginToAccount(email = email, password = password)
        }
    }

    internal fun attemptToLoginToAccount(email: String?, password: String?) {
        val emptyString = application.getString(StringsIds.empty)
        val newEmail = email?.filterNot { it.isWhitespace() } ?: emptyString
        val newPassword = password?.filterNot { it.isWhitespace() } ?: emptyString

        onEmailValueChanged(newEmail = application.getString(StringsIds.empty))
        onPasswordValueChanged(newPassword = application.getString(StringsIds.empty))

        accountManager.login(
            email = newEmail,
            password = newPassword
        )
    }

    fun onForgotPasswordClicked() = navigation.navigateToForgotPassword()

    fun onCreateAccountClicked() = navigation.navigateToCreateAccount()

    fun onEmailValueChanged(newEmail: String) {
        loginMutableStateFlow.value = loginMutableStateFlow.value.copy(email = newEmail)
    }

    fun onPasswordValueChanged(newPassword: String) {
        loginMutableStateFlow.value = loginMutableStateFlow.value.copy(password = newPassword)
    }

    internal fun emailEmptyAlert(): Alert {
        return Alert(
            title = application.getString(StringsIds.emptyField),
            dismissButton = AlertConfirmAndDismissButton(
                buttonText = application.getString(StringsIds.gotIt)
            ),
            description = application.getString(StringsIds.emailIsRequiredPleaseEnterAEmailToLoginToExistingAccount)
        )
    }

    internal fun passwordEmptyAlert(): Alert {
        return Alert(
            title = application.getString(StringsIds.emptyField),
            dismissButton = AlertConfirmAndDismissButton(
                buttonText = application.getString(StringsIds.gotIt)
            ),
            description = application.getString(StringsIds.passwordIsRequiredPleaseEnterAPasswordToLoginToExistingAccount)
        )
    }
}
