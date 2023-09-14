package com.nicholas.rutherford.track.my.shot.feature.splash

import com.nicholas.rutherford.track.my.shot.data.room.repository.ActiveUserRepository
import com.nicholas.rutherford.track.my.shot.data.test.room.TestActiveUser
import com.nicholas.rutherford.track.my.shot.firebase.read.ReadFirebaseUserInfo
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class SplashViewModelTest {

    lateinit var viewModel: SplashViewModel

    internal var navigation = mockk<SplashNavigation>(relaxed = true)
    internal var readFirebaseUserInfo = mockk<ReadFirebaseUserInfo>(relaxed = true)

    internal var activeUserRepository = mockk<ActiveUserRepository>(relaxed = true)

    internal val delayTime = 4001L // needs 1 extra millisecond to account for function below call

    internal val activeUser = TestActiveUser().create()

    @OptIn(ExperimentalCoroutinesApi::class)
    val dispatcher = StandardTestDispatcher()

    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeEach
    fun beforeEach() {
        Dispatchers.setMain(dispatcher)
        viewModel = SplashViewModel(
            navigation = navigation,
            readFirebaseUserInfo = readFirebaseUserInfo,
            activeUserRepository = activeUserRepository
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @AfterEach
    fun afterEach() {
        Dispatchers.resetMain()
    }

    @Test
    fun constants() {
        Assertions.assertEquals(SPLASH_DELAY_IN_MILLIS, 4000L)
    }

    @Nested
    inner class Init {

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
                        activeUserRepository = activeUserRepository
                    )

                    viewModel.navigateToHomeLoginOrAuthentication()

                    delay(delayTime)

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
                        activeUserRepository = activeUserRepository
                    )

                    viewModel.navigateToHomeLoginOrAuthentication()

                    delay(delayTime)

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
                        activeUserRepository = activeUserRepository
                    )

                    viewModel.navigateToHomeLoginOrAuthentication()

                    delay(delayTime)

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
                        activeUserRepository = activeUserRepository
                    )

                    viewModel.navigateToHomeLoginOrAuthentication()

                    delay(delayTime)

                    coVerify(exactly = 0) {
                        navigation.navigateToAuthentication(
                            username = any(),
                            email = any()
                        )
                    }
                }
        }

        @Nested
        inner class NavigateToHomeOrLogin {

            @OptIn(ExperimentalCoroutinesApi::class)
            @Test
            fun `should delay for 4 seconds and verifies that it calls navigateToLogin when isLoggedIn is set to false`() =
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
                        activeUserRepository = activeUserRepository
                    )

                    viewModel.navigateToHomeLoginOrAuthentication()

                    delay(delayTime)

                    coVerify { navigation.navigateToLogin() }
                }

            @OptIn(ExperimentalCoroutinesApi::class)
            @Test
            fun `should delay for 4 seconds and verifies it calls navigateToHome when isLoggedIn is set to true`() =
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
                        activeUserRepository = activeUserRepository
                    )

                    viewModel.navigateToHomeLoginOrAuthentication()

                    delay(delayTime)

                    coVerify { navigation.navigateToHome() }
                }
        }
    }
}
