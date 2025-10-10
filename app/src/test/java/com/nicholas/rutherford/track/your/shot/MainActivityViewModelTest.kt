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
