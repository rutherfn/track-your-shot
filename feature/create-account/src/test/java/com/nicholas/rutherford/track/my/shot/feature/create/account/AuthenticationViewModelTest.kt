package com.nicholas.rutherford.track.my.shot.feature.create.account

import android.app.Application
import com.nicholas.rutherford.track.my.shot.data.test.account.info.TestAuthenticateUserViaEmailFirebaseResponse
import com.nicholas.rutherford.track.my.shot.feature.create.account.authentication.AuthenticationNavigation
import com.nicholas.rutherford.track.my.shot.feature.create.account.authentication.AuthenticationViewModel
import com.nicholas.rutherford.track.my.shot.feature.splash.StringsIds
import com.nicholas.rutherford.track.my.shot.firebase.create.CreateFirebaseUserInfo
import com.nicholas.rutherford.track.my.shot.firebase.read.ReadFirebaseUserInfo
import com.nicholas.rutherford.track.my.shot.firebase.util.authentication.AuthenticationFirebase
import com.nicholas.rutherford.track.my.shot.shared.preference.create.CreateSharedPreferences
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
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

class AuthenticationViewModelTest {

    lateinit var viewModel: AuthenticationViewModel

    private var readFirebaseUserInfo = mockk<ReadFirebaseUserInfo>(relaxed = true)
    private var navigation = mockk<AuthenticationNavigation>(relaxed = true)

    private val application = mockk<Application>(relaxed = true)

    private val authenticationFirebase = mockk<AuthenticationFirebase>(relaxed = true)

    private val createSharedPreferences = mockk<CreateSharedPreferences>(relaxed = true)
    private val createFirebaseUserInfo = mockk<CreateFirebaseUserInfo>(relaxed = true)

    private val username = "testUsername11"
    private val email = "testemail@yahoo.com"

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
            createFirebaseUserInfo = createFirebaseUserInfo
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

        @OptIn(ExperimentalCoroutinesApi::class)
        @Test
        fun `when isEmailVerified returns back false should not attempt to create account`() = runTest {
            every { readFirebaseUserInfo.isEmailVerified() } returns flowOf(value = false)

            viewModel.username = username
            viewModel.email = email

            viewModel.onResume()

            verify(exactly = 0) { createFirebaseUserInfo.attemptToCreateAccountFirebaseRealTimeDatabaseResponseFlow(userName = any(), email = any()) }
        }

        @OptIn(ExperimentalCoroutinesApi::class)
        @Test
        fun `when isEmailVerified returns back true and username is null should not attempt to create account`() = runTest {
            every { readFirebaseUserInfo.isEmailVerified() } returns flowOf(value = true)

            viewModel.username = null
            viewModel.email = email

            viewModel.onResume()

            verify(exactly = 0) { createFirebaseUserInfo.attemptToCreateAccountFirebaseRealTimeDatabaseResponseFlow(userName = any(), email = any()) }
        }

        @OptIn(ExperimentalCoroutinesApi::class)
        @Test
        fun `when isEmailVerified returns back true and email is null should not attempt to create account`() = runTest {
            every { readFirebaseUserInfo.isEmailVerified() } returns flowOf(value = true)

            viewModel.username = username
            viewModel.email = null

            viewModel.onResume()

            verify(exactly = 0) { createFirebaseUserInfo.attemptToCreateAccountFirebaseRealTimeDatabaseResponseFlow(userName = any(), email = any()) }
        }

        @OptIn(ExperimentalCoroutinesApi::class)
        @Test
        fun `when isEmailVerified returns back true, fields are not null, with attemptToCreateAccountFirebase returns back not successful should show error creating account alert`() = runTest {
            coEvery { readFirebaseUserInfo.isEmailVerified() } returns flowOf(value = true)
            coEvery {
                createFirebaseUserInfo.attemptToCreateAccountFirebaseRealTimeDatabaseResponseFlow(userName = username, email = email)
            } returns flowOf(value = false)

            viewModel = AuthenticationViewModel(
                readFirebaseUserInfo = readFirebaseUserInfo,
                navigation = navigation,
                application = application,
                authenticationFirebase = authenticationFirebase,
                createSharedPreferences = createSharedPreferences,
                createFirebaseUserInfo = createFirebaseUserInfo
            )

            viewModel.username = username
            viewModel.email = email

            viewModel.onResume()

            verify { navigation.enableProgress(progress = any()) }
            verify { navigation.disableProgress() }
            verify { navigation.alert(alert = viewModel.errorCreatingAccountAlert()) }
        }

        @OptIn(ExperimentalCoroutinesApi::class)
        @Test
        fun `when isEmailVerified returns back true, fields are not null, with attemptToCreateAccountFirebase returns back successful should navigate to home`() = runTest {
            coEvery { readFirebaseUserInfo.isEmailVerified() } returns flowOf(value = true)
            coEvery {
                createFirebaseUserInfo.attemptToCreateAccountFirebaseRealTimeDatabaseResponseFlow(userName = username, email = email)
            } returns flowOf(value = true)

            viewModel = AuthenticationViewModel(
                readFirebaseUserInfo = readFirebaseUserInfo,
                navigation = navigation,
                application = application,
                authenticationFirebase = authenticationFirebase,
                createSharedPreferences = createSharedPreferences,
                createFirebaseUserInfo = createFirebaseUserInfo
            )

            viewModel.username = username
            viewModel.email = email

            viewModel.onResume()

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
    fun `on open email clicked`() {
        viewModel.onOpenEmailClicked()

        verify { navigation.openEmail() }
    }
}
