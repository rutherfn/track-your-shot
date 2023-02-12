package com.nicholas.rutherford.track.my.shot.feature.create.account.authentication

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nicholas.rutherford.track.my.shot.data.shared.alert.Alert
import com.nicholas.rutherford.track.my.shot.data.shared.alert.AlertConfirmAndDismissButton
import com.nicholas.rutherford.track.my.shot.data.shared.progress.Progress
import com.nicholas.rutherford.track.my.shot.feature.splash.StringsIds
import com.nicholas.rutherford.track.my.shot.firebase.create.CreateFirebaseUserInfo
import com.nicholas.rutherford.track.my.shot.firebase.read.ReadFirebaseUserInfo
import com.nicholas.rutherford.track.my.shot.firebase.util.AuthenticationFirebase
import com.nicholas.rutherford.track.my.shot.helper.extensions.safeLet
import com.nicholas.rutherford.track.my.shot.shared.preference.create.CreateSharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class AuthenticationViewModel(
    private val readFirebaseUserInfo: ReadFirebaseUserInfo,
    private val navigation: AuthenticationNavigation,
    private val application: Application,
    private val authenticationFirebase: AuthenticationFirebase,
    private val createSharedPreferences: CreateSharedPreferences,
    private val createFirebaseUserInfo: CreateFirebaseUserInfo
) : ViewModel() {

    private var username: String? = null
    private var email: String? = null

    private val authenticationMutableStateFlow = MutableStateFlow(value = AuthenticationState(test = ""))
    val authenticationStateFlow = authenticationMutableStateFlow.asStateFlow()

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

    internal fun onResume() {
        viewModelScope.launch {
            readFirebaseUserInfo.isEmailVerified().collectLatest { isVerified ->
                if (isVerified) {
                    navigation.enableProgress(progress = Progress(onDismissClicked = {}))
                    safeLet(username, email) { usernameArgument, emailArgument ->
                        createFirebaseUserInfo.attemptToCreateAccountFirebaseRealTimeDatabaseResponseFlow(
                            userName = usernameArgument,
                            email = emailArgument
                        ).collectLatest { isSuccessful ->
                            if (isSuccessful) {
                                createSharedPreferences.createAccountHasBeenCreatedPreference(value = true)
                                navigation.disableProgress()
                                navigation.navigateToHome()
                            } else {
                                // show some type of alert
                            }
                        }
                    }
                } else {
                    println("email is not verified")
                }
            }
        }
    }

    internal fun onResendEmailClicked() {
        viewModelScope.launch {
            authenticationFirebase.attemptToSendEmailVerificationForCurrentUser()
                .collectLatest { authenticationUserViaEmailFirebaseResponse ->
                    if (authenticationUserViaEmailFirebaseResponse.isSuccessful) {
                        navigation.alert(alert = successfullySentEmailVerificationAlert())
                    } else {
                        navigation.alert(alert = unsuccessfullySendEmailVerificationAlert())
                    }
                }
        }
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
