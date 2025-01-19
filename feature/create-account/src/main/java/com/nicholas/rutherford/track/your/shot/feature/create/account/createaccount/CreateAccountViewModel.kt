package com.nicholas.rutherford.track.your.shot.feature.create.account.createaccount

import android.app.Application
import androidx.lifecycle.ViewModel
import com.nicholas.rutherford.track.your.shot.base.resources.StringsIds
import com.nicholas.rutherford.track.your.shot.data.shared.alert.Alert
import com.nicholas.rutherford.track.your.shot.data.shared.alert.AlertConfirmAndDismissButton
import com.nicholas.rutherford.track.your.shot.data.shared.progress.Progress
import com.nicholas.rutherford.track.your.shot.feature.players.createeditplayer.RESET_SCREEN_DELAY_IN_MILLIS
import com.nicholas.rutherford.track.your.shot.firebase.core.create.CreateFirebaseUserInfo
import com.nicholas.rutherford.track.your.shot.firebase.util.authentication.AuthenticationFirebase
import com.nicholas.rutherford.track.your.shot.helper.extensions.safeLet
import com.nicholas.rutherford.track.your.shot.helper.network.Network
import com.nicholas.rutherford.track.your.shot.shared.preference.create.CreateSharedPreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

// - Allows dots, underscores, pluses, and other common characters in the local part of the email.
// - Supports domains with numbers, hyphens, and multiple subdomains (e.g., "sub.domain.com").
// - Permits top-level domains (TLDs) with 2 or more letters (e.g., ".com", ".info").
const val EMAIL_PATTERN = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"

// Minimum eight characters, at least one uppercase letter, one lowercase letter, one number and one special character
const val PASSWORD_PATTERN = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@\$!%*?&])[A-Za-z\\d@\$!%*?&]{8,}\$"

// 8 to 30 characters, only contain alphanumeric characters and underscores, first character must be alphabetic character
const val USERNAME_PATTERN = "^(?=[a-zA-Z\\d._]{8,20}\$)(?!.*[_.]{2})[^_.].*[^_.]\$"

