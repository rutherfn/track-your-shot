package com.nicholas.rutherford.track.my.shot.feature.login

import android.app.Application
import com.nicholas.rutherford.track.my.shot.build.type.BuildTypeImpl
import com.nicholas.rutherford.track.my.shot.data.room.repository.ActiveUserRepository
import com.nicholas.rutherford.track.my.shot.data.room.repository.UserRepository
import com.nicholas.rutherford.track.my.shot.feature.splash.DrawablesIds
import com.nicholas.rutherford.track.my.shot.feature.splash.StringsIds
import com.nicholas.rutherford.track.my.shot.firebase.util.existinguser.ExistingUserFirebase
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class LoginViewModelTest {

    private lateinit var viewModel: LoginViewModel

    private var existingUserFirebase = mockk<ExistingUserFirebase>(relaxed = true)

    private var navigation = mockk<LoginNavigation>(relaxed = true)
    private var application = mockk<Application>(relaxed = true)

    private var activeUserRepository = mockk<ActiveUserRepository>(relaxed = true)
    private var userRepository = mockk<UserRepository>(relaxed = true)

    private val debugVersionName = "debug"
    private val releaseVersionName = "release"
    private val stageVersionName = "stage"

    private val buildTypeDebug = BuildTypeImpl(buildTypeValue = debugVersionName)
    private val buildTypeRelease = BuildTypeImpl(buildTypeValue = releaseVersionName)
    private val buildTypeStage = BuildTypeImpl(buildTypeValue = stageVersionName)

    private val state = LoginState(launcherDrawableId = null, email = null, password = null)

    @BeforeEach
    fun beforeEach() {
        viewModel = LoginViewModel(
            existingUserFirebase = existingUserFirebase,
            navigation = navigation,
            buildType = buildTypeDebug,
            application = application,
            activeUserRepository = activeUserRepository,
            userRepository = userRepository
        )
    }

    @Test fun initializeLoginState() {
        Assertions.assertEquals(
            viewModel.loginStateFlow.value,
            state.copy(launcherDrawableId = DrawablesIds.launcherRoundTest)
        )
    }

    @Nested inner class UpdateLauncherDrawableIdState {

        @Test fun `when build type is debug should set launcherDrawableId state property to launcherRoundTest`() {
            viewModel = LoginViewModel(
                existingUserFirebase = existingUserFirebase,
                navigation = navigation,
                buildType = buildTypeDebug,
                application = application,
                activeUserRepository = activeUserRepository,
                userRepository = userRepository
            )

            Assertions.assertEquals(
                viewModel.loginStateFlow.value,
                state.copy(launcherDrawableId = DrawablesIds.launcherRoundTest)
            )
        }

        @Test fun `when build type is stage should set launcherDrawableId state property to launcherRoundStage`() {
            viewModel = LoginViewModel(
                existingUserFirebase = existingUserFirebase,
                navigation = navigation,
                buildType = buildTypeStage,
                application = application,
                activeUserRepository = activeUserRepository,
                userRepository = userRepository
            )

            Assertions.assertEquals(
                viewModel.loginStateFlow.value,
                state.copy(launcherDrawableId = DrawablesIds.launcherRoundStage)
            )
        }

        @Test fun `when build type is release should set launcherDrawableId property to launcherRound`() {
            viewModel = LoginViewModel(
                existingUserFirebase = existingUserFirebase,
                navigation = navigation,
                buildType = buildTypeRelease,
                application = application,
                activeUserRepository = activeUserRepository,
                userRepository = userRepository
            )

            Assertions.assertEquals(
                viewModel.loginStateFlow.value,
                state.copy(launcherDrawableId = DrawablesIds.launcherRound)
            )
        }
    }

    @Nested
    inner class OnLoginButtonClicked {

        private val emailTest = "newuser@yahoo.com"
        private val passwordTest = "password1"

        @OptIn(ExperimentalCoroutinesApi::class)
        @Test
        fun `when email is set to null should call email empty alert`() = runTest {
            viewModel.loginMutableStateFlow.value = LoginState(email = null, password = passwordTest)

            viewModel.onLoginButtonClicked()

            verify { navigation.alert(alert = viewModel.emailEmptyAlert()) }
        }

        @OptIn(ExperimentalCoroutinesApi::class)
        @Test
        fun `when password is set to null should call password empty alert`() = runTest {
            viewModel.loginMutableStateFlow.value = LoginState(email = emailTest, password = null)

            viewModel.onLoginButtonClicked()

            verify { navigation.alert(alert = viewModel.passwordEmptyAlert()) }
        }

        @OptIn(ExperimentalCoroutinesApi::class)
        @Test
        fun `when email is set to empty should call email empty alert`() = runTest {
            viewModel.loginMutableStateFlow.value = LoginState(email = "", password = passwordTest)

            viewModel.onLoginButtonClicked()

            verify { navigation.alert(alert = viewModel.emailEmptyAlert()) }
        }

        @OptIn(ExperimentalCoroutinesApi::class)
        @Test
        fun `when password is set to empty should call password empty alert`() = runTest {
            viewModel.loginMutableStateFlow.value = LoginState(email = emailTest, password = "")

            viewModel.onLoginButtonClicked()

            verify { navigation.alert(alert = viewModel.passwordEmptyAlert()) }
        }

        @OptIn(ExperimentalCoroutinesApi::class)
        @Test
        fun `when email and password has valid values and loginFlow returns back not successful should call unable to login to account alert`() = runTest {
            coEvery { existingUserFirebase.logInFlow(email = emailTest, password = passwordTest) } returns flowOf(value = false)

            viewModel.loginMutableStateFlow.value = LoginState(email = emailTest, password = passwordTest)

            viewModel.onLoginButtonClicked()

            verify { navigation.enableProgress(progress = any()) }
            verify { navigation.disableProgress() }
            verify { navigation.alert(alert = viewModel.unableToLoginToAccountAlert()) }
        }

        @OptIn(ExperimentalCoroutinesApi::class)
        @Test
        fun `when email and password has valid values and loginFlow returns back successful should call navigate to home`() = runTest {
            coEvery { existingUserFirebase.logInFlow(email = emailTest, password = passwordTest) } returns flowOf(value = true)

            viewModel.loginMutableStateFlow.value = LoginState(email = emailTest, password = passwordTest)

            viewModel.onLoginButtonClicked()

            verify { navigation.enableProgress(progress = any()) }
            verify { navigation.disableProgress() }
            verify { navigation.navigateToHome() }

            Assertions.assertEquals(
                viewModel.loginStateFlow.value,
                state.copy(email = "", password = "")
            )
        }
    }

    @Test fun `on forgot password clicked should call navigate to forgot password`() {
        viewModel.onForgotPasswordClicked()

        verify { navigation.navigateToForgotPassword() }
    }

    @Test fun `on create account clicked should call navigate to create account`() {
        viewModel.onCreateAccountClicked()

        verify { navigation.navigateToCreateAccount() }
    }

    @Test fun `on email value changed should update username state value`() {
        val emailTest = "newuser@yahoo.com"

        viewModel.onEmailValueChanged(newEmail = emailTest)

        Assertions.assertEquals(
            viewModel.loginStateFlow.value,
            state.copy(launcherDrawableId = DrawablesIds.launcherRoundTest, email = emailTest)
        )
    }

    @Test fun `on password value changed should update password state value`() {
        val passwordTest = "password1"

        viewModel.onPasswordValueChanged(newPassword = passwordTest)

        Assertions.assertEquals(
            viewModel.loginStateFlow.value,
            state.copy(launcherDrawableId = DrawablesIds.launcherRoundTest, password = passwordTest)
        )
    }

    @Test
    fun `emailEmptyAlert should have valid values`() {
        Assertions.assertEquals(
            viewModel.emailEmptyAlert().title,
            application.getString(StringsIds.emptyField)
        )
        Assertions.assertEquals(
            viewModel.emailEmptyAlert().description,
            application.getString(StringsIds.emailIsRequiredPleaseEnterAEmailToLoginToExistingAccount)
        )
        Assertions.assertEquals(
            viewModel.emailEmptyAlert().dismissButton!!.buttonText,
            application.getString(StringsIds.gotIt)
        )
    }

    @Test
    fun `passwordEmptyAlert should have valid values`() {
        Assertions.assertEquals(
            viewModel.passwordEmptyAlert().title,
            application.getString(StringsIds.emptyField)
        )
        Assertions.assertEquals(
            viewModel.passwordEmptyAlert().description,
            application.getString(StringsIds.passwordIsRequiredPleaseEnterAPasswordToLoginToExistingAccount)
        )
        Assertions.assertEquals(
            viewModel.passwordEmptyAlert().dismissButton!!.buttonText,
            application.getString(StringsIds.gotIt)
        )
    }

    @Test
    fun `unableToLoginAccountAlert should have valid values`() {
        Assertions.assertEquals(
            viewModel.unableToLoginToAccountAlert().title,
            application.getString(StringsIds.unableToLoginToAccount)
        )
        Assertions.assertEquals(
            viewModel.unableToLoginToAccountAlert().description,
            application.getString(StringsIds.havingTroubleLoggingIntoYourAccountPleaseTryAgainAndEnsureCredentialsExistAndAreValid)
        )
        Assertions.assertEquals(
            viewModel.unableToLoginToAccountAlert().dismissButton!!.buttonText,
            application.getString(StringsIds.gotIt)
        )
    }
}
