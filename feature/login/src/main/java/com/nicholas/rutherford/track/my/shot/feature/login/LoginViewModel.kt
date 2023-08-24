package com.nicholas.rutherford.track.my.shot.feature.login

import android.app.Application
import androidx.lifecycle.ViewModel
import com.nicholas.rutherford.track.my.shot.build.type.BuildType
import com.nicholas.rutherford.track.my.shot.data.room.repository.activeuser.ActiveUserRepository
import com.nicholas.rutherford.track.my.shot.data.room.repository.user.UserRepository
import com.nicholas.rutherford.track.my.shot.data.room.response.ActiveUser
import com.nicholas.rutherford.track.my.shot.data.shared.alert.Alert
import com.nicholas.rutherford.track.my.shot.data.shared.alert.AlertConfirmAndDismissButton
import com.nicholas.rutherford.track.my.shot.data.shared.progress.Progress
import com.nicholas.rutherford.track.my.shot.feature.splash.DrawablesIds
import com.nicholas.rutherford.track.my.shot.feature.splash.StringsIds
import com.nicholas.rutherford.track.my.shot.firebase.util.existinguser.ExistingUserFirebase
import com.nicholas.rutherford.track.my.shot.helper.constants.Constants
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest

class LoginViewModel(
    private val application: Application,
    private val existingUserFirebase: ExistingUserFirebase,
    private val navigation: LoginNavigation,
    private val buildType: BuildType,
    private val activeUserRepository: ActiveUserRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    internal val _loginStateFlow = MutableStateFlow(LoginState())
    val loginStateFlow = _loginStateFlow.asStateFlow()

    init {
        updateLauncherDrawableIdState()
    }

    private fun updateLauncherDrawableIdState() {
        if (buildType.isDebug()) {
            _loginStateFlow.value = _loginStateFlow.value.copy(launcherDrawableId = DrawablesIds.launcherRoundTest)
        } else if (buildType.isStage()) {
            _loginStateFlow.value = _loginStateFlow.value.copy(launcherDrawableId = DrawablesIds.launcherRoundStage)
        } else if (buildType.isRelease()) {
            _loginStateFlow.value = _loginStateFlow.value.copy(launcherDrawableId = DrawablesIds.launcherRound)
        }
    }

    suspend fun onLoginButtonClicked() {
        loginStateFlow.value.email?.let { userEmail ->
            loginStateFlow.value.password?.let { userPassword ->
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

                                updateActiveUserFromLoggedInUser(email = userEmail)

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

    suspend fun updateActiveUserFromLoggedInUser(email: String) {
        userRepository.fetchUserByEmail(email = email)?.let { user ->
            if (activeUserRepository.fetchActiveUser() == null) {
                activeUserRepository.createActiveUser(
                    activeUser = ActiveUser(
                        id = Constants.ACTIVE_USER_ID,
                        accountHasBeenCreated = true,
                        username = user.username,
                        email = user.email
                    )
                )
            }
        }
    }

    fun onForgotPasswordClicked() = navigation.navigateToForgotPassword()

    fun onCreateAccountClicked() = navigation.navigateToCreateAccount()

    fun onEmailValueChanged(newEmail: String) {
        _loginStateFlow.value = _loginStateFlow.value.copy(email = newEmail)
    }

    fun onPasswordValueChanged(newPassword: String) {
        _loginStateFlow.value = _loginStateFlow.value.copy(password = newPassword)
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
