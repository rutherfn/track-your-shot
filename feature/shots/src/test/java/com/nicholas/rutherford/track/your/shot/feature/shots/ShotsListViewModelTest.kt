package com.nicholas.rutherford.track.your.shot.feature.shots

import com.nicholas.rutherford.track.your.shot.data.room.repository.PlayerRepository
import com.nicholas.rutherford.track.your.shot.data.room.response.fullName
import com.nicholas.rutherford.track.your.shot.data.test.room.TestPlayer
import com.nicholas.rutherford.track.your.shot.data.test.room.TestShotLogged
import com.nicholas.rutherford.track.your.shot.helper.extensions.dataadditionupdates.DataAdditionUpdates
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

    private val emptyShotList: List<ShotLoggedWithPlayer> = listOf()

    @BeforeEach
    fun beforeEach() {
        viewModel = ShotsListViewModel(
            scope = scope,
            navigation = navigation,
            dataAdditionUpdates = dataAdditionUpdates,
            playerRepository = playerRepository
        )
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
        }
    }

    @Nested
    inner class OnNavigatedTo {

        @Test
        fun `when fetch all players returns empty list should not update current array list or state`() = runTest {
            coEvery { playerRepository.fetchAllPlayers() } returns emptyList()

            viewModel.onNavigatedTo()

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

            Assertions.assertEquals(
                viewModel.shotListMutableStateFlow.value,
                ShotsListState(shotList = listOf(ShotLoggedWithPlayer(shotLogged = player.shotsLoggedList.first(), playerId = playerId, playerName = player.fullName())))
            )
            Assertions.assertEquals(
                viewModel.currentShotArrayList.toList(),
                listOf(ShotLoggedWithPlayer(shotLogged = player.shotsLoggedList.first(), playerId = playerId, playerName = player.fullName()))
            )
        }
    }

    @Test
    fun `on toolbar menu clicked`() {
        viewModel.onToolbarMenuClicked()

        verify { navigation.openNavigationDrawer() }

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
