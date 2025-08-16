package com.nicholas.rutherford.track.your.shot.feature.create.account.createaccount

import android.app.Application
import com.nicholas.rutherford.track.your.shot.base.resources.StringsIds
import com.nicholas.rutherford.track.your.shot.data.room.repository.ActiveUserRepository
import com.nicholas.rutherford.track.your.shot.data.room.repository.DeclaredShotRepository
import com.nicholas.rutherford.track.your.shot.data.room.response.ActiveUser
import com.nicholas.rutherford.track.your.shot.firebase.TestAuthenticateUserViaEmailFirebaseResponse
import com.nicholas.rutherford.track.your.shot.firebase.TestCreateAccountFirebaseAuthResponse
import com.nicholas.rutherford.track.your.shot.firebase.core.create.CreateFirebaseUserInfo
import com.nicholas.rutherford.track.your.shot.firebase.util.authentication.AuthenticationFirebase
import com.nicholas.rutherford.track.your.shot.helper.account.AccountManager
import com.nicholas.rutherford.track.your.shot.helper.constants.Constants
import com.nicholas.rutherford.track.your.shot.shared.preference.create.CreateSharedPreferences
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class CreateAccountViewModelTest {

    private lateinit var viewModel: CreateAccountViewModel

    private var navigation = mockk<CreateAccountNavigation>(relaxed = true)
    private val application = mockk<Application>(relaxed = true)

    private val createFirebaseUserInfo = mockk<CreateFirebaseUserInfo>(relaxed = true)
    private val createSharedPreferences = mockk<CreateSharedPreferences>(relaxed = true)
    private val authenticationFirebase = mockk<AuthenticationFirebase>(relaxed = true)

    private val accountManager = mockk<AccountManager>(relaxed = true)

    private val activeUserRepository = mockk<ActiveUserRepository>(relaxed = true)
    private val declaredShotRepository = mockk<DeclaredShotRepository>(relaxed = true)

    private val state = CreateAccountState(username = null, email = null, password = null)

    private val createAccountFirebaseAuthResponse = TestCreateAccountFirebaseAuthResponse().create()
    private var authenticateUserViaEmailFirebaseResponse = TestAuthenticateUserViaEmailFirebaseResponse().create()

    @OptIn(ExperimentalCoroutinesApi::class)
    private val dispatcher = UnconfinedTestDispatcher()

    private val scope = CoroutineScope(SupervisorJob() + dispatcher)

    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeEach
    fun beforeEach() {
        Dispatchers.setMain(dispatcher)
        viewModel = CreateAccountViewModel(
            navigation = navigation,
            application = application,
            createFirebaseUserInfo = createFirebaseUserInfo,
            createSharedPreferences = createSharedPreferences,
            authenticationFirebase = authenticationFirebase,
            accountManager = accountManager,
            activeUserRepository = activeUserRepository,
            declaredShotRepository = declaredShotRepository,
            scope = scope
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @AfterEach
    fun afterEach() {
        Dispatchers.resetMain()
    }

    @Test
    fun constants() {
        Assertions.assertEquals(EMAIL_PATTERN, "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")
        Assertions.assertEquals(
            PASSWORD_PATTERN,
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$"
        )
        Assertions.assertEquals(
            USERNAME_PATTERN,
            "^(?=[a-zA-Z\\d._]{8,20}$)(?!.*[_.]{2})[^_.].*[^_.]$"
        )
    }

    @Test
    fun `clear state`() {
        Assertions.assertEquals(
            viewModel.createAccountStateFlow.value,
            CreateAccountState()
        )
    }

    @Test
    fun initializeCreateAccountState() {
        Assertions.assertEquals(
            viewModel.createAccountStateFlow.value,
            state
        )
    }

    @Nested
    inner class EmailPattern {
        private val regex = EMAIL_PATTERN.toRegex()

        private val invalidEmails = listOf(
            "joe@123aspx@",
            "joe@web.info.com ",
            "user@domain.c"
        )
        private val validEmails = listOf(
            "oe@aol.com",
            "ssmith@aspalliance.com",
            "a@b.cc"
        )

        @Test
        fun `should not contain match with invalid emails`() {
            invalidEmails.forEach { value ->
                Assertions.assertFalse(regex.containsMatchIn(value))
            }
        }

        @Test
        fun `should contain match with valid emails`() {
            validEmails.forEach { value ->
                Assertions.assertTrue(regex.containsMatchIn(value))
            }
        }
    }

    @Nested
    inner class PasswordPattern {
        private val regex = PASSWORD_PATTERN.toRegex()

        private val invalidPasswords = listOf(
            "Pass12",
            "password#@@@12",
            "PASS1234@@#",
            "Password$$$$",
            "Password121212"
        )
        private val validPasswords = listOf(
            "Password$123",
            "CorrectNickPassword&122",
            "passWord%12121",
            "trackMyShotIsAwesome@12",
            "Password$$$$121"
        )

        @Test
        fun `should not contain match with invalid passwords`() {
            invalidPasswords.forEach { value ->
                Assertions.assertFalse(regex.containsMatchIn(value))
            }
        }

        @Test
        fun `should contain match with valid passwords`() {
            validPasswords.forEach { value ->
                Assertions.assertTrue(regex.containsMatchIn(value))
            }
        }
    }

    @Nested
    inner class UsernamePattern {
        private val regex = USERNAME_PATTERN.toRegex()

        private val invalidUsernames = listOf(
            "user",
            "username12121212121212121212121",
            ".username",
            "username.",
            "_username",
            "username-1",
            "username_",
            "username$$$",
            "..username",
            "._username",
            "_.username",
            "__username"
        )
        private val validUsernames = listOf(
            "UsernameValid1291",
            "testAccount4112",
            "OneTestUsername2222",
            "Test2211",
            "Username124"
        )

        @Test
        fun `should not contain match with invalid usernames`() {
            invalidUsernames.forEach { value ->
                Assertions.assertFalse(regex.containsMatchIn(value))
            }
        }

        @Test
        fun `should contain match with valid usernames`() {
            validUsernames.forEach { value ->
                Assertions.assertTrue(regex.containsMatchIn(value))
            }
        }
    }

    @Test fun `on back button clicked should pop`() {
        viewModel.onBackButtonClicked()

        verify { navigation.pop() }
    }

    @Nested
    inner class SetIsUsernameEmptyOrNull {

        @Test fun `when username is null should set isUsernameEmptyOrNull to true`() {
            viewModel.isUsernameEmptyOrNull = false

            viewModel.setIsUsernameEmptyOrNull(username = null)

            Assertions.assertEquals(
                viewModel.isUsernameEmptyOrNull,
                true
            )
        }

        @Test fun `when username is not null with a actual value should set isUsernameEmptyOrNull to false`() {
            viewModel.isUsernameEmptyOrNull = true

            viewModel.setIsUsernameEmptyOrNull(username = "username")

            Assertions.assertEquals(
                viewModel.isUsernameEmptyOrNull,
                false
            )
        }

        @Test fun `when username is not null and empty should set isUsernameEmptyOrNull to true`() {
            viewModel.isUsernameEmptyOrNull = false

            viewModel.setIsUsernameEmptyOrNull(username = "")

            Assertions.assertEquals(
                viewModel.isUsernameEmptyOrNull,
                true
            )
        }
    }

    @Nested
    inner class SetIsUsernameInNotCorrectFormat {
        private val invalidUsername = "_username"
        private val validUsername = "UsernameTest12212"

        @Test fun `when username is null should set isUsernameInNotCorrectFormat to true`() {
            viewModel.isUsernameInNotCorrectFormat = false

            viewModel.setIsUsernameInNotCorrectFormat(username = null)

            Assertions.assertEquals(
                viewModel.isUsernameInNotCorrectFormat,
                true
            )
        }

        @Test fun `when username is not null and invalid should set isUsernameInNotCorrectFormat to true`() {
            viewModel.isUsernameInNotCorrectFormat = false

            viewModel.setIsUsernameInNotCorrectFormat(username = invalidUsername)

            Assertions.assertEquals(
                viewModel.isUsernameInNotCorrectFormat,
                true
            )
        }

        @Test fun `when username is not null and valid should set isUsernameInNotCorrectFormat to false`() {
            viewModel.isUsernameInNotCorrectFormat = false

            viewModel.setIsUsernameInNotCorrectFormat(username = validUsername)

            Assertions.assertEquals(
                viewModel.isUsernameInNotCorrectFormat,
                false
            )
        }
    }

    @Nested
    inner class SetIsEmailEmptyOrNull {

        @Test fun `when email is null should set isEmailEmptyOrNull to true`() {
            viewModel.isEmailEmptyOrNull = false

            viewModel.setIsEmailEmptyOrNull(email = null)

            Assertions.assertEquals(
                viewModel.isEmailEmptyOrNull,
                true
            )
        }

        @Test fun `when email is not null with a actual value should set isEmailEmptyOrNull to false`() {
            viewModel.isEmailEmptyOrNull = true

            viewModel.setIsEmailEmptyOrNull(email = "email@gmail.com")

            Assertions.assertEquals(
                viewModel.isEmailEmptyOrNull,
                false
            )
        }

        @Test fun `when email is not null and empty should set isEmailEmptyOrNull to true`() {
            viewModel.isEmailEmptyOrNull = false

            viewModel.setIsEmailEmptyOrNull(email = "")

            Assertions.assertEquals(
                viewModel.isEmailEmptyOrNull,
                true
            )
        }
    }

    @Nested
    inner class SetIsPasswordInNotCorrectFormat {
        private val testPassword = "Password$12341"
        private val invalidTestPassword = "Password"

        @Test fun `when password is null should set isPasswordNotInNotCorrectFormat to true`() {
            viewModel.isPasswordInNotCorrectFormat = false

            viewModel.setIsPasswordNotInCorrectFormat(password = null)

            Assertions.assertEquals(
                viewModel.isPasswordInNotCorrectFormat,
                true
            )
        }

        @Test fun `when password is not null and is invalid should set isPasswordNotInNotCorrectFormat to true`() {
            viewModel.isPasswordInNotCorrectFormat = false

            viewModel.setIsPasswordNotInCorrectFormat(password = invalidTestPassword)

            Assertions.assertEquals(
                viewModel.isPasswordInNotCorrectFormat,
                true
            )
        }

        @Test fun `when password is not null and is valid should set isPasswordNotInNotCorrectFormat to false`() {
            viewModel.isPasswordInNotCorrectFormat = false

            viewModel.setIsPasswordNotInCorrectFormat(password = testPassword)

            Assertions.assertEquals(
                viewModel.isPasswordInNotCorrectFormat,
                false
            )
        }
    }

    @Nested
    inner class SetIsEmailInNotCorrectFormat {
        private val testEmail = "testemail@yahoo.com"
        private val invalidTestEmail = "testEmail"

        @Test fun `when email is null should set isEmailInNotCorrectFormat to true`() {
            viewModel.isEmailInNotCorrectFormat = false

            viewModel.setIsEmailInNotCorrectFormat(email = null)

            Assertions.assertEquals(
                viewModel.isEmailInNotCorrectFormat,
                true
            )
        }

        @Test fun `when email is not null and is invalid should set isEmailInNotCorrectFormat to true`() {
            viewModel.isEmailInNotCorrectFormat = false

            viewModel.setIsEmailInNotCorrectFormat(email = invalidTestEmail)

            Assertions.assertEquals(
                viewModel.isEmailInNotCorrectFormat,
                true
            )
        }

        @Test fun `when email is not null and is valid should set isEmailInNotCorrectFormat to false`() {
            viewModel.isEmailInNotCorrectFormat = false

            viewModel.setIsEmailInNotCorrectFormat(email = testEmail)

            Assertions.assertEquals(
                viewModel.isEmailInNotCorrectFormat,
                false
            )
        }
    }

    @Nested
    inner class SetIsPasswordEmptyOrNull {

        @Test fun `when password is null should set isPasswordEmptyOrNull is true`() {
            viewModel.isPasswordEmptyOrNull = false

            viewModel.setIsPasswordEmptyOrNull(password = null)

            Assertions.assertEquals(
                viewModel.isPasswordEmptyOrNull,
                true
            )
        }

        @Test fun `when password is not null with a actual value should set isPasswordEmptyOrNull to false`() {
            viewModel.isPasswordEmptyOrNull = true

            viewModel.setIsPasswordEmptyOrNull(password = "password")

            Assertions.assertEquals(
                viewModel.isPasswordEmptyOrNull,
                false
            )
        }

        @Test fun `when password is not null and empty should set isPasswordEmptyOrNull to true`() {
            viewModel.isPasswordEmptyOrNull = false

            viewModel.setIsPasswordEmptyOrNull(password = "")

            Assertions.assertEquals(
                viewModel.isPasswordEmptyOrNull,
                true
            )
        }
    }

    @Nested
    inner class SetIsTwoOrMoreFieldsEmptyOrNull {

        @Test fun `when none of the fields are empty or null isTwoMoreFieldsEmptyOrNull should be set to false`() {
            viewModel.isUsernameEmptyOrNull = false
            viewModel.isEmailEmptyOrNull = false
            viewModel.isPasswordEmptyOrNull = false
            viewModel.isTwoOrMoreFieldsEmptyOrNull = true

            viewModel.setIsTwoOrMoreFieldsEmptyOrNull()

            Assertions.assertEquals(
                viewModel.isTwoOrMoreFieldsEmptyOrNull,
                false
            )
        }

        @Test fun `when 1 of the fields are empty or null isTwoMoreFieldsEmptyOrNull should be set to false`() {
            viewModel.isUsernameEmptyOrNull = true
            viewModel.isEmailEmptyOrNull = false
            viewModel.isPasswordEmptyOrNull = false
            viewModel.isTwoOrMoreFieldsEmptyOrNull = true

            viewModel.setIsTwoOrMoreFieldsEmptyOrNull()

            Assertions.assertEquals(
                viewModel.isTwoOrMoreFieldsEmptyOrNull,
                false
            )
        }

        @Test fun `when 2 of the fields are empty or null isTwoMoreFieldsEmptyOrNull should be set to true`() {
            viewModel.isUsernameEmptyOrNull = true
            viewModel.isEmailEmptyOrNull = true
            viewModel.isPasswordEmptyOrNull = false
            viewModel.isTwoOrMoreFieldsEmptyOrNull = false

            viewModel.setIsTwoOrMoreFieldsEmptyOrNull()

            Assertions.assertEquals(
                viewModel.isTwoOrMoreFieldsEmptyOrNull,
                true
            )
        }

        @Test fun `when all of the fields are empty or null isTwoMoreFieldsEmptyOrNull should be set to true`() {
            viewModel.isUsernameEmptyOrNull = true
            viewModel.isEmailEmptyOrNull = true
            viewModel.isPasswordEmptyOrNull = true
            viewModel.isTwoOrMoreFieldsEmptyOrNull = false

            viewModel.setIsTwoOrMoreFieldsEmptyOrNull()

            Assertions.assertEquals(
                viewModel.isTwoOrMoreFieldsEmptyOrNull,
                true
            )
        }
    }

    @Nested
    inner class AttemptToShowErrorOrCreateFirebaseAuth {

        private val testUsername = "testUsername11"
        private val testEmail = "test@yahoo.com"
        private val testPassword = "PasswordTest"

        @OptIn(ExperimentalCoroutinesApi::class)
        @Test
        fun `when validateFieldsWithOptionalAlert is not null should call disableProgress and alert`() = runTest {
            Dispatchers.setMain(dispatcher)

            viewModel.attemptToShowErrorAlertOrCreateFirebaseAuth(
                isConnectedToInternet = false,
                createAccountState = CreateAccountState(
                    username = null,
                    email = null,
                    password = null
                )
            )

            verify { navigation.disableProgress() }
            verify { navigation.alert(alert = any()) }

            coVerify(exactly = 0) {
                viewModel.attemptToCreateFirebaseAuthAndSendEmailVerification(
                    email = "",
                    username = "",
                    password = ""
                )
            }
        }

        @OptIn(ExperimentalCoroutinesApi::class)
        @Test
        fun `when validateFieldsWithOptionalAlert is not null and username is null should not call any functions`() = runTest {
            Dispatchers.setMain(dispatcher)

            viewModel.isTwoOrMoreFieldsEmptyOrNull = false
            viewModel.isUsernameEmptyOrNull = false
            viewModel.isEmailEmptyOrNull = false
            viewModel.isPasswordEmptyOrNull = false

            viewModel.attemptToShowErrorAlertOrCreateFirebaseAuth(
                isConnectedToInternet = true,
                createAccountState = CreateAccountState(
                    username = null,
                    email = testEmail,
                    password = testPassword
                )
            )

            verify(exactly = 0) { navigation.disableProgress() }
            verify(exactly = 0) { navigation.alert(alert = any()) }

            coVerify(exactly = 0) {
                viewModel.attemptToCreateFirebaseAuthAndSendEmailVerification(
                    email = testEmail,
                    username = "",
                    password = testPassword
                )
            }
        }

        @OptIn(ExperimentalCoroutinesApi::class)
        @Test
        fun `when validateFieldsWithOptionalAlert is not null and email is null should not call any functions`() = runTest {
            Dispatchers.setMain(dispatcher)

            viewModel.isTwoOrMoreFieldsEmptyOrNull = false
            viewModel.isUsernameEmptyOrNull = false
            viewModel.isEmailEmptyOrNull = false
            viewModel.isPasswordEmptyOrNull = false

            viewModel.attemptToShowErrorAlertOrCreateFirebaseAuth(
                isConnectedToInternet = true,
                createAccountState = CreateAccountState(
                    username = testUsername,
                    email = null,
                    password = testPassword
                )
            )

            verify(exactly = 0) { navigation.disableProgress() }
            verify(exactly = 0) { navigation.alert(alert = any()) }

            coVerify(exactly = 0) {
                viewModel.attemptToCreateFirebaseAuthAndSendEmailVerification(
                    email = "",
                    username = testUsername,
                    password = testPassword
                )
            }
        }

        @OptIn(ExperimentalCoroutinesApi::class)
        @Test
        fun `when validateFieldsWithOptionalAlert is not null and password is null should not call any functions`() = runTest {
            Dispatchers.setMain(dispatcher)

            viewModel.isTwoOrMoreFieldsEmptyOrNull = false
            viewModel.isUsernameEmptyOrNull = false
            viewModel.isEmailEmptyOrNull = false
            viewModel.isPasswordEmptyOrNull = false

            viewModel.attemptToShowErrorAlertOrCreateFirebaseAuth(
                isConnectedToInternet = true,
                createAccountState = CreateAccountState(
                    username = testUsername,
                    email = testEmail,
                    password = null
                )
            )

            verify(exactly = 0) { navigation.disableProgress() }
            verify(exactly = 0) { navigation.alert(alert = any()) }

            coVerify(exactly = 0) {
                viewModel.attemptToCreateFirebaseAuthAndSendEmailVerification(
                    email = testEmail,
                    username = testUsername,
                    password = ""
                )
            }
        }
    }

    @Nested
    inner class AttemptToCreateAccount {

        private val username = "username"
        private val email = "testemail@gmail.com"
        private val key = "ke"

        @Test
        fun `when attemptToCreateAccountFirebaseRealTimeDatabaseResponseFlow first value returns false should disable progress and show alert`() = runTest {
            coEvery { createFirebaseUserInfo.attemptToCreateAccountFirebaseRealTimeDatabaseResponseFlow(userName = username, email = email) } returns flowOf(Pair(first = false, second = key))

            viewModel.attemptToCreateAccount(username = username, email = email)

            coVerify(exactly = 0) {
                activeUserRepository.updateActiveUser(
                    activeUser = ActiveUser(
                        id = Constants.ACTIVE_USER_ID,
                        accountHasBeenCreated = true,
                        email = email,
                        username = username,
                        firebaseAccountInfoKey = key
                    )
                )
            }
            verify(exactly = 0) { createSharedPreferences.createShouldShowTermsAndConditionsPreference(value = true) }
            coVerify(exactly = 0) { declaredShotRepository.createDeclaredShots(shotIdsToFilterOut = emptyList()) }
            verify(exactly = 0) { navigation.navigateToTermsAndConditions() }

            verify { navigation.disableProgress() }
            verify { navigation.alert(alert = any()) }
        }

        @Test
        fun `when attemptToCreateAccountFirebaseRealTimeDatabaseResponseFlow first value returns true should disable progress and show terms and conditions`() = runTest {
            coEvery { createFirebaseUserInfo.attemptToCreateAccountFirebaseRealTimeDatabaseResponseFlow(userName = username, email = email) } returns flowOf(Pair(first = true, second = key))

            viewModel.attemptToCreateAccount(username = username, email = email)

            coVerify {
                activeUserRepository.updateActiveUser(
                    activeUser = ActiveUser(
                        id = Constants.ACTIVE_USER_ID,
                        accountHasBeenCreated = true,
                        email = email,
                        username = username,
                        firebaseAccountInfoKey = key
                    )
                )
            }
            verify(exactly = 1) { createSharedPreferences.createShouldShowTermsAndConditionsPreference(value = true) }
            coVerify(exactly = 1) { declaredShotRepository.createDeclaredShots(shotIdsToFilterOut = emptyList()) }
            verify(exactly = 1) { navigation.disableProgress() }
            verify(exactly = 1) { navigation.navigateToTermsAndConditions() }

            verify(exactly = 0) { navigation.alert(alert = any()) }
        }
    }

    @Nested
    inner class ValidateFieldsWithOptionalAlert {

        @Test
        fun `when isDeviceConnectedToInternet is set to true should return back not connected to internet alert`() {
            Assertions.assertEquals(
                viewModel.validateFieldsWithOptionalAlert(isConnectedToInternet = false),
                viewModel.defaultAlert.copy(
                    title = application.getString(StringsIds.notConnectedToInternet),
                    description = application.getString(StringsIds.deviceIsCurrentlyNotConnectedToInternetDesc)
                )
            )
        }

        @Test
        fun `when isTwoOrMoreFieldsEmptyOrNull is set to true should return back multiple fields are required alert`() {
            viewModel.isTwoOrMoreFieldsEmptyOrNull = false
            viewModel.isUsernameEmptyOrNull = false
            viewModel.isEmailEmptyOrNull = false
            viewModel.isEmailInNotCorrectFormat = false
            viewModel.isPasswordEmptyOrNull = false
            viewModel.isPasswordInNotCorrectFormat = false
            viewModel.isTwoOrMoreFieldsEmptyOrNull = true

            Assertions.assertEquals(
                viewModel.validateFieldsWithOptionalAlert(isConnectedToInternet = true),
                viewModel.defaultAlert.copy(
                    title = application.getString(StringsIds.emptyFields),
                    description = application.getString(StringsIds.multipleFieldsAreRequiredThatAreNotEnteredPleaseEnterAllFields)
                )
            )
        }

        @Test
        fun `when isUsernameEmptyOrNull is set to true should return back username is not in correct format alert`() {
            viewModel.isTwoOrMoreFieldsEmptyOrNull = false
            viewModel.isUsernameEmptyOrNull = false
            viewModel.isUsernameInNotCorrectFormat = false
            viewModel.isEmailEmptyOrNull = false
            viewModel.isEmailInNotCorrectFormat = true
            viewModel.isPasswordEmptyOrNull = false
            viewModel.isPasswordInNotCorrectFormat = false

            Assertions.assertEquals(
                viewModel.validateFieldsWithOptionalAlert(isConnectedToInternet = true),
                viewModel.defaultAlert.copy(
                    title = application.getString(StringsIds.emptyFields),
                    description = application.getString(StringsIds.usernameIsNotInCorrectFormatPleaseEnterUsernameInCorrectFormat)
                )
            )
        }

        @Test
        fun `when isUsernameEmptyOrNull is set to true should return back username is required please enter a username alert`() {
            viewModel.isTwoOrMoreFieldsEmptyOrNull = false
            viewModel.isUsernameEmptyOrNull = true
            viewModel.isUsernameInNotCorrectFormat = false
            viewModel.isEmailEmptyOrNull = false
            viewModel.isEmailInNotCorrectFormat = false
            viewModel.isPasswordEmptyOrNull = false
            viewModel.isPasswordInNotCorrectFormat = false

            Assertions.assertEquals(
                viewModel.validateFieldsWithOptionalAlert(isConnectedToInternet = true),
                viewModel.defaultAlert.copy(
                    title = application.getString(StringsIds.emptyFields),
                    description = application.getString(StringsIds.usernameIsRequiredPleaseEnterAUsernameToCreateAAccount)
                )
            )
        }

        @Test
        fun `when isEmailEmptyOrNull is set to true should return back email is required alert`() {
            viewModel.isTwoOrMoreFieldsEmptyOrNull = false
            viewModel.isUsernameEmptyOrNull = false
            viewModel.isUsernameInNotCorrectFormat = false
            viewModel.isEmailEmptyOrNull = true
            viewModel.isEmailInNotCorrectFormat = false
            viewModel.isPasswordEmptyOrNull = false
            viewModel.isPasswordInNotCorrectFormat = false

            Assertions.assertEquals(
                viewModel.validateFieldsWithOptionalAlert(isConnectedToInternet = true),
                viewModel.defaultAlert.copy(
                    title = application.getString(StringsIds.emptyFields),
                    description = application.getString(StringsIds.emailIsRequiredPleaseEnterAEmailToCreateAAccount)
                )
            )
        }

        @Test
        fun `when isEmailInNotCorrectFormat is set to true should return back email is not in correct format alert`() {
            viewModel.isTwoOrMoreFieldsEmptyOrNull = false
            viewModel.isUsernameEmptyOrNull = false
            viewModel.isUsernameInNotCorrectFormat = false
            viewModel.isEmailInNotCorrectFormat = true
            viewModel.isEmailEmptyOrNull = false
            viewModel.isPasswordEmptyOrNull = false
            viewModel.isPasswordInNotCorrectFormat = false

            Assertions.assertEquals(
                viewModel.validateFieldsWithOptionalAlert(isConnectedToInternet = true),
                viewModel.defaultAlert.copy(
                    title = application.getString(StringsIds.emptyFields),
                    description = application.getString(
                        StringsIds.emailIsNotInCorrectFormatPleaseEnterEmailInCorrectFormat
                    )
                )
            )
        }

        @Test
        fun `when isPasswordEmptyOrNull is set to true should return back password is required alert`() {
            viewModel.isTwoOrMoreFieldsEmptyOrNull = false
            viewModel.isUsernameEmptyOrNull = false
            viewModel.isUsernameInNotCorrectFormat = false
            viewModel.isEmailEmptyOrNull = false
            viewModel.isEmailInNotCorrectFormat = false
            viewModel.isPasswordEmptyOrNull = true
            viewModel.isPasswordInNotCorrectFormat = false

            Assertions.assertEquals(
                viewModel.validateFieldsWithOptionalAlert(isConnectedToInternet = true),
                viewModel.defaultAlert.copy(
                    title = application.getString(StringsIds.emptyFields),
                    description = application.getString(StringsIds.passwordIsRequiredPleaseEnterAPasswordToCreateAAccount)
                )
            )
        }

        @Test
        fun `when isPasswordInNotCorrectFormat is set to true should return back password is not in correct format alert`() {
            viewModel.isTwoOrMoreFieldsEmptyOrNull = false
            viewModel.isUsernameEmptyOrNull = false
            viewModel.isUsernameInNotCorrectFormat = false
            viewModel.isEmailInNotCorrectFormat = false
            viewModel.isEmailEmptyOrNull = false
            viewModel.isPasswordEmptyOrNull = false
            viewModel.isPasswordInNotCorrectFormat = true

            Assertions.assertEquals(
                viewModel.validateFieldsWithOptionalAlert(isConnectedToInternet = true),
                viewModel.defaultAlert.copy(
                    title = application.getString(StringsIds.emptyFields),
                    description = application.getString(
                        StringsIds.passwordIsNotInCorrectFormatPleaseEnterPasswordInCorrectFormat
                    )
                )
            )
        }

        @Test
        fun `when all validation fields are set to false should return back null`() {
            viewModel.isTwoOrMoreFieldsEmptyOrNull = false
            viewModel.isUsernameEmptyOrNull = false
            viewModel.isUsernameInNotCorrectFormat = false
            viewModel.isEmailEmptyOrNull = false
            viewModel.isEmailInNotCorrectFormat = false
            viewModel.isPasswordEmptyOrNull = false
            viewModel.isPasswordInNotCorrectFormat = false

            Assertions.assertEquals(
                viewModel.validateFieldsWithOptionalAlert(isConnectedToInternet = true),
                null
            )
        }
    }

    @Nested
    inner class AttemptToCreateFirebaseAuthAndSendEmailVerification {

        private val testEmail = "testemail@gmail.com"
        private val testPassword = "testPassword"
        private val testUsername = "testUsername"

        @Test
        fun `when attemptToCreateAccountFirebaseAuthResponse returns back as not successful should call functions`() =
            runTest {
                coEvery {
                    createFirebaseUserInfo.attemptToCreateAccountFirebaseAuthResponseFlow(
                        email = testEmail,
                        password = testPassword
                    )
                } returns flowOf(createAccountFirebaseAuthResponse.copy(isSuccessful = false))

                viewModel.attemptToCreateFirebaseAuthAndSendEmailVerification(
                    email = testEmail,
                    password = testPassword,
                    username = testUsername
                )

                verify { navigation.disableProgress() }
                verify { navigation.alert(alert = any()) }
            }

        @Test
        fun `when attemptToAttemptToCreateAccountFirebaseAuthResponse returns successful and send email returns back not successful should call functions`() =
            runTest {
                coEvery {
                    createFirebaseUserInfo.attemptToCreateAccountFirebaseAuthResponseFlow(
                        email = testEmail,
                        password = testPassword
                    )
                } returns flowOf(createAccountFirebaseAuthResponse.copy(isSuccessful = true))
                coEvery {
                    authenticationFirebase.attemptToSendEmailVerificationForCurrentUser()
                } returns flowOf(authenticateUserViaEmailFirebaseResponse.copy(isSuccessful = false))

                viewModel.attemptToCreateFirebaseAuthAndSendEmailVerification(
                    email = testEmail,
                    password = testPassword,
                    username = testUsername
                )

                verify { navigation.disableProgress() }
                verify { navigation.alert(alert = any()) }
                verify { navigation.navigateToAuthentication(email = testEmail, username = testUsername) }
            }

        @Test
        fun `when attemptToAttemptToCreateAccountFirebaseAuthResponse returns successful and send email returns back successful should call functions`() =
            runTest {
                coEvery {
                    createFirebaseUserInfo.attemptToCreateAccountFirebaseAuthResponseFlow(
                        email = testEmail,
                        password = testPassword
                    )
                } returns flowOf(createAccountFirebaseAuthResponse.copy(isSuccessful = true))
                coEvery {
                    authenticationFirebase.attemptToSendEmailVerificationForCurrentUser()
                } returns flowOf(authenticateUserViaEmailFirebaseResponse.copy(isSuccessful = true))

                viewModel.attemptToCreateFirebaseAuthAndSendEmailVerification(
                    email = testEmail,
                    password = testPassword,
                    username = testUsername
                )

                verify { createSharedPreferences.createIsLoggedIn(value = true) }
                coVerify { accountManager.createActiveUser(username = testUsername, email = testEmail) }
                //   coVerify { viewModel.attemptToCreateAccount(username = testUsername, email = testEmail) }
            }
    }

    @Test
    fun `onCreateAccountButton clicked verify functions are called`() {
        viewModel.onCreateAccountButtonClicked(isConnectedToInternet = true)

        verify { navigation.enableProgress(any()) }
    }

    @Nested
    inner class ShowUnableToCreateFirebaseAuthAlert {

        @Test
        fun `should call dismiss progress and alert with given message`() {
            val message = "hello world"

            viewModel.showUnableToCreateFirebaseAuthAlert(message = message)

            verify { navigation.disableProgress() }

            verify {
                navigation.alert(
                    alert = viewModel.defaultAlert.copy(
                        title = application.getString(
                            StringsIds.unableToCreateAccount
                        ),
                        description = message
                    )
                )
            }
        }

        @Test
        fun `should call dismiss progress and alert with default message if passed in message is null`() {
            viewModel.showUnableToCreateFirebaseAuthAlert(message = null)

            verify { navigation.disableProgress() }

            verify {
                navigation.alert(
                    alert = viewModel.defaultAlert.copy(
                        title = application.getString(
                            StringsIds.unableToCreateAccount
                        ),
                        description = application.getString(StringsIds.havingTroubleCreatingYourAccountPleaseTryAgain)
                    )
                )
            }
        }
    }

    @Test fun `on user name value changed should update username state value`() {
        val usernameTest = "user name 1"

        viewModel.onUsernameValueChanged(newUsername = usernameTest)

        Assertions.assertEquals(
            viewModel.createAccountStateFlow.value,
            state.copy(username = usernameTest)
        )
    }

    @Test fun `on email value changed should update email state value`() {
        val emailTest = "new email"

        viewModel.onEmailValueChanged(newEmail = emailTest)

        Assertions.assertEquals(
            viewModel.createAccountStateFlow.value,
            state.copy(email = emailTest)
        )
    }

    @Test fun `on password value changed should update password state value`() {
        val passwordTest = "new password"

        viewModel.onPasswordValueChanged(newPassword = passwordTest)

        Assertions.assertEquals(
            viewModel.createAccountStateFlow.value,
            state.copy(password = passwordTest)
        )
    }
}
