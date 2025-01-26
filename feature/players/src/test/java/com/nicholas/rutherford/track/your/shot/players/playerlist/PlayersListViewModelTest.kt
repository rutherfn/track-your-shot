package com.nicholas.rutherford.track.your.shot.players.playerlist

import android.app.Application
import com.nicholas.rutherford.track.your.shot.base.resources.StringsIds
import com.nicholas.rutherford.track.your.shot.data.room.repository.PendingPlayerRepository
import com.nicholas.rutherford.track.your.shot.data.room.repository.PlayerRepository
import com.nicholas.rutherford.track.your.shot.data.room.response.Player
import com.nicholas.rutherford.track.your.shot.data.room.response.PlayerPositions
import com.nicholas.rutherford.track.your.shot.data.shared.alert.Alert
import com.nicholas.rutherford.track.your.shot.data.shared.alert.AlertConfirmAndDismissButton
import com.nicholas.rutherford.track.your.shot.data.test.room.TestPlayer
import com.nicholas.rutherford.track.your.shot.feature.players.playerlist.DELETE_PLAYER_DELAY_IN_MILLIS
import com.nicholas.rutherford.track.your.shot.feature.players.playerlist.EDIT_PLAYER_OPTION_INDEX
import com.nicholas.rutherford.track.your.shot.feature.players.playerlist.PlayersListNavigation
import com.nicholas.rutherford.track.your.shot.feature.players.playerlist.PlayersListState
import com.nicholas.rutherford.track.your.shot.feature.players.playerlist.PlayersListViewModel
import com.nicholas.rutherford.track.your.shot.firebase.core.delete.DeleteFirebaseUserInfo
import com.nicholas.rutherford.track.your.shot.helper.extensions.dataadditionupdates.DataAdditionUpdates
import com.nicholas.rutherford.track.your.shot.helper.network.Network
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PlayersListViewModelTest {

    private lateinit var playersListViewModel: PlayersListViewModel

    private val application = mockk<Application>(relaxed = true)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val testDispatcher = UnconfinedTestDispatcher()

    private val scope = CoroutineScope(SupervisorJob() + testDispatcher)

    private val navigation = mockk<PlayersListNavigation>(relaxed = true)

    private val network = mockk<Network>(relaxed = true)

    private val deleteFirebaseUserInfo = mockk<DeleteFirebaseUserInfo>(relaxed = true)

    private val dataAdditionUpdates = mockk<DataAdditionUpdates>(relaxed = true)

    private val playerRepository = mockk<PlayerRepository>(relaxed = true)
    private val pendingPlayerRepository = mockk<PendingPlayerRepository>(relaxed = true)

    val player = TestPlayer().create()

    val emptyPlayerList: List<Player> = listOf()

    @BeforeEach
    fun beforeEach() {
        playersListViewModel = PlayersListViewModel(
            application = application,
            scope = scope,
            navigation = navigation,
            network = network,
            deleteFirebaseUserInfo = deleteFirebaseUserInfo,
            dataAdditionUpdates = dataAdditionUpdates,
            playerRepository = playerRepository,
            pendingPlayerRepository = pendingPlayerRepository
        )
    }

    @Test
    fun `constants for player list`() {
        Assertions.assertEquals(DELETE_PLAYER_DELAY_IN_MILLIS, 2000L)
        Assertions.assertEquals(EDIT_PLAYER_OPTION_INDEX, 0)
    }

    @Test
    fun `update player info list state should update state property`() {
        val playerList = listOf(TestPlayer().create())

        coEvery { playerRepository.fetchAllPlayers() } returns playerList

        playersListViewModel.updatePlayerListState()

        Assertions.assertEquals(
            playersListViewModel.playerListMutableStateFlow.value,
            PlayersListState(playerList = playerList)
        )
        Assertions.assertEquals(
            playersListViewModel.currentPlayerArrayList.toList(),
            playerList
        )
    }

    @Nested
    inner class CollectPlayerAdditionUpdates {

        @Test
        fun `when newPlayerHasBeenAddedSharedFlow emits a false value should not update state`() = runTest {
            val playerList = listOf(TestPlayer().create())
            val currentPlayerArrayList: ArrayList<Player> = arrayListOf()

            val newPlayerHasBeenAddedSharedFlow = MutableSharedFlow<Boolean>(replay = Int.MAX_VALUE)
            newPlayerHasBeenAddedSharedFlow.emit(value = false)

            coEvery { playerRepository.fetchAllPlayers() } returns playerList
            every { dataAdditionUpdates.newPlayerHasBeenAddedSharedFlow } returns newPlayerHasBeenAddedSharedFlow

            playersListViewModel.collectPlayerAdditionUpdates()

            Assertions.assertEquals(
                playersListViewModel.playerListMutableStateFlow.value,
                PlayersListState(playerList = emptyList())
            )
            Assertions.assertEquals(
                playersListViewModel.currentPlayerArrayList.toList(),
                currentPlayerArrayList
            )
        }

        @Test
        fun `when newPlayerHasBeenAddedSharedFlow emits a true value should update state`() = runTest {
            val playerList = listOf(TestPlayer().create())

            val newPlayerHasBeenAddedSharedFlow = MutableSharedFlow<Boolean>(replay = Int.MAX_VALUE)
            newPlayerHasBeenAddedSharedFlow.emit(value = true)

            coEvery { playerRepository.fetchAllPlayers() } returns playerList
            every { dataAdditionUpdates.newPlayerHasBeenAddedSharedFlow } returns newPlayerHasBeenAddedSharedFlow

            playersListViewModel.collectPlayerAdditionUpdates()

            Assertions.assertEquals(
                playersListViewModel.playerListMutableStateFlow.value,
                PlayersListState(playerList = playerList)
            )
            Assertions.assertEquals(
                playersListViewModel.currentPlayerArrayList.toList(),
                playerList
            )
        }
    }

    @Nested
    inner class DeleteAllNonEmptyPendingPlayers {

        @Test
        fun `when fetch all pending players returns a empty list should not call delete all pending players`() {
            val players: List<Player> = emptyList()

            coEvery { pendingPlayerRepository.fetchAllPendingPlayers() } returns players

            playersListViewModel.deleteAllNonEmptyPendingPlayers()

            coVerify(exactly = 0) { pendingPlayerRepository.deleteAllPendingPlayers() }
        }

        @Test
        fun `when fetch all pending players returns a player list should call delete all pending players`() {
            val players = listOf(player)

            coEvery { pendingPlayerRepository.fetchAllPendingPlayers() } returns players

            playersListViewModel.deleteAllNonEmptyPendingPlayers()

            coVerify(exactly = 1) { pendingPlayerRepository.deleteAllPendingPlayers() }
        }
    }

    @Nested
    inner class ShouldUpdateFromUserLoggedIn {

        @Test
        fun `when loggedInPlayerList is set to a empty list should return false`() {
            val result = playersListViewModel.shouldUpdateFromUserLoggedIn(
                loggedInPlayerList = emptyPlayerList,
                shouldUpdateLoggedInPlayerListState = true
            )
            Assertions.assertEquals(result, false)
        }

        @Test
        fun `when shouldUpdateLoggedInPlayerListState is set to false should return false`() {
            val playerList = listOf(player)

            val result = playersListViewModel.shouldUpdateFromUserLoggedIn(
                loggedInPlayerList = playerList,
                shouldUpdateLoggedInPlayerListState = false
            )
            Assertions.assertEquals(result, false)
        }

        @Test
        fun `when all conditions are met should return true`() {
            val playerList = listOf(player)

            val result = playersListViewModel.shouldUpdateFromUserLoggedIn(
                loggedInPlayerList = playerList,
                shouldUpdateLoggedInPlayerListState = true
            )
            Assertions.assertEquals(result, true)
        }
    }

    @Test
    fun `HandleLoggedInPlayerList should clear out given list and update state`() = runTest {
        val newPlayer = Player(
            firstName = "first1",
            lastName = "last1",
            position = PlayerPositions.Center,
            firebaseKey = "key",
            imageUrl = "url",
            shotsLoggedList = emptyList()
        )
        val emptyPlayerList: List<Player> = emptyList()
        val newPlayerList: List<Player> = listOf(newPlayer)

        playersListViewModel.currentPlayerArrayList = arrayListOf()
        playersListViewModel.playerListMutableStateFlow.value =
            PlayersListState(playerList = emptyPlayerList)

        playersListViewModel.handleLoggedInPlayerList(playerList = newPlayerList)

        Assertions.assertEquals(
            playersListViewModel.playerListMutableStateFlow.value,
            PlayersListState(playerList = newPlayerList)
        )
        Assertions.assertEquals(
            playersListViewModel.currentPlayerArrayList.toList(),
            newPlayerList
        )
    }

    @Test
    fun `on toolbar menu clicked`() {
        playersListViewModel.onToolbarMenuClicked()

        verify { navigation.openNavigationDrawer() }

        Assertions.assertEquals(
            playersListViewModel.playerListMutableStateFlow.value,
            PlayersListState(playerList = emptyList())
        )
        Assertions.assertEquals(
            playersListViewModel.currentPlayerArrayList.toList(),
            emptyPlayerList
        )
    }

    @Test
    fun `on add player clicked`() {
        playersListViewModel.onAddPlayerClicked()

        verify { navigation.navigateToCreateEditPlayer(firstName = null, lastName = null) }

        Assertions.assertEquals(
            playersListViewModel.playerListMutableStateFlow.value,
            PlayersListState(playerList = emptyList())
        )
        Assertions.assertEquals(
            playersListViewModel.currentPlayerArrayList.toList(),
            emptyPlayerList
        )
    }

    @Test
    fun `on yes delete player clicked should call expected function`() = runTest {
        coEvery { network.isDeviceConnectedToInternet() } returns false

        playersListViewModel.onYesDeletePlayerClicked(player = player)

        Assertions.assertEquals(
            playersListViewModel.playerListMutableStateFlow.value,
            PlayersListState(playerList = emptyList())
        )
        Assertions.assertEquals(
            playersListViewModel.currentPlayerArrayList.toList(),
            emptyPlayerList
        )
        coVerify { playersListViewModel.enableProgressAndDelay() }
        coVerify { playersListViewModel.deletePlayer(player = player) }
        verify { navigation.disableProgress() }
        verify { navigation.alert(alert = any()) }
    }

    @Test
    fun `enable progress and delay should call enable progress`() = runTest {
        playersListViewModel.enableProgressAndDelay()

        Assertions.assertEquals(
            playersListViewModel.playerListMutableStateFlow.value,
            PlayersListState(playerList = emptyList())
        )
        Assertions.assertEquals(
            playersListViewModel.currentPlayerArrayList.toList(),
            emptyPlayerList
        )
        verify { navigation.enableProgress(progress = any()) }
    }

    @Nested
    inner class DeletePlayer {
        private val notConnectedToInternet = "Not connected to internet"
        private val weHaveDetectedCurrentlyNotConnectedToInternetDescription = "We have detected currently not connected to internet. Please connect to service, and try again."
        private val gotIt = "Got It"
        private val empty = ""
        private val unableToDeletePlayerPleaseContactSupport = "Unable to delete player. Please contact support."
        private val weHaveDetectedAProblemWithYourAccountPleaseContactSupportToResolveIssue = "We have detected a problem with your account. Please contact support to resolve issue."

        @BeforeEach
        fun beforeEach() {
            every { application.getString(StringsIds.notConnectedToInternet) } returns notConnectedToInternet
            every { application.getString(StringsIds.weHaveDetectedCurrentlyNotConnectedToInternetDescription) } returns weHaveDetectedCurrentlyNotConnectedToInternetDescription
            every { application.getString(StringsIds.gotIt) } returns gotIt
            every { application.getString(StringsIds.empty) } returns empty
            every { application.getString(StringsIds.unableToDeletePlayerPleaseContactSupport) } returns unableToDeletePlayerPleaseContactSupport
            every { application.getString(StringsIds.weHaveDetectedAProblemWithYourAccountPleaseContactSupportToResolveIssue) } returns weHaveDetectedAProblemWithYourAccountPleaseContactSupportToResolveIssue
        }

        @Test
        fun `when device is connected to internet returns false should show alert`() = runTest {
            val expectedAlert = Alert(
                title = notConnectedToInternet,
                description = weHaveDetectedCurrentlyNotConnectedToInternetDescription,
                dismissButton = AlertConfirmAndDismissButton(buttonText = gotIt)
            )
            coEvery { network.isDeviceConnectedToInternet() } returns false

            playersListViewModel.deletePlayer(player = player)

            Assertions.assertEquals(
                playersListViewModel.playerListMutableStateFlow.value,
                PlayersListState(playerList = emptyList())
            )
            Assertions.assertEquals(
                playersListViewModel.currentPlayerArrayList.toList(),
                emptyPlayerList
            )
            verify { navigation.disableProgress() }
            verify { navigation.alert(alert = expectedAlert) }
        }

        @Test
        fun `when device is connected to internet returns true  and delete player was not successful should show alert`() = runTest {
            val expectedAlert = Alert(
                title = empty,
                description = unableToDeletePlayerPleaseContactSupport,
                dismissButton = AlertConfirmAndDismissButton(buttonText = gotIt)
            )
            coEvery { network.isDeviceConnectedToInternet() } returns true
            coEvery { deleteFirebaseUserInfo.deletePlayer(player.firebaseKey) } returns flowOf(false)

            playersListViewModel.deletePlayer(player = player)

            Assertions.assertEquals(
                playersListViewModel.playerListMutableStateFlow.value,
                PlayersListState(playerList = emptyList())
            )
            Assertions.assertEquals(
                playersListViewModel.currentPlayerArrayList.toList(),
                emptyPlayerList
            )
            verify { navigation.disableProgress() }
            verify { navigation.alert(alert = expectedAlert) }
        }

        @Test
        fun `when all conditions are met should delete player`() = runTest {
            playersListViewModel.playerListMutableStateFlow.value = PlayersListState(playerList = listOf(player))
            playersListViewModel.currentPlayerArrayList = arrayListOf(player)

            Assertions.assertEquals(
                playersListViewModel.playerListMutableStateFlow.value,
                PlayersListState(playerList = listOf(player))
            )
            Assertions.assertEquals(
                playersListViewModel.currentPlayerArrayList.toList(),
                listOf(player)
            )

            coEvery { network.isDeviceConnectedToInternet() } returns true
            coEvery { deleteFirebaseUserInfo.deletePlayer(player.firebaseKey) } returns flowOf(true)

            playersListViewModel.deletePlayer(player = player)

            Assertions.assertEquals(
                playersListViewModel.playerListMutableStateFlow.value,
                PlayersListState(playerList = emptyList())
            )
            Assertions.assertEquals(
                playersListViewModel.currentPlayerArrayList.toList(),
                emptyPlayerList
            )

            coVerify { playerRepository.deletePlayerByName(player.firstName, player.lastName) }
            verify { navigation.disableProgress() }
        }
    }

    @Test
    fun `on player clicked should update state`() {
        val player = TestPlayer().create()

        playersListViewModel.onPlayerClicked(player = player)

        val result = playersListViewModel.playerListMutableStateFlow.value

        Assertions.assertEquals(result, PlayersListState(selectedPlayer = player))
    }

    @Nested
    inner class OnSheetItemClicked {

        @Test
        fun `when index passed in is set to 0 should call on edit player clicked`() {
            val index = 0
            val player = TestPlayer().create()

            playersListViewModel.playerListMutableStateFlow.update { it.copy(selectedPlayer = player) }

            playersListViewModel.onSheetItemClicked(index = index)

            verify(exactly = 1) { playersListViewModel.onEditPlayerClicked(player = player) }
            verify(exactly = 0) { playersListViewModel.onDeletePlayerClicked(player = player) }
        }
    }

    @Test
    fun `on edit player clicked`() {
        playersListViewModel.onEditPlayerClicked(player = player)

        verify { navigation.navigateToCreateEditPlayer(firstName = player.firstName, lastName = player.lastName) }
    }

    @Test
    fun `on delete player clicked`() {
        playersListViewModel.onDeletePlayerClicked(player = player)

        verify { navigation.alert(alert = any()) }
    }
}
