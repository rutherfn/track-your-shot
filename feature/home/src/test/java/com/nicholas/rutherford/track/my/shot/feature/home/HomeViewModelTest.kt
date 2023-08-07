package com.nicholas.rutherford.track.my.shot.feature.home

import com.nicholas.rutherford.track.my.shot.data.room.repository.ActiveUserRepository
import com.nicholas.rutherford.track.my.shot.data.test.account.info.realtime.TestAccountInfoRealTimeResponse
import com.nicholas.rutherford.track.my.shot.data.test.room.TestActiveUser
import com.nicholas.rutherford.track.my.shot.firebase.read.ReadFirebaseUserInfo
import com.nicholas.rutherford.track.my.shot.firebase.util.existinguser.ExistingUserFirebase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class HomeViewModelTest {

    private lateinit var viewModel: HomeViewModel

    private var navigation = mockk<HomeNavigation>(relaxed = true)
    private var activeUserRepository = mockk<ActiveUserRepository>(relaxed = true)

    private var existingUserFirebase = mockk<ExistingUserFirebase>(relaxed = true)
    private var readFirebaseUserInfo = mockk<ReadFirebaseUserInfo>(relaxed = true)

    private var activeUser = TestActiveUser().create()
    private var accountInfoRealTimeResponse = TestAccountInfoRealTimeResponse().create()

    @BeforeEach
    fun beforeEach() {
        viewModel = HomeViewModel(
            navigation = navigation,
            existingUserFirebase = existingUserFirebase,
            activeUserRepository = activeUserRepository,
            readFirebaseUserInfo = readFirebaseUserInfo
        )
    }

    @Nested
    inner class CollectAccountInfoFlowByEmail {

        @OptIn(ExperimentalCoroutinesApi::class)
        @Test
        fun `when get account info flow by email emits a new value and fetch active user is not null should call create active user`() = runTest {
            coEvery { activeUserRepository.fetchActiveUser() } returns activeUser
            every { readFirebaseUserInfo.getAccountInfoFlowByEmail(email = accountInfoRealTimeResponse.email) } returns flowOf(accountInfoRealTimeResponse)

            viewModel.collectAccountInfoFlowByEmail(email = accountInfoRealTimeResponse.email)

            coVerify { viewModel.attemptToUpdateActiveUser(email = accountInfoRealTimeResponse.email, username = accountInfoRealTimeResponse.userName) }
            coVerify(exactly = 0) { activeUserRepository.createActiveUser(activeUser.copy(email = accountInfoRealTimeResponse.email, username = accountInfoRealTimeResponse.userName)) }
        }

        @OptIn(ExperimentalCoroutinesApi::class)
        @Test
        fun `when get account info flow by email emits a new value and fetch active user is null should call create active user`() = runTest {
            coEvery { activeUserRepository.fetchActiveUser() } returns null
            every { readFirebaseUserInfo.getAccountInfoFlowByEmail(email = accountInfoRealTimeResponse.email) } returns flowOf(accountInfoRealTimeResponse)

            viewModel.collectAccountInfoFlowByEmail(email = accountInfoRealTimeResponse.email)

            coVerify { viewModel.attemptToUpdateActiveUser(email = accountInfoRealTimeResponse.email, username = accountInfoRealTimeResponse.userName) }
            coVerify { activeUserRepository.createActiveUser(activeUser.copy(accountHasBeenCreated = true, email = accountInfoRealTimeResponse.email, username = accountInfoRealTimeResponse.userName)) }
        }
    }

    @Test
    fun `navigate test`() {
        viewModel.navigateTest()

        verify { existingUserFirebase.logOut() }
        verify { navigation.navigateToLogin() }
    }
}