class CreateAccountViewModel(
    private val navigation: CreateAccountNavigation,
    private val application: Application,
    private val network: Network,
    private val createFirebaseUserInfo: CreateFirebaseUserInfo,
    private val createSharedPreferences: CreateSharedPreferences,
    private val authenticationFirebase: AuthenticationFirebase,
    private val scope: CoroutineScope
) : ViewModel() {

    internal var isUsernameEmptyOrNull: Boolean = false
    internal var isUsernameInNotCorrectFormat: Boolean = false
    internal var isEmailEmptyOrNull: Boolean = false
    internal var isEmailInNotCorrectFormat: Boolean = false
    internal var isPasswordEmptyOrNull: Boolean = false
    internal var isPasswordInNotCorrectFormat: Boolean = false
    internal var isTwoOrMoreFieldsEmptyOrNull: Boolean = false

    private val createAccountMutableStateFlow = MutableStateFlow(value = CreateAccountState())
    val createAccountStateFlow = createAccountMutableStateFlow.asStateFlow()

    internal val defaultAlert = Alert(
        title = application.getString(StringsIds.empty),
        dismissButton = AlertConfirmAndDismissButton(
            buttonText = application.getString(StringsIds.gotIt)
        )
    )

    private fun clearState() {
        scope.launch {
            delay(RESET_SCREEN_DELAY_IN_MILLIS)
            createAccountMutableStateFlow.value = CreateAccountState()
        }
    }

    fun onBackButtonClicked() {
        clearState()
        navigation.pop()
    }

    fun onCreateAccountButtonClicked() {
        val createAccountState = createAccountMutableStateFlow.value

        // Enable progress and prepare for field validation
        navigation.enableProgress(progress = Progress())

        // Validate username
        setIsUsernameEmptyOrNull(username = createAccountState.username)
        setIsUsernameInNotCorrectFormat(username = createAccountState.username)

        // Validate email
        setIsEmailEmptyOrNull(email = createAccountState.email)
        setIsEmailInNotCorrectFormat(email = createAccountState.email)

        // Validate password
        setIsPasswordEmptyOrNull(password = createAccountState.password)
        setIsPasswordNotInCorrectFormat(password = createAccountState.password)

        // Check if multiple fields are empty
        setIsTwoOrMoreFieldsEmptyOrNull()

        // Attempt to show error alert or create Firebase Auth
        scope.launch {
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

    internal fun setIsUsernameInNotCorrectFormat(username: String?) {
        username?.let { value ->
            isUsernameInNotCorrectFormat = !USERNAME_PATTERN.toRegex().matches(value)
        } ?: run {
            isUsernameInNotCorrectFormat = true
        }
    }

    internal fun setIsEmailEmptyOrNull(email: String?) {
        email?.let { value ->
            isEmailEmptyOrNull = value.isEmpty()
        } ?: run {
            isEmailEmptyOrNull = true
        }
    }

    internal fun setIsEmailInNotCorrectFormat(email: String?) {
        email?.let { value ->
            isEmailInNotCorrectFormat = !EMAIL_PATTERN.toRegex().matches(value)
        } ?: run {
            isEmailInNotCorrectFormat = true
        }
    }

    internal fun setIsPasswordEmptyOrNull(password: String?) {
        password?.let { value ->
            isPasswordEmptyOrNull = value.isEmpty()
        } ?: run {
            isPasswordEmptyOrNull = true
        }
    }

    internal fun setIsPasswordNotInCorrectFormat(password: String?) {
        password?.let { value ->
            isPasswordInNotCorrectFormat = !PASSWORD_PATTERN.toRegex().matches(value)
        } ?: run {
            isPasswordInNotCorrectFormat = true
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
        val alertTitle = application.getString(StringsIds.error)

        return when {
            !network.isDeviceConnectedToInternet() -> createAlert(application.getString(StringsIds.notConnectedToInternet), StringsIds.deviceIsCurrentlyNotConnectedToInternetDesc)
            isTwoOrMoreFieldsEmptyOrNull -> createAlert(application.getString(StringsIds.emptyFields), StringsIds.emptyFieldsDescription)
            isUsernameEmptyOrNull -> createAlert(alertTitle, StringsIds.usernameIsRequiredPleaseEnterAUsernameToCreateAAccount)
            isUsernameInNotCorrectFormat -> createAlert(alertTitle, StringsIds.usernameIsNotInCorrectFormatPleaseEnterUsernameInCorrectFormat)
            isEmailEmptyOrNull -> createAlert(alertTitle, StringsIds.emailIsRequiredPleaseEnterAEmailToCreateAAccount)
            isEmailInNotCorrectFormat -> createAlert(alertTitle, StringsIds.emailIsNotInCorrectFormatPleaseEnterEmailInCorrectFormat)
            isPasswordEmptyOrNull -> createAlert(alertTitle, StringsIds.passwordIsRequiredPleaseEnterAPasswordToCreateAAccount)
            isPasswordInNotCorrectFormat -> createAlert(alertTitle, StringsIds.passwordIsNotInCorrectFormatPleaseEnterPasswordInCorrectFormat)
            else -> null
        }
    }

    private fun createAlert(title: String, descriptionId: Int): Alert {
        return defaultAlert.copy(
            title = title,
            description = application.getString(descriptionId)
        )
    }

    internal suspend fun attemptToCreateFirebaseAuthAndSendEmailVerification(email: String, username: String, password: String) {
        createFirebaseUserInfo.attemptToCreateAccountFirebaseAuthResponseFlow(email = email, password)
            .collectLatest { createAccountFirebaseAuthResponse ->
                if (createAccountFirebaseAuthResponse.isSuccessful) {
                    authenticationFirebase.attemptToSendEmailVerificationForCurrentUser()
                        .collectLatest { authenticatedUserViaEmailFirebaseResponse ->
                            if (authenticatedUserViaEmailFirebaseResponse.isSuccessful) {
                                createSharedPreferences.createIsLoggedIn(value = true)
                                navigateToAuthentication(email = email, username = username)
                                clearState()
                            } else {
                                showUnableToSendEmailVerificationAlert(email = email, username = username)
                            }
                        }
                } else {
                    showUnableToCreateFirebaseAuthAlert(message = createAccountFirebaseAuthResponse.exception?.message)
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

    internal fun showUnableToCreateFirebaseAuthAlert(message: String?) {
        navigation.disableProgress()
        navigation.alert(
            alert = defaultAlert.copy(
                title = application.getString(StringsIds.unableToCreateAccount),
                description = message ?: application.getString(StringsIds.havingTroubleCreatingYourAccountPleaseTryAgain)
            )
        )
    }

    fun onUsernameValueChanged(newUsername: String) {
        createAccountMutableStateFlow.value = createAccountStateFlow.value.copy(username = newUsername)
    }

    fun onEmailValueChanged(newEmail: String) {
        createAccountMutableStateFlow.value = createAccountStateFlow.value.copy(email = newEmail)
    }

    fun onPasswordValueChanged(newPassword: String) {
        createAccountMutableStateFlow.value = createAccountStateFlow.value.copy(password = newPassword)
    }
}
