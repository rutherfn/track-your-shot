package com.nicholas.rutherford.track.your.shot.feature.statistics

import android.app.Application
import com.nicholas.rutherford.track.your.shot.base.resources.StringsIds
import com.nicholas.rutherford.track.your.shot.data.room.repository.PlayerRepository
import com.nicholas.rutherford.track.your.shot.data.room.response.Player
import com.nicholas.rutherford.track.your.shot.data.room.response.PlayerPositions
import com.nicholas.rutherford.track.your.shot.data.room.response.ShotLogged
import com.nicholas.rutherford.track.your.shot.data.shared.alert.Alert
import com.nicholas.rutherford.track.your.shot.data.test.room.TestShotLogged
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class StatisticsViewModelTest {

    private lateinit var viewModel: StatisticsViewModel

    private val application = mockk<Application>(relaxed = true)
    private val navigation = mockk<StatisticsNavigation>(relaxed = true)
    private val playerRepository = mockk<PlayerRepository>(relaxed = true)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val dispatcher = UnconfinedTestDispatcher()

    private val scope = CoroutineScope(SupervisorJob() + dispatcher)

    @BeforeEach
    fun beforeEach() {
        every { application.getString(StringsIds.statistics) } returns "Statistics"
        every { application.getString(StringsIds.all) } returns "All"
        every { application.getString(StringsIds.trackYourProgressDescription) } returns "Track your progress description"
        every { application.getString(StringsIds.gotIt) } returns "Got it"

        viewModel = StatisticsViewModel(
            application = application,
            scope = scope,
            navigation = navigation,
            playerRepository = playerRepository
        )
    }

    @Test
    fun `when no players have logged shots should update empty state`() = runTest {
        coEvery { playerRepository.fetchAllPlayers() } returns emptyList()

        viewModel.updateStatisticsState()

        Assertions.assertTrue(viewModel.statisticsStateFlow.value.hasNoStatistics)
        Assertions.assertTrue(viewModel.statisticsStateFlow.value.playerStatistics.isEmpty())
    }

    @Test
    fun `when players have logged shots should update statistics state`() = runTest {
        val player = buildPlayer(
            firstName = "John",
            lastName = "Doe",
            shotsLoggedList = listOf(TestShotLogged.build())
        )

        coEvery { playerRepository.fetchAllPlayers() } returns listOf(player)

        viewModel.updateStatisticsState()

        val playerStatistics = viewModel.statisticsStateFlow.value.playerStatistics

        Assertions.assertFalse(viewModel.statisticsStateFlow.value.hasNoStatistics)
        Assertions.assertEquals(1, playerStatistics.size)
        Assertions.assertEquals("John Doe", playerStatistics.first().playerName)
        Assertions.assertEquals(TestShotLogged.build().shotsAttempted, playerStatistics.first().totalShotsAttempted)
        Assertions.assertEquals(TestShotLogged.build().shotsMade, playerStatistics.first().totalShotsMade)
        Assertions.assertEquals(TestShotLogged.build().shotsMissed, playerStatistics.first().totalShotsMissed)
        Assertions.assertEquals("All", viewModel.statisticsStateFlow.value.selectedPlayerFilter)
        Assertions.assertEquals(listOf("All", "John Doe"), viewModel.statisticsStateFlow.value.playerFilterOptions)
        Assertions.assertNotNull(viewModel.statisticsStateFlow.value.teamOverview)
    }

    @Test
    fun `on player filter selected should update displayed player statistics`() = runTest {
        val firstPlayer = buildPlayer(
            firstName = "John",
            lastName = "Doe",
            shotsLoggedList = listOf(TestShotLogged.build())
        )
        val secondPlayer = buildPlayer(
            firstName = "Jane",
            lastName = "Smith",
            shotsLoggedList = listOf(TestShotLogged.build())
        )

        coEvery { playerRepository.fetchAllPlayers() } returns listOf(firstPlayer, secondPlayer)

        viewModel.updateStatisticsState()
        viewModel.onPlayerFilterSelected(filter = "Jane Smith")

        val state = viewModel.statisticsStateFlow.value

        Assertions.assertEquals("Jane Smith", state.selectedPlayerFilter)
        Assertions.assertEquals(1, state.displayedPlayerStatistics.size)
        Assertions.assertEquals("Jane Smith", state.displayedPlayerStatistics.first().playerName)
        Assertions.assertFalse(state.isShowingAllPlayers)
    }

    @Test
    fun `on view detailed stats clicked should navigate to player detailed statistics`() {
        val playerStatisticsSummary = PlayerStatisticsSummary(
            playerName = "John Doe",
            totalShotsAttempted = 10,
            totalShotsMade = 7,
            totalShotsMissed = 3,
            overallMadePercentage = 70.0,
            loggedShotsCount = 1
        )

        viewModel.onViewDetailedStatsClicked(playerStatisticsSummary = playerStatisticsSummary)

        verify {
            navigation.navigateToPlayerDetailedStatistics(playerName = "John Doe")
        }
    }

    @Test
    fun `build team statistics overview should return null when no players exist`() {
        Assertions.assertNull(viewModel.buildTeamStatisticsOverview(playerStatistics = emptyList()))
    }

    @Test
    fun `build team statistics overview should aggregate player statistics`() {
        val overview = viewModel.buildTeamStatisticsOverview(
            playerStatistics = listOf(
                PlayerStatisticsSummary(
                    playerName = "John Doe",
                    totalShotsAttempted = 10,
                    totalShotsMade = 7,
                    totalShotsMissed = 3,
                    overallMadePercentage = 70.0,
                    loggedShotsCount = 1
                ),
                PlayerStatisticsSummary(
                    playerName = "Jane Smith",
                    totalShotsAttempted = 20,
                    totalShotsMade = 10,
                    totalShotsMissed = 10,
                    overallMadePercentage = 50.0,
                    loggedShotsCount = 2
                )
            )
        )

        Assertions.assertEquals(2, overview?.totalPlayers)
        Assertions.assertEquals(30, overview?.totalShotsAttempted)
        Assertions.assertEquals(17, overview?.totalShotsMade)
        Assertions.assertEquals(13, overview?.totalShotsMissed)
        Assertions.assertEquals(56.666666666666664, overview?.averageMadePercentage)
    }

    @Test
    fun `build player statistics summary should exclude pending shots`() {
        val pendingShot = TestShotLogged.build().copy(isPending = true)
        val finalizedShot = TestShotLogged.build()
        val player = buildPlayer(
            firstName = "Jane",
            lastName = "Smith",
            shotsLoggedList = listOf(pendingShot, finalizedShot)
        )

        val summary = viewModel.buildPlayerStatisticsSummary(player = player)

        Assertions.assertEquals(1, summary.loggedShotsCount)
        Assertions.assertEquals(finalizedShot.shotsAttempted, summary.totalShotsAttempted)
    }

    @Test
    fun `format percentage should return formatted value`() {
        Assertions.assertEquals("70.0%", viewModel.formatPercentage(value = 70.0))
    }

    @Test
    fun `on toolbar menu clicked should open navigation drawer`() {
        viewModel.onToolbarMenuClicked()

        verify { navigation.openNavigationDrawer() }
    }

    @Test
    fun `on help clicked should show statistics help alert`() {
        val alertSlot = slot<Alert>()

        viewModel.onHelpClicked()

        verify { navigation.alert(alert = capture(alertSlot)) }
        Assertions.assertEquals("Statistics", alertSlot.captured.title)
        Assertions.assertEquals("Track your progress description", alertSlot.captured.description)
    }

    private fun buildPlayer(
        firstName: String,
        lastName: String,
        shotsLoggedList: List<ShotLogged>
    ): Player {
        return Player(
            firstName = firstName,
            lastName = lastName,
            position = PlayerPositions.PointGuard,
            firebaseKey = "firebaseKey",
            imageUrl = null,
            shotsLoggedList = shotsLoggedList
        )
    }
}
