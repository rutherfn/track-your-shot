package com.nicholas.rutherford.track.my.shot.feature.home

import com.nicholas.rutherford.track.my.shot.data.room.repository.ActiveUserRepository
import com.nicholas.rutherford.track.my.shot.firebase.util.existinguser.ExistingUserFirebase
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class HomeViewModelTest {

    private lateinit var viewModel: HomeViewModel

    private var navigation = mockk<HomeNavigation>(relaxed = true)
    private var activeUserRepository = mockk<ActiveUserRepository>(relaxed = true)

    private var existingUserFirebase = mockk<ExistingUserFirebase>(relaxed = true)

    @BeforeEach
    fun beforeEach() {
        viewModel = HomeViewModel(
            navigation = navigation,
            existingUserFirebase = existingUserFirebase,
            activeUserRepository = activeUserRepository
        )
    }

    @Test
    fun `navigate test`() {
        viewModel.navigateTest()

        coVerify { activeUserRepository.deleteActiveUser() }
        verify { existingUserFirebase.logOut() }
        verify { navigation.navigateToLogin() }
    }
}
