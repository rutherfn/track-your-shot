package com.nicholas.rutherford.track.your.shot.feature.settings.accountinfo

import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class AccountInfoViewModelTest {

    private lateinit var accountInfoViewModel: AccountInfoViewModel

    private var navigation = mockk<AccountInfoNavigation>(relaxed = true)

    @BeforeEach
    fun beforeEach() {
        accountInfoViewModel = AccountInfoViewModel(navigation = navigation)
    }

    @Test
    fun `on toolbar menu clicked should call pop`() {
        accountInfoViewModel.onToolbarMenuClicked()

        verify { navigation.pop() }
    }
}