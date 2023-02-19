package com.nicholas.rutherford.track.my.shot.feature.login

import android.app.Application
import androidx.lifecycle.ViewModel
import com.nicholas.rutherford.track.my.shot.build.type.BuildType
import com.nicholas.rutherford.track.my.shot.data.shared.alert.Alert
import com.nicholas.rutherford.track.my.shot.data.shared.alert.AlertConfirmAndDismissButton
import com.nicholas.rutherford.track.my.shot.data.shared.progress.Progress
import com.nicholas.rutherford.track.my.shot.feature.splash.DrawablesIds
import com.nicholas.rutherford.track.my.shot.feature.splash.StringsIds
import com.nicholas.rutherford.track.my.shot.firebase.util.existinguser.ExistingUserFirebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest

class LoginViewModel(
    private val application: Application,
    private val existingUserFirebase: ExistingUserFirebase,
    private val navigation: LoginNavigation,
    private val buildType: BuildType
) : ViewModel() {

    private val loginMutableStateFlow = MutableStateFlow(
        value = LoginState(
            launcherDrawableId = null,
            email = null,
            password = null
        )
    )
    val loginStateFlow = loginMutableStateFlow.asStateFlow()

    init {
        updateLauncherDrawableIdState()
    }

    private fun updateLauncherDrawableIdState() {
        if (buildType.isDebug()) {
            loginMutableStateFlow.value = loginMutableStateFlow.value.copy(launcherDrawableId = DrawablesIds.launcherRoundTest)
        } else if (buildType.isStage()) {
            loginMutableStateFlow.value = loginMutableStateFlow.value.copy(launcherDrawableId = DrawablesIds.launcherRoundStage)
        } else if (buildType.isRelease()) {
            loginMutableStateFlow.value = loginMutableStateFlow.value.copy(launcherDrawableId = DrawablesIds.launcherRound)
        }
    }

    internal suspend fun onLoginButtonClicked(email: String?, password: String?) {
        email?.let { userEmail ->
            password?.let { userPassword ->
                if (userEmail.isEmpty()) {
                    navigation.alert(alert = emailEmptyAlert())
                } else if (userPassword.isEmpty()) {
                    navigation.alert(alert = passwordEmptyAlert())
                } else {
                    navigation.enableProgress(progress = Progress(onDismissClicked = {}))

                    existingUserFirebase.logInFlow(
                        email = userEmail.filterNot { it.isWhitespace() },
                        password = userPassword.filterNot { it.isWhitespace() }
                    )
                        .collectLatest { isSuccessful ->
                            if (isSuccessful) {
                                onEmailValueChanged(newEmail = application.getString(StringsIds.empty))
                                onPasswordValueChanged(newPassword = application.getString(StringsIds.empty))
                                navigation.disableProgress()
                                navigation.navigateToHome()
                            } else {
                                navigation.disableProgress()
                                navigation.alert(alert = unableToLoginToAccountAlert())
                            }
                        }
                }
            } ?: run {
                navigation.alert(alert = passwordEmptyAlert())
            }
        } ?: run {
            navigation.alert(alert = emailEmptyAlert())
        }
    }

    internal fun onLoginClicked() = navigation.navigateToHome()

    internal fun onForgotPasswordClicked() = navigation.navigateToForgotPassword()

    internal fun onCreateAccountClicked() = navigation.navigateToCreateAccount()

    internal fun onEmailValueChanged(newEmail: String) {
        loginMutableStateFlow.value = loginMutableStateFlow.value.copy(email = newEmail)
    }

    internal fun onPasswordValueChanged(newPassword: String) {
        loginMutableStateFlow.value = loginMutableStateFlow.value.copy(password = newPassword)
    }

    internal fun emailEmptyAlert(): Alert {
        return Alert(
            onDismissClicked = {},
            title = application.getString(StringsIds.emptyField),
            dismissButton = AlertConfirmAndDismissButton(
                onButtonClicked = {},
                buttonText = application.getString(StringsIds.gotIt)
            ),
            description = application.getString(StringsIds.emailIsRequiredPleaseEnterAEmailToLoginToExistingAccount)
        )
    }

    internal fun passwordEmptyAlert(): Alert {
        return Alert(
            onDismissClicked = {},
            title = application.getString(StringsIds.emptyField),
            dismissButton = AlertConfirmAndDismissButton(
                onButtonClicked = {},
                buttonText = application.getString(StringsIds.gotIt)
            ),
            description = application.getString(StringsIds.passwordIsRequiredPleaseEnterAPasswordToLoginToExistingAccount)
        )
    }

    internal fun unableToLoginToAccountAlert(): Alert {
        return Alert(
            onDismissClicked = {},
            title = application.getString(StringsIds.unableToLoginToAccount),
            dismissButton = AlertConfirmAndDismissButton(
                onButtonClicked = {},
                buttonText = application.getString(StringsIds.gotIt)
            ),
            description = application.getString(StringsIds.havingTroubleLoggingIntoYourAccountPleaseTryAgainAndEnsureCredentialsExistAndAreValid)
        )
    }
}
