package com.nicholas.rutherford.track.my.shot.feature.create.account.authentication

import android.app.Application
import androidx.lifecycle.ViewModel
import com.nicholas.rutherford.track.my.shot.data.shared.alert.Alert
import com.nicholas.rutherford.track.my.shot.data.shared.alert.AlertConfirmAndDismissButton
import com.nicholas.rutherford.track.my.shot.data.shared.progress.Progress
import com.nicholas.rutherford.track.my.shot.feature.splash.StringsIds
import com.nicholas.rutherford.track.my.shot.firebase.create.CreateFirebaseUserInfo
import com.nicholas.rutherford.track.my.shot.firebase.read.ReadFirebaseUserInfo
import com.nicholas.rutherford.track.my.shot.firebase.util.AuthenticationFirebase
import com.nicholas.rutherford.track.my.shot.helper.extensions.safeLet
import com.nicholas.rutherford.track.my.shot.shared.preference.create.CreateSharedPreferences
import kotlinx.coroutines.flow.collectLatest

class AuthenticationViewModel(
    private val readFirebaseUserInfo: ReadFirebaseUserInfo,
    private val navigation: AuthenticationNavigation,
    private val application: Application,
    private val authenticationFirebase: AuthenticationFirebase,
    private val createSharedPreferences: CreateSharedPreferences,
    private val createFirebaseUserInfo: CreateFirebaseUserInfo
) : ViewModel() {

    internal var username: String? = null
    internal var email: String? = null

    internal fun updateUsernameAndEmail(usernameArgument: String?, emailArgument: String?) {
        this.username = usernameArgument
        this.email = emailArgument
        createSharedPreferencesForUnAuthenticatedUser()
    }

    internal fun createSharedPreferencesForUnAuthenticatedUser() {
        safeLet(username, email) { usernameArgument, emailArgument ->
            createSharedPreferences.createUnverifiedUsernamePreference(value = usernameArgument)
            createSharedPreferences.createUnverifiedEmailPreference(value = emailArgument)
        }
    }

    internal fun onNavigateClose() {
        navigation.alert(
            alert = Alert(
                onDismissClicked = {},
                title = application.getString(StringsIds.areYouSureYouWantLeaveTrackMyShot),
                description = application.getString(StringsIds.leavingTheAppWillResultInYouNotFinishingTheAccountCreationProcessDescription),
                confirmButton = AlertConfirmAndDismissButton(
                    onButtonClicked = { onAlertConfirmButtonClicked() },
                    buttonText = application.getString(StringsIds.yes)
                ),
                dismissButton = AlertConfirmAndDismissButton(
                    onButtonClicked = {},
                    buttonText = application.getString(StringsIds.no)
                )
            )
        )
    }

    internal fun onAlertConfirmButtonClicked() = navigation.finish()

    internal suspend fun onResume() {
        collectIfUserIsVerifiedAndAttemptToCreateAccount()
    }

    private suspend fun collectIfUserIsVerifiedAndAttemptToCreateAccount() {
        readFirebaseUserInfo.isEmailVerified().collectLatest { isVerified ->
            if (isVerified) {
                safeLet(username, email) { usernameArgument, emailArgument ->
                    navigation.enableProgress(progress = Progress(onDismissClicked = {}))
                    createFirebaseUserInfo.attemptToCreateAccountFirebaseRealTimeDatabaseResponseFlow(
                        userName = usernameArgument,
                        email = emailArgument
                    ).collectLatest { isSuccessful ->
                        if (isSuccessful) {
                            createSharedPreferences.createAccountHasBeenCreatedPreference(value = true)
                            navigation.disableProgress()
                            navigation.navigateToHome()
                        } else {
                            navigation.disableProgress()
                            navigation.alert(alert = errorCreatingAccountAlert())
                        }
                    }
                }
            }
        }
    }

    internal suspend fun onResendEmailClicked() {
        authenticationFirebase.attemptToSendEmailVerificationForCurrentUser()
            .collectLatest { authenticationUserViaEmailFirebaseResponse ->
                if (authenticationUserViaEmailFirebaseResponse.isSuccessful) {
                    navigation.alert(alert = successfullySentEmailVerificationAlert())
                } else {
                    navigation.alert(alert = unsuccessfullySendEmailVerificationAlert())
                }
            }
    }

    internal fun errorCreatingAccountAlert(): Alert {
        return Alert(
            onDismissClicked = {},
            title = application.getString(StringsIds.errorCreatingAccount),
            dismissButton = AlertConfirmAndDismissButton(
                onButtonClicked = {},
                buttonText = application.getString(StringsIds.gotIt)
            ),
            description = application.getString(StringsIds.thereWasAErrorCreatingYourAccountPleaseTryAgain)
        )
    }

    internal fun successfullySentEmailVerificationAlert(): Alert {
        return Alert(
            onDismissClicked = {},
            title = application.getString(StringsIds.successfullySendEmailVerification),
            dismissButton = AlertConfirmAndDismissButton(
                onButtonClicked = {},
                buttonText = application.getString(StringsIds.gotIt)
            ),
            description = application.getString(StringsIds.weWereAbleToSendEmailVerificationPleaseCheckYourEmailToVerifyAccount)
        )
    }

    internal fun unsuccessfullySendEmailVerificationAlert(): Alert {
        return Alert(
            onDismissClicked = {},
            title = application.getString(StringsIds.unableToSendEmailVerification),
            dismissButton = AlertConfirmAndDismissButton(
                onButtonClicked = {},
                buttonText = application.getString(StringsIds.gotIt)
            ),
            description = application.getString(StringsIds.weWereUnableToSendEmailVerificationPleaseClickSendEmailVerificationToTryAgain)
        )
    }

    internal fun onOpenEmailClicked() = navigation.openEmail()
}
