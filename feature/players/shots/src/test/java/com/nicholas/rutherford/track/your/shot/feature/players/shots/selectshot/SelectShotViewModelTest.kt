package com.nicholas.rutherford.track.your.shot.feature.players.shots.selectshot

import android.app.Application
import androidx.lifecycle.SavedStateHandle
import com.nicholas.rutherford.track.your.shot.base.resources.StringsIds
import com.nicholas.rutherford.track.your.shot.data.room.repository.DeclaredShotRepository
import com.nicholas.rutherford.track.your.shot.data.room.repository.PendingPlayerRepository
import com.nicholas.rutherford.track.your.shot.data.room.repository.PlayerRepository
import com.nicholas.rutherford.track.your.shot.data.room.response.DeclaredShot
import com.nicholas.rutherford.track.your.shot.data.shared.alert.Alert
import com.nicholas.rutherford.track.your.shot.data.shared.alert.AlertConfirmAndDismissButton
import com.nicholas.rutherford.track.your.shot.data.test.room.TestDeclaredShot
import com.nicholas.rutherford.track.your.shot.data.test.room.TestPlayer
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SelectShotViewModelTest {

    private lateinit var selectShotViewModel: SelectShotViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    private val testDispatcher = UnconfinedTestDispatcher()

    private var savedStateHandle = mockk<SavedStateHandle>(relaxed = true)

    private val application = mockk<Application>(relaxed = true)

    private val scope = CoroutineScope(SupervisorJob() + testDispatcher)

    private val navigation = mockk<SelectShotNavigation>(relaxed = true)

    private val declaredShotRepository = mockk<DeclaredShotRepository>(relaxed = true)

    private val playerRepository = mockk<PlayerRepository>(relaxed = true)
    private val pendingPlayerRepository = mockk<PendingPlayerRepository>(relaxed = true)

    @BeforeEach
    fun beforeEach() {
        every { savedStateHandle.get<Boolean>("isExistingPlayer") } returns true
        every { savedStateHandle.get<Int>("playerId") } returns 1
        selectShotViewModel = SelectShotViewModel(
            savedStateHandle = savedStateHandle,
            application = application,
            scope = scope,
            navigation = navigation,
            declaredShotRepository = declaredShotRepository,
            playerRepository = playerRepository,
            pendingPlayerRepository = pendingPlayerRepository
        )
    }

    @Test
    fun `update is existing player and player id should update internal fields`() {
        selectShotViewModel.updateIsExistingPlayerAndPlayerId()

        Assertions.assertEquals(selectShotViewModel.isExistingPlayer, true)
        Assertions.assertEquals(selectShotViewModel.playerId, 1)
    }

    @Test
    fun `fetch declared shots and update state state should update state property`() {
        val shotDeclaredList = listOf(TestDeclaredShot.build())

        coEvery { declaredShotRepository.fetchAllDeclaredShots() } returns shotDeclaredList

        selectShotViewModel.fetchDeclaredShotsAndUpdateState()

        Assertions.assertEquals(
            selectShotViewModel.selectShotMutableStateFlow.value,
            SelectShotState(declaredShotList = shotDeclaredList)
        )
        Assertions.assertEquals(
            selectShotViewModel.currentDeclaredShotArrayList.toList(),
            shotDeclaredList
        )
    }

    @Test
    fun `on search value changed should update state`() {
        val title = TestDeclaredShot.build().title
        val shotDeclaredList = listOf(TestDeclaredShot.build())

        coEvery { declaredShotRepository.fetchDeclaredShotsBySearchQuery(searchQuery = title) } returns shotDeclaredList

        selectShotViewModel.onSearchValueChanged(newSearchQuery = title)

        Assertions.assertEquals(
            selectShotViewModel.selectShotMutableStateFlow.value,
            SelectShotState(declaredShotList = shotDeclaredList)
        )
        Assertions.assertEquals(
            selectShotViewModel.currentDeclaredShotArrayList.toList(),
            shotDeclaredList
        )
    }

    @Nested
    inner class OnCancelIconClicked {

        @Test
        fun `when search query is not empty should update state`() {
            val searchQuery = "searchQuery"

            val shotDeclaredList = listOf(TestDeclaredShot.build())

            coEvery { declaredShotRepository.fetchAllDeclaredShots() } returns shotDeclaredList

            selectShotViewModel.onCancelIconClicked(query = searchQuery)

            Assertions.assertEquals(
                selectShotViewModel.selectShotMutableStateFlow.value,
                SelectShotState(declaredShotList = shotDeclaredList)
            )
            Assertions.assertEquals(
                selectShotViewModel.currentDeclaredShotArrayList.toList(),
                shotDeclaredList
            )
        }

        @Test
        fun `when search query is empty should not update state`() {
            val emptyShotDeclaredList: List<DeclaredShot> = emptyList()

            selectShotViewModel.onCancelIconClicked(query = "")

            Assertions.assertEquals(
                selectShotViewModel.selectShotMutableStateFlow.value,
                SelectShotState(declaredShotList = emptyShotDeclaredList)
            )
            Assertions.assertEquals(
                selectShotViewModel.currentDeclaredShotArrayList.toList(),
                emptyShotDeclaredList
            )
        }
    }

    @Nested
    inner class OnBackButtonClicked {

        @Test
        fun `when isExistingPlayer set to false should call pop from create player`() {
            selectShotViewModel.isExistingPlayer = false

            selectShotViewModel.onBackButtonClicked()

            verify { navigation.popFromCreatePlayer() }
        }

        @Test
        fun `when isExistingPlayer set to true should call pop from edit player`() {
            selectShotViewModel.isExistingPlayer = true

            selectShotViewModel.onBackButtonClicked()

            verify { navigation.popFromEditPlayer() }
        }
    }

    @Test
    fun `more info alert`() {
        every { application.getString(StringsIds.selectingAShot) } returns "Selecting A Shot"
        every { application.getString(StringsIds.gotIt) } returns "Got It"
        every { application.getString(StringsIds.chooseAShotToLogInfoDescription) } returns "Choose a shot to log its information. To view additional details about the shot, click \"Show More\"."

        val alert = Alert(
            title = "Selecting A Shot",
            dismissButton = AlertConfirmAndDismissButton(buttonText = "Got It"),
            description = "Choose a shot to log its information. To view additional details about the shot, click \"Show More\"."
        )

        Assertions.assertEquals(
            selectShotViewModel.moreInfoAlert(),
            alert
        )
    }

    @Test
    fun `on help icon clicked`() {
        every { application.getString(StringsIds.selectingAShot) } returns "Selecting A Shot"
        every { application.getString(StringsIds.gotIt) } returns "Got It"
        every { application.getString(StringsIds.chooseAShotToLogInfoDescription) } returns "Choose a shot to log its information. To view additional details about the shot, click \"Show More\"."

        val alert = Alert(
            title = "Selecting A Shot",
            dismissButton = AlertConfirmAndDismissButton(buttonText = "Got It"),
            description = "Choose a shot to log its information. To view additional details about the shot, click \"Show More\"."
        )

        selectShotViewModel.onHelpIconClicked()

        verify { navigation.alert(alert = alert) }
    }

    @Nested
    inner class DetermineShotId {

        @Test
        fun `when player shotLoggedList is not empty should return the size of list`() {
            val player = TestPlayer().create()

            Assertions.assertEquals(
                selectShotViewModel.determineShotId(player = player),
                1
            )
        }

        @Test
        fun `when player shotLoggedList is empty should default value of 0`() {
            val player = TestPlayer().create().copy(shotsLoggedList = emptyList())

            Assertions.assertEquals(
                selectShotViewModel.determineShotId(player = player),
                0
            )
        }
    }

    @Nested
    inner class LoggedShotId {
        private val playerId = 2
        private val player = TestPlayer().create()

        @Test
        fun `when isExistingPlayer is true and fetch player by id returns null should return default value of 0`() = runTest {
            coEvery { playerRepository.fetchPlayerById(id = playerId) } returns null

            Assertions.assertEquals(
                selectShotViewModel.loggedShotId(isExistingPlayer = true, playerId = playerId),
                0
            )
        }

        @Test
        fun `when isExistingPlayer is true and fetch player by id returns player should return player shotLoggedList size`() = runTest {
            coEvery { playerRepository.fetchPlayerById(id = playerId) } returns player

            Assertions.assertEquals(
                selectShotViewModel.loggedShotId(isExistingPlayer = true, playerId = playerId),
                player.shotsLoggedList.size
            )
        }

        @Test
        fun `when isExistingPlayer is set to false and fetch pending player by id returns null should return default value of 0`() = runTest {
            coEvery { pendingPlayerRepository.fetchPlayerById(id = playerId) } returns null

            Assertions.assertEquals(
                selectShotViewModel.loggedShotId(isExistingPlayer = false, playerId = playerId),
                0
            )
        }

        @Test
        fun `when isExistingPlayer is set to false and fetch pending player by id returns player should return player shotLoggedList size`() = runTest {
            coEvery { pendingPlayerRepository.fetchPlayerById(id = playerId) } returns player

            Assertions.assertEquals(
                selectShotViewModel.loggedShotId(isExistingPlayer = false, playerId = playerId),
                player.shotsLoggedList.size
            )
        }
    }

    @Nested
    inner class OnDeclaredShotItemClicked {

        @Test
        fun `when isExistingPlayer is set to null should not call navigateToLogShot`() = runTest {
            selectShotViewModel.isExistingPlayer = null
            selectShotViewModel.playerId = 2

            selectShotViewModel.onDeclaredShotItemClicked(shotType = 2)

            verify(exactly = 0) {
                navigation.navigateToLogShot(
                    isExistingPlayer = any(),
                    playerId = any(),
                    shotType = any(),
                    shotId = any(),
                    viewCurrentExistingShot = any(),
                    viewCurrentPendingShot = any(),
                    fromShotList = any()
                )
            }
        }

        @Test
        fun `when playerId is set to null should not call navigateToLogShot`() = runTest {
            selectShotViewModel.isExistingPlayer = false
            selectShotViewModel.playerId = null

            selectShotViewModel.onDeclaredShotItemClicked(shotType = 2)

            verify(exactly = 0) {
                navigation.navigateToLogShot(
                    isExistingPlayer = any(),
                    playerId = any(),
                    shotType = any(),
                    shotId = any(),
                    viewCurrentExistingShot = any(),
                    viewCurrentPendingShot = any(),
                    fromShotList = any()
                )
            }
        }

        @Test
        fun `when playerId and isExistingPlayer are not set to null should call navigateToLogShot`() = runTest {
            val playerId = 22
            val player = TestPlayer().create()

            coEvery { pendingPlayerRepository.fetchPlayerById(id = playerId) } returns player

            selectShotViewModel.isExistingPlayer = false
            selectShotViewModel.playerId = playerId

            selectShotViewModel.onDeclaredShotItemClicked(shotType = 2)

            verify {
                navigation.navigateToLogShot(
                    isExistingPlayer = false,
                    playerId = playerId,
                    shotType = 2,
                    shotId = player.shotsLoggedList.size,
                    viewCurrentExistingShot = false,
                    viewCurrentPendingShot = false,
                    fromShotList = any()
                )
            }
        }
    }
}
