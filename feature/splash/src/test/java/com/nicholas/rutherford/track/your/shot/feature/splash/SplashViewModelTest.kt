package com.nicholas.rutherford.track.your.shot.feature.splash

import com.nicholas.rutherford.track.your.shot.data.room.repository.ActiveUserRepository
import com.nicholas.rutherford.track.your.shot.data.store.reader.DataStorePreferencesReader
import com.nicholas.rutherford.track.your.shot.data.store.writer.DataStorePreferencesWriter
import com.nicholas.rutherford.track.your.shot.data.test.room.TestActiveUser
import com.nicholas.rutherford.track.your.shot.firebase.core.read.ReadFirebaseUserInfo
import com.nicholas.rutherford.track.your.shot.helper.account.AccountManager
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

    internal val dataStorePreferencesReader = mockk<DataStorePreferencesReader>(relaxed = true)
    internal val dataStorePreferencesWriter = mockk<DataStorePreferencesWriter>(relaxed = true)

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
            dataStorePreferencesReader = dataStorePreferencesReader,
            dataStorePreferencesWriter = dataStorePreferencesWriter,
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
            fun `when appHasBeenLaunched returns false should call expected functions`() = runTest {
                viewModel.checkIfAppHasBeenLaunchedBefore(appHasBeenLaunched = false)

                verify { accountManager.checkIfWeNeedToLogoutOnLaunch() }
                coVerify { dataStorePreferencesWriter.saveAppHasLaunched(value = true) }
            }

            @Test
            fun `when appHasBeenLaunched returns true should not call expected functions`() = runTest {
                viewModel.checkIfAppHasBeenLaunchedBefore(appHasBeenLaunched = true)

                verify(exactly = 0) { accountManager.checkIfWeNeedToLogoutOnLaunch() }
                coVerify(exactly = 0) { dataStorePreferencesWriter.saveAppHasLaunched(value = true) }
            }
        }

        @Nested
        inner class NavigatePostAuthDestination {

            @OptIn(ExperimentalCoroutinesApi::class)
            @Test
            fun `when isLoggedIn is set to false should not navigateToAuthentication`() =
                runTest {
                    coEvery { activeUserRepository.fetchActiveUser() } returns activeUser.copy(
                        accountHasBeenCreated = false
                    )
                    every { readFirebaseUserInfo.isLoggedInFlow() } returns flowOf(false)
                    every { readFirebaseUserInfo.isEmailVerifiedFlow() } returns flowOf(false)
                    every { dataStorePreferencesReader.readAppHasBeenLaunchedFlow() } returns flowOf(true)
                    every { dataStorePreferencesReader.readIsLoggedInFlow() } returns flowOf(false)
                    every { dataStorePreferencesReader.readShouldShowTermsAndConditionsFlow() } returns flowOf(false)

                    Dispatchers.setMain(dispatcher)
                    viewModel = SplashViewModel(
                        navigation = navigation,
                        readFirebaseUserInfo = readFirebaseUserInfo,
                        activeUserRepository = activeUserRepository,
                        accountManager = accountManager,
                        dataStorePreferencesReader = dataStorePreferencesReader,
                        dataStorePreferencesWriter = dataStorePreferencesWriter,
                        scope = scope
                    )

                    viewModel.navigateToPlayersListLoginOrAuthentication()

                    coVerify(exactly = 0) {
                        navigation.navigateToAuthentication(
                            username = any(),
                            email = any()
                        )
                    }
                    verify { navigation.navigateToLogin() }
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
                    every { dataStorePreferencesReader.readAppHasBeenLaunchedFlow() } returns flowOf(true)
                    every { dataStorePreferencesReader.readIsLoggedInFlow() } returns flowOf(false)
                    every { dataStorePreferencesReader.readShouldShowTermsAndConditionsFlow() } returns flowOf(false)

                    Dispatchers.setMain(dispatcher)
                    viewModel = SplashViewModel(
                        navigation = navigation,
                        readFirebaseUserInfo = readFirebaseUserInfo,
                        activeUserRepository = activeUserRepository,
                        accountManager = accountManager,
                        dataStorePreferencesReader = dataStorePreferencesReader,
                        dataStorePreferencesWriter = dataStorePreferencesWriter,
                        scope = scope
                    )

                    viewModel.navigateToPlayersListLoginOrAuthentication()

                    coVerify(exactly = 0) {
                        navigation.navigateToAuthentication(
                            username = any(),
                            email = any()
                        )
                    }
                    verify { navigation.navigateToPlayersList() }
                }

            @OptIn(ExperimentalCoroutinesApi::class)
            @Test
            fun `when isLoggedIn preference is set to true, isEmailVerified set to true and accountHasBeenCreated is set to true should not call navigateToAuthentication`() =
                runTest {
                    every { readFirebaseUserInfo.isLoggedInFlow() } returns flowOf(false)
                    every { readFirebaseUserInfo.isEmailVerifiedFlow() } returns flowOf(true)
                    coEvery { activeUserRepository.fetchActiveUser() } returns activeUser.copy(
                        accountHasBeenCreated = true
                    )
                    every { dataStorePreferencesReader.readAppHasBeenLaunchedFlow() } returns flowOf(true)
                    every { dataStorePreferencesReader.readIsLoggedInFlow() } returns flowOf(true)
                    every { dataStorePreferencesReader.readShouldShowTermsAndConditionsFlow() } returns flowOf(false)

                    Dispatchers.setMain(dispatcher)
                    viewModel = SplashViewModel(
                        navigation = navigation,
                        readFirebaseUserInfo = readFirebaseUserInfo,
                        activeUserRepository = activeUserRepository,
                        accountManager = accountManager,
                        dataStorePreferencesReader = dataStorePreferencesReader,
                        dataStorePreferencesWriter = dataStorePreferencesWriter,
                        scope = scope
                    )

                    viewModel.navigateToPlayersListLoginOrAuthentication()

                    coVerify(exactly = 0) {
                        navigation.navigateToAuthentication(
                            username = any(),
                            email = any()
                        )
                    }
                    verify { navigation.navigateToPlayersList() }
                }

            @OptIn(ExperimentalCoroutinesApi::class)
            @Test
            fun `when has been authenticated is set to true, isEmailVerified set to true and accountHasBeenCreated is set to true should not call navigateToAuthentication`() =
                runTest {
                    every { readFirebaseUserInfo.isLoggedInFlow() } returns flowOf(false)
                    every { readFirebaseUserInfo.isEmailVerifiedFlow() } returns flowOf(true)
                    coEvery { activeUserRepository.fetchActiveUser() } returns activeUser.copy(
                        accountHasBeenCreated = true
                    )
                    every { dataStorePreferencesReader.readAppHasBeenLaunchedFlow() } returns flowOf(true)
                    every { dataStorePreferencesReader.readIsLoggedInFlow() } returns flowOf(false)
                    every { dataStorePreferencesReader.readShouldShowTermsAndConditionsFlow() } returns flowOf(false)

                    Dispatchers.setMain(dispatcher)
                    viewModel = SplashViewModel(
                        navigation = navigation,
                        readFirebaseUserInfo = readFirebaseUserInfo,
                        activeUserRepository = activeUserRepository,
                        accountManager = accountManager,
                        dataStorePreferencesReader = dataStorePreferencesReader,
                        dataStorePreferencesWriter = dataStorePreferencesWriter,
                        scope = scope
                    )

                    viewModel.navigateToPlayersListLoginOrAuthentication()

                    coVerify(exactly = 0) {
                        navigation.navigateToAuthentication(
                            username = any(),
                            email = any()
                        )
                    }
                    verify { navigation.navigateToLogin() }
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
                    every { dataStorePreferencesReader.readAppHasBeenLaunchedFlow() } returns flowOf(true)
                    every { dataStorePreferencesReader.readIsLoggedInFlow() } returns flowOf(false)
                    every { dataStorePreferencesReader.readShouldShowTermsAndConditionsFlow() } returns flowOf(false)

                    Dispatchers.setMain(dispatcher)

                    viewModel = SplashViewModel(
                        navigation = navigation,
                        readFirebaseUserInfo = readFirebaseUserInfo,
                        activeUserRepository = activeUserRepository,
                        accountManager = accountManager,
                        dataStorePreferencesReader = dataStorePreferencesReader,
                        dataStorePreferencesWriter = dataStorePreferencesWriter,
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
                    every { dataStorePreferencesReader.readAppHasBeenLaunchedFlow() } returns flowOf(true)
                    every { dataStorePreferencesReader.readIsLoggedInFlow() } returns flowOf(false)
                    every { dataStorePreferencesReader.readShouldShowTermsAndConditionsFlow() } returns flowOf(false)

                    Dispatchers.setMain(dispatcher)

                    viewModel = SplashViewModel(
                        navigation = navigation,
                        readFirebaseUserInfo = readFirebaseUserInfo,
                        activeUserRepository = activeUserRepository,
                        accountManager = accountManager,
                        dataStorePreferencesReader = dataStorePreferencesReader,
                        dataStorePreferencesWriter = dataStorePreferencesWriter,
                        scope = scope
                    )

                    viewModel.navigateToPlayersListLoginOrAuthentication()

                    coVerify { navigation.navigateToPlayersList() }
                }
        }

        @Nested
        inner class NavigateToPostAuthDestination {

            @Test
            fun `when shouldShowTermsAndConditions and isLoggedIn is set to true should navigate to terms and conditions`() {
                val isLoggedIn = true
                val email = "emailtest@gmail.com"

                viewModel.navigatePostAuthDestination(shouldShowTermAndConditions = true, isLoggedIn = isLoggedIn, email = email)

                verify { navigation.navigateToTermsAndConditions() }

                verify(exactly = 0) { navigation.navigateToPlayersList() }
                verify(exactly = 0) { navigation.navigateToLogin() }
            }

            @Test
            fun `when shouldShowTermsAndConditions is set to false, isLoggedIn set to true, end email is null should navigate to login`() {
                val isLoggedIn = true

                viewModel.navigatePostAuthDestination(shouldShowTermAndConditions = false, isLoggedIn = isLoggedIn, email = null)

                verify { navigation.navigateToLogin() }

                verify(exactly = 0) { navigation.navigateToPlayersList() }
                verify(exactly = 0) { navigation.navigateToTermsAndConditions() }
            }

            @Test
            fun `when shouldShowTermsAndConditions is set to false, isLoggedIn set to true, end email is not null should navigate to players list`() {
                val isLoggedIn = true
                val email = "emailtest@gmail.com"

                viewModel.navigatePostAuthDestination(shouldShowTermAndConditions = false, isLoggedIn = isLoggedIn, email = email)

                verify { navigation.navigateToPlayersList() }

                verify(exactly = 0) { navigation.navigateToLogin() }
                verify(exactly = 0) { navigation.navigateToTermsAndConditions() }
            }

            @Test
            fun `when shouldShowTermsAndConditions is set to false and isLoggedIn set to false should navigate to login`() {
                val isLoggedIn = false

                viewModel.navigatePostAuthDestination(shouldShowTermAndConditions = false, isLoggedIn = isLoggedIn, email = null)

                verify { navigation.navigateToLogin() }

                verify(exactly = 0) { navigation.navigateToPlayersList() }
                verify(exactly = 0) { navigation.navigateToTermsAndConditions() }
            }
        }
    }
}
