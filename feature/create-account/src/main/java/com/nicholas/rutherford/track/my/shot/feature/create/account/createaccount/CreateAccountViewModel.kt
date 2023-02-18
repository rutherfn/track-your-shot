package com.nicholas.rutherford.track.my.shot.feature.create.account.createaccount

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nicholas.rutherford.track.my.shot.data.shared.alert.Alert
import com.nicholas.rutherford.track.my.shot.data.shared.alert.AlertConfirmAndDismissButton
import com.nicholas.rutherford.track.my.shot.data.shared.progress.Progress
import com.nicholas.rutherford.track.my.shot.feature.splash.StringsIds
import com.nicholas.rutherford.track.my.shot.firebase.create.CreateFirebaseUserInfo
import com.nicholas.rutherford.track.my.shot.firebase.util.AuthenticationFirebase
import com.nicholas.rutherford.track.my.shot.helper.extensions.safeLet
import com.nicholas.rutherford.track.my.shot.helper.network.Network
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class CreateAccountViewModel(
    private val navigation: CreateAccountNavigation,
    private val application: Application,
    private val network: Network,
    private val createFirebaseUserInfo: CreateFirebaseUserInfo,
    private val authenticationFirebase: AuthenticationFirebase
) : ViewModel() {

    internal var isUsernameEmptyOrNull: Boolean = false
    internal var isEmailEmptyOrNull: Boolean = false
    internal var isPasswordEmptyOrNull: Boolean = false
    internal var isTwoOrMoreFieldsEmptyOrNull: Boolean = false

    private val createAccountMutableStateFlow = MutableStateFlow(
        value = CreateAccountState(
            username = null,
            email = null,
            password = null
        )
    )
    val createAccountStateFlow = createAccountMutableStateFlow.asStateFlow()

    internal val defaultAlert = Alert(
        onDismissClicked = {},
        title = application.getString(StringsIds.empty),
        dismissButton = AlertConfirmAndDismissButton(
            onButtonClicked = {},
            buttonText = application.getString(StringsIds.gotIt)
        )
    )

    fun onBackButtonClicked() = navigation.pop()

    fun onCreateAccountButtonClicked() {
        navigation.enableProgress(progress = Progress(onDismissClicked = {}))
        val createAccountState = createAccountMutableStateFlow.value

        setIsUsernameEmptyOrNull(username = createAccountState.username)
        setIsEmailEmptyOrNull(email = createAccountState.email)
        setIsPasswordEmptyOrNull(password = createAccountState.password)
        setIsTwoOrMoreFieldsEmptyOrNull()

        viewModelScope.launch {
            attemptToShowErrorAlertOrCreateFirebaseAuth(createAccountState = createAccountState)
        }
    }

    internal suspend fun attemptToShowErrorAlertOrCreateFirebaseAuth(createAccountState: CreateAccountState) {
        validateFieldsWithOptionalAlert()?.let { alert ->
            navigation.disableProgress()
            navigation.alert(alert = alert)
        } ?: run {
            safeLet(createAccountState.email, createAccountState.username, createAccountState.password) { email, username, password ->
                attemptToCreateFirebaseAuthAndSendEmailVerification(email = email, username = username, password = password)
            }
        }
    }

    internal fun setIsUsernameEmptyOrNull(username: String?) {
        username?.let { value ->
            isUsernameEmptyOrNull = value.isEmpty()
        } ?: run {
            isUsernameEmptyOrNull = true
        }
    }

    internal fun setIsEmailEmptyOrNull(email: String?) {
        email?.let { value ->
            isEmailEmptyOrNull = value.isEmpty()
        } ?: run {
            isEmailEmptyOrNull = true
        }
    }

    internal fun setIsPasswordEmptyOrNull(password: String?) {
        password?.let { value ->
            isPasswordEmptyOrNull = value.isEmpty()
        } ?: run {
            isPasswordEmptyOrNull = true
        }
    }

    internal fun setIsTwoOrMoreFieldsEmptyOrNull() {
        var counter = 0

        if (isUsernameEmptyOrNull) {
            counter += 1
        }
        if (isEmailEmptyOrNull) {
            counter += 1
        }
        if (isPasswordEmptyOrNull) {
            counter += 1
        }

        isTwoOrMoreFieldsEmptyOrNull = counter >= 2
    }

    internal suspend fun validateFieldsWithOptionalAlert(): Alert? {
        if (!network.isDeviceConnectedToInternet()) {
            return defaultAlert.copy(
                title = application.getString(StringsIds.notConnectedToInternet),
                description = application.getString(StringsIds.deviceIsCurrentlyNotConnectedToInternetDesc)
            )
        } else if (isTwoOrMoreFieldsEmptyOrNull) {
            return defaultAlert.copy(
                title = application.getString(StringsIds.emptyFields),
                description = application.getString(StringsIds.multipleFieldsAreRequiredThatAreNotEnteredPleaseEnterAllFields)
            )
        } else if (isUsernameEmptyOrNull) {
            return defaultAlert.copy(
                title = application.getString(StringsIds.emptyField),
                description = application.getString(StringsIds.usernameIsRequiredPleaseEnterAUsernameToCreateAAccount)
            )
        } else if (isEmailEmptyOrNull) {
            return defaultAlert.copy(
                title = application.getString(StringsIds.emptyField),
                description = application.getString(
                    StringsIds.emailIsRequiredPleaseEnterAEmailToCreateAAccount
                )
            )
        } else if (isPasswordEmptyOrNull) {
            return defaultAlert.copy(
                title = application.getString(StringsIds.emptyField),
                description = application.getString(StringsIds.passwordIsRequiredPleaseEnterAPasswordToCreateAAccount)
            )
        } else {
            return null
        }
    }

    internal suspend fun attemptToCreateFirebaseAuthAndSendEmailVerification(email: String, username: String, password: String) {
        createFirebaseUserInfo.attemptToCreateAccountFirebaseAuthResponseFlow(email = email, password)
            .collectLatest { createAccountFirebaseAuthResponse ->
                if (createAccountFirebaseAuthResponse.isSuccessful) {
                    authenticationFirebase.attemptToSendEmailVerificationForCurrentUser()
                        .collectLatest { authenticatedUserViaEmailFirebaseResponse ->
                            if (authenticatedUserViaEmailFirebaseResponse.isSuccessful) {
                                navigateToAuthentication(email = email, username = username)
                            } else {
                                showUnableToSendEmailVerificationAlert(email = email, username = username)
                            }
                        }
                } else {
                    showUnableToCreateFirebaseAuthAlert()
                }
            }
    }

    private fun navigateToAuthentication(email: String, username: String) {
        navigation.disableProgress()
        navigation.navigateToAuthentication(email = email, username = username)
    }

    private fun showUnableToSendEmailVerificationAlert(email: String, username: String) {
        navigation.disableProgress()
        navigation.alert(
            alert = defaultAlert.copy(
                title = application.getString(StringsIds.unableToSendEmailVerification),
                description = application.getString(StringsIds.weWereUnableToSendEmailVerificationPleaseClickSendEmailVerificationToTryAgain)
            )
        )
        navigation.navigateToAuthentication(email = email, username = username)
    }

    private fun showUnableToCreateFirebaseAuthAlert() {
        navigation.disableProgress()
        navigation.alert(
            alert = defaultAlert.copy(
                title = application.getString(StringsIds.unableToCreateAccount),
                description = application.getString(StringsIds.havingTroubleCreatingYourAccountPleaseTryAgain)
            )
        )
    }

    internal fun onUsernameValueChanged(newUsername: String) {
        createAccountMutableStateFlow.value = createAccountStateFlow.value.copy(username = newUsername)
    }

    internal fun onEmailValueChanged(newEmail: String) {
        createAccountMutableStateFlow.value = createAccountStateFlow.value.copy(email = newEmail)
    }

    internal fun onPasswordValueChanged(newPassword: String) {
        createAccountMutableStateFlow.value = createAccountStateFlow.value.copy(password = newPassword)
    }
}
