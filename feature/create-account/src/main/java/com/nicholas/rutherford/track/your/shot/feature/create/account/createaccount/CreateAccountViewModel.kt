package com.nicholas.rutherford.track.your.shot.feature.create.account.createaccount

import android.app.Application
import com.nicholas.rutherford.track.your.shot.base.resources.StringsIds
import com.nicholas.rutherford.track.your.shot.base.vm.BaseViewModel
import com.nicholas.rutherford.track.your.shot.data.room.repository.ActiveUserRepository
import com.nicholas.rutherford.track.your.shot.data.room.repository.DeclaredShotRepository
import com.nicholas.rutherford.track.your.shot.data.room.response.ActiveUser
import com.nicholas.rutherford.track.your.shot.data.shared.alert.Alert
import com.nicholas.rutherford.track.your.shot.data.shared.alert.AlertConfirmAndDismissButton
import com.nicholas.rutherford.track.your.shot.data.shared.progress.Progress
import com.nicholas.rutherford.track.your.shot.feature.players.createeditplayer.RESET_SCREEN_DELAY_IN_MILLIS
import com.nicholas.rutherford.track.your.shot.firebase.core.create.CreateFirebaseUserInfo
import com.nicholas.rutherford.track.your.shot.firebase.util.authentication.AuthenticationFirebase
import com.nicholas.rutherford.track.your.shot.helper.account.AccountManager
import com.nicholas.rutherford.track.your.shot.helper.constants.Constants
import com.nicholas.rutherford.track.your.shot.helper.extensions.safeLet
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
const val PASSWORD_PATTERN =
    "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$"

// 8 to 30 characters, only contain alphanumeric characters and underscores, first character must be alphabetic character
const val USERNAME_PATTERN = "^(?=[a-zA-Z\\d._]{8,20}$)(?!.*[_.]{2})[^_.].*[^_.]$"

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * ViewModel responsible for managing the state and logic of the Create Account screen.
 *
 * Responsibilities include:
 * - Managing UI state of username, email, and password fields.
 * - Validating input fields against defined regex patterns.
 * - Showing error alerts for invalid or missing inputs.
 * - Handling user creation via Firebase Authentication.
 * - Managing navigation and progress state during account creation.
 *
 * @param navigation Interface to handle navigation events and alerts.
 * @param application Provides access to string resources.
 * @param createFirebaseUserInfo Handles Firebase user creation logic.
 * @param createSharedPreferences Manages shared preferences related to user state.
 * @param authenticationFirebase Handles Firebase authentication operations.
 * @param accountManager Handles account-related operations.
 * @param activeUserRepository Manages active user data store in Room
 * @param declaredShotRepository Manages declared shot data store in Room
 * @param scope CoroutineScope for asynchronous operations.
 */
