package com.nicholas.rutherford.track.my.shot.feature.forgot.password

import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ForgotPasswordViewModelTest {

    lateinit var viewModel: ForgotPasswordViewModel

    private var navigation = mockk<ForgotPasswordNavigation>(relaxed = true)

    private var state = ForgotPasswordState(email = null)

    @BeforeEach
    fun beforeEach() {
        viewModel = ForgotPasswordViewModel(navigation = navigation)
    }

    @Test
    fun initializeForgotPasswordState() {
        Assertions.assertEquals(
            viewModel.forgotPasswordStateFlow.value,
            state
        )
    }

    @Test
    fun `on back button clicked should pop to forgot password`() {
        viewModel.onBackButtonClicked()

        verify { navigation.pop() }
    }

    @Test fun `on email value change should update email state value`() {
        val newEmail = "new email"

        viewModel.onEmailValueChanged(newEmail = newEmail)

        Assertions.assertEquals(
            viewModel.forgotPasswordStateFlow.value,
            state.copy(email = newEmail)
        )
    }
}
