package com.nicholas.rutherford.track.your.shot.feature.reports.reportlist.reportlist

import android.app.Application
import com.nicholas.rutherford.track.your.shot.base.resources.StringsIds
import com.nicholas.rutherford.track.your.shot.data.room.repository.PlayerRepository
import com.nicholas.rutherford.track.your.shot.data.test.room.TestPlayer
import com.nicholas.rutherford.track.your.shot.feature.reports.reportlist.ReportListNavigation
import com.nicholas.rutherford.track.your.shot.feature.reports.reportlist.ReportListState
import com.nicholas.rutherford.track.your.shot.feature.reports.reportlist.ReportListViewModel
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

class ReportListViewModelTest {

    private lateinit var reportListViewModel: ReportListViewModel

    private var navigation = mockk<ReportListNavigation>(relaxed = true)
    private val application = mockk<Application>(relaxed = true)

    private val playerRepsitory = mockk<PlayerRepository>(relaxed = true)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val testDispatcher = UnconfinedTestDispatcher()

    private val scope = CoroutineScope(SupervisorJob() + testDispatcher)

    @BeforeEach
    fun beforeEach() {
        every { application.getString(StringsIds.createPlayerReport) } returns "Create Player Report"
        every { application.getString(StringsIds.viewCreatedReportTimes) } returns "View Create Report Times"

        reportListViewModel = ReportListViewModel(
            navigation = navigation,
            application = application,
            playerRepository = playerRepsitory,
            scope = scope
        )
    }

    @Test
    fun `init should update state`() {
        val state = reportListViewModel.reportListStateFlow.value

        Assertions.assertEquals(
            state,
            ReportListState(
                reportInfoList = listOf(
                    "Create Player Report",
                    "View Create Report Times"
                )
            )
        )
    }

    @Test
    fun `report list should return a list of titles`() {
        val result = reportListViewModel.reportList()

        Assertions.assertEquals(
            result,
            listOf(
                "Create Player Report",
                "View Create Report Times"
            )
        )
    }

    @Test
    fun `on toolbar menu clicked should call open navigation drawer`() {
        reportListViewModel.onToolbarMenuClicked()

        verify { navigation.openNavigationDrawer() }
    }

    @Test
    fun `settings help alert should return alert info`() {
        every { application.getString(StringsIds.reports) } returns "Reports"
        every { application.getString(StringsIds.reportsHelpDescription) } returns "On the reports page, you can create and view reports for player shots recorded within the app."
        every { application.getString(StringsIds.gotIt) } returns "Got It"

        val alert = reportListViewModel.settingsHelpAlert()

        Assertions.assertEquals(alert.title, "Reports")
        Assertions.assertEquals(alert.description, "On the reports page, you can create and view reports for player shots recorded within the app.")
        Assertions.assertEquals(alert.confirmButton!!.buttonText, "Got It")
    }

    @Test
    fun `on players added alert should alert info`() {
        every { application.getString(StringsIds.noPlayersCreated) } returns "No Players Created"
        every { application.getString(StringsIds.noPlayersCreatedDescription) } returns "No players have been created for your account yet. Please create at least one player and record at least one shot to generate a report of shots taken by the player."
        every { application.getString(StringsIds.gotIt) } returns "Got It"

        val alert = reportListViewModel.noPlayersAddedAlert()

        Assertions.assertEquals(alert.title, "No Players Created")
        Assertions.assertEquals(alert.description, "No players have been created for your account yet. Please create at least one player and record at least one shot to generate a report of shots taken by the player.")
        Assertions.assertEquals(alert.confirmButton!!.buttonText, "Got It")
    }

    @Test
    fun `no shots added for players alert should return alert info`() {
        every { application.getString(StringsIds.noShotsCreated) } returns "No Shots Created"
        every { application.getString(StringsIds.noShotsCreatedDescription) } returns "None of the players have any shots logged yet. Please log at least one shot for a player to generate a report."
        every { application.getString(StringsIds.gotIt) } returns "Got It"

        val alert = reportListViewModel.noShotsAddedForPlayersAlert()

        Assertions.assertEquals(alert.title, "No Shots Created")
        Assertions.assertEquals(alert.description, "None of the players have any shots logged yet. Please log at least one shot for a player to generate a report.")
        Assertions.assertEquals(alert.confirmButton!!.buttonText, "Got It")
    }

    @Test
    fun `on help clicked should navigate to alert`() {
        reportListViewModel.onHelpClicked()

        verify { navigation.alert(alert = any()) }
    }

    @Nested
    inner class OnReportItemClicked {

        @Test
        fun `when index is create report index and fetch all players returns back players with shots should call navigate to create player report`() = runTest {
            coEvery { playerRepsitory.fetchAllPlayers() } returns listOf(TestPlayer().create())

            reportListViewModel.onReportItemClicked(index = 0)

            verify { navigation.navigateToCreateReport() }
            verify(exactly = 0) { navigation.navigateToViewCreatedReportTimes() }
            verify(exactly = 0) { navigation.alert(alert = any()) }
        }

        @Test
        fun `when index is create report index and fetch all players returns back not players should call alert`() = runTest {
            coEvery { playerRepsitory.fetchAllPlayers() } returns emptyList()

            reportListViewModel.onReportItemClicked(index = 0)

            verify { navigation.alert(alert = any()) }
            verify(exactly = 0) { navigation.navigateToCreateReport() }
            verify(exactly = 0) { navigation.navigateToViewCreatedReportTimes() }
        }

        @Test
        fun `when index is create report index and fetch all players returns back players but no shots should call alert`() = runTest {
            coEvery { playerRepsitory.fetchAllPlayers() } returns listOf(TestPlayer().create().copy(shotsLoggedList = emptyList()))

            reportListViewModel.onReportItemClicked(index = 0)

            verify { navigation.alert(alert = any()) }
            verify(exactly = 0) { navigation.navigateToCreateReport() }
            verify(exactly = 0) { navigation.navigateToViewCreatedReportTimes() }
        }

        @Test
        fun `when index is not create report index should call navigate to view created report times`() {
            reportListViewModel.onReportItemClicked(index = 2)

            verify { navigation.navigateToViewCreatedReportTimes() }
            verify(exactly = 0) { navigation.navigateToCreateReport() }
            verify(exactly = 0) { navigation.alert(alert = any()) }
        }
    }
}
