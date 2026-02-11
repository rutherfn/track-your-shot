package com.nicholas.rutherford.track.your.shot.feature.shots

import com.nicholas.rutherford.track.your.shot.data.room.repository.PlayerRepository
import com.nicholas.rutherford.track.your.shot.data.room.response.fullName
import com.nicholas.rutherford.track.your.shot.data.store.reader.DataStorePreferencesReader
import com.nicholas.rutherford.track.your.shot.data.store.writer.DataStorePreferencesWriter
import com.nicholas.rutherford.track.your.shot.data.test.room.TestPlayer
import com.nicholas.rutherford.track.your.shot.data.test.room.TestShotLogged
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class ShotsListViewModelTest {

    private lateinit var viewModel: ShotsListViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    private val testDispatcher = UnconfinedTestDispatcher()

    private val scope = CoroutineScope(SupervisorJob() + testDispatcher)

    private val navigation = mockk<ShotsListNavigation>(relaxed = true)

    private val playerRepository = mockk<PlayerRepository>(relaxed = true)

    private val dataStorePreferencesWriter = mockk<DataStorePreferencesWriter>(relaxed = true)
    private val dataStorePreferencesReader = mockk<DataStorePreferencesReader>(relaxed = true)

    @BeforeEach
    fun beforeEach() {
        viewModel = ShotsListViewModel(
            scope = scope,
            navigation = navigation,
            playerRepository = playerRepository,
            dataStorePreferencesWriter = dataStorePreferencesWriter,
            dataStorePreferencesReader = dataStorePreferencesReader
        )
    }

    @Nested
    inner class Init {

        @Test
        fun `when fetch all players returns empty list should not update current array list or state`() = runTest {
            coEvery { playerRepository.fetchAllPlayers() } returns emptyList()
            every { dataStorePreferencesReader.readPlayerFilterNameFlow() } returns flow { emit("") }

            viewModel.updateShotListState()
            viewModel.checkToCreatePlayerFilterName()

            Assertions.assertEquals("", viewModel.shotListStateFlow.value.playerFilteredName)
            coVerify(exactly = 0) { dataStorePreferencesWriter.savePlayerFilterName(value = "") }
            Assertions.assertEquals(
                viewModel.shotListMutableStateFlow.value,
                ShotsListState(shotList = emptyList(), playerFilteredName = "")
            )
        }

        @Test
        fun `when fetch all players returns info should update current array list and state`() = runTest {
            val player = TestPlayer().create()
            val playerId = 1

            coEvery { playerRepository.fetchAllPlayers() } returns listOf(player)
            coEvery { playerRepository.fetchPlayerIdByName(firstName = player.firstName, lastName = player.lastName) } returns playerId
            every { dataStorePreferencesReader.readPlayerFilterNameFlow() } returns flow { emit("") }

            viewModel.updateShotListState()
            viewModel.checkToCreatePlayerFilterName()

            Assertions.assertEquals("", viewModel.shotListStateFlow.value.playerFilteredName)
            coVerify(exactly = 0) { dataStorePreferencesWriter.savePlayerFilterName(value = "") }
            Assertions.assertEquals(
                viewModel.shotListMutableStateFlow.value,
                ShotsListState(
                    shotList = listOf(ShotLoggedWithPlayer(shotLogged = player.shotsLoggedList.first(), playerId = playerId, playerName = player.fullName())),
                    playerFilteredName = ""
                )
            )
        }

        @Test
        fun `when player filter name returns a value should update playerFilteredName`() = runTest {
            val playerFilteredName = "playerFilteredName"

            coEvery { playerRepository.fetchAllPlayers() } returns emptyList()
            every { dataStorePreferencesReader.readPlayerFilterNameFlow() } returns flow { emit(playerFilteredName) }

            viewModel.updateShotListState()
            viewModel.checkToCreatePlayerFilterName()

            Assertions.assertEquals(playerFilteredName, viewModel.shotListStateFlow.value.playerFilteredName)
            coVerify { dataStorePreferencesWriter.savePlayerFilterName(value = "") }
            Assertions.assertEquals(
                viewModel.shotListMutableStateFlow.value,
                ShotsListState(shotList = emptyList(), playerFilteredName = playerFilteredName)
            )
        }
    }

    @Nested
    inner class FilterShotList {

        private val playerFilteredName = "PlayerA"
        private val defaultShot = ShotLoggedWithPlayer(
            shotLogged = TestShotLogged.build(),
            playerId = 1,
            playerName = "playerName"
        )

        @Test
        fun `should return shots matching playerFilteredName`() {
            viewModel.shotListMutableStateFlow.update { state -> state.copy(playerFilteredName = playerFilteredName) }

            val result = viewModel.filterShotList(
                shotList = listOf(defaultShot, defaultShot.copy(playerName = "playerB"), defaultShot.copy(playerName = playerFilteredName)),
                playerFilteredName = playerFilteredName
            )

            Assertions.assertEquals(result.size, 1)
            Assertions.assertEquals(result, listOf(defaultShot.copy(playerName = playerFilteredName)))
        }

        @Test
        fun `should return empty list if no shots match the playerFilteredName`() {
            viewModel.shotListMutableStateFlow.update { it.copy(playerFilteredName = playerFilteredName) }

            val result = viewModel.filterShotList(
                shotList = listOf(defaultShot, defaultShot.copy(playerName = "playerB"), defaultShot.copy(playerName = "test")),
                playerFilteredName = playerFilteredName
            )

            Assertions.assertEquals(result.size, 0)
            Assertions.assertEquals(result, emptyList<ShotLoggedWithPlayer>())
        }
    }

    @Nested
    inner class UpdateShotListState {
        private val playerFilteredName = "Player A"

        @Test
        fun `when playerFilterName is empty should not filter shots and update state`() = runTest {
            val expectedShotList = listOf(
                ShotLoggedWithPlayer(
                    shotLogged = TestPlayer().create().copy(firstName = "test", lastName = "first").shotsLoggedList.first(),
                    playerId = 2,
                    playerName = "test first"
                ),
                ShotLoggedWithPlayer(
                    shotLogged = TestPlayer().create().copy(firstName = "test", lastName = "second").shotsLoggedList.first(),
                    playerId = 3,
                    playerName = "test second"
                ),
                ShotLoggedWithPlayer(
                    shotLogged = TestPlayer().create().copy(firstName = "Player", lastName = "A").shotsLoggedList.first(),
                    playerId = 4,
                    playerName = playerFilteredName
                )
            )
            coEvery { playerRepository.fetchAllPlayers() } returns listOf(
                TestPlayer().create().copy(firstName = "test", lastName = "first"),
                TestPlayer().create().copy(firstName = "test", lastName = "second"),
                TestPlayer().create().copy(firstName = "Player", lastName = "A")
            )
            coEvery { playerRepository.fetchPlayerIdByName(firstName = "test", lastName = "first") } returns 2
            coEvery { playerRepository.fetchPlayerIdByName(firstName = "test", lastName = "second") } returns 3
            coEvery { playerRepository.fetchPlayerIdByName(firstName = "Player", lastName = "A") } returns 4

            viewModel.shotListMutableStateFlow.update { state -> state.copy(playerFilteredName = "") }

            viewModel.updateShotListState()

            Assertions.assertEquals(viewModel.shotListMutableStateFlow.value.shotList, expectedShotList)
            Assertions.assertEquals(viewModel.shotListMutableStateFlow.value.playerFilteredName, "")
        }

        @Test
        fun `when playerFilterName is not empty should filter shots and update state`() = runTest {
            val expectedShotList = listOf(
                ShotLoggedWithPlayer(
                    shotLogged = TestPlayer().create().copy(firstName = "Player", lastName = "A").shotsLoggedList.first(),
                    playerId = 4,
                    playerName = playerFilteredName
                )
            )
            coEvery { playerRepository.fetchAllPlayers() } returns listOf(
                TestPlayer().create().copy(firstName = "test", lastName = "first"),
                TestPlayer().create().copy(firstName = "test", lastName = "second"),
                TestPlayer().create().copy(firstName = "Player", lastName = "A")
            )
            coEvery { playerRepository.fetchPlayerIdByName(firstName = "test", lastName = "first") } returns 2
            coEvery { playerRepository.fetchPlayerIdByName(firstName = "test", lastName = "second") } returns 3
            coEvery { playerRepository.fetchPlayerIdByName(firstName = "Player", lastName = "A") } returns 4

            viewModel.shotListMutableStateFlow.update { it.copy(playerFilteredName = playerFilteredName) }

            viewModel.updateShotListState()

            Assertions.assertEquals(viewModel.shotListMutableStateFlow.value.shotList, expectedShotList)
            Assertions.assertEquals(viewModel.shotListMutableStateFlow.value.playerFilteredName, playerFilteredName)
        }
    }

    @Nested
    inner class OnToolbarMenuClicked {

        @Test
        fun `when playerFilterName is empty and shouldShowAllPlayerShots is set to true should call openNavigationDrawer`() {
            viewModel.shotListMutableStateFlow.update { state -> state.copy(playerFilteredName = "") }

            viewModel.onToolbarMenuClicked(shouldShowAllPlayerShots = true)

            verify { navigation.openNavigationDrawer() }
            verify(exactly = 0) { navigation.popToPlayerList() }

            Assertions.assertEquals(
                viewModel.shotListMutableStateFlow.value,
                ShotsListState(shotList = emptyList(), playerFilteredName = "")
            )
        }

        @Test
        fun `when playerFilterName is empty and shouldShowAllPlayerShots is set to false should call popToPlayerList`() {
            viewModel.shotListMutableStateFlow.update { state -> state.copy(playerFilteredName = "") }

            viewModel.onToolbarMenuClicked(shouldShowAllPlayerShots = false)

            verify(exactly = 0) { navigation.openNavigationDrawer() }
            verify { navigation.popToPlayerList() }

            Assertions.assertEquals(
                viewModel.shotListMutableStateFlow.value,
                ShotsListState(shotList = emptyList(), playerFilteredName = "")
            )
        }

        @Test
        fun `when playerFilterName is not empty and shouldShowAllPlayerShots is set to true should call popToPlayerList`() {
            viewModel.shotListMutableStateFlow.update { state -> state.copy(playerFilteredName = "filteredName") }

            viewModel.onToolbarMenuClicked(shouldShowAllPlayerShots = true)

            verify(exactly = 0) { navigation.openNavigationDrawer() }
            verify { navigation.popToPlayerList() }

            Assertions.assertEquals(
                viewModel.shotListMutableStateFlow.value,
                ShotsListState(shotList = emptyList(), playerFilteredName = "filteredName")
            )
        }
    }

    @Test
    fun `on shot item clicked`() {
        val player = TestPlayer().create()
        val playerId = 1
        val shotLoggedWithPlayer = ShotLoggedWithPlayer(
            shotLogged = TestShotLogged.build(),
            playerId = playerId,
            playerName = player.fullName()
        )

        viewModel.onShotItemClicked(shotLoggedWithPlayer = shotLoggedWithPlayer)

        verify {
            navigation.navigateToLogShot(
                isExistingPlayer = true,
                playerId = shotLoggedWithPlayer.playerId,
                shotType = shotLoggedWithPlayer.shotLogged.shotType,
                shotId = shotLoggedWithPlayer.shotLogged.id,
                viewCurrentExistingShot = true,
                viewCurrentPendingShot = false,
                fromShotList = true
            )
        }

        Assertions.assertEquals(
            viewModel.shotListMutableStateFlow.value,
            ShotsListState(shotList = emptyList(), playerFilteredName = "")
        )
    }

    @Test
    fun `on help clicked`() {
        viewModel.onHelpClicked()

        verify { navigation.alert(alert = any()) }
    }
}
