package com.nicholas.rutherford.track.your.shot.feature.shots

import com.nicholas.rutherford.track.your.shot.data.room.repository.PlayerRepository
import com.nicholas.rutherford.track.your.shot.data.room.response.fullName
import com.nicholas.rutherford.track.your.shot.data.test.room.TestPlayer
import com.nicholas.rutherford.track.your.shot.data.test.room.TestShotLogged
import com.nicholas.rutherford.track.your.shot.helper.extensions.dataadditionupdates.DataAdditionUpdates
import com.nicholas.rutherford.track.your.shot.shared.preference.create.CreateSharedPreferences
import com.nicholas.rutherford.track.your.shot.shared.preference.read.ReadSharedPreferences
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableSharedFlow
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

    private val dataAdditionUpdates = mockk<DataAdditionUpdates>(relaxed = true)

    private val playerRepository = mockk<PlayerRepository>(relaxed = true)

    private val createSharedPreferences = mockk<CreateSharedPreferences>(relaxed = true)
    private val readSharedPreferences = mockk<ReadSharedPreferences>(relaxed = true)

    private val emptyShotList: List<ShotLoggedWithPlayer> = listOf()

    @BeforeEach
    fun beforeEach() {
        viewModel = ShotsListViewModel(
            scope = scope,
            navigation = navigation,
            dataAdditionUpdates = dataAdditionUpdates,
            playerRepository = playerRepository,
            createSharedPreferences = createSharedPreferences,
            readSharedPreferences = readSharedPreferences
        )
    }

    @Nested
    inner class OnNavigatedTo {

        @Test
        fun `when fetch all players returns empty list should not update current array list or state`() = runTest {
            coEvery { playerRepository.fetchAllPlayers() } returns emptyList()

            viewModel.onNavigatedTo()

            Assertions.assertEquals("", viewModel.playerFilteredName)
            verify(exactly = 0) { createSharedPreferences.createPlayerFilterName(value = "") }
            Assertions.assertEquals(
                viewModel.shotListMutableStateFlow.value,
                ShotsListState(shotList = emptyList())
            )
            Assertions.assertEquals(
                viewModel.currentShotArrayList.toList(),
                emptyShotList
            )
        }

        @Test
        fun `when fetch all players returns info should update current array list and state`() = runTest {
            val player = TestPlayer().create()
            val playerId = 1

            coEvery { playerRepository.fetchAllPlayers() } returns listOf(player)
            coEvery { playerRepository.fetchPlayerIdByName(firstName = player.firstName, lastName = player.lastName) } returns playerId

            viewModel.onNavigatedTo()

            Assertions.assertEquals("", viewModel.playerFilteredName)
            verify(exactly = 0) { createSharedPreferences.createPlayerFilterName(value = "") }
            Assertions.assertEquals(
                viewModel.shotListMutableStateFlow.value,
                ShotsListState(shotList = listOf(ShotLoggedWithPlayer(shotLogged = player.shotsLoggedList.first(), playerId = playerId, playerName = player.fullName())))
            )
            Assertions.assertEquals(
                viewModel.currentShotArrayList.toList(),
                listOf(ShotLoggedWithPlayer(shotLogged = player.shotsLoggedList.first(), playerId = playerId, playerName = player.fullName()))
            )
        }

        @Test
        fun `when player filter name returns a value should update playerFilteredName`() = runTest {
            val playerFilteredName = "playerFilteredName"

            coEvery { playerRepository.fetchAllPlayers() } returns emptyList()
            every { readSharedPreferences.playerFilterName() } returns playerFilteredName

            viewModel.onNavigatedTo()

            Assertions.assertEquals(playerFilteredName, viewModel.playerFilteredName)
            verify { createSharedPreferences.createPlayerFilterName(value = "") }
            Assertions.assertEquals(
                viewModel.shotListMutableStateFlow.value,
                ShotsListState(shotList = emptyList())
            )
            Assertions.assertEquals(
                viewModel.currentShotArrayList.toList(),
                emptyShotList
            )
        }
    }

    @Nested
    inner class CollectShotHasBeenUpdatedSharedFlow {

        @Test
        fun `when shotHasBeenUpdatedSharedFlow returns back a flow of true should update state`() = runTest {
            val player = TestPlayer().create()
            val playerId = 1

            val shotHasBeenUpdatedSharedFlow = MutableSharedFlow<Boolean>(replay = Int.MAX_VALUE)
            shotHasBeenUpdatedSharedFlow.emit(value = true)

            coEvery { playerRepository.fetchAllPlayers() } returns listOf(player)
            coEvery { playerRepository.fetchPlayerIdByName(firstName = player.firstName, lastName = player.lastName) } returns playerId
            every { dataAdditionUpdates.shotHasBeenUpdatedSharedFlow } returns shotHasBeenUpdatedSharedFlow

            viewModel.collectShotHasBeenUpdatedSharedFlow()

            Assertions.assertEquals(
                viewModel.shotListMutableStateFlow.value,
                ShotsListState(shotList = listOf(ShotLoggedWithPlayer(shotLogged = player.shotsLoggedList.first(), playerId = playerId, playerName = player.fullName())))
            )
            Assertions.assertEquals(
                viewModel.currentShotArrayList.toList(),
                listOf(ShotLoggedWithPlayer(shotLogged = player.shotsLoggedList.first(), playerId = playerId, playerName = player.fullName()))
            )
            verify(exactly = 0) { navigation.popToPlayerList() }
        }

        @Test
        fun `when shotHasBeenUpdatedSharedFlow returns back a flow of false should not update state`() = runTest {
            val player = TestPlayer().create()
            val playerId = 1

            val shotHasBeenUpdatedSharedFlow = MutableSharedFlow<Boolean>(replay = Int.MAX_VALUE)
            shotHasBeenUpdatedSharedFlow.emit(value = false)

            coEvery { playerRepository.fetchAllPlayers() } returns listOf(player)
            coEvery { playerRepository.fetchPlayerIdByName(firstName = player.firstName, lastName = player.lastName) } returns playerId
            every { dataAdditionUpdates.shotHasBeenUpdatedSharedFlow } returns shotHasBeenUpdatedSharedFlow

            viewModel.collectShotHasBeenUpdatedSharedFlow()

            Assertions.assertEquals(
                viewModel.shotListMutableStateFlow.value,
                ShotsListState(shotList = emptyList())
            )
            Assertions.assertEquals(
                viewModel.currentShotArrayList.toList(),
                emptyShotList
            )
            verify(exactly = 0) { navigation.popToPlayerList() }
        }

        @Test
        fun `when currentShotArrayList is empty and player filtered name is not empty should pop to player list`() = runTest {
            val player = TestPlayer().create()
            val playerId = 1

            val shotHasBeenUpdatedSharedFlow = MutableSharedFlow<Boolean>(replay = Int.MAX_VALUE)
            shotHasBeenUpdatedSharedFlow.emit(value = false)

            coEvery { playerRepository.fetchAllPlayers() } returns listOf(player)
            coEvery { playerRepository.fetchPlayerIdByName(firstName = player.firstName, lastName = player.lastName) } returns playerId
            every { dataAdditionUpdates.shotHasBeenUpdatedSharedFlow } returns shotHasBeenUpdatedSharedFlow

            viewModel.currentShotArrayList = arrayListOf()
            viewModel.playerFilteredName = "filteredName"

            viewModel.collectShotHasBeenUpdatedSharedFlow()

            Assertions.assertEquals(
                viewModel.shotListMutableStateFlow.value,
                ShotsListState(shotList = emptyList())
            )
            Assertions.assertEquals(
                viewModel.currentShotArrayList.toList(),
                emptyShotList
            )
            verify { navigation.popToPlayerList() }
        }
    }

    @Nested
    inner class FilterShotList {

        val playerFilteredName = "PlayerA"
        val defaultShot = ShotLoggedWithPlayer(
            shotLogged = TestShotLogged.build(),
            playerId = 1,
            playerName = "playerName"
        )

        @Test
        fun `should return shots matching playerFilteredName`() {
            viewModel.playerFilteredName = playerFilteredName

            val result = viewModel.filterShotList(shotList = listOf(defaultShot, defaultShot.copy(playerName = "playerB"), defaultShot.copy(playerName = playerFilteredName)))

            Assertions.assertEquals(result.size, 1)
            Assertions.assertEquals(result, listOf(defaultShot.copy(playerName = playerFilteredName)))
        }

        @Test
        fun `should return empty list if no shots match the playerFilteredName`() {
            viewModel.playerFilteredName = playerFilteredName

            val result = viewModel.filterShotList(shotList = listOf(defaultShot, defaultShot.copy(playerName = "playerB"), defaultShot.copy(playerName = "test")))

            Assertions.assertEquals(result.size, 0)
            Assertions.assertEquals(result, emptyList<ShotLoggedWithPlayer>())
        }
    }

    @Nested
    inner class UpdateShotListState {
        val playerFilteredName = "Player A"

        @Test
        fun `when playerFilterName is empty should not filter shots and update state`() = runTest {
            val shotLoggedWithPlayerArrayList = arrayListOf(
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

            viewModel.playerFilteredName = ""

            viewModel.updateShotListState()

            Assertions.assertEquals(viewModel.currentShotArrayList, shotLoggedWithPlayerArrayList)
            Assertions.assertEquals(viewModel.shotListMutableStateFlow.value.shotList, shotLoggedWithPlayerArrayList)
        }

        @Test
        fun `when playerFilterName is not empty should filter shots and update state`() = runTest {
            val shotLoggedWithPlayer = ShotLoggedWithPlayer(
                shotLogged = TestPlayer().create().copy(firstName = "Player", lastName = "A").shotsLoggedList.first(),
                playerId = 4,
                playerName = playerFilteredName
            )
            coEvery { playerRepository.fetchAllPlayers() } returns listOf(
                TestPlayer().create().copy(firstName = "test", lastName = "first"),
                TestPlayer().create().copy(firstName = "test", lastName = "second"),
                TestPlayer().create().copy(firstName = "Player", lastName = "A")
            )
            coEvery { playerRepository.fetchPlayerIdByName(firstName = "test", lastName = "first") } returns 2
            coEvery { playerRepository.fetchPlayerIdByName(firstName = "test", lastName = "second") } returns 3
            coEvery { playerRepository.fetchPlayerIdByName(firstName = "Player", lastName = "A") } returns 4

            viewModel.playerFilteredName = playerFilteredName

            viewModel.updateShotListState()

            Assertions.assertEquals(viewModel.currentShotArrayList, arrayListOf(shotLoggedWithPlayer))
            Assertions.assertEquals(viewModel.shotListMutableStateFlow.value.shotList, arrayListOf(shotLoggedWithPlayer))
        }
    }

    @Nested
    inner class OnToolbarMenuClicked {

        @Test
        fun `when playerFilterName is empty should call openNavigationDrawer`() {
            viewModel.playerFilteredName = ""

            viewModel.onToolbarMenuClicked()

            verify { navigation.openNavigationDrawer() }
            verify(exactly = 0) { navigation.popToPlayerList() }

            Assertions.assertEquals(
                viewModel.shotListMutableStateFlow.value,
                ShotsListState(shotList = emptyList())
            )
            Assertions.assertEquals(
                viewModel.currentShotArrayList.toList(),
                emptyShotList
            )
        }

        @Test
        fun `when playerFilterName is not empty should call popToPlayerList`() {
            viewModel.playerFilteredName = "filteredName"

            viewModel.onToolbarMenuClicked()

            verify(exactly = 0) { navigation.openNavigationDrawer() }
            verify { navigation.popToPlayerList() }

            Assertions.assertEquals(
                viewModel.shotListMutableStateFlow.value,
                ShotsListState(shotList = emptyList())
            )
            Assertions.assertEquals(
                viewModel.currentShotArrayList.toList(),
                emptyShotList
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
            ShotsListState(shotList = emptyList())
        )
        Assertions.assertEquals(
            viewModel.currentShotArrayList.toList(),
            emptyShotList
        )
    }
}
