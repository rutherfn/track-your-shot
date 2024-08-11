package com.nicholas.rutherford.track.your.shot.feature.create.account.authentication

import android.app.Application
import com.nicholas.rutherford.track.your.shot.data.room.repository.ActiveUserRepository
import com.nicholas.rutherford.track.your.shot.data.room.response.ActiveUser
import com.nicholas.rutherford.track.your.shot.data.test.room.TestActiveUser
import com.nicholas.rutherford.track.your.shot.feature.splash.StringsIds
import com.nicholas.rutherford.track.your.shot.firebase.TestAuthenticateUserViaEmailFirebaseResponse
import com.nicholas.rutherford.track.your.shot.firebase.core.create.CreateFirebaseUserInfo
import com.nicholas.rutherford.track.your.shot.firebase.core.read.ReadFirebaseUserInfo
import com.nicholas.rutherford.track.your.shot.firebase.util.authentication.AuthenticationFirebase
import io.mockk.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
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

    @OptIn(ExperimentalCoroutinesApi::class)
    private val testDispatcher = UnconfinedTestDispatcher()

    private val scope = CoroutineScope(SupervisorJob() + testDispatcher)

    private val activeUser = TestActiveUser().create()

    private val username = activeUser.username
    private val email = activeUser.email
    private val firebaseAccountInfoKey = activeUser.firebaseAccountInfoKey

    private val dispatcher = StandardTestDispatcher()

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
            activeUserRepository = activeUserRepository,
            scope = scope
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @AfterEach
    fun afterEach() {
        Dispatchers.resetMain()
    }

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

        @Test
        fun `when fetchActiveUser does not return null should not call create active user function`() = runTest {
            coEvery { activeUserRepository.fetchActiveUser() } returns newActiveUser

            viewModel.attemptToCreateActiveUser(email = email, username = username)

            coVerify(exactly = 0) { activeUserRepository.createActiveUser(activeUser = newActiveUser.copy(firebaseAccountInfoKey = null)) }
        }

        @Test
        fun `when fetchActiveUser does return null and all conditions are met should call create active user function`() = runTest {
            coEvery { activeUserRepository.fetchActiveUser() } returns null

            viewModel.attemptToCreateActiveUser(email = email, username = username)

            coVerify { activeUserRepository.createActiveUser(activeUser = activeUser.copy(firebaseAccountInfoKey = null)) }
        }
    }

    @Nested
    inner class OnCheckIfAccountHasBeenVerifiedClicked {

        @Test
        fun `when isEmailVerified returns back false should not attempt to create account`() = runTest {
            every { readFirebaseUserInfo.isEmailVerifiedFlow() } returns flowOf(value = false)

            viewModel.username = username
            viewModel.email = email

            viewModel.onCheckIfAccountHaBeenVerifiedClicked()

            verify { navigation.alert(alert = viewModel.errorVerifyingAccount()) }
            verify(exactly = 0) { createFirebaseUserInfo.attemptToCreateAccountFirebaseRealTimeDatabaseResponseFlow(userName = any(), email = any()) }
        }

        @Test
        fun `when isEmailVerified returns back true and username is null should not attempt to create account`() = runTest {
            every { readFirebaseUserInfo.isEmailVerifiedFlow() } returns flowOf(value = true)

            viewModel.username = null
            viewModel.email = email

            viewModel.onCheckIfAccountHaBeenVerifiedClicked()

            verify(exactly = 0) { createFirebaseUserInfo.attemptToCreateAccountFirebaseRealTimeDatabaseResponseFlow(userName = any(), email = any()) }
        }

        @Test
        fun `when isEmailVerified returns back true and email is null should not attempt to create account`() = runTest {
            every { readFirebaseUserInfo.isEmailVerifiedFlow() } returns flowOf(value = true)

            viewModel.username = username
            viewModel.email = null

            viewModel.onCheckIfAccountHaBeenVerifiedClicked()

            verify(exactly = 0) { createFirebaseUserInfo.attemptToCreateAccountFirebaseRealTimeDatabaseResponseFlow(userName = any(), email = any()) }
        }

        @Test
        fun `when isEmailVerified returns back true, fields are not null, with attemptToCreateAccountFirebase returns back not successful should show error creating account alert`() = runTest {
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
                activeUserRepository = activeUserRepository,
                scope = scope
            )

            viewModel.username = username
            viewModel.email = email

            viewModel.onCheckIfAccountHaBeenVerifiedClicked()

            verify { navigation.enableProgress(progress = any()) }
            verify { navigation.disableProgress() }
            verify { navigation.alert(alert = viewModel.errorCreatingAccountAlert()) }
        }

        @Test
        fun `when isEmailVerified returns back true, fields are not null, with attemptToCreateAccountFirebase returns back successful should navigate to players list`() = runTest {
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
                activeUserRepository = activeUserRepository,
                scope = scope
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

        @Test
        fun `when isEmailVerified returns back false should not attempt to create account`() = runTest {
            every { readFirebaseUserInfo.isEmailVerifiedFlow() } returns flowOf(value = false)

            viewModel.username = username
            viewModel.email = email

            viewModel.onResume()

            verify(exactly = 0) { navigation.alert(alert = viewModel.errorVerifyingAccount()) }
            verify(exactly = 0) { createFirebaseUserInfo.attemptToCreateAccountFirebaseRealTimeDatabaseResponseFlow(userName = any(), email = any()) }
        }

        @Test
        fun `when isEmailVerified returns back true and username is null should not attempt to create account`() = runTest {
            every { readFirebaseUserInfo.isEmailVerifiedFlow() } returns flowOf(value = true)

            viewModel.username = null
            viewModel.email = email

            viewModel.onResume()

            verify(exactly = 0) { createFirebaseUserInfo.attemptToCreateAccountFirebaseRealTimeDatabaseResponseFlow(userName = any(), email = any()) }
        }

        @Test
        fun `when isEmailVerified returns back true and email is null should not attempt to create account`() = runTest {
            every { readFirebaseUserInfo.isEmailVerifiedFlow() } returns flowOf(value = true)

            viewModel.username = username
            viewModel.email = null

            viewModel.onResume()

            verify(exactly = 0) { createFirebaseUserInfo.attemptToCreateAccountFirebaseRealTimeDatabaseResponseFlow(userName = any(), email = any()) }
        }

        @Test
        fun `when isEmailVerified returns back true, fields are not null, with attemptToCreateAccountFirebase returns back not successful should show error creating account alert`() = runTest {
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
                activeUserRepository = activeUserRepository,
                scope = scope
            )

            viewModel.username = username
            viewModel.email = email

            viewModel.onResume()

            verify { navigation.enableProgress(progress = any()) }
            verify { navigation.disableProgress() }
            verify { navigation.alert(alert = viewModel.errorCreatingAccountAlert()) }
        }

        @Test
        fun `when isEmailVerified returns back true, fields are not null, with attemptToCreateAccountFirebase returns back successful should navigate to players list`() = runTest {
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
                activeUserRepository = activeUserRepository,
                scope = scope
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

        @Test
        fun `when attemptToSendEmailVerificationForCurrentUser returns back not successful should call unsuccessfullySendEmailVerificationAlert`() = runTest {
            coEvery {
                authenticationFirebase.attemptToSendEmailVerificationForCurrentUser()
            } returns flowOf(value = authenticateViaEmailFirebaseResponse.copy(isSuccessful = false))

            viewModel.onResendEmailClicked()

            verify { navigation.alert(alert = viewModel.unsuccessfullySendEmailVerificationAlert()) }
        }

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
    fun `on delete pending account clicked`() {
        viewModel.onDeletePendingAccountClicked()

        verify { navigation.alert(alert = any()) }
    }

    @Nested
    inner class `on yes delete pending account clicked` {

        @Test
        fun `when attemptToDeleteCurrentUserFlow returns back a flow of true should navigate to login`() = runTest {
            every { authenticationFirebase.attemptToDeleteCurrentUserFlow() } returns flowOf(value = true)

            viewModel.onYesDeletePendingAccountClicked()

            verify { navigation.enableProgress(progress = any()) }
            coVerify { activeUserRepository.deleteActiveUser() }
            verify { navigation.disableProgress() }
            verify { navigation.navigateToLogin() }
        }

        @Test
        fun `when attemptToDeleteCurrentUserFlow returns back a flow of false should show alert`() = runTest {
            every { authenticationFirebase.attemptToDeleteCurrentUserFlow() } returns flowOf(value = false)

            viewModel.onYesDeletePendingAccountClicked()

            verify { navigation.enableProgress(progress = any()) }
            verify { navigation.disableProgress() }
            verify { navigation.alert(alert = any()) }
        }
    }

    @Test
    fun `are you sure you want to delete account alert`() {
        every { application.getString(StringsIds.deletingPendingAccount) } returns "Deleting Pending Account"
        every { application.getString(StringsIds.yes) } returns "Yes"
        every { application.getString(StringsIds.no) } returns "No"
        every { application.getString(StringsIds.areYouSureYouWantToDeletePendingAccountDescription) } returns "Deleting your pending account will require you to either register a new account or log in to an existing one. Are you sure you want to proceed with deleting your pending account?"

        val alert = viewModel.areYouSureYouWantToDeleteAccountAlert()

        Assertions.assertEquals(alert.title, "Deleting Pending Account")
        Assertions.assertEquals(alert.confirmButton!!.buttonText, "Yes")
        Assertions.assertEquals(alert.dismissButton!!.buttonText, "No")
        Assertions.assertEquals(alert.description, "Deleting your pending account will require you to either register a new account or log in to an existing one. Are you sure you want to proceed with deleting your pending account?")
    }

    @Test
    fun `error creating account alert`() {
        every { application.getString(StringsIds.errorCreatingAccount) } returns "Error Creating Account"
        every { application.getString(StringsIds.gotIt) } returns "Got It"
        every { application.getString(StringsIds.thereWasAErrorDeletingPendingAccountPleaseTryAgain) } returns "There was a error deleting your account. Please try again."

        val alert = viewModel.errorCreatingAccountAlert()

        Assertions.assertEquals(alert.title, "Error Creating Account")
        Assertions.assertEquals(alert.dismissButton!!.buttonText, "Got It")
        Assertions.assertEquals(alert.description, "There was a error deleting your account. Please try again.")
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
