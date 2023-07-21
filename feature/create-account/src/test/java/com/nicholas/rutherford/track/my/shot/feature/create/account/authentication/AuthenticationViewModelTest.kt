package com.nicholas.rutherford.track.my.shot.feature.create.account.authentication

import android.app.Application
import com.nicholas.rutherford.track.my.shot.data.room.dao.ActiveUserDao
import com.nicholas.rutherford.track.my.shot.data.room.entities.ActiveUserEntity
import com.nicholas.rutherford.track.my.shot.data.test.account.info.TestAuthenticateUserViaEmailFirebaseResponse
import com.nicholas.rutherford.track.my.shot.data.test.account.info.realtime.TestAccountInfoRealTimeResponse
import com.nicholas.rutherford.track.my.shot.data.test.account.info.realtime.USER_NAME_ACCOUNT_INFO_REALTIME_RESPONSE
import com.nicholas.rutherford.track.my.shot.data.test.room.TestActiveUserEntity
import com.nicholas.rutherford.track.my.shot.feature.splash.StringsIds
import com.nicholas.rutherford.track.my.shot.firebase.create.CreateFirebaseUserInfo
import com.nicholas.rutherford.track.my.shot.firebase.read.ReadFirebaseUserInfo
import com.nicholas.rutherford.track.my.shot.firebase.util.authentication.AuthenticationFirebase
import com.nicholas.rutherford.track.my.shot.shared.preference.create.CreateSharedPreferences
import io.mockk.*
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

class AuthenticationViewModelTest {

    lateinit var viewModel: AuthenticationViewModel

    private var readFirebaseUserInfo = mockk<ReadFirebaseUserInfo>(relaxed = true)
    private var navigation = mockk<AuthenticationNavigation>(relaxed = true)

    private val application = mockk<Application>(relaxed = true)

    private val authenticationFirebase = mockk<AuthenticationFirebase>(relaxed = true)

    private val createSharedPreferences = mockk<CreateSharedPreferences>(relaxed = true)
    private val createFirebaseUserInfo = mockk<CreateFirebaseUserInfo>(relaxed = true)

    private val activeUserDao = mockk<ActiveUserDao>(relaxed = true)

    private val activeUserEntity = TestActiveUserEntity().create()

    private val username = activeUserEntity.username
    private val email = activeUserEntity.email

