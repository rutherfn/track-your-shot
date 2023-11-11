package com.nicholas.rutherford.track.your.shot.helper.account

import com.nicholas.rutherford.track.your.shot.data.room.repository.ActiveUserRepository
import com.nicholas.rutherford.track.your.shot.data.room.repository.PlayerRepository
import com.nicholas.rutherford.track.your.shot.data.room.repository.UserRepository
import com.nicholas.rutherford.track.your.shot.firebase.util.existinguser.ExistingUserFirebase
import com.nicholas.rutherford.track.your.shot.helper.constants.Constants
import com.nicholas.rutherford.track.your.shot.navigation.Navigator
import io.mockk.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AccountAuthManagerImplTest {

    private lateinit var accountAuthManagerImpl: AccountAuthManagerImpl

    @OptIn(ExperimentalCoroutinesApi::class)
    private val testDispatcher = UnconfinedTestDispatcher()

    private val scope = CoroutineScope(SupervisorJob() + testDispatcher)

    private val navigator = mockk<Navigator>(relaxed = true)

    private val activeUserRepository = mockk<ActiveUserRepository>(relaxed = true)
    private val playerRepository = mockk<PlayerRepository>(relaxed = true)
    private val userRepository = mockk<UserRepository>(relaxed = true)

    private val existingUserFirebase = mockk<ExistingUserFirebase>(relaxed = true)

    @BeforeEach
    fun beforeEach() {
        accountAuthManagerImpl = AccountAuthManagerImpl(
            scope = scope,
            navigator = navigator,
            activeUserRepository = activeUserRepository,
            playerRepository = playerRepository,
            userRepository = userRepository,
            existingUserFirebase = existingUserFirebase
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `logout verify should call functions in order`() = runTest {
        accountAuthManagerImpl.logout()

        verify { navigator.progress(progressAction = any()) }
        verify { existingUserFirebase.logout() }

        coVerify { accountAuthManagerImpl.clearOutDatabase() }

        testDispatcher.scheduler.apply { advanceTimeBy(Constants.DELAY_IN_MILLISECONDS_BEFORE_LOGGING_OUT); runCurrent() }

        verify { navigator.progress(progressAction = null) }
        verify { navigator.navigate(navigationAction = any()) }
    }

    @Test
    fun `clear out database verify should call delete functions`() = runTest {
        accountAuthManagerImpl.clearOutDatabase()

        coVerify { activeUserRepository.deleteActiveUser() }
        coVerify { playerRepository.deleteAllPlayers() }
        coVerify { userRepository.deleteAllUsers() }
    }
}
