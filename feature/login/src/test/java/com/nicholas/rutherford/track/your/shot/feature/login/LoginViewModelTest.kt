package com.nicholas.rutherford.track.your.shot.feature.login

import android.app.Application
import com.nicholas.rutherford.track.your.shot.build.type.BuildTypeImpl
import com.nicholas.rutherford.track.your.shot.feature.splash.DrawablesIds
import com.nicholas.rutherford.track.your.shot.feature.splash.StringsIds
import com.nicholas.rutherford.track.your.shot.helper.account.AccountManager
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class LoginViewModelTest {

    private lateinit var viewModel: LoginViewModel

    private var navigation = mockk<LoginNavigation>(relaxed = true)
    private var application = mockk<Application>(relaxed = true)

    private var accountManager = mockk<AccountManager>(relaxed = true)

    private val sdkValue = 2
    private val debugVersionName = "debug"
    private val releaseVersionName = "release"
    private val stageVersionName = "stage"

    private val emailTest = "newuser@yahoo.com"
    private val passwordTest = "password1"

    private val buildTypeDebug = BuildTypeImpl(sdkValue = sdkValue, buildTypeValue = debugVersionName)
    private val buildTypeRelease = BuildTypeImpl(sdkValue = sdkValue, buildTypeValue = releaseVersionName)
    private val buildTypeStage = BuildTypeImpl(sdkValue = sdkValue, buildTypeValue = stageVersionName)

    private val state = LoginState(launcherDrawableId = null, email = null, password = null)

    @BeforeEach
    fun beforeEach() {
        viewModel = LoginViewModel(
            application = application,
            navigation = navigation,
            buildType = buildTypeDebug,
            accountManager = accountManager
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
                application = application,
                navigation = navigation,
                buildType = buildTypeDebug,
                accountManager = accountManager
            )

            viewModel.updateLauncherDrawableIdState()

            Assertions.assertEquals(
                viewModel.loginStateFlow.value,
                state.copy(launcherDrawableId = DrawablesIds.launcherRoundTest)
            )
        }

        @Test fun `when build type is stage should set launcherDrawableId state property to launcherRoundStage`() {
            viewModel = LoginViewModel(
                application = application,
                navigation = navigation,
                buildType = buildTypeStage,
                accountManager = accountManager
            )

            viewModel.updateLauncherDrawableIdState()

            Assertions.assertEquals(
                viewModel.loginStateFlow.value,
                state.copy(launcherDrawableId = DrawablesIds.launcherRoundStage)
            )
        }

        @Test fun `when build type is release should set launcherDrawableId property to launcherRound`() {
            viewModel = LoginViewModel(
                application = application,
                navigation = navigation,
                buildType = buildTypeRelease,
                accountManager = accountManager
            )

            viewModel.updateLauncherDrawableIdState()

            Assertions.assertEquals(
                viewModel.loginStateFlow.value,
                state.copy(launcherDrawableId = DrawablesIds.launcherRound)
            )
        }
    }

    @Nested
    inner class FieldsErrorAlert {

        @Test
        fun `when email is null should return back email empty alert`() {
            Assertions.assertEquals(
                viewModel.fieldsErrorAlert(email = null, password = passwordTest),
                viewModel.emailEmptyAlert()
            )
        }

        @Test
        fun `when email is empty should return back email empty alert`() {
            Assertions.assertEquals(
                viewModel.fieldsErrorAlert(email = "", password = passwordTest),
                viewModel.emailEmptyAlert()
            )
        }

        @Test
        fun `when password is empty should return back password empty alert`() {
            Assertions.assertEquals(
                viewModel.fieldsErrorAlert(email = emailTest, password = ""),
                viewModel.passwordEmptyAlert()
            )
        }

        @Test
        fun `when password is null should return back password empty alert`() {
            Assertions.assertEquals(
                viewModel.fieldsErrorAlert(email = emailTest, password = null),
                viewModel.passwordEmptyAlert()
            )
        }

        @Test
        fun `when password or email is not null or empty should return back null`() {
            Assertions.assertEquals(
                viewModel.fieldsErrorAlert(email = emailTest, password = passwordTest),
                null
            )
        }
    }

    @Nested
    inner class OnLoginButtonClicked {

        @Test
        fun `when email is set to null should call email empty alert`() = runTest {
            viewModel.loginMutableStateFlow.value = LoginState(email = null, password = passwordTest)

            viewModel.onLoginButtonClicked()

            verify { navigation.alert(alert = viewModel.emailEmptyAlert()) }
        }

        @Test
        fun `when password is set to null should call password empty alert`() = runTest {
            viewModel.loginMutableStateFlow.value = LoginState(email = emailTest, password = null)

            viewModel.onLoginButtonClicked()

            verify { navigation.alert(alert = viewModel.passwordEmptyAlert()) }
        }

        @Test
        fun `when email is set to empty should call email empty alert`() = runTest {
            viewModel.loginMutableStateFlow.value = LoginState(email = "", password = passwordTest)

            viewModel.onLoginButtonClicked()

            verify { navigation.alert(alert = viewModel.emailEmptyAlert()) }
        }

        @Test
        fun `when password is set to empty should call password empty alert`() = runTest {
            viewModel.loginMutableStateFlow.value = LoginState(email = emailTest, password = "")

            viewModel.onLoginButtonClicked()

            verify { navigation.alert(alert = viewModel.passwordEmptyAlert()) }
        }
    }

    @Test fun `attempt to login to account should call login function`() {
        viewModel.attemptToLoginToAccount(email = emailTest, password = passwordTest)

        Assertions.assertEquals(
            LoginState(
                launcherDrawableId = DrawablesIds.launcherRoundTest,
                email = "",
                password = ""
            ),
            viewModel.loginMutableStateFlow.value
        )
        verify { accountManager.login(email = emailTest, password = passwordTest) }
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
    }
}
