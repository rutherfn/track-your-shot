package com.nicholas.rutherford.track.your.shot.feature.reports.reportlist

import com.nicholas.rutherford.track.your.shot.data.shared.alert.Alert
import com.nicholas.rutherford.track.your.shot.navigation.NavigationAction
import com.nicholas.rutherford.track.your.shot.navigation.NavigationActions
import com.nicholas.rutherford.track.your.shot.navigation.Navigator
import io.mockk.CapturingSlot
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ReportListNavigationImplTest {

    private lateinit var reportListNavigationImpl: ReportListNavigationImpl

    private var navigator = mockk<Navigator>(relaxed = true)

    @BeforeEach
    fun beforeEach() {
        reportListNavigationImpl = ReportListNavigationImpl(navigator = navigator)
    }

    @Test
    fun `alert action`() {
        val alert = Alert(title = "Title")

        reportListNavigationImpl.alert(alert = alert)

        verify { navigator.alert(alertAction = alert) }
    }

    @Test
    fun `open navigation drawer`() {
        reportListNavigationImpl.openNavigationDrawer()

        verify { navigator.showNavigationDrawer(navigationDrawerAction = true) }
    }

    @Test
    fun `navigate to create player report`() {
        val argumentCapture: CapturingSlot<NavigationAction> = slot()

        reportListNavigationImpl.navigateToCreatePlayerReport()

        verify { navigator.navigate(capture(argumentCapture)) }

        val capturedArgument = argumentCapture.captured
        val expectedAction = NavigationActions.ReportList.createPlayerReport()

        Assertions.assertEquals(expectedAction.destination, capturedArgument.destination)
    }

    @Test
    fun `navigate to view created report times`() {
        val argumentCapture: CapturingSlot<NavigationAction> = slot()

        reportListNavigationImpl.navigateToViewCreatedReportTimes()

        verify { navigator.navigate(capture(argumentCapture)) }

        val capturedArgument = argumentCapture.captured
        val expectedAction = NavigationActions.ReportList.viewReportTimes()

        Assertions.assertEquals(expectedAction.destination, capturedArgument.destination)
    }
}