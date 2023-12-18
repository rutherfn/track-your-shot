package com.nicholas.rutherford.track.your.shot.players.playerlist

import android.app.Application
import com.nicholas.rutherford.track.your.shot.data.room.repository.ActiveUserRepository
import com.nicholas.rutherford.track.your.shot.data.room.repository.PlayerRepository
import com.nicholas.rutherford.track.your.shot.data.room.response.Player
import com.nicholas.rutherford.track.your.shot.data.room.response.PlayerPositions
import com.nicholas.rutherford.track.your.shot.data.test.room.TestActiveUser
import com.nicholas.rutherford.track.your.shot.data.test.room.TestPlayer
import com.nicholas.rutherford.track.your.shot.feature.players.PlayersAdditionUpdates
import com.nicholas.rutherford.track.your.shot.feature.players.playerlist.PlayersListNavigation
import com.nicholas.rutherford.track.your.shot.feature.players.playerlist.PlayersListState
import com.nicholas.rutherford.track.your.shot.feature.players.playerlist.PlayersListViewModel
import com.nicholas.rutherford.track.your.shot.firebase.core.delete.DeleteFirebaseUserInfo
import com.nicholas.rutherford.track.your.shot.helper.account.AccountAuthManager
import com.nicholas.rutherford.track.your.shot.helper.network.Network
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
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

    private val accountAuthManager = mockk<AccountAuthManager>(relaxed = true)

    private val deleteFirebaseUserInfo = mockk<DeleteFirebaseUserInfo>(relaxed = true)
    private val activeUserRepository = mockk<ActiveUserRepository>(relaxed = true)

    private val playersAdditionUpdates = mockk<PlayersAdditionUpdates>(relaxed = true)

    private val playerRepository = mockk<PlayerRepository>(relaxed = true)

    val user = TestActiveUser().create()
    val player = TestPlayer().create()

    val emptyPlayerList: List<Player> = listOf()

    @BeforeEach
    fun beforeEach() {
        playersListViewModel = PlayersListViewModel(
            application = application,
            scope = scope,
            navigation = navigation,
            network = network,
            accountAuthManager = accountAuthManager,
            deleteFirebaseUserInfo = deleteFirebaseUserInfo,
            activeUserRepository = activeUserRepository,
            playersAdditionUpdates = playersAdditionUpdates,
            playerRepository = playerRepository
        )
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
        fun `when newPlayerAddedStateFlow emits a null value should not update playerList state value`() = runTest {
            val playerList: List<Player> = emptyList()

            every { playersAdditionUpdates.newPlayerAddedStateFlow } returns MutableStateFlow<Player?>(null)

            playersListViewModel = PlayersListViewModel(
                application = application,
                scope = scope,
                navigation = navigation,
                network = network,
                accountAuthManager = accountAuthManager,
                deleteFirebaseUserInfo = deleteFirebaseUserInfo,
                activeUserRepository = activeUserRepository,
                playersAdditionUpdates = playersAdditionUpdates,
                playerRepository = playerRepository
            )

            Assertions.assertEquals(
                playersListViewModel.playerListMutableStateFlow.value,
                PlayersListState(playerList = playerList)
            )
            Assertions.assertEquals(
                playersListViewModel.currentPlayerArrayList.toList(),
                playerList
            )
        }

        @Test
        fun `when newPlayerAddedStateFlow emits a new player should update list and state`() = runTest {
            val player = Player(
                firstName = "first1",
                lastName = "last1",
                position = PlayerPositions.Center,
                firebaseKey = "key",
                imageUrl = "url"
            )
            val playerList = listOf(player)

            every { playersAdditionUpdates.newPlayerAddedStateFlow } returns MutableStateFlow<Player?>(player)

            playersListViewModel = PlayersListViewModel(
                application = application,
                scope = scope,
                navigation = navigation,
                network = network,
                accountAuthManager = accountAuthManager,
                deleteFirebaseUserInfo = deleteFirebaseUserInfo,
                activeUserRepository = activeUserRepository,
                playersAdditionUpdates = playersAdditionUpdates,
                playerRepository = playerRepository
            )

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
    inner class CollectLoggedInPlayerListStateFlow {

        @Test
        fun `when loggedInPlayerListStateFlow emits a empty list should not update state or list`() = runTest {
            val playerList: List<Player> = emptyList()

            every { accountAuthManager.loggedInPlayerListStateFlow } returns MutableStateFlow(playerList)

            playersListViewModel = PlayersListViewModel(
                application = application,
                scope = scope,
                navigation = navigation,
                network = network,
                accountAuthManager = accountAuthManager,
                deleteFirebaseUserInfo = deleteFirebaseUserInfo,
                activeUserRepository = activeUserRepository,
                playersAdditionUpdates = playersAdditionUpdates,
                playerRepository = playerRepository
            )

            Assertions.assertEquals(
                playersListViewModel.playerListMutableStateFlow.value,
                PlayersListState(playerList = playerList)
            )
            Assertions.assertEquals(
                playersListViewModel.currentPlayerArrayList.toList(),
                playerList
            )
        }

        @Test
        fun `when loggedInPlayerListStateFlow emits a list should update state and list`() = runTest {
            val player = Player(
                firstName = "first1",
                lastName = "last1",
                position = PlayerPositions.Center,
                firebaseKey = "key",
                imageUrl = "url"
            )
            val playerList = listOf(player)

            every { accountAuthManager.loggedInPlayerListStateFlow } returns MutableStateFlow(playerList)

            playersListViewModel = PlayersListViewModel(
                application = application,
                scope = scope,
                navigation = navigation,
                network = network,
                accountAuthManager = accountAuthManager,
                deleteFirebaseUserInfo = deleteFirebaseUserInfo,
                activeUserRepository = activeUserRepository,
                playersAdditionUpdates = playersAdditionUpdates,
                playerRepository = playerRepository
            )

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
    inner class HandlePlayerAdded {

        @Test
        fun `when currentPlayerArrayList contains a player should not update state or list`() {
            val playerList = listOf(player)

            playersListViewModel.currentPlayerArrayList = arrayListOf(player)
            playersListViewModel.playerListMutableStateFlow.value =
                PlayersListState(playerList = listOf(player))

            playersListViewModel.handlePlayerAdded(player = player)

            Assertions.assertEquals(
                playersListViewModel.playerListMutableStateFlow.value,
                PlayersListState(playerList = playerList)
            )
            Assertions.assertEquals(
                playersListViewModel.currentPlayerArrayList.toList(),
                playerList
            )
        }

        @Test
        fun `when currentPlayerList does not contain player should update state and list`() {
            val newPlayer = Player(
                firstName = "first1",
                lastName = "last1",
                position = PlayerPositions.Center,
                firebaseKey = "key",
                imageUrl = "url"
            )
            val playerList = listOf(newPlayer)

            playersListViewModel.handlePlayerAdded(player = newPlayer)

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
    inner class HandleLoggedInPlayerList {
        val newPlayer = Player(
            firstName = "first1",
            lastName = "last1",
            position = PlayerPositions.Center,
            firebaseKey = "key",
            imageUrl = "url"
        )

        @Test
        fun `when player list is empty should not update state and list`() = runTest {
            val playerList: List<Player> = emptyList()

            playersListViewModel.currentPlayerArrayList = arrayListOf()
            playersListViewModel.playerListMutableStateFlow.value =
                PlayersListState(playerList = playerList)

            playersListViewModel.handleLoggedInPlayerList(playerList = playerList)

            Assertions.assertEquals(
                playersListViewModel.playerListMutableStateFlow.value,
                PlayersListState(playerList = playerList)
            )
            Assertions.assertEquals(
                playersListViewModel.currentPlayerArrayList.toList(),
                playerList
            )
        }

        @Test
        fun `when current state player list is not empty the passed in list should not update state and list`() = runTest {
            val playerList: List<Player> = listOf(player)
            val newPlayerList: List<Player> = listOf(newPlayer)

            playersListViewModel.currentPlayerArrayList = arrayListOf(player)
            playersListViewModel.playerListMutableStateFlow.value =
                PlayersListState(playerList = playerList)

            playersListViewModel.handleLoggedInPlayerList(playerList = newPlayerList)

            Assertions.assertEquals(
                playersListViewModel.playerListMutableStateFlow.value,
                PlayersListState(playerList = playerList)
            )
            Assertions.assertEquals(
                playersListViewModel.currentPlayerArrayList.toList(),
                playerList
            )
        }

        @Test
        fun `when player list is not empty and current player list state is empty should update state and list`() = runTest {
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

        verify { navigation.navigateToCreatePlayer() }

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
        @Test
        fun `when device is connected to internet returns false should show alert`() = runTest {
//            val expectedAlert = Alert(
//                title = application.getString(StringsIds.empty),
//                description = application.getString(StringsIds.notConnectedToInternet),
//                dismissButton = AlertConfirmAndDismissButton(buttonText = application.getString(StringsIds.gotIt))
//            )
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
            verify { navigation.alert(alert = any()) }
        }

        @Test
        fun `when device is connected to internet returns true, and active user returns null should show alert`() = runTest {
//            val expectedAlert = Alert(
//                title = application.getString(StringsIds.empty),
//                description = application.getString(StringsIds.weHaveDetectedAProblemWithYourAccountPleaseContactSupportToResolveIssue),
//                dismissButton = AlertConfirmAndDismissButton(buttonText = application.getString(StringsIds.gotIt))
//            )
            coEvery { network.isDeviceConnectedToInternet() } returns true
            coEvery { activeUserRepository.fetchActiveUser() } returns null

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
            verify { navigation.alert(alert = any()) }
        }

        @Test
        fun `when device is connected to internet returns false, active user returns user, and delete player was not sucessful should show alert`() = runTest {
//            val expectedAlert = Alert(
//                title = application.getString(StringsIds.empty),
//                description = application.getString(StringsIds.unableToDeletePlayerPleaseContactSupport),
//                dismissButton = AlertConfirmAndDismissButton(buttonText = application.getString(StringsIds.gotIt))
//            )
            coEvery { network.isDeviceConnectedToInternet() } returns true
            coEvery { activeUserRepository.fetchActiveUser() } returns user
            coEvery { deleteFirebaseUserInfo.deletePlayer(user.firebaseAccountInfoKey!!, player.firebaseKey) } returns flowOf(false)

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
            verify { navigation.alert(alert = any()) }
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
            coEvery { activeUserRepository.fetchActiveUser() } returns user
            coEvery { deleteFirebaseUserInfo.deletePlayer(user.firebaseAccountInfoKey!!, player.firebaseKey) } returns flowOf(true)

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
}
