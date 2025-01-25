package com.nicholas.rutherford.track.your.shot.feature.create.account.authentication

import android.app.Application
import androidx.lifecycle.ViewModel
import com.nicholas.rutherford.track.your.shot.base.resources.StringsIds
import com.nicholas.rutherford.track.your.shot.data.room.repository.ActiveUserRepository
import com.nicholas.rutherford.track.your.shot.data.room.repository.DeclaredShotRepository
import com.nicholas.rutherford.track.your.shot.data.room.response.ActiveUser
import com.nicholas.rutherford.track.your.shot.data.shared.alert.Alert
import com.nicholas.rutherford.track.your.shot.data.shared.alert.AlertConfirmAndDismissButton
import com.nicholas.rutherford.track.your.shot.data.shared.progress.Progress
import com.nicholas.rutherford.track.your.shot.firebase.core.create.CreateFirebaseUserInfo
import com.nicholas.rutherford.track.your.shot.firebase.core.read.ReadFirebaseUserInfo
import com.nicholas.rutherford.track.your.shot.firebase.util.authentication.AuthenticationFirebase
import com.nicholas.rutherford.track.your.shot.helper.constants.Constants
import com.nicholas.rutherford.track.your.shot.helper.extensions.safeLet
import com.nicholas.rutherford.track.your.shot.shared.preference.create.CreateSharedPreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class AuthenticationViewModel(
    private val readFirebaseUserInfo: ReadFirebaseUserInfo,
    private val navigation: AuthenticationNavigation,
    private val application: Application,
    private val authenticationFirebase: AuthenticationFirebase,
    private val createFirebaseUserInfo: CreateFirebaseUserInfo,
    private val activeUserRepository: ActiveUserRepository,
    private val createSharedPreferences: CreateSharedPreferences,
    private val declaredShotRepository: DeclaredShotRepository,
    private val scope: CoroutineScope
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
                            createSharedPreferences.createShouldShowTermsAndConditionsPreference(value = true)
                            createSharedPreferences.createShouldUpdateLoggedInDeclaredShotListPreference(value = true)
                            createSharedPreferences.createHasAuthenticatedAccount(value = true)
                            declaredShotRepository.createDeclaredShots()
                            navigation.disableProgress()
                            navigation.navigateToTermsAndConditions()
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

    fun onDeletePendingAccountClicked() =
        navigation.alert(alert = areYouSureYouWantToDeleteAccountAlert())

    internal suspend fun onYesDeletePendingAccountClicked() {
        navigation.enableProgress(progress = Progress())
        authenticationFirebase.attemptToDeleteCurrentUserFlow()
            .collectLatest { isSuccessful ->
                if (isSuccessful) {
                    activeUserRepository.deleteActiveUser()
                    navigation.disableProgress()
                    navigation.navigateToLogin()
                } else {
                    navigation.disableProgress()
                    navigation.alert(alert = errorDeletingPendingAccountAlert())
                }
            }
    }

    internal fun areYouSureYouWantToDeleteAccountAlert(): Alert {
        return Alert(
            title = application.getString(StringsIds.deletingPendingAccount),
            confirmButton = AlertConfirmAndDismissButton(
                buttonText = application.getString(StringsIds.yes),
                onButtonClicked = {
                    scope.launch { onYesDeletePendingAccountClicked() }
                }
            ),
            dismissButton = AlertConfirmAndDismissButton(
                buttonText = application.getString(StringsIds.no)
            ),
            description = application.getString(StringsIds.areYouSureYouWantToDeletePendingAccountDescription)
        )
    }

    internal fun errorCreatingAccountAlert(): Alert {
        return Alert(
            title = application.getString(StringsIds.errorCreatingAccount),
            dismissButton = AlertConfirmAndDismissButton(
                buttonText = application.getString(StringsIds.gotIt)
            ),
            description = application.getString(StringsIds.thereWasAErrorDeletingPendingAccountPleaseTryAgain)
        )
    }

    internal fun errorDeletingPendingAccountAlert(): Alert {
        return Alert(
            title = application.getString(StringsIds.errorDeletingPendingAccount),
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
