package com.nicholas.rutherford.track.my.shot.feature.create.account.authentication

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nicholas.rutherford.track.my.shot.data.shared.alert.Alert
import com.nicholas.rutherford.track.my.shot.data.shared.alert.AlertConfirmAndDismissButton
import com.nicholas.rutherford.track.my.shot.feature.splash.StringsIds
import com.nicholas.rutherford.track.my.shot.firebase.read.ReadFirebaseUserInfo
import com.nicholas.rutherford.track.my.shot.firebase.util.AuthenticationFirebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class AuthenticationViewModel(
    private val readFirebaseUserInfo: ReadFirebaseUserInfo,
    private val navigation: AuthenticationNavigation,
    private val application: Application,
    private val authenticationFirebase: AuthenticationFirebase
) : ViewModel() {

    private var username: String? = null
    private var email: String = ""

    private val authenticationMutableStateFlow = MutableStateFlow(value = AuthenticationState(test = ""))
    val authenticationStateFlow = authenticationMutableStateFlow.asStateFlow()

    internal fun updateUsernameAndEmail(username: String?) {
        this.username = username

        println("here is the new username: ${this.username}")
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
        if (readFirebaseUserInfo.isEmailVerified) {
            readFirebaseUserInfo.isEmailVerified
            println("attempt to actually create the user account via database")
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
