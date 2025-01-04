package com.nicholas.rutherford.track.your.shot.feature.reports.reportlist

import android.app.Application
import com.nicholas.rutherford.track.your.shot.base.resources.StringsIds
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class ReportListViewModelTest {

    private lateinit var reportListViewModel: ReportListViewModel

    private var navigation = mockk<ReportListNavigation>(relaxed = true)
    private val application = mockk<Application>(relaxed = true)

    @BeforeEach
    fun beforeEach() {
        every { application.getString(StringsIds.createPlayerReport) } returns "Create Player Report"
        every { application.getString(StringsIds.viewCreatedReportTimes) } returns "View Create Report Times"

        reportListViewModel = ReportListViewModel(
            navigation = navigation,
            application = application
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
    fun `on help clicked should navigate to alert`() {
        reportListViewModel.onHelpClicked()

        verify { navigation.alert(alert = any()) }
    }

    @Nested
    inner class OnReportItemClicked {

        @Test
        fun `when index is create player report index should call navigate to create player report`() {
            reportListViewModel.onReportItemClicked(index = 0)

            verify { navigation.navigateToCreatePlayerReport() }
            verify(exactly = 0) { navigation.navigateToViewCreatedReportTimes() }
        }

        @Test
        fun `when index is not create player report index should call navigate to view created report times`() {
            reportListViewModel.onReportItemClicked(index = 2)

            verify { navigation.navigateToViewCreatedReportTimes() }
            verify(exactly = 0) { navigation.navigateToCreatePlayerReport() }
        }
    }
}
