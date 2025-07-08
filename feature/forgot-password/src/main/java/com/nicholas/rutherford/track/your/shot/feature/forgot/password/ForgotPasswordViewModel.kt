package com.nicholas.rutherford.track.your.shot.feature.forgot.password

import android.app.Application
import com.nicholas.rutherford.track.your.shot.base.resources.StringsIds
import com.nicholas.rutherford.track.your.shot.base.vm.BaseViewModel
import com.nicholas.rutherford.track.your.shot.data.shared.alert.Alert
import com.nicholas.rutherford.track.your.shot.data.shared.alert.AlertConfirmAndDismissButton
import com.nicholas.rutherford.track.your.shot.data.shared.progress.Progress
import com.nicholas.rutherford.track.your.shot.firebase.util.authentication.AuthenticationFirebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel for managing Forgot Password screen state and business logic.
 *
 * Responsibilities include:
 * - Managing forgot password UI state (email field).
 * - Validating email input and showing error alerts.
 * - Initiating password reset flow via [AuthenticationFirebase].
 * - Showing progress and success/failure alerts.
 * - Handling back navigation.
 *
 * @param application Provides access to string resources.
 * @param authenticationFirebase Provides Firebase authentication functions.
 * @param navigation Handles navigation and alert display.
 * @param scope CoroutineScope for launching asynchronous tasks.
 */
class ForgotPasswordViewModel(
    private val application: Application,
    private val authenticationFirebase: AuthenticationFirebase,
    private val navigation: ForgotPasswordNavigation,
    private val scope: CoroutineScope
) : BaseViewModel() {

    private val forgotPasswordMutableStateFlow = MutableStateFlow(ForgotPasswordState(email = null))
    val forgotPasswordStateFlow = forgotPasswordMutableStateFlow.asStateFlow()

    init {
        // Initialize email with empty string
        onEmailValueChanged(newEmail = application.getString(StringsIds.empty))
    }

    /** Navigates back when back button is pressed */
    fun onBackButtonClicked() = navigation.pop()

    /**
     * Handles sending the password reset email.
     *
     * Validates the email field, shows progress indicator,
     * then collects the result of the Firebase password reset attempt.
     * Shows appropriate success or failure alert.
     *
     * @param newEmail Email address to send reset instructions to.
     */
    fun onSendPasswordResetButtonClicked(newEmail: String?) {
        scope.launch {
            newEmail?.let { email ->
                if (email.isEmpty()) {
                    navigation.alert(emailEmptyAlert())
                } else {
                    navigation.enableProgress(Progress())
                    authenticationFirebase
                        .attemptToSendPasswordResetFlow(email.filterNot { it.isWhitespace() })
                        .collectLatest { isSuccessful ->
                            navigation.disableProgress()
                            if (isSuccessful) {
                                navigation.alert(successSendingResetPasswordAlert())
                                onEmailValueChanged(application.getString(StringsIds.empty))
                            } else {
                                navigation.alert(unableToSendResetPasswordAlert())
                            }
                        }
                }
            } ?: navigation.alert(emailEmptyAlert())
        }
    }

    /**
     * Updates the email value in the UI state.
     *
     * @param newEmail New email input value.
     */
    fun onEmailValueChanged(newEmail: String) = forgotPasswordMutableStateFlow.update { state -> state.copy(email = newEmail) }

    /** Returns alert for empty email field. */
    internal fun emailEmptyAlert(): Alert = Alert(
        title = application.getString(StringsIds.emptyField),
        dismissButton = AlertConfirmAndDismissButton(buttonText = application.getString(StringsIds.gotIt)),
        description = application.getString(StringsIds.emailIsRequiredPleaseEnterAEmailToResetPasswordForExistingAccount)
    )

    /** Returns alert when password reset email is sent successfully. */
    internal fun successSendingResetPasswordAlert(): Alert = Alert(
        title = application.getString(StringsIds.resetPasswordEmailSent),
        dismissButton = AlertConfirmAndDismissButton(buttonText = application.getString(StringsIds.gotIt)),
        description = application.getString(StringsIds.emailHasBeenSentToRestPasswordPleaseFollowDirectionsToResetPassword)
    )

    /** Returns alert when unable to send password reset email. */
    internal fun unableToSendResetPasswordAlert(): Alert = Alert(
        title = application.getString(StringsIds.unableToResetPassword),
        dismissButton = AlertConfirmAndDismissButton(buttonText = application.getString(StringsIds.gotIt)),
        description = application.getString(StringsIds.havingTroubleResettingPasswordForThisAccountPleaseTryAgainAndOrEnsureCredentialsExistAndAreValid)
    )
}

