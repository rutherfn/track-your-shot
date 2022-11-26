package com.nicholas.rutherford.track.my.shot.feature.login

import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class LoginViewModelTest {

    lateinit var viewModel: LoginViewModel

    private var navigation = mockk<LoginNavigation>(relaxed = true)

    private val state = LoginState(username = null, password = null)

    @BeforeEach
    fun beforeEach() {
        viewModel = LoginViewModel(navigation = navigation)
    }

    @Test fun initializeLoginState() {
        Assertions.assertEquals(
            viewModel.loginStateFlow.value,
            state
        )
    }

    @Test fun `on login clicked should call navigate to home`() {
        viewModel.onLoginClicked()

        verify { navigation.navigateToHome() }
    }

    @Test fun `on reset password clicked should call navigate to reset password`() {
        viewModel.onResetPasswordClicked()

        verify { navigation.navigateToResetPassword() }
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
            state.copy(username = usernameTest)
        )
    }

    @Test fun `on password value changed should update password state value`() {
        val passwordTest = "password 1"

        viewModel.onPasswordValueChanged(newPassword = passwordTest)

        Assertions.assertEquals(
            viewModel.loginStateFlow.value,
            state.copy(password = passwordTest)
        )
    }
}
