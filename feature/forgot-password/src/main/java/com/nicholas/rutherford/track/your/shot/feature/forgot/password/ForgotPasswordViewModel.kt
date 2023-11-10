package com.nicholas.rutherford.track.your.shot.feature.forgot.password

import android.app.Application
import androidx.lifecycle.ViewModel
import com.nicholas.rutherford.track.your.shot.data.shared.alert.Alert
import com.nicholas.rutherford.track.your.shot.data.shared.alert.AlertConfirmAndDismissButton
import com.nicholas.rutherford.track.your.shot.data.shared.progress.Progress
import com.nicholas.rutherford.track.your.shot.feature.splash.StringsIds
import com.nicholas.rutherford.track.your.shot.firebase.util.authentication.AuthenticationFirebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest

class ForgotPasswordViewModel(
    private val application: Application,
    private val authenticationFirebase: AuthenticationFirebase,
    private val navigation: ForgotPasswordNavigation
) : ViewModel() {

    private val forgotPasswordMutableStateFlow = MutableStateFlow(value = ForgotPasswordState(email = null))
    val forgotPasswordStateFlow = forgotPasswordMutableStateFlow.asStateFlow()

    fun onBackButtonClicked() = navigation.pop()

    suspend fun onSendPasswordResetButtonClicked(newEmail: String?) {
        newEmail?.let { email ->
            if (email.isEmpty()) {
                navigation.alert(alert = emailEmptyAlert())
            } else {
                navigation.enableProgress(progress = Progress())
                authenticationFirebase.attemptToSendPasswordResetFlow(email = email.filterNot { it.isWhitespace() })
                    .collectLatest { isSuccessful ->
                        navigation.disableProgress()
                        if (isSuccessful) {
                            navigation.alert(alert = successSendingRestPasswordAlert())
                            onEmailValueChanged(newEmail = application.getString(StringsIds.empty))
                        } else {
                            navigation.alert(alert = unableToSendResetPasswordAlert())
                        }
                    }
            }
        } ?: run {
            navigation.alert(alert = emailEmptyAlert())
        }
    }

    fun onEmailValueChanged(newEmail: String) {
        forgotPasswordMutableStateFlow.value = forgotPasswordMutableStateFlow.value.copy(email = newEmail)
    }

    internal fun emailEmptyAlert(): Alert {
        return Alert(
            title = application.getString(StringsIds.emptyField),
            dismissButton = AlertConfirmAndDismissButton(
                buttonText = application.getString(StringsIds.gotIt)
            ),
            description = application.getString(StringsIds.emailIsRequiredPleaseEnterAEmailToResetPasswordForExistingAccount)
        )
    }

    internal fun successSendingRestPasswordAlert(): Alert {
        return Alert(
            title = application.getString(StringsIds.resetPasswordEmailSent),
            dismissButton = AlertConfirmAndDismissButton(
                buttonText = application.getString(StringsIds.gotIt)
            ),
            description = application.getString(StringsIds.emailHasBeenSentToRestPasswordPleaseFollowDirectionsToResetPassword)
        )
    }

    internal fun unableToSendResetPasswordAlert(): Alert {
        return Alert(
            title = application.getString(StringsIds.unableToResetPassword),
            dismissButton = AlertConfirmAndDismissButton(
                buttonText = application.getString(StringsIds.gotIt)
            ),
            description = application.getString(StringsIds.havingTroubleResettingPasswordForThisAccountPleaseTryAgainAndOrEnsureCredentialsExistAndAreValid)
        )
    }
}
