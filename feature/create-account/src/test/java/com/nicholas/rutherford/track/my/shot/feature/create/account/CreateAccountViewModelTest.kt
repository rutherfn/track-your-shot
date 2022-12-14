package com.nicholas.rutherford.track.my.shot.feature.create.account

import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class CreateAccountViewModelTest {

    private lateinit var viewModel: CreateAccountViewModel

    private var navigation = mockk<CreateAccountNavigation>(relaxed = true)

    private val state = CreateAccountState(email = null)

    @BeforeEach
    fun beforeEach() {
        viewModel = CreateAccountViewModel(navigation = navigation)
    }

    @Test
    fun initializeCreateAccountState() {
        Assertions.assertEquals(
            viewModel.createAccountStateFlow.value, state
        ) }

    @Test fun `on back button clicked should pop`() {
        viewModel.onBackButtonClicked()

        verify { navigation.pop() }
    }
}
