package com.nicholas.rutherford.track.your.shot

import com.nicholas.rutherford.track.your.shot.base.resources.StringsIds
import com.nicholas.rutherford.track.your.shot.data.store.reader.DataStorePreferencesReader
import com.nicholas.rutherford.track.your.shot.helper.account.AccountManager
import com.nicholas.rutherford.track.your.shot.helper.network.Network
import com.nicholas.rutherford.track.your.shot.navigation.LogoutAction
import com.nicholas.rutherford.track.your.shot.navigation.PlayersListAction
import com.nicholas.rutherford.track.your.shot.navigation.ReportingAction
import com.nicholas.rutherford.track.your.shot.navigation.SettingsAction
import com.nicholas.rutherford.track.your.shot.navigation.ShotsAction
import com.nicholas.rutherford.track.your.shot.navigation.VoiceCommandsAction
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class MainActivityViewModelTest {

    private lateinit var mainActivityViewModel: MainActivityViewModel

    private val network = mockk<Network>(relaxed = true)
    private val accountManager = mockk<AccountManager>(relaxed = true)
    private val dataStorePreferencesReader = mockk<DataStorePreferencesReader>(relaxed = true)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val dispatcher = StandardTestDispatcher()

    private val scope = CoroutineScope(SupervisorJob() + dispatcher)

    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeEach
    fun beforeEach() {
        Dispatchers.setMain(dispatcher)

        every { network.isConnected } returns flowOf(true)
        every { dataStorePreferencesReader.readVoiceToggledDebugEnabledFlow() } returns flowOf(false)

        mainActivityViewModel = MainActivityViewModel(
            network = network,
            scope = scope,
            accountManager = accountManager,
            dataStorePreferenceReader = dataStorePreferencesReader
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @AfterEach
    fun afterEach() {
        Dispatchers.resetMain()
    }

    @Test
    fun constants() {
        Assertions.assertEquals(IS_CONNECTED_TIMEOUT_MILLIS, 5000L)
    }

    @Nested
    inner class IsConnectedStateFlow {

        @Test
        fun `isConnected should have initial value of false`() = runTest {
            every { network.isConnected } returns flowOf(true)

            val viewModel = MainActivityViewModel(
                network = network,
                scope = scope,
                accountManager = accountManager,
                dataStorePreferenceReader = dataStorePreferencesReader
            )

            Assertions.assertEquals(false, viewModel.isConnected.value)
        }

        @Test
        fun `isConnected should update when network flow emits true`() = runTest {
            val networkFlow = MutableSharedFlow<Boolean>()

            every { network.isConnected } returns networkFlow

            val viewModel = MainActivityViewModel(
                network = network,
                scope = scope,
                accountManager = accountManager,
                dataStorePreferenceReader = dataStorePreferencesReader
            )

            val stateFlowJob = launch { viewModel.isConnected.collect { } }

            dispatcher.scheduler.advanceUntilIdle()

            networkFlow.emit(true)
            dispatcher.scheduler.advanceUntilIdle()

            Assertions.assertEquals(true, viewModel.isConnected.value)

            stateFlowJob.cancel()
        }

        @Test
        fun `isConnected should update when network flow emits false`() = runTest {
            val networkFlow = MutableSharedFlow<Boolean>()
            every { network.isConnected } returns networkFlow

            val viewModel = MainActivityViewModel(
                network = network,
                scope = scope,
                accountManager = accountManager,
                dataStorePreferenceReader = dataStorePreferencesReader
            )

            val stateFlowJob = launch { viewModel.isConnected.collect { } }

            networkFlow.emit(false)
            dispatcher.scheduler.advanceUntilIdle()

            Assertions.assertEquals(false, viewModel.isConnected.value)

            stateFlowJob.cancel()
        }

        @Test
        fun `isConnected should handle multiple emissions correctly`() = runTest {
            val networkFlow = MutableSharedFlow<Boolean>()

            every { network.isConnected } returns networkFlow

            val viewModel = MainActivityViewModel(
                network = network,
                scope = scope,
                accountManager = accountManager,
                dataStorePreferenceReader = dataStorePreferencesReader
            )

            val stateFlowJob = launch { viewModel.isConnected.collect { } }

            dispatcher.scheduler.advanceUntilIdle()

            networkFlow.emit(true)
            dispatcher.scheduler.advanceUntilIdle()
            Assertions.assertEquals(true, viewModel.isConnected.value)

            networkFlow.emit(false)
            dispatcher.scheduler.advanceUntilIdle()
            Assertions.assertEquals(false, viewModel.isConnected.value)

            networkFlow.emit(true)
            dispatcher.scheduler.advanceUntilIdle()

            Assertions.assertEquals(true, viewModel.isConnected.value)

            stateFlowJob.cancel()
        }
    }

    @Test
    fun `init should update state property when collectReadVoiceToggledDebugEnabledFlow returns back a value`() = runTest {
        coEvery { dataStorePreferencesReader.readVoiceToggledDebugEnabledFlow() } returns flowOf(value = true)

        mainActivityViewModel = MainActivityViewModel(
            network = network,
            scope = scope,
            accountManager = accountManager,
            dataStorePreferenceReader = dataStorePreferencesReader
        )

        dispatcher.scheduler.advanceUntilIdle()

        Assertions.assertEquals(mainActivityViewModel.isVoiceToggleEnabled, true)
    }

    @Nested
    inner class BuildDrawerActions {

        @Test
        fun `should return to default list when isVoiceToggleEnabled is set to false`() {
            mainActivityViewModel.isVoiceToggleEnabled = false

            val result = mainActivityViewModel.buildDrawerActions()

            val expectedActions = listOf(
                PlayersListAction,
                ShotsAction,
                ReportingAction,
                SettingsAction,
                LogoutAction
            )
            Assertions.assertEquals(expectedActions, result)
        }

        @Test
        fun `should return to default list with voice command action when isVoiceToggleEnabled is set to true`() {
            mainActivityViewModel.isVoiceToggleEnabled = true

            val result = mainActivityViewModel.buildDrawerActions()

            val expectedActions = listOf(
                PlayersListAction,
                ShotsAction,
                ReportingAction,
                SettingsAction,
                VoiceCommandsAction,
                LogoutAction
            )

            Assertions.assertEquals(expectedActions, result)
        }
    }

    @Nested
    inner class Logout {

        @Test
        fun `should call logout when passed in titleId is logout`() {
            mainActivityViewModel.logout(titleId = StringsIds.logout)

            verify { accountManager.logout() }
        }

        @Test
        fun `should not call logout when passed in titleId is not logout`() {
            mainActivityViewModel.logout(titleId = StringsIds.players)

            verify(exactly = 0) { accountManager.logout() }
        }
    }
}
