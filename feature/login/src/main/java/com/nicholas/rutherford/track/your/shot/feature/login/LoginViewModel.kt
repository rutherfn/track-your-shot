package com.nicholas.rutherford.track.your.shot.feature.login

import android.app.Application
import com.nicholas.rutherford.track.your.shot.base.resources.DrawablesIds
import com.nicholas.rutherford.track.your.shot.base.resources.StringsIds
import com.nicholas.rutherford.track.your.shot.base.vm.BaseViewModel
import com.nicholas.rutherford.track.your.shot.build.type.BuildType
import com.nicholas.rutherford.track.your.shot.data.shared.alert.Alert
import com.nicholas.rutherford.track.your.shot.data.shared.alert.AlertConfirmAndDismissButton
import com.nicholas.rutherford.track.your.shot.helper.account.AccountManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel for handling login screen state and logic.
 *
 * Responsibilities include:
 * - Managing login screen state (email, password, UI assets).
 * - Validating login input and showing error alerts.
 * - Initiating login flow via [AccountManager].
 * - Reacting to login success and clearing input fields.
 * - Routing user to create account or forgot password screens.
 *
 * @param application Application context used for accessing string resources.
 * @param navigation Interface defining navigation actions from the login screen.
 * @param buildType Indicates current app build type (debug, stage, release).
 * @param accountManager Handles user account authentication.
 * @param scope Coroutine scope for background flows and operations.
 */
class LoginViewModel(
    private val application: Application,
    private val navigation: LoginNavigation,
    private val buildType: BuildType,
    private val accountManager: AccountManager,
    private val scope: CoroutineScope
) : BaseViewModel() {

    internal val loginMutableStateFlow = MutableStateFlow(LoginState())
    val loginStateFlow = loginMutableStateFlow.asStateFlow()

    init {
        updateLauncherDrawableIdState()
        clearState()
    }

    /**
     * Resets UI state of [LoginState].
     */
    internal fun clearState() {
        onEmailValueChanged(newEmail = application.getString(StringsIds.empty))
        onPasswordValueChanged(newPassword = application.getString(StringsIds.empty))
    }

    /**
     * Updates the launcher icon used on the login screen
     * based on the current build type (debug, stage, release).
     */
    internal fun updateLauncherDrawableIdState() {
        val launcherDrawableId = when {
            buildType.isDebug() -> DrawablesIds.launcherRoundTest
            buildType.isStage() -> DrawablesIds.launcherRoundStage
            buildType.isRelease() -> DrawablesIds.launcherRound
            else -> DrawablesIds.launcherRound
        }

        loginMutableStateFlow.update { state ->
            state.copy(launcherDrawableId = launcherDrawableId)
        }
    }

    /**
     * Returns an appropriate [Alert] if login fields are missing.
     *
     * @param email The email input value.
     * @param password The password input value.
     * @return [Alert] indicating the missing field, or null if inputs are valid.
     */
    internal fun fieldsErrorAlert(email: String?, password: String?): Alert? {
        return if (email.isNullOrEmpty()) {
            emailEmptyAlert()
        } else if (password.isNullOrEmpty()) {
            passwordEmptyAlert()
        } else {
            null
        }
    }

    /**
     * Triggered when the user taps the login button.
     * Validates fields and initiates login if valid.
     */
    fun onLoginButtonClicked() {
        scope.launch {
            val email = loginStateFlow.value.email
            val password = loginStateFlow.value.password

            fieldsErrorAlert(email = email, password = password)?.let { alert ->
                navigation.alert(alert = alert)
            } ?: run {
                attemptToLoginToAccount(email = email, password = password)
            }
        }
    }

    /**
     * Triggers login via [AccountManager] after sanitizing input.
     */
    internal fun attemptToLoginToAccount(email: String?, password: String?) {
        val emptyString = application.getString(StringsIds.empty)
        val newEmail = email?.filterNot { it.isWhitespace() } ?: emptyString
        val newPassword = password?.filterNot { it.isWhitespace() } ?: emptyString

        accountManager.login(
            email = newEmail,
            password = newPassword
        )
    }

    /** Navigates to forgot password screen. */
    fun onForgotPasswordClicked() {
        clearState()
        navigation.navigateToForgotPassword()
    }

    /** Navigates to create account screen. */
    fun onCreateAccountClicked() {
        clearState()
        navigation.navigateToCreateAccount()
    }

    /**
     * Updates email in the UI state.
     *
     * @param newEmail The new email value.
     */
    fun onEmailValueChanged(newEmail: String) {
        loginMutableStateFlow.value = loginMutableStateFlow.value.copy(email = newEmail)
    }

    /**
     * Updates password in the UI state.
     *
     * @param newPassword The new password value.
     */
    fun onPasswordValueChanged(newPassword: String) {
        loginMutableStateFlow.value = loginMutableStateFlow.value.copy(password = newPassword)
    }

    /** Returns an alert for when the email field is empty. */
    internal fun emailEmptyAlert(): Alert {
        return Alert(
            title = application.getString(StringsIds.emptyField),
            dismissButton = AlertConfirmAndDismissButton(
                buttonText = application.getString(StringsIds.gotIt)
            ),
            description = application.getString(StringsIds.emailIsRequiredPleaseEnterAEmailToLoginToExistingAccount)
        )
    }

    /** Returns an alert for when the password field is empty. */
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

