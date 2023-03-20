package com.nicholas.rutherford.track.my.shot.feature.create.account

import android.app.Application
import androidx.core.util.PatternsCompat
import com.nicholas.rutherford.track.my.shot.data.test.account.info.TestAuthenticateUserViaEmailFirebaseResponse
import com.nicholas.rutherford.track.my.shot.data.test.account.info.TestCreateAccountFirebaseAuthResponse
import com.nicholas.rutherford.track.my.shot.feature.create.account.createaccount.CreateAccountNavigation
import com.nicholas.rutherford.track.my.shot.feature.create.account.createaccount.CreateAccountState
import com.nicholas.rutherford.track.my.shot.feature.create.account.createaccount.CreateAccountViewModel
import com.nicholas.rutherford.track.my.shot.feature.create.account.createaccount.EMAIL_PATTERN
import com.nicholas.rutherford.track.my.shot.feature.create.account.createaccount.PASSWORD_PATTERN
import com.nicholas.rutherford.track.my.shot.feature.splash.StringsIds
import com.nicholas.rutherford.track.my.shot.firebase.create.CreateFirebaseUserInfo
import com.nicholas.rutherford.track.my.shot.firebase.util.authentication.AuthenticationFirebase
import com.nicholas.rutherford.track.my.shot.helper.network.Network
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
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

    private val network = mockk<Network>(relaxed = true)

    private val createFirebaseUserInfo = mockk<CreateFirebaseUserInfo>(relaxed = true)
    private val authenticationFirebase = mockk<AuthenticationFirebase>(relaxed = true)

    private val state = CreateAccountState(username = null, email = null, password = null)

    private val createAccountFirebaseAuthResponse = TestCreateAccountFirebaseAuthResponse().create()
    private var authenticateUserViaEmailFirebaseResponse = TestAuthenticateUserViaEmailFirebaseResponse().create()

    @OptIn(ExperimentalCoroutinesApi::class)
    val dispatcher = StandardTestDispatcher()

    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeEach
    fun beforeEach() {
        Dispatchers.setMain(dispatcher)
        viewModel = CreateAccountViewModel(
            navigation = navigation,
            application = application,
            network = network,
            createFirebaseUserInfo = createFirebaseUserInfo,
            authenticationFirebase = authenticationFirebase
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @AfterEach
    fun afterEach() {
        Dispatchers.resetMain()
    }

    @Test
    fun constants() {
        Assertions.assertEquals(EMAIL_PATTERN, "^\\w+@[a-zA-Z_]+?\\.[a-zA-Z]{2,3}\$")
        Assertions.assertEquals(PASSWORD_PATTERN, "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@\$!%*?&])[A-Za-z\\d@\$!%*?&]{8,}\$")
    }

    @Test
    fun initializeCreateAccountState() {
        Assertions.assertEquals(
            viewModel.createAccountStateFlow.value, state
        )
    }

    @Nested
    inner class EmailPattern {
        private val regex = EMAIL_PATTERN.toRegex()

        private val invalidEmails = listOf(
            "joe@123aspx.com",
            "joe@web.info ",
            "joe@company.co.uk"
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

//    @Nested
//    inner class SetIsPasswordInNotCorrectFormat {
//
//        @Test fun `when password is null should set isPasswordNotInNotCorrectFormat to true`() {
//            viewModel.isPasswordInNotCorrectFormat = false
//
//            viewModel.setIsPasswordNotInCorrectFormat("dsds")
//        }
//    }

    @Nested
    inner class SetIsEmailInNotCorrectFormat {
        private val testEmail = "testemail@yahoo.com"

        @Test fun `when email is null should set isEmailInNotCorrectFormat to true`() {
            viewModel.isEmailInNotCorrectFormat = false

            viewModel.setIsEmailInNotCorrectFormat(email = null)

            Assertions.assertEquals(
                viewModel.isEmailInNotCorrectFormat,
                true
            )
        }

        @Test fun `when email is not null and not in correct format should set isEmailInNotCorrectFormat to true`() {
            mockkObject(PatternsCompat.EMAIL_ADDRESS)

            every { PatternsCompat.EMAIL_ADDRESS.matcher(testEmail).matches() } returns false

            viewModel.isEmailInNotCorrectFormat = false

            viewModel.setIsEmailInNotCorrectFormat(email = testEmail)

            Assertions.assertEquals(
                viewModel.isEmailInNotCorrectFormat,
                true
            )
        }

        @Test fun `when email is not null and in correct format should set isEmailInNotCorrectFormat to false`() {
            mockkObject(PatternsCompat.EMAIL_ADDRESS)

            every { PatternsCompat.EMAIL_ADDRESS.matcher(testEmail).matches() } returns true

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

            coEvery { network.isDeviceConnectedToInternet() } returns false

            viewModel.attemptToShowErrorAlertOrCreateFirebaseAuth(
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

            coEvery { network.isDeviceConnectedToInternet() } returns true

            viewModel.isTwoOrMoreFieldsEmptyOrNull = false
            viewModel.isUsernameEmptyOrNull = false
            viewModel.isEmailEmptyOrNull = false
            viewModel.isPasswordEmptyOrNull = false

            viewModel.attemptToShowErrorAlertOrCreateFirebaseAuth(
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

            coEvery { network.isDeviceConnectedToInternet() } returns true

            viewModel.isTwoOrMoreFieldsEmptyOrNull = false
            viewModel.isUsernameEmptyOrNull = false
            viewModel.isEmailEmptyOrNull = false
            viewModel.isPasswordEmptyOrNull = false

            viewModel.attemptToShowErrorAlertOrCreateFirebaseAuth(
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
        fun `when validafeFieldsWithOptionalAlert is not null and password is null should not call any functions`() = runTest {
            Dispatchers.setMain(dispatcher)

            coEvery { network.isDeviceConnectedToInternet() } returns true

            viewModel.isTwoOrMoreFieldsEmptyOrNull = false
            viewModel.isUsernameEmptyOrNull = false
            viewModel.isEmailEmptyOrNull = false
            viewModel.isPasswordEmptyOrNull = false

            viewModel.attemptToShowErrorAlertOrCreateFirebaseAuth(
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
    inner class ValidateFieldsWithOptionalAlert {

        @OptIn(ExperimentalCoroutinesApi::class)
        @Test
        fun `when isDeviceConnectedToInternet is set to true should return back not connected to internet alert`() = runTest {
            Dispatchers.setMain(dispatcher)

            coEvery { network.isDeviceConnectedToInternet() } returns false

            Assertions.assertEquals(
                viewModel.validateFieldsWithOptionalAlert(),
                viewModel.defaultAlert.copy(
                    title = application.getString(StringsIds.notConnectedToInternet),
                    description = application.getString(StringsIds.deviceIsCurrentlyNotConnectedToInternetDesc)
                )
            )
        }

        @OptIn(ExperimentalCoroutinesApi::class)
        @Test
        fun `when isTwoOrMoreFieldsEmptyOrNull is set to true should return back mulitple fields are required alert`() = runTest {
            Dispatchers.setMain(dispatcher)

            coEvery { network.isDeviceConnectedToInternet() } returns true

            viewModel.isTwoOrMoreFieldsEmptyOrNull = false
            viewModel.isUsernameEmptyOrNull = false
            viewModel.isEmailEmptyOrNull = false
            viewModel.isEmailInNotCorrectFormat = false
            viewModel.isPasswordEmptyOrNull = false
            viewModel.isTwoOrMoreFieldsEmptyOrNull = true

            Assertions.assertEquals(
                viewModel.validateFieldsWithOptionalAlert(),
                viewModel.defaultAlert.copy(
                    title = application.getString(StringsIds.emptyFields),
                    description = application.getString(StringsIds.multipleFieldsAreRequiredThatAreNotEnteredPleaseEnterAllFields)
                )
            )
        }

        @OptIn(ExperimentalCoroutinesApi::class)
        @Test
        fun `when isUsernameEmptyOrNull is set to true should return back username is required please enter a username alert`() = runTest {
            Dispatchers.setMain(dispatcher)

            coEvery { network.isDeviceConnectedToInternet() } returns true

            viewModel.isTwoOrMoreFieldsEmptyOrNull = false
            viewModel.isUsernameEmptyOrNull = true
            viewModel.isEmailEmptyOrNull = false
            viewModel.isEmailInNotCorrectFormat = false
            viewModel.isPasswordEmptyOrNull = false

            Assertions.assertEquals(
                viewModel.validateFieldsWithOptionalAlert(),
                viewModel.defaultAlert.copy(
                    title = application.getString(StringsIds.emptyFields),
                    description = application.getString(StringsIds.usernameIsRequiredPleaseEnterAUsernameToCreateAAccount)
                )
            )
        }

        @OptIn(ExperimentalCoroutinesApi::class)
        @Test
        fun `when isEmailEmptyOrNull is set to true should return back email is required alert`() = runTest {
            Dispatchers.setMain(dispatcher)

            coEvery { network.isDeviceConnectedToInternet() } returns true

            viewModel.isTwoOrMoreFieldsEmptyOrNull = false
            viewModel.isUsernameEmptyOrNull = false
            viewModel.isEmailEmptyOrNull = true
            viewModel.isEmailInNotCorrectFormat = false
            viewModel.isPasswordEmptyOrNull = false

            Assertions.assertEquals(
                viewModel.validateFieldsWithOptionalAlert(),
                viewModel.defaultAlert.copy(
                    title = application.getString(StringsIds.emptyFields),
                    description = application.getString(StringsIds.emailIsRequiredPleaseEnterAEmailToCreateAAccount)
                )
            )
        }

        @OptIn(ExperimentalCoroutinesApi::class)
        @Test
        fun `when isEmailInNotCorrectFormat is set to true should return back email is not in correct format alert`() = runTest {
            Dispatchers.setMain(dispatcher)

            Dispatchers.setMain(dispatcher)

            coEvery { network.isDeviceConnectedToInternet() } returns true

            viewModel.isTwoOrMoreFieldsEmptyOrNull = false
            viewModel.isUsernameEmptyOrNull = false
            viewModel.isEmailInNotCorrectFormat = true
            viewModel.isEmailEmptyOrNull = false
            viewModel.isPasswordEmptyOrNull = false

            Assertions.assertEquals(
                viewModel.validateFieldsWithOptionalAlert(),
                viewModel.defaultAlert.copy(
                    title = application.getString(StringsIds.emptyFields),
                    description = application.getString(
                        StringsIds.emailIsNotInCorrectFormatPleaseEnterEmailInCorrectFormat
                    )
                )
            )
        }

        @OptIn(ExperimentalCoroutinesApi::class)
        @Test
        fun `when isPasswordEmptyOrNull is set to true should return back password is required alert`() = runTest {
            Dispatchers.setMain(dispatcher)

            coEvery { network.isDeviceConnectedToInternet() } returns true

            viewModel.isTwoOrMoreFieldsEmptyOrNull = false
            viewModel.isUsernameEmptyOrNull = false
            viewModel.isEmailEmptyOrNull = false
            viewModel.isEmailInNotCorrectFormat = false
            viewModel.isPasswordEmptyOrNull = true

            Assertions.assertEquals(
                viewModel.validateFieldsWithOptionalAlert(),
                viewModel.defaultAlert.copy(
                    title = application.getString(StringsIds.emptyFields),
                    description = application.getString(StringsIds.passwordIsRequiredPleaseEnterAPasswordToCreateAAccount)
                )
            )
        }

        @OptIn(ExperimentalCoroutinesApi::class)
        @Test
        fun `when all validation fields are set to false should return back null`() = runTest {
            Dispatchers.setMain(dispatcher)

            coEvery { network.isDeviceConnectedToInternet() } returns true

            viewModel.isTwoOrMoreFieldsEmptyOrNull = false
            viewModel.isUsernameEmptyOrNull = false
            viewModel.isEmailEmptyOrNull = false
            viewModel.isEmailInNotCorrectFormat = false
            viewModel.isPasswordEmptyOrNull = false

            Assertions.assertEquals(
                viewModel.validateFieldsWithOptionalAlert(),
                null
            )
        }
    }

    @Nested
    inner class AttemptToCreateFirebaseAuthAndSendEmailVerification {

        private val testEmail = "testemail@gmail.com"
        private val testPassword = "testPassword"
        private val testUsername = "testUsername"

        @OptIn(ExperimentalCoroutinesApi::class)
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

        @OptIn(ExperimentalCoroutinesApi::class)
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

        @OptIn(ExperimentalCoroutinesApi::class)
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

                verify { navigation.disableProgress() }
                verify { navigation.navigateToAuthentication(email = testEmail, username = testUsername) }
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
