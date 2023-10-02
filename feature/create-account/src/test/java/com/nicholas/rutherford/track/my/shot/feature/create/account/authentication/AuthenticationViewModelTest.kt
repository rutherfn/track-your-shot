package com.nicholas.rutherford.track.my.shot.feature.create.account.authentication

import android.app.Application
import com.nicholas.rutherford.track.my.shot.data.room.repository.ActiveUserRepository
import com.nicholas.rutherford.track.my.shot.data.room.response.ActiveUser
import com.nicholas.rutherford.track.my.shot.data.test.room.TestActiveUser
import com.nicholas.rutherford.track.my.shot.feature.splash.StringsIds
import com.nicholas.rutherford.track.my.shot.firebase.TestAuthenticateUserViaEmailFirebaseResponse
import com.nicholas.rutherford.track.my.shot.firebase.core.create.CreateFirebaseUserInfo
import com.nicholas.rutherford.track.my.shot.firebase.core.read.ReadFirebaseUserInfo
import com.nicholas.rutherford.track.my.shot.firebase.util.authentication.AuthenticationFirebase
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

    private val createFirebaseUserInfo = mockk<CreateFirebaseUserInfo>(relaxed = true)

    private val activeUserRepository = mockk<ActiveUserRepository>(relaxed = true)

    private val activeUser = TestActiveUser().create()

    private val username = activeUser.username
    private val email = activeUser.email
    private val firebaseAccountInfoKey = activeUser.firebaseAccountInfoKey

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
            createFirebaseUserInfo = createFirebaseUserInfo,
            activeUserRepository = activeUserRepository
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @AfterEach
    fun afterEach() {
        Dispatchers.resetMain()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `updateUsernameAndEmail should update local username and email and call createSharedPreferencesForUnAuthenticatedUser`() = runTest {
        Assertions.assertEquals(null, viewModel.username)
        Assertions.assertEquals(null, viewModel.email)

        viewModel.updateUsernameAndEmail(usernameArgument = username, emailArgument = email)

        Assertions.assertEquals(username, viewModel.username)
        Assertions.assertEquals(email, viewModel.email)
    }

    @Nested
    inner class AttemptToCreateActiveUser {
        private val newActiveUser = ActiveUser(
            id = 1,
            accountHasBeenCreated = false,
            username = username,
            email = email,
            firebaseAccountInfoKey = firebaseAccountInfoKey
        )

        @OptIn(ExperimentalCoroutinesApi::class)
        @Test
        fun `when fetchActiveUser does not return null should not call create active user function`() = runTest {
            coEvery { activeUserRepository.fetchActiveUser() } returns newActiveUser

            viewModel.attemptToCreateActiveUser(email = email, username = username)

            coVerify(exactly = 0) { activeUserRepository.createActiveUser(activeUser = newActiveUser.copy(firebaseAccountInfoKey = null)) }
        }

        @OptIn(ExperimentalCoroutinesApi::class)
        @Test
        fun `when fetchActiveUser does return null and all conditions are met should call create active user function`() = runTest {
            coEvery { activeUserRepository.fetchActiveUser() } returns null

            viewModel.attemptToCreateActiveUser(email = email, username = username)

            coVerify { activeUserRepository.createActiveUser(activeUser = activeUser.copy(firebaseAccountInfoKey = null)) }
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
            } returns flowOf(value = Pair(first = false, second = null))

            viewModel = AuthenticationViewModel(
                readFirebaseUserInfo = readFirebaseUserInfo,
                navigation = navigation,
                application = application,
                authenticationFirebase = authenticationFirebase,
                createFirebaseUserInfo = createFirebaseUserInfo,
                activeUserRepository = activeUserRepository
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
        fun `when isEmailVerified returns back true, fields are not null, with attemptToCreateAccountFirebase returns back successful should navigate to players list`() = runTest {
            every { readFirebaseUserInfo.getAccountInfoListFlow() } returns flowOf(value = emptyList())
            coEvery { readFirebaseUserInfo.isEmailVerifiedFlow() } returns flowOf(value = true)
            coEvery {
                createFirebaseUserInfo.attemptToCreateAccountFirebaseRealTimeDatabaseResponseFlow(userName = username, email = email)
            } returns flowOf(value = Pair(first = true, second = activeUser.firebaseAccountInfoKey))

            viewModel = AuthenticationViewModel(
                readFirebaseUserInfo = readFirebaseUserInfo,
                navigation = navigation,
                application = application,
                authenticationFirebase = authenticationFirebase,
                createFirebaseUserInfo = createFirebaseUserInfo,
                activeUserRepository = activeUserRepository
            )

            viewModel.username = username
            viewModel.email = email

            viewModel.onCheckIfAccountHaBeenVerifiedClicked()

            verify { navigation.enableProgress(progress = any()) }
            coVerify { activeUserRepository.updateActiveUser(activeUser = activeUser.copy(accountHasBeenCreated = true)) }
            verify { navigation.disableProgress() }
            verify { navigation.navigateToPlayersList() }
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
        fun `when isEmailVerified returns back false should not attempt to create account`() = runTest {
            every { readFirebaseUserInfo.isEmailVerifiedFlow() } returns flowOf(value = false)

            viewModel.username = username
            viewModel.email = email

            viewModel.onResume()

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

            verify(exactly = 0) { createFirebaseUserInfo.attemptToCreateAccountFirebaseRealTimeDatabaseResponseFlow(userName = any(), email = any()) }
        }

        @OptIn(ExperimentalCoroutinesApi::class)
        @Test
        fun `when isEmailVerified returns back true and email is null should not attempt to create account`() = runTest {
            every { readFirebaseUserInfo.isEmailVerifiedFlow() } returns flowOf(value = true)

            viewModel.username = username
            viewModel.email = null

            viewModel.onResume()

            verify(exactly = 0) { createFirebaseUserInfo.attemptToCreateAccountFirebaseRealTimeDatabaseResponseFlow(userName = any(), email = any()) }
        }

        @OptIn(ExperimentalCoroutinesApi::class)
        @Test
        fun `when isEmailVerified returns back true, fields are not null, with attemptToCreateAccountFirebase returns back not successful should show error creating account alert`() = runTest {
            every { readFirebaseUserInfo.getAccountInfoListFlow() } returns flowOf(value = emptyList())
            coEvery { readFirebaseUserInfo.isEmailVerifiedFlow() } returns flowOf(value = true)
            coEvery {
                createFirebaseUserInfo.attemptToCreateAccountFirebaseRealTimeDatabaseResponseFlow(userName = username, email = email)
            } returns flowOf(value = Pair(first = false, second = null))

            viewModel = AuthenticationViewModel(
                readFirebaseUserInfo = readFirebaseUserInfo,
                navigation = navigation,
                application = application,
                authenticationFirebase = authenticationFirebase,
                createFirebaseUserInfo = createFirebaseUserInfo,
                activeUserRepository = activeUserRepository
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
        fun `when isEmailVerified returns back true, fields are not null, with attemptToCreateAccountFirebase returns back successful should navigate to players list`() = runTest {
            every { readFirebaseUserInfo.getAccountInfoListFlow() } returns flowOf(value = emptyList())
            coEvery { readFirebaseUserInfo.isEmailVerifiedFlow() } returns flowOf(value = true)
            coEvery {
                createFirebaseUserInfo.attemptToCreateAccountFirebaseRealTimeDatabaseResponseFlow(userName = username, email = email)
            } returns flowOf(value = Pair(first = true, second = activeUser.firebaseAccountInfoKey))

            viewModel = AuthenticationViewModel(
                readFirebaseUserInfo = readFirebaseUserInfo,
                navigation = navigation,
                application = application,
                authenticationFirebase = authenticationFirebase,
                createFirebaseUserInfo = createFirebaseUserInfo,
                activeUserRepository = activeUserRepository
            )

            viewModel.username = username
            viewModel.email = email

            viewModel.onResume()

            verify { navigation.enableProgress(progress = any()) }
            verify { navigation.disableProgress() }
            verify { navigation.navigateToPlayersList() }
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
    fun `on open email clicked`() {
        viewModel.onOpenEmailClicked()

        verify { navigation.openEmail() }
    }
}
