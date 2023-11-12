package com.nicholas.rutherford.track.your.shot.players

import com.nicholas.rutherford.track.your.shot.data.room.repository.PlayerRepository
import com.nicholas.rutherford.track.your.shot.data.test.room.TestPlayer
import com.nicholas.rutherford.track.your.shot.feature.players.PlayersListNavigation
import com.nicholas.rutherford.track.your.shot.feature.players.PlayersListState
import com.nicholas.rutherford.track.your.shot.feature.players.PlayersListViewModel
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PlayersListViewModelTest {

    private lateinit var playersListViewModel: PlayersListViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    private val testDispatcher = UnconfinedTestDispatcher()

    private val scope = CoroutineScope(SupervisorJob() + testDispatcher)

    private val navigation = mockk< PlayersListNavigation>(relaxed = true)
    private val playerRepository = mockk<PlayerRepository>(relaxed = true)

    @BeforeEach
    fun beforeEach() {
        playersListViewModel = PlayersListViewModel(
            scope = scope,
            navigation = navigation,
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
    }

    @Test
    fun `on toolbar menu clicked`() {
        playersListViewModel.onToolbarMenuClicked()

        verify { navigation.openNavigationDrawer() }
    }
}