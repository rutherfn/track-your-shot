package com.nicholas.rutherford.track.your.shot.feature.create.account.authentication

import android.app.Application
import androidx.lifecycle.ViewModel
import com.nicholas.rutherford.track.your.shot.data.room.repository.ActiveUserRepository
import com.nicholas.rutherford.track.your.shot.data.room.response.ActiveUser
import com.nicholas.rutherford.track.your.shot.data.shared.alert.Alert
import com.nicholas.rutherford.track.your.shot.data.shared.alert.AlertConfirmAndDismissButton
import com.nicholas.rutherford.track.your.shot.data.shared.progress.Progress
import com.nicholas.rutherford.track.your.shot.feature.splash.StringsIds
import com.nicholas.rutherford.track.your.shot.firebase.core.create.CreateFirebaseUserInfo
import com.nicholas.rutherford.track.your.shot.firebase.core.read.ReadFirebaseUserInfo
import com.nicholas.rutherford.track.your.shot.firebase.util.authentication.AuthenticationFirebase
import com.nicholas.rutherford.track.your.shot.helper.constants.Constants
import com.nicholas.rutherford.track.your.shot.helper.extensions.safeLet
import kotlinx.coroutines.flow.collectLatest

class AuthenticationViewModel(
    private val readFirebaseUserInfo: ReadFirebaseUserInfo,
    private val navigation: AuthenticationNavigation,
    private val application: Application,
    private val authenticationFirebase: AuthenticationFirebase,
    private val createFirebaseUserInfo: CreateFirebaseUserInfo,
    private val activeUserRepository: ActiveUserRepository
) : ViewModel() {

    internal var username: String? = null
    internal var email: String? = null

    suspend fun updateUsernameAndEmail(usernameArgument: String?, emailArgument: String?) {
        this.username = usernameArgument
        this.email = emailArgument

        safeLet(emailArgument, usernameArgument) { email, username ->
            attemptToCreateActiveUser(email = email, username = username)
        }
    }

    suspend fun attemptToCreateActiveUser(email: String, username: String) {
        if (activeUserRepository.fetchActiveUser() == null) {
            activeUserRepository.createActiveUser(
                activeUser = ActiveUser(
                    id = Constants.ACTIVE_USER_ID,
                    accountHasBeenCreated = false,
                    username = username,
                    email = email,
                    firebaseAccountInfoKey = null
                )
            )
        }
    }

    internal suspend fun onCheckIfAccountHaBeenVerifiedClicked() = collectIfUserIsVerifiedAndAttemptToCreateAccount(shouldShowAccountIsNotVerifiedAlert = true)

    internal fun onNavigateClose() {
        navigation.alert(
            alert = Alert(
                title = application.getString(StringsIds.areYouSureYouWantLeaveTrackYourShot),
                description = application.getString(StringsIds.leavingTheAppWillResultInYouNotFinishingTheAccountCreationProcessDescription),
                confirmButton = AlertConfirmAndDismissButton(
                    onButtonClicked = { onAlertConfirmButtonClicked() },
                    buttonText = application.getString(StringsIds.yes)
                ),
                dismissButton = AlertConfirmAndDismissButton(
                    buttonText = application.getString(StringsIds.no)
                )
            )
        )
    }

    internal fun onAlertConfirmButtonClicked() = navigation.finish()

    internal suspend fun onResume() {
        collectIfUserIsVerifiedAndAttemptToCreateAccount(shouldShowAccountIsNotVerifiedAlert = false)
    }

    private suspend fun collectIfUserIsVerifiedAndAttemptToCreateAccount(shouldShowAccountIsNotVerifiedAlert: Boolean) {
        readFirebaseUserInfo.isEmailVerifiedFlow().collectLatest { isVerified ->
            if (isVerified) {
                safeLet(username, email) { usernameArgument, emailArgument ->
                    navigation.enableProgress(progress = Progress(onDismissClicked = {}))

                    createFirebaseUserInfo.attemptToCreateAccountFirebaseRealTimeDatabaseResponseFlow(
                        userName = usernameArgument,
                        email = emailArgument
                    ).collectLatest { response ->
                        val isSuccessful = response.first
                        val firebaseAccountInfoKey = response.second

                        if (isSuccessful) {
                            activeUserRepository.updateActiveUser(
                                activeUser = ActiveUser(
                                    id = Constants.ACTIVE_USER_ID,
                                    accountHasBeenCreated = true,
                                    email = emailArgument,
                                    username = usernameArgument,
                                    firebaseAccountInfoKey = firebaseAccountInfoKey
                                )
                            )
                            navigation.disableProgress()
                            navigation.navigateToPlayersList()
                        } else {
                            navigation.disableProgress()
                            navigation.alert(alert = errorCreatingAccountAlert())
                        }
                    }
                }
            } else {
                if (shouldShowAccountIsNotVerifiedAlert) {
                    navigation.alert(alert = errorVerifyingAccount())
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
            title = application.getString(StringsIds.errorCreatingAccount),
            dismissButton = AlertConfirmAndDismissButton(
                buttonText = application.getString(StringsIds.gotIt)
            ),
            description = application.getString(StringsIds.thereWasAErrorCreatingYourAccountPleaseTryAgain)
        )
    }

    internal fun errorVerifyingAccount(): Alert {
        return Alert(
            title = application.getString(StringsIds.accountHasNotBeenVerified),
            dismissButton = AlertConfirmAndDismissButton(
                buttonText = application.getString(StringsIds.gotIt)
            ),
            description = application.getString(StringsIds.currentAccountHasNotBeenVerifiedPleaseOpenEmailToVerifyAccount)
        )
    }

    internal fun successfullySentEmailVerificationAlert(): Alert {
        return Alert(
            title = application.getString(StringsIds.successfullySendEmailVerification),
            dismissButton = AlertConfirmAndDismissButton(
                buttonText = application.getString(StringsIds.gotIt)
            ),
            description = application.getString(StringsIds.weWereAbleToSendEmailVerificationPleaseCheckYourEmailToVerifyAccount)
        )
    }

    internal fun unsuccessfullySendEmailVerificationAlert(): Alert {
        return Alert(
            title = application.getString(StringsIds.unableToSendEmailVerification),
            dismissButton = AlertConfirmAndDismissButton(
                buttonText = application.getString(StringsIds.gotIt)
            ),
            description = application.getString(StringsIds.weWereUnableToSendEmailVerificationPleaseClickSendEmailVerificationToTryAgain)
        )
    }

    internal fun onOpenEmailClicked() = navigation.openEmail()
}