class CreateAccountViewModel(
    private val navigation: CreateAccountNavigation,
    private val application: Application,
    private val createFirebaseUserInfo: CreateFirebaseUserInfo,
    private val createSharedPreferences: CreateSharedPreferences,
    private val authenticationFirebase: AuthenticationFirebase,
    private val accountManager: AccountManager,
    private val activeUserRepository: ActiveUserRepository,
    private val declaredShotRepository: DeclaredShotRepository,
    private val scope: CoroutineScope
) : BaseViewModel() {

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

    /**
     * Clears the UI state of the Create Account screen after a delay.
     */
    private fun clearState() {
        scope.launch {
            delay(RESET_SCREEN_DELAY_IN_MILLIS)
            createAccountMutableStateFlow.value = CreateAccountState()
        }
    }

    /** Handles the back button click by clearing state and navigating back. */
    fun onBackButtonClicked() {
        clearState()
        navigation.pop()
    }

    /**
     * Handles the Create Account button click.
     *
     * Validates fields, shows progress, and attempts account creation if validation passes.
     *
     * @param isConnectedToInternet Indicates whether device is connected to the internet.
     */
    fun onCreateAccountButtonClicked(isConnectedToInternet: Boolean) {
        val createAccountState = createAccountMutableStateFlow.value

        navigation.enableProgress(progress = Progress())

        setIsUsernameEmptyOrNull(username = createAccountState.username)
        setIsUsernameInNotCorrectFormat(username = createAccountState.username)
        setIsEmailEmptyOrNull(email = createAccountState.email)
        setIsEmailInNotCorrectFormat(email = createAccountState.email)
        setIsPasswordEmptyOrNull(password = createAccountState.password)
        setIsPasswordNotInCorrectFormat(password = createAccountState.password)
        setIsTwoOrMoreFieldsEmptyOrNull()

        scope.launch {
            attemptToShowErrorAlertOrCreateFirebaseAuth(
                isConnectedToInternet = isConnectedToInternet,
                createAccountState = createAccountState
            )
        }
    }

    /**
     * Validates fields and either shows an error alert or proceeds to create Firebase Authentication user.
     *
     * @param isConnectedToInternet Whether device is connected to internet.
     * @param createAccountState Current input state for account creation.
     */
    internal suspend fun attemptToShowErrorAlertOrCreateFirebaseAuth(
        isConnectedToInternet: Boolean,
        createAccountState: CreateAccountState
    ) {
        validateFieldsWithOptionalAlert(isConnectedToInternet)?.let { alert ->
            navigation.disableProgress()
            navigation.alert(alert = alert)
        } ?: run {
            safeLet(createAccountState.email, createAccountState.username, createAccountState.password) { email, username, password ->
                attemptToCreateFirebaseAuthAndSendEmailVerification(email = email, username = username, password = password)
            }
        }
    }

    /** Setters for input validation flags */
    internal fun setIsUsernameEmptyOrNull(username: String?) {
        isUsernameEmptyOrNull = username.isNullOrEmpty()
    }

    internal fun setIsUsernameInNotCorrectFormat(username: String?) {
        isUsernameInNotCorrectFormat = username?.let { !USERNAME_PATTERN.toRegex().matches(it) } ?: true
    }

    internal fun setIsEmailEmptyOrNull(email: String?) {
        isEmailEmptyOrNull = email.isNullOrEmpty()
    }

    internal fun setIsEmailInNotCorrectFormat(email: String?) {
        isEmailInNotCorrectFormat = email?.let { !EMAIL_PATTERN.toRegex().matches(it) } ?: true
    }

    internal fun setIsPasswordEmptyOrNull(password: String?) {
        isPasswordEmptyOrNull = password.isNullOrEmpty()
    }

    internal fun setIsPasswordNotInCorrectFormat(password: String?) {
        isPasswordInNotCorrectFormat = password?.let { !PASSWORD_PATTERN.toRegex().matches(it) } ?: true
    }

    /**
     * Checks if two or more input fields are empty or null.
     */
    internal fun setIsTwoOrMoreFieldsEmptyOrNull() {
        val emptyFieldsCount = listOf(isUsernameEmptyOrNull, isEmailEmptyOrNull, isPasswordEmptyOrNull).count { it }
        isTwoOrMoreFieldsEmptyOrNull = emptyFieldsCount >= 2
    }

    /**
     * Validates all fields and returns an Alert for the first encountered error, or null if inputs are valid.
     *
     * @param isConnectedToInternet Whether the device is connected to the internet.
     * @return Alert describing the validation error, or null if valid.
     */
    internal fun validateFieldsWithOptionalAlert(isConnectedToInternet: Boolean): Alert? {
        val alertTitle = application.getString(StringsIds.error)

        return when {
            !isConnectedToInternet -> createAlert(application.getString(StringsIds.notConnectedToInternet), StringsIds.deviceIsCurrentlyNotConnectedToInternetDesc)
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

    /** Helper to create an Alert with given title and description resource. */
    private fun createAlert(title: String, descriptionId: Int): Alert {
        return defaultAlert.copy(
            title = title,
            description = application.getString(descriptionId)
        )
    }

    /**
     * Attempts to create a Firebase Authentication user and send email verification.
     *
     * @param email User email.
     * @param username Username.
     * @param password Password.
     */
    internal suspend fun attemptToCreateFirebaseAuthAndSendEmailVerification(email: String, username: String, password: String) {
        createFirebaseUserInfo.attemptToCreateAccountFirebaseAuthResponseFlow(email = email, password)
            .collectLatest { createAccountFirebaseAuthResponse ->
                if (createAccountFirebaseAuthResponse.isSuccessful) {
                    authenticationFirebase.attemptToSendEmailVerificationForCurrentUser()
                        .collectLatest { authenticatedUserViaEmailFirebaseResponse ->
                            if (authenticatedUserViaEmailFirebaseResponse.isSuccessful) {
                                createSharedPreferences.createIsLoggedIn(value = true)
                                accountManager.createActiveUser(username = username, email = email)
                                attemptToCreateAccount(email = email, username = username)
                            } else {
                                showUnableToSendEmailVerificationAlert(email = email, username = username)
                            }
                        }
                } else {
                    showUnableToCreateFirebaseAuthAlert(message = createAccountFirebaseAuthResponse.exception?.message)
                }
            }
    }

    /**
     * Attempts to create a Firebase and Room account
     *
     * Note -> This needs to be moved over to the Authentication screen; once we fix that fucntionality
     *
     * @param email User email.
     * @param username Username.
     */
    internal suspend fun attemptToCreateAccount(username: String, email: String) {
        createFirebaseUserInfo.attemptToCreateAccountFirebaseRealTimeDatabaseResponseFlow(
            userName = username,
            email = email
        ).collectLatest { response ->
            val isSuccessful = response.first
            val firebaseAccountInfoKey = response.second

            if (isSuccessful) {
                activeUserRepository.updateActiveUser(
                    activeUser = ActiveUser(
                        id = Constants.ACTIVE_USER_ID,
                        accountHasBeenCreated = true,
                        email = email,
                        username = username,
                        firebaseAccountInfoKey = firebaseAccountInfoKey
                    )
                )
                createSharedPreferences.createShouldShowTermsAndConditionsPreference(value = true)
                declaredShotRepository.createDeclaredShots(shotIdsToFilterOut = emptyList())
                navigation.disableProgress()
                navigation.navigateToTermsAndConditions()
                clearState()
            } else {
                navigation.disableProgress()
                navigation.alert(
                    alert = defaultAlert.copy(
                        title = application.getString(StringsIds.errorCreatingAccount),
                        description = application.getString(StringsIds.thereWasAErrorCreatingYourAccountPleaseTryAgain)
                    )
                )
            }
        }
    }

    /** Navigates to Authentication screen and disables progress indicator. */
    private fun navigateToAuthentication(email: String, username: String) {
        navigation.disableProgress()
        navigation.navigateToAuthentication(email = email, username = username)
    }

    /**
     * Shows an alert when email verification cannot be sent and then navigates to Authentication screen.
     *
     * @param email User email.
     * @param username Username.
     */
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

    /**
     * Shows an alert when account creation fails.
     *
     * @param message Optional error message to display.
     */
    internal fun showUnableToCreateFirebaseAuthAlert(message: String?) {
        navigation.disableProgress()
        navigation.alert(
            alert = defaultAlert.copy(
                title = application.getString(StringsIds.unableToCreateAccount),
                description = message ?: application.getString(StringsIds.havingTroubleCreatingYourAccountPleaseTryAgain)
            )
        )
    }

    /** Updates the username in the UI state. */
    fun onUsernameValueChanged(newUsername: String) {
        createAccountMutableStateFlow.value = createAccountStateFlow.value.copy(username = newUsername)
    }

    /** Updates the email in the UI state. */
    fun onEmailValueChanged(newEmail: String) {
        createAccountMutableStateFlow.value = createAccountStateFlow.value.copy(email = newEmail)
    }

    /** Updates the password in the UI state. */
    fun onPasswordValueChanged(newPassword: String) {
        createAccountMutableStateFlow.value = createAccountStateFlow.value.copy(password = newPassword)
    }
}
