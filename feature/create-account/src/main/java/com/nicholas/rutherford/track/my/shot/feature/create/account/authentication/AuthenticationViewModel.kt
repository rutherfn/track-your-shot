package com.nicholas.rutherford.track.my.shot.feature.create.account.authentication

import android.app.Application
import androidx.lifecycle.ViewModel
import com.nicholas.rutherford.track.my.shot.data.shared.alert.Alert
import com.nicholas.rutherford.track.my.shot.data.shared.alert.AlertConfirmAndDismissButton
import com.nicholas.rutherford.track.my.shot.feature.splash.StringsIds
import com.nicholas.rutherford.track.my.shot.firebase.read.ReadFirebaseUserInfo
import com.nicholas.rutherford.track.my.shot.firebase.util.AuthenticationFirebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AuthenticationViewModel(
    private val readFirebaseUserInfo: ReadFirebaseUserInfo,
    private val navigation: AuthenticationNavigation,
    private val application: Application,
    private val authenticationFirebase: AuthenticationFirebase
) : ViewModel() {

    private val authenticationMutableStateFlow = MutableStateFlow(value = AuthenticationState(test = ""))
    val authenticationStateFlow = authenticationMutableStateFlow.asStateFlow()

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
            println("attempt to actually create the user account via database")
        }
    }
}