    @OptIn(ExperimentalCoroutinesApi::class)
    val dispatcher = StandardTestDispatcher()

    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeEach
    fun beforeEach() {
        Dispatchers.setMain(dispatcher)
        viewModel = AuthenticationViewModel(
            readFirebaseUserInfo = readFirebaseUserInfo,
            navigation = navigation,
            application = application,
            authenticationFirebase = authenticationFirebase,
            createSharedPreferences = createSharedPreferences,
            createFirebaseUserInfo = createFirebaseUserInfo,
            activeUserDao = activeUserDao
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @AfterEach
    fun afterEach() {
        Dispatchers.resetMain()
    }

    @Test
    fun `updateUsernameAndEmail should update local username and email and call createSharedPreferencesForUnAuthenticatedUser`() {
        Assertions.assertEquals(null, viewModel.username)
        Assertions.assertEquals(null, viewModel.email)

        viewModel.updateUsernameAndEmail(usernameArgument = username, emailArgument = email)

        Assertions.assertEquals(username, viewModel.username)
        Assertions.assertEquals(email, viewModel.email)

        verify { viewModel.createSharedPreferencesForUnAuthenticatedUser() }
    }

    @Nested
    inner class CreateActiveUser {
        private val activeUserEntity = ActiveUserEntity(
            id = 1,
            accountHasBeenCreated = false,
            username = username,
            email = email
        )

        @Test
        fun `when username is set to null should not call insert function`() {
            viewModel.username = null
            viewModel.email = email

            viewModel.createActiveUser()

            verify(exactly = 0) { activeUserDao.insert(activeUserEntity = activeUserEntity) }
        }

        @Test
        fun `when email is set to null should not call insert function`() {
            viewModel.username = username
            viewModel.email = null

            viewModel.createActiveUser()

            verify(exactly = 0) { activeUserDao.insert(activeUserEntity = activeUserEntity) }
        }

        @Test
        fun `when getActiveUser does not return null should not call insert function`() {
            every { activeUserDao.getActiveUser() } returns activeUserEntity

            viewModel.username = username
            viewModel.email = email

            viewModel.createActiveUser()

            verify(exactly = 0) { activeUserDao.insert(activeUserEntity = activeUserEntity) }
        }

        @Test
        fun `when getActiveUser does return null should call insert function`() {
            every { activeUserDao.getActiveUser() } returns null

            viewModel.username = username
            viewModel.email = email

            viewModel.createActiveUser()

            verify { activeUserDao.insert(activeUserEntity = activeUserEntity) }
        }
    }

    @Nested
    inner class CreateSharedPreferencesForUnAuthenticatedUser {

        @Test
        fun `should not call functions if username is set to null`() {
            viewModel.username = null
            viewModel.email = email

            viewModel.createSharedPreferencesForUnAuthenticatedUser()

            verify(exactly = 0) { createSharedPreferences.createUnverifiedUsernamePreference(value = any()) }
            verify(exactly = 0) { createSharedPreferences.createUnverifiedEmailPreference(value = any()) }
        }

        @Test
        fun `should not call functions if email is set to null`() {
            viewModel.username = username
            viewModel.email = null

            viewModel.createSharedPreferencesForUnAuthenticatedUser()

            verify(exactly = 0) { createSharedPreferences.createUnverifiedUsernamePreference(value = any()) }
            verify(exactly = 0) { createSharedPreferences.createUnverifiedEmailPreference(value = any()) }
        }

        @Test
        fun `should call functions if email and username is not set to null`() {
            viewModel.username = username
            viewModel.email = email

            viewModel.createSharedPreferencesForUnAuthenticatedUser()

            verify { createSharedPreferences.createUnverifiedUsernamePreference(value = username) }
            verify { createSharedPreferences.createUnverifiedEmailPreference(value = email) }
        }
    }

    @Nested
    inner class OnCheckIfAccountHasBeenVerifiedClicked {

        @OptIn(ExperimentalCoroutinesApi::class)
        @Test
        fun `when isEmailVerified returns back false should not attempt to create account`() = runTest {
            every { readFirebaseUserInfo.getAccountInfoListFlow() } returns flowOf(value = emptyList())
            every { readFirebaseUserInfo.isEmailVerifiedFlow() } returns flowOf(value = false)

            viewModel.username = username
            viewModel.email = email

            viewModel.onCheckIfAccountHaBeenVerifiedClicked()

            verify { navigation.alert(alert = viewModel.errorVerifyingAccount()) }
            verify(exactly = 0) { createFirebaseUserInfo.attemptToCreateAccountFirebaseRealTimeDatabaseResponseFlow(userName = any(), email = any()) }
        }

        @OptIn(ExperimentalCoroutinesApi::class)
        @Test
        fun `when isEmailVerified returns back true and username is null should not attempt to create account`() = runTest {
            every { readFirebaseUserInfo.getAccountInfoListFlow() } returns flowOf(value = emptyList())
            every { readFirebaseUserInfo.isEmailVerifiedFlow() } returns flowOf(value = true)

            viewModel.username = null
            viewModel.email = email

            viewModel.onCheckIfAccountHaBeenVerifiedClicked()

            verify(exactly = 0) { createFirebaseUserInfo.attemptToCreateAccountFirebaseRealTimeDatabaseResponseFlow(userName = any(), email = any()) }
        }

        @OptIn(ExperimentalCoroutinesApi::class)
        @Test
        fun `when isEmailVerified returns back true and email is null should not attempt to create account`() = runTest {
            every { readFirebaseUserInfo.getAccountInfoListFlow() } returns flowOf(value = emptyList())
            every { readFirebaseUserInfo.isEmailVerifiedFlow() } returns flowOf(value = true)

            viewModel.username = username
            viewModel.email = null

            viewModel.onCheckIfAccountHaBeenVerifiedClicked()

            verify(exactly = 0) { createFirebaseUserInfo.attemptToCreateAccountFirebaseRealTimeDatabaseResponseFlow(userName = any(), email = any()) }
        }

        @OptIn(ExperimentalCoroutinesApi::class)
        @Test
        fun `when isEmailVerified returns back true, fields are not null, with attemptToCreateAccountFirebase returns back not successful should show error creating account alert`() = runTest {
            every { readFirebaseUserInfo.getAccountInfoListFlow() } returns flowOf(value = emptyList())
            coEvery { readFirebaseUserInfo.isEmailVerifiedFlow() } returns flowOf(value = true)
            coEvery {
                createFirebaseUserInfo.attemptToCreateAccountFirebaseRealTimeDatabaseResponseFlow(userName = username, email = email)
            } returns flowOf(value = false)

            viewModel = AuthenticationViewModel(
                readFirebaseUserInfo = readFirebaseUserInfo,
                navigation = navigation,
                application = application,
                authenticationFirebase = authenticationFirebase,
                createSharedPreferences = createSharedPreferences,
                createFirebaseUserInfo = createFirebaseUserInfo,
                activeUserDao = activeUserDao
            )

            viewModel.username = username
            viewModel.email = email

            viewModel.onCheckIfAccountHaBeenVerifiedClicked()

            verify { navigation.enableProgress(progress = any()) }
            verify { navigation.disableProgress() }
            verify { navigation.alert(alert = viewModel.errorCreatingAccountAlert()) }
        }

        @OptIn(ExperimentalCoroutinesApi::class)
        @Test
        fun `when isEmailVerified returns back true, fields are not null, with attemptToCreateAccountFirebase returns back successful should navigate to home`() = runTest {
            every { readFirebaseUserInfo.getAccountInfoListFlow() } returns flowOf(value = emptyList())
            coEvery { readFirebaseUserInfo.isEmailVerifiedFlow() } returns flowOf(value = true)
            coEvery {
                createFirebaseUserInfo.attemptToCreateAccountFirebaseRealTimeDatabaseResponseFlow(userName = username, email = email)
            } returns flowOf(value = true)

            viewModel = AuthenticationViewModel(
                readFirebaseUserInfo = readFirebaseUserInfo,
                navigation = navigation,
                application = application,
                authenticationFirebase = authenticationFirebase,
                createSharedPreferences = createSharedPreferences,
                createFirebaseUserInfo = createFirebaseUserInfo,
                activeUserDao = activeUserDao
            )

            viewModel.username = username
            viewModel.email = email

            viewModel.onCheckIfAccountHaBeenVerifiedClicked()

            verify { navigation.enableProgress(progress = any()) }
            verify { createSharedPreferences.createAccountHasBeenCreatedPreference(value = true) }
            verify { activeUserDao.update(activeUserEntity = activeUserEntity.copy(accountHasBeenCreated = true)) }
            verify { navigation.disableProgress() }
            verify { navigation.navigateToHome() }
        }
    }

    @Test
    fun `on navigate close should call navigation alert`() {
        viewModel.onNavigateClose()

        verify { navigation.alert(alert = any()) }
    }

    @Test
    fun `on alert button clicked should call navigation finish`() {
        viewModel.onAlertConfirmButtonClicked()

        verify { navigation.finish() }
    }

    @Nested
    inner class OnResume {
        private val username = "username"

        @OptIn(ExperimentalCoroutinesApi::class)
        @Test
        fun `when getAccountInfoListFlow returns back as empty should set usernameIsContainedInFirebase to false`() = runTest {
            viewModel.username = username

            every { readFirebaseUserInfo.getAccountInfoListFlow() } returns flowOf(emptyList())

            viewModel.onResume()

            Assertions.assertFalse(viewModel.shouldShowDialogWithTextFieldMutableStateFlow.value)
            Assertions.assertFalse(viewModel.usernameIsContainedInFirebase)
        }

        @OptIn(ExperimentalCoroutinesApi::class)
        @Test
        fun `when getAccountInfoListFlow returns back as valid response but it is not contained in username should set usernameIsContainedInFirebase to false`() = runTest {
            viewModel.username = username

            every { readFirebaseUserInfo.getAccountInfoListFlow() } returns flowOf(
                listOf(TestAccountInfoRealTimeResponse().create())
            )

            viewModel.onResume()

            Assertions.assertFalse(viewModel.shouldShowDialogWithTextFieldMutableStateFlow.value)
            Assertions.assertFalse(viewModel.usernameIsContainedInFirebase)
        }

        @OptIn(ExperimentalCoroutinesApi::class)
        @Test
        fun `when getAccountInfoListFlow returns back as valid response but username is null should set usernameIsContainedInFirebase to false`() = runTest {
            viewModel.username = null

            every { readFirebaseUserInfo.getAccountInfoListFlow() } returns flowOf(
                listOf(TestAccountInfoRealTimeResponse().create())
            )

            viewModel.onResume()

            Assertions.assertFalse(viewModel.shouldShowDialogWithTextFieldMutableStateFlow.value)
            Assertions.assertFalse(viewModel.usernameIsContainedInFirebase)
        }

        @OptIn(ExperimentalCoroutinesApi::class)
        @Test
        fun `when getAccountInfoListFlow returns back as valid response with included username should set usernameIsContainedInFirebase to true`() = runTest {
            viewModel.username = USER_NAME_ACCOUNT_INFO_REALTIME_RESPONSE

            every { readFirebaseUserInfo.getAccountInfoListFlow() } returns flowOf(
                listOf(TestAccountInfoRealTimeResponse().create())
            )

            viewModel.onResume()

            Assertions.assertTrue(viewModel.shouldShowDialogWithTextFieldMutableStateFlow.value)
            Assertions.assertTrue(viewModel.usernameIsContainedInFirebase)
        }

        @OptIn(ExperimentalCoroutinesApi::class)
        @Test
        fun `when isEmailVerified returns back false should not attempt to create account`() = runTest {
            every { readFirebaseUserInfo.isEmailVerifiedFlow() } returns flowOf(value = false)

            viewModel.username = username
            viewModel.email = email

            viewModel.onResume()

            Assertions.assertFalse(viewModel.shouldShowDialogWithTextFieldMutableStateFlow.value)

            verify(exactly = 0) { navigation.alert(alert = viewModel.errorVerifyingAccount()) }
            verify(exactly = 0) { createFirebaseUserInfo.attemptToCreateAccountFirebaseRealTimeDatabaseResponseFlow(userName = any(), email = any()) }
        }

        @OptIn(ExperimentalCoroutinesApi::class)
        @Test
        fun `when isEmailVerified returns back true and username is null should not attempt to create account`() = runTest {
            every { readFirebaseUserInfo.isEmailVerifiedFlow() } returns flowOf(value = true)

            viewModel.username = null
            viewModel.email = email

            viewModel.onResume()

            Assertions.assertFalse(viewModel.shouldShowDialogWithTextFieldMutableStateFlow.value)

            verify(exactly = 0) { createFirebaseUserInfo.attemptToCreateAccountFirebaseRealTimeDatabaseResponseFlow(userName = any(), email = any()) }
        }

        @OptIn(ExperimentalCoroutinesApi::class)
        @Test
        fun `when isEmailVerified returns back true and email is null should not attempt to create account`() = runTest {
            every { readFirebaseUserInfo.isEmailVerifiedFlow() } returns flowOf(value = true)

            viewModel.username = username
            viewModel.email = null

            viewModel.onResume()

            Assertions.assertFalse(viewModel.shouldShowDialogWithTextFieldMutableStateFlow.value)
            verify(exactly = 0) { createFirebaseUserInfo.attemptToCreateAccountFirebaseRealTimeDatabaseResponseFlow(userName = any(), email = any()) }
        }

        @OptIn(ExperimentalCoroutinesApi::class)
        @Test
        fun `when isEmailVerified returns back true, fields are not null, with attemptToCreateAccountFirebase returns back not successful should show error creating account alert`() = runTest {
            every { readFirebaseUserInfo.getAccountInfoListFlow() } returns flowOf(value = emptyList())
            coEvery { readFirebaseUserInfo.isEmailVerifiedFlow() } returns flowOf(value = true)
            coEvery {
                createFirebaseUserInfo.attemptToCreateAccountFirebaseRealTimeDatabaseResponseFlow(userName = username, email = email)
            } returns flowOf(value = false)

            viewModel = AuthenticationViewModel(
                readFirebaseUserInfo = readFirebaseUserInfo,
                navigation = navigation,
                application = application,
                authenticationFirebase = authenticationFirebase,
                createSharedPreferences = createSharedPreferences,
                createFirebaseUserInfo = createFirebaseUserInfo,
                activeUserDao = activeUserDao
            )

            viewModel.username = username
            viewModel.email = email

            viewModel.onResume()

            Assertions.assertFalse(viewModel.shouldShowDialogWithTextFieldMutableStateFlow.value)

            verify { navigation.enableProgress(progress = any()) }
            verify { navigation.disableProgress() }
            verify { navigation.alert(alert = viewModel.errorCreatingAccountAlert()) }
        }

        @OptIn(ExperimentalCoroutinesApi::class)
        @Test
        fun `when isEmailVerified returns back true, fields are not null, with attemptToCreateAccountFirebase returns back successful should navigate to home`() = runTest {
            every { readFirebaseUserInfo.getAccountInfoListFlow() } returns flowOf(value = emptyList())
            coEvery { readFirebaseUserInfo.isEmailVerifiedFlow() } returns flowOf(value = true)
            coEvery {
                createFirebaseUserInfo.attemptToCreateAccountFirebaseRealTimeDatabaseResponseFlow(userName = username, email = email)
            } returns flowOf(value = true)

            viewModel = AuthenticationViewModel(
                readFirebaseUserInfo = readFirebaseUserInfo,
                navigation = navigation,
                application = application,
                authenticationFirebase = authenticationFirebase,
                createSharedPreferences = createSharedPreferences,
                createFirebaseUserInfo = createFirebaseUserInfo,
                activeUserDao = activeUserDao
            )

            viewModel.username = username
            viewModel.email = email

            viewModel.onResume()

            Assertions.assertFalse(viewModel.shouldShowDialogWithTextFieldMutableStateFlow.value)

            verify { navigation.enableProgress(progress = any()) }
            verify { createSharedPreferences.createAccountHasBeenCreatedPreference(value = true) }
            verify { navigation.disableProgress() }
            verify { navigation.navigateToHome() }
        }
    }

    @Nested
    inner class OnResendEmailClicked {

        private val authenticateViaEmailFirebaseResponse = TestAuthenticateUserViaEmailFirebaseResponse().create()

        @OptIn(ExperimentalCoroutinesApi::class)
        @Test
        fun `when attemptToSendEmailVerificationForCurrentUser returns back not successful should call unsuccessfullySendEmailVerificationAlert`() = runTest {
            coEvery {
                authenticationFirebase.attemptToSendEmailVerificationForCurrentUser()
            } returns flowOf(value = authenticateViaEmailFirebaseResponse.copy(isSuccessful = false))

            viewModel.onResendEmailClicked()

            verify { navigation.alert(alert = viewModel.unsuccessfullySendEmailVerificationAlert()) }
        }

        @OptIn(ExperimentalCoroutinesApi::class)
        @Test
        fun `when attemptToSendEmailVerificationForCurrentUser returns back successful should call successfullySentEmailVerificationAlert`() = runTest {
            coEvery {
                authenticationFirebase.attemptToSendEmailVerificationForCurrentUser()
            } returns flowOf(value = authenticateViaEmailFirebaseResponse.copy(isSuccessful = true))

            viewModel.onResendEmailClicked()

            verify { navigation.alert(alert = viewModel.successfullySentEmailVerificationAlert()) }
        }
    }

    @Test
    fun `errorCreatingAccount content should match`() {
        Assertions.assertEquals(
            application.getString(StringsIds.errorCreatingAccount),
            viewModel.errorCreatingAccountAlert().title
        )
        Assertions.assertEquals(
            application.getString(StringsIds.gotIt),
            viewModel.errorCreatingAccountAlert().dismissButton!!.buttonText
        )
        Assertions.assertEquals(
            application.getString(StringsIds.thereWasAErrorCreatingYourAccountPleaseTryAgain),
            viewModel.errorCreatingAccountAlert().description
        )
    }

    @Test
    fun `errorVerifyingAccount content should match`() {
        Assertions.assertEquals(
            application.getString(StringsIds.accountHasNotBeenVerified),
            viewModel.errorCreatingAccountAlert().title
        )
        Assertions.assertEquals(
            application.getString(StringsIds.gotIt),
            viewModel.errorCreatingAccountAlert().dismissButton!!.buttonText
        )
        Assertions.assertEquals(
            application.getString(StringsIds.currentAccountHasNotBeenVerifiedPleaseOpenEmailToVerifyAccount),
            viewModel.errorCreatingAccountAlert().description
        )
    }

    @Test
    fun `successfullySendEmailVerificationAlert content should match`() {
        Assertions.assertEquals(
            application.getString(StringsIds.successfullySendEmailVerification),
            viewModel.successfullySentEmailVerificationAlert().title
        )
        Assertions.assertEquals(
            application.getString(StringsIds.gotIt),
            viewModel.successfullySentEmailVerificationAlert().dismissButton!!.buttonText
        )
        Assertions.assertEquals(
            application.getString(StringsIds.weWereAbleToSendEmailVerificationPleaseCheckYourEmailToVerifyAccount),
            viewModel.successfullySentEmailVerificationAlert().description
        )
    }

    @Test
    fun `unsuccessfullySendEmailVerificationAlert() content should match`() {
        Assertions.assertEquals(
            application.getString(StringsIds.unableToSendEmailVerification),
            viewModel.unsuccessfullySendEmailVerificationAlert().title
        )
        Assertions.assertEquals(
            application.getString(StringsIds.gotIt),
            viewModel.unsuccessfullySendEmailVerificationAlert().dismissButton!!.buttonText
        )
        Assertions.assertEquals(
            application.getString(StringsIds.weWereUnableToSendEmailVerificationPleaseClickSendEmailVerificationToTryAgain),
            viewModel.unsuccessfullySendEmailVerificationAlert().description
        )
    }

    @Test
    fun `newUsernameIsEmptyAlert() content should match`() {
        Assertions.assertEquals(
            application.getString(StringsIds.unableToSetNewUsername),
            viewModel.newUsernameIsEmptyAlert().title
        )
        Assertions.assertEquals(
            application.getString(StringsIds.tryAgain),
            viewModel.newUsernameIsEmptyAlert().confirmButton!!.buttonText
        )
        Assertions.assertEquals(
            application.getString(StringsIds.weWereUnableToSetNewUsernameSinceTheUsernameIsEmptyPleaseClickTryAgainToSetNewUsername),
            viewModel.newUsernameIsEmptyAlert().description
        )
    }

    @Test
    fun `newUsernameIsDuplicateAlert() content should match`() {
        Assertions.assertEquals(
            application.getString(StringsIds.unableToSetNewUsername),
            viewModel.newUsernameIsDuplicateAlert().title
        )
        Assertions.assertEquals(
            application.getString(StringsIds.tryAgain),
            viewModel.newUsernameIsDuplicateAlert().confirmButton!!.buttonText
        )
        Assertions.assertEquals(
            application.getString(StringsIds.weWereUnableToSetNewUsernameSinceTheUsernameIsTheSameAsTheCurrentUsernamePleaseClickTryAgainToSetNewUsername),
            viewModel.newUsernameIsDuplicateAlert().description
        )
    }

    @Nested
    inner class OnConfirmNewUsernameClicked {
        private val defaultUsername = "defaultUsername"

        @Test
        fun `when newUsername is empty should call alert`() {
            viewModel.onConfirmNewUsernameClicked(newUsername = "")

            Assertions.assertFalse(viewModel.shouldShowDialogWithTextFieldMutableStateFlow.value)
            verify { navigation.alert(alert = any()) }
        }

        @Test
        fun `when newUsername is equal to the existing username should call alert`() {
            viewModel.username = defaultUsername

            viewModel.onConfirmNewUsernameClicked(newUsername = defaultUsername)

            Assertions.assertFalse(viewModel.shouldShowDialogWithTextFieldMutableStateFlow.value)
            verify { navigation.alert(alert = any()) }
        }
    }

    @Test
    fun `on open email clicked`() {
        viewModel.onOpenEmailClicked()

        verify { navigation.openEmail() }
    }
}
