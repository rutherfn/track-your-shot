package com.nicholas.rutherford.track.my.shot.feature.login

import com.nicholas.rutherford.track.my.shot.build.type.BuildTypeImpl
import com.nicholas.rutherford.track.my.shot.feature.splash.DrawablesIds
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class LoginViewModelTest {

    private lateinit var viewModel: LoginViewModel

    private var navigation = mockk<LoginNavigation>(relaxed = true)

    private val debugVersionName = "debug"
    private val releaseVersionName = "release"
    private val stageVersionName = "stage"

    private val buildTypeDebug = BuildTypeImpl(buildTypeValue = debugVersionName)
    private val buildTypeRelease = BuildTypeImpl(buildTypeValue = releaseVersionName)
    private val buildTypeStage = BuildTypeImpl(buildTypeValue = stageVersionName)

    private val state = LoginState(launcherDrawableId = null, username = null, password = null)

    @BeforeEach
    fun beforeEach() {
        viewModel = LoginViewModel(navigation = navigation, buildType = buildTypeDebug)
    }

    @Test fun initializeLoginState() {
        Assertions.assertEquals(
            viewModel.loginStateFlow.value,
            state.copy(launcherDrawableId = DrawablesIds.launcherRoundTest)
        )
    }

    @Nested inner class UpdateLauncherDrawableIdState {

        @Test fun `when build type is debug should set launcherDrawableId state property to launcherRoundTest`() {
            viewModel = LoginViewModel(navigation = navigation, buildType = buildTypeDebug)

            Assertions.assertEquals(
                viewModel.loginStateFlow.value,
                state.copy(launcherDrawableId = DrawablesIds.launcherRoundTest)
            )
        }

        @Test fun `when build type is stage should set launcherDrawableId state property to launcherRoundStage`() {
            viewModel = LoginViewModel(navigation = navigation, buildType = buildTypeStage)

            Assertions.assertEquals(
                viewModel.loginStateFlow.value,
                state.copy(launcherDrawableId = DrawablesIds.launcherRoundStage)
            )
        }

        @Test fun `when build type is release should set launcherDrawableId property to launcherRound`() {
            viewModel = LoginViewModel(navigation = navigation, buildType = buildTypeRelease)

            Assertions.assertEquals(
                viewModel.loginStateFlow.value,
                state.copy(launcherDrawableId = DrawablesIds.launcherRound)
            )
        }
    }

    @Test fun `on login clicked should call navigate to home`() {
        viewModel.onLoginClicked()

        verify { navigation.navigateToHome() }
    }

    @Test fun `on forgot password clicked should call navigate to forgot password`() {
        viewModel.onForgotPasswordClicked()

        verify { navigation.navigateToForgotPassword() }
    }

    @Test fun `on create account clicked should call navigate to create account`() {
        viewModel.onCreateAccountClicked()

        verify { navigation.navigateToCreateAccount() }
    }

    @Test fun `on user name value changed should update username state value`() {
        val usernameTest = "user name 1"

        viewModel.onUsernameValueChanged(newUsername = usernameTest)

        Assertions.assertEquals(
            viewModel.loginStateFlow.value,
            state.copy(launcherDrawableId = DrawablesIds.launcherRoundTest, username = usernameTest)
        )
    }

    @Test fun `on password value changed should update password state value`() {
        val passwordTest = "password 1"

        viewModel.onPasswordValueChanged(newPassword = passwordTest)

        Assertions.assertEquals(
            viewModel.loginStateFlow.value,
            state.copy(launcherDrawableId = DrawablesIds.launcherRoundTest, password = passwordTest)
        )
    }
}
