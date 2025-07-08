package com.nicholas.rutherford.track.your.shot.feature.forgot.password

import android.app.Application
import com.nicholas.rutherford.track.your.shot.base.resources.StringsIds
import com.nicholas.rutherford.track.your.shot.firebase.util.authentication.AuthenticationFirebase
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class ForgotPasswordViewModelTest {

    lateinit var viewModel: ForgotPasswordViewModel

    private val application = mockk<Application>(relaxed = true)
    private val authenticationFirebase = mockk<AuthenticationFirebase>(relaxed = true)

    private var navigation = mockk<ForgotPasswordNavigation>(relaxed = true)

    private var state = ForgotPasswordState(email = null)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val dispatcher = UnconfinedTestDispatcher()

    private val scope = CoroutineScope(SupervisorJob() + dispatcher)

    @BeforeEach
    fun beforeEach() {
        viewModel = ForgotPasswordViewModel(
            application = application,
            authenticationFirebase = authenticationFirebase,
            navigation = navigation,
            scope = scope
        )
    }

    @Test
    fun initializeForgotPasswordState() {
        Assertions.assertEquals(
            viewModel.forgotPasswordStateFlow.value,
            state.copy(email = "")
        )
    }

    @Test
    fun `on back button clicked should pop`() {
        viewModel.onBackButtonClicked()

        verify { navigation.pop() }
    }

    @Nested
    inner class OnSendPasswordResetButtonClicked {

        private val emailTest = "newuser@yahoo.com"

        @Test
        fun `when newEmail is set to null should show email empty alert`() = runTest {
            viewModel.onSendPasswordResetButtonClicked(newEmail = null)

            verify { navigation.alert(alert = viewModel.emailEmptyAlert()) }
        }

        @Test
        fun `when newEmail is set to empty should show email empty alert`() = runTest {
            viewModel.onSendPasswordResetButtonClicked(newEmail = "")

            verify { navigation.alert(alert = viewModel.emailEmptyAlert()) }
        }

        @Test
        fun `when newEmail is not empty or null and attemptToSendPasswordResetFlow returns back not successful should call unable to send reset password alert`() = runTest {
            coEvery { authenticationFirebase.attemptToSendPasswordResetFlow(email = emailTest) } returns flowOf(value = false)

            viewModel.onSendPasswordResetButtonClicked(newEmail = emailTest)

            verify { navigation.enableProgress(progress = any()) }
            verify { navigation.disableProgress() }
            verify { navigation.alert(alert = viewModel.unableToSendResetPasswordAlert()) }
        }

        @Test
        fun `when newEmail is not empty or null and attemptToSendPasswordResetFlow returns back successful should call success sending reset password alert`() = runTest {
            coEvery { authenticationFirebase.attemptToSendPasswordResetFlow(email = emailTest) } returns flowOf(value = true)

            viewModel.onSendPasswordResetButtonClicked(newEmail = emailTest)

            verify { navigation.enableProgress(progress = any()) }
            verify { navigation.disableProgress() }
            verify { navigation.alert(alert = viewModel.successSendingResetPasswordAlert()) }

            Assertions.assertEquals(
                viewModel.forgotPasswordStateFlow.value,
                state.copy(email = "")
            )
        }
    }

    @Test
    fun `emailEmptyAlert should have valid values`() {
        Assertions.assertEquals(
            viewModel.emailEmptyAlert().title,
            application.getString(StringsIds.emptyField)
        )
        Assertions.assertEquals(
            viewModel.emailEmptyAlert().description,
            application.getString(StringsIds.emailIsRequiredPleaseEnterAEmailToResetPasswordForExistingAccount)
        )
        Assertions.assertEquals(
            viewModel.emailEmptyAlert().dismissButton!!.buttonText,
            application.getString(StringsIds.gotIt)
        )
    }

    @Test
    fun `successSendingResetPasswordAlert should have valid values`() {
        Assertions.assertEquals(
            viewModel.successSendingResetPasswordAlert().title,
            application.getString(StringsIds.resetPasswordEmailSent)
        )
        Assertions.assertEquals(
            viewModel.successSendingResetPasswordAlert().description,
            application.getString(StringsIds.emailHasBeenSentToRestPasswordPleaseFollowDirectionsToResetPassword)
        )
        Assertions.assertEquals(
            viewModel.successSendingResetPasswordAlert().dismissButton!!.buttonText,
            application.getString(StringsIds.gotIt)
        )
    }

    @Test
    fun `unableToSendResetPasswordAlert should have valid values`() {
        Assertions.assertEquals(
            viewModel.unableToSendResetPasswordAlert().title,
            application.getString(StringsIds.unableToResetPassword)
        )
        Assertions.assertEquals(
            viewModel.unableToSendResetPasswordAlert().description,
            application.getString(StringsIds.havingTroubleResettingPasswordForThisAccountPleaseTryAgainAndOrEnsureCredentialsExistAndAreValid)
        )
        Assertions.assertEquals(
            viewModel.unableToSendResetPasswordAlert().dismissButton!!.buttonText,
            application.getString(StringsIds.gotIt)
        )
    }

    @Test
    fun `on email value change should update email state value`() {
        val newEmail = "testemail@yahoo.com"

        Assertions.assertEquals(
            viewModel.forgotPasswordStateFlow.value,
            state.copy(email = "")
        )

        viewModel.onEmailValueChanged(newEmail = newEmail)

        Assertions.assertEquals(
            viewModel.forgotPasswordStateFlow.value,
            state.copy(email = newEmail)
        )
    }
}
