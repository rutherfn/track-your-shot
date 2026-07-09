package com.nicholas.rutherford.track.your.shot.feature.statistics.playerstatistics

import android.app.Application
import androidx.lifecycle.SavedStateHandle
import com.nicholas.rutherford.track.your.shot.base.resources.StringsIds
import com.nicholas.rutherford.track.your.shot.data.room.repository.PlayerRepository
import com.nicholas.rutherford.track.your.shot.data.room.response.Player
import com.nicholas.rutherford.track.your.shot.data.room.response.PlayerPositions
import com.nicholas.rutherford.track.your.shot.data.room.response.ShotLogged
import com.nicholas.rutherford.track.your.shot.data.test.room.TestShotLogged
import com.nicholas.rutherford.track.your.shot.helper.extensions.parseDateValueToString
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
import org.junit.jupiter.api.Test
import java.util.Calendar

class PlayerStatisticsViewModelTest {

    private lateinit var viewModel: PlayerStatisticsViewModel

    private val application = mockk<Application>(relaxed = true)
    private val navigation = mockk<PlayerStatisticsNavigation>(relaxed = true)
    private val playerRepository = mockk<PlayerRepository>(relaxed = true)
    private val savedStateHandle = SavedStateHandle(mapOf("playerIdParam" to PLAYER_ID))

    @OptIn(ExperimentalCoroutinesApi::class)
    private val dispatcher = UnconfinedTestDispatcher()

    private val scope = CoroutineScope(SupervisorJob() + dispatcher)

    @BeforeEach
    fun beforeEach() {
        every { application.getString(StringsIds.all) } returns ALL_DATES_LABEL
        every { application.getString(StringsIds.make) } returns "Make"
        every { application.getString(StringsIds.miss) } returns "Miss"

        viewModel = PlayerStatisticsViewModel(
            savedStateHandle = savedStateHandle,
            application = application,
            scope = scope,
            navigation = navigation,
            playerRepository = playerRepository
        )
    }

    @Test
    fun `when player is not found should pop navigation`() = runTest {
        coEvery { playerRepository.fetchPlayerById(id = PLAYER_ID) } returns null

        viewModel.updateStatisticsForPlayer()

        verify { navigation.pop() }
    }

    @Test
    fun `when player has logged shots should update player statistics state`() = runTest {
        val player = buildPlayer(
            shotsLoggedList = listOf(
                buildShotLogged(
                    shotName = "Free Throws",
                    loggedDateMillis = JULY_FIRST_2026_MILLIS
                )
            )
        )

        coEvery { playerRepository.fetchPlayerById(id = PLAYER_ID) } returns player

        viewModel.updateStatisticsForPlayer()

        val state = viewModel.playerStatisticsStateFlow.value

        Assertions.assertFalse(state.hasNoLoggedShots)
        Assertions.assertEquals("John Doe", state.playerStatisticsSummary?.playerName)
        Assertions.assertEquals(1, state.loggedShotEntries.size)
        Assertions.assertEquals(ALL_DATES_LABEL, state.selectedDateFilter)
        Assertions.assertNotNull(state.chartInfo)
        Assertions.assertEquals(1, state.chartInfo?.entries?.size)
    }

    @Test
    fun `on date filter selected should filter chart entries by date`() = runTest {
        val julyFirstLabel = parseDateValueToString(JULY_FIRST_2026_MILLIS)
        val julyThirdLabel = parseDateValueToString(JULY_THIRD_2026_MILLIS)
        val player = buildPlayer(
            shotsLoggedList = listOf(
                buildShotLogged(
                    shotName = "Free Throws",
                    loggedDateMillis = JULY_FIRST_2026_MILLIS
                ),
                buildShotLogged(
                    shotName = "Three Pointers",
                    loggedDateMillis = JULY_THIRD_2026_MILLIS
                )
            )
        )

        coEvery { playerRepository.fetchPlayerById(id = PLAYER_ID) } returns player

        viewModel.updateStatisticsForPlayer()
        viewModel.onDateFilterSelected(filter = julyFirstLabel)

        val state = viewModel.playerStatisticsStateFlow.value

        Assertions.assertEquals(julyFirstLabel, state.selectedDateFilter)
        Assertions.assertEquals(1, state.chartInfo?.entries?.size)
        Assertions.assertEquals("Free Throws", state.chartInfo?.entries?.first()?.sessionLabel)
        Assertions.assertTrue(state.dateFilterOptions.contains(julyThirdLabel))
    }

    @Test
    fun `on toolbar menu clicked should pop navigation`() {
        viewModel.onToolbarMenuClicked()

        verify { navigation.pop() }
    }

    private fun buildPlayer(shotsLoggedList: List<ShotLogged>): Player {
        return Player(
            id = PLAYER_ID,
            firstName = "John",
            lastName = "Doe",
            position = PlayerPositions.PointGuard,
            firebaseKey = "firebaseKey",
            imageUrl = null,
            shotsLoggedList = shotsLoggedList
        )
    }

    private fun buildShotLogged(
        shotName: String,
        loggedDateMillis: Long
    ): ShotLogged {
        return TestShotLogged.build().copy(
            shotName = shotName,
            shotsLoggedMillisecondsValue = loggedDateMillis
        )
    }

    private companion object {
        const val PLAYER_ID = 1
        const val ALL_DATES_LABEL = "All"

        val JULY_FIRST_2026_MILLIS: Long = Calendar.getInstance().apply {
            set(2026, Calendar.JULY, 1, 12, 0, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis

        val JULY_THIRD_2026_MILLIS: Long = Calendar.getInstance().apply {
            set(2026, Calendar.JULY, 3, 12, 0, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis
    }
}
