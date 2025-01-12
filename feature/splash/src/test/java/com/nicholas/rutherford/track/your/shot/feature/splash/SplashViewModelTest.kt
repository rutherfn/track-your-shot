package com.nicholas.rutherford.track.your.shot.feature.splash

import com.nicholas.rutherford.track.your.shot.data.room.repository.ActiveUserRepository
import com.nicholas.rutherford.track.your.shot.data.test.room.TestActiveUser
import com.nicholas.rutherford.track.your.shot.firebase.core.read.ReadFirebaseUserInfo
import com.nicholas.rutherford.track.your.shot.helper.account.AccountManager
import com.nicholas.rutherford.track.your.shot.shared.preference.create.CreateSharedPreferences
import com.nicholas.rutherford.track.your.shot.shared.preference.read.ReadSharedPreferences
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class SplashViewModelTest {

    lateinit var viewModel: SplashViewModel

    internal var navigation = mockk<SplashNavigation>(relaxed = true)
    internal var readFirebaseUserInfo = mockk<ReadFirebaseUserInfo>(relaxed = true)

    internal var activeUserRepository = mockk<ActiveUserRepository>(relaxed = true)
    internal val accountManager = mockk<AccountManager>(relaxed = true)

    internal val readSharedPreferences = mockk<ReadSharedPreferences>(relaxed = true)
    internal val createSharedPreferences = mockk<CreateSharedPreferences>(relaxed = true)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val testDispatcher = UnconfinedTestDispatcher()

    private val scope = CoroutineScope(SupervisorJob() + testDispatcher)

    internal val activeUser = TestActiveUser().create()

    val dispatcher = StandardTestDispatcher()

    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeEach
    fun beforeEach() {
        Dispatchers.setMain(dispatcher)
        viewModel = SplashViewModel(
            navigation = navigation,
            readFirebaseUserInfo = readFirebaseUserInfo,
            activeUserRepository = activeUserRepository,
            accountManager = accountManager,
            readSharedPreferences = readSharedPreferences,
            createSharedPreferences = createSharedPreferences,
            scope = scope
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @AfterEach
    fun afterEach() {
        Dispatchers.resetMain()
    }

    @Nested
    inner class Init {

        @Nested
        inner class CheckIfAppHasBeenLaunchedBefore {

            @Test
            fun `when appHasBeenLaunched returns false should call expected functions`() {
                every { readSharedPreferences.appHasBeenLaunched() } returns false

                viewModel.checkIfAppHasBeenLaunchedBefore()

                verify { accountManager.checkIfWeNeedToLogoutOnLaunch() }
                verify { createSharedPreferences.createAppHasLaunchedPreference(value = true) }
            }

            @Test
            fun `when appHasBeenLaunched returns true should not call expected functions`() {
                every { readSharedPreferences.appHasBeenLaunched() } returns true

                viewModel.checkIfAppHasBeenLaunchedBefore()

                verify(exactly = 0) { accountManager.checkIfWeNeedToLogoutOnLaunch() }
                verify(exactly = 0) { createSharedPreferences.createAppHasLaunchedPreference(value = true) }
            }
        }

        @Nested
        inner class NavigateToAuthentication {

            @OptIn(ExperimentalCoroutinesApi::class)
            @Test
            fun `when isLoggedIn is set to true, isEmailVerified set to false, accountHasBeenCreated is set to true, and fields are not null should call navigateToAuthentication`() =
                runTest {
                    coEvery { activeUserRepository.fetchActiveUser() } returns activeUser.copy(
                        accountHasBeenCreated = true
                    )

                    every { readFirebaseUserInfo.isLoggedInFlow() } returns flowOf(true)
                    every { readFirebaseUserInfo.isEmailVerifiedFlow() } returns flowOf(false)

                    Dispatchers.setMain(dispatcher)
                    viewModel = SplashViewModel(
                        navigation = navigation,
                        readFirebaseUserInfo = readFirebaseUserInfo,
                        activeUserRepository = activeUserRepository,
                        accountManager = accountManager,
                        readSharedPreferences = readSharedPreferences,
                        createSharedPreferences = createSharedPreferences,
                        scope = scope
                    )

                    viewModel.navigateToPlayersListLoginOrAuthentication()

                    coVerify {
                        navigation.navigateToAuthentication(
                            username = activeUser.username,
                            email = activeUser.email
                        )
                    }
                }

            @OptIn(ExperimentalCoroutinesApi::class)
            @Test
            fun `when isLoggedIn is set to true, isEmailVerified set to true, accountHasBeenCreated is set to false and fields are not null should call navigateToAuthentication`() =
                runTest {
                    coEvery { activeUserRepository.fetchActiveUser() } returns activeUser.copy(
                        accountHasBeenCreated = false
                    )

                    every { readFirebaseUserInfo.isLoggedInFlow() } returns flowOf(true)
                    every { readFirebaseUserInfo.isEmailVerifiedFlow() } returns flowOf(true)

                    Dispatchers.setMain(dispatcher)
                    viewModel = SplashViewModel(
                        navigation = navigation,
                        readFirebaseUserInfo = readFirebaseUserInfo,
                        activeUserRepository = activeUserRepository,
                        accountManager = accountManager,
                        readSharedPreferences = readSharedPreferences,
                        createSharedPreferences = createSharedPreferences,
                        scope = scope
                    )

                    viewModel.navigateToPlayersListLoginOrAuthentication()

                    coVerify {
                        navigation.navigateToAuthentication(
                            username = activeUser.username,
                            email = activeUser.email
                        )
                    }
                }

            @OptIn(ExperimentalCoroutinesApi::class)
            @Test
            fun `when isLoggedIn is set to false should not navigateToAuthentication`() =
                runTest {
                    every { readFirebaseUserInfo.isLoggedInFlow() } returns flowOf(false)

                    Dispatchers.setMain(dispatcher)
                    viewModel = SplashViewModel(
                        navigation = navigation,
                        readFirebaseUserInfo = readFirebaseUserInfo,
                        activeUserRepository = activeUserRepository,
                        accountManager = accountManager,
                        readSharedPreferences = readSharedPreferences,
                        createSharedPreferences = createSharedPreferences,
                        scope = scope
                    )

                    viewModel.navigateToPlayersListLoginOrAuthentication()

                    coVerify(exactly = 0) {
                        navigation.navigateToAuthentication(
                            username = any(),
                            email = any()
                        )
                    }
                }

            @OptIn(ExperimentalCoroutinesApi::class)
            @Test
            fun `when isLoggedIn is set to true, isEmailVerified set to true and accountHasBeenCreated is set to true should not call navigateToAuthentication`() =
                runTest {
                    every { readFirebaseUserInfo.isLoggedInFlow() } returns flowOf(true)
                    every { readFirebaseUserInfo.isEmailVerifiedFlow() } returns flowOf(true)
                    coEvery { activeUserRepository.fetchActiveUser() } returns activeUser.copy(
                        accountHasBeenCreated = true
                    )

                    Dispatchers.setMain(dispatcher)
                    viewModel = SplashViewModel(
                        navigation = navigation,
                        readFirebaseUserInfo = readFirebaseUserInfo,
                        activeUserRepository = activeUserRepository,
                        accountManager = accountManager,
                        readSharedPreferences = readSharedPreferences,
                        createSharedPreferences = createSharedPreferences,
                        scope = scope
                    )

                    viewModel.navigateToPlayersListLoginOrAuthentication()

                    coVerify(exactly = 0) {
                        navigation.navigateToAuthentication(
                            username = any(),
                            email = any()
                        )
                    }
                }
        }

        @Nested
        inner class NavigateToPlayersListOrLogin {

            @OptIn(ExperimentalCoroutinesApi::class)
            @Test
            fun `verifies that it calls navigateToLogin when isLoggedIn is set to false`() =
                runTest {
                    every { readFirebaseUserInfo.isLoggedInFlow() } returns flowOf(false)
                    every { readFirebaseUserInfo.isEmailVerifiedFlow() } returns flowOf(true)
                    coEvery { activeUserRepository.fetchActiveUser() } returns activeUser.copy(
                        accountHasBeenCreated = false
                    )

                    Dispatchers.setMain(dispatcher)

                    viewModel = SplashViewModel(
                        navigation = navigation,
                        readFirebaseUserInfo = readFirebaseUserInfo,
                        activeUserRepository = activeUserRepository,
                        accountManager = accountManager,
                        readSharedPreferences = readSharedPreferences,
                        createSharedPreferences = createSharedPreferences,
                        scope = scope
                    )

                    viewModel.navigateToPlayersListLoginOrAuthentication()

                    coVerify { navigation.navigateToLogin() }
                }

            @OptIn(ExperimentalCoroutinesApi::class)
            @Test
            fun `verifies it calls navigateToPlayersList when isLoggedIn is set to true`() =
                runTest {
                    every { readFirebaseUserInfo.isLoggedInFlow() } returns flowOf(true)
                    every { readFirebaseUserInfo.isEmailVerifiedFlow() } returns flowOf(true)
                    coEvery { activeUserRepository.fetchActiveUser() } returns activeUser.copy(
                        accountHasBeenCreated = true
                    )

                    Dispatchers.setMain(dispatcher)

                    viewModel = SplashViewModel(
                        navigation = navigation,
                        readFirebaseUserInfo = readFirebaseUserInfo,
                        activeUserRepository = activeUserRepository,
                        accountManager = accountManager,
                        readSharedPreferences = readSharedPreferences,
                        createSharedPreferences = createSharedPreferences,
                        scope = scope
                    )

                    viewModel.navigateToPlayersListLoginOrAuthentication()

                    coVerify { navigation.navigateToPlayersList() }
                }
        }
    }
}
