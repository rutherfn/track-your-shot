package com.nicholas.rutherford.track.your.shot.feature.reports.reportlist.viewplayerreports

import com.nicholas.rutherford.track.your.shot.data.shared.alert.Alert
import com.nicholas.rutherford.track.your.shot.data.shared.progress.Progress
import com.nicholas.rutherford.track.your.shot.feature.reports.viewplayerreports.ViewPlayerReportsNavigationImpl
import com.nicholas.rutherford.track.your.shot.helper.constants.Constants
import com.nicholas.rutherford.track.your.shot.navigation.Navigator
import io.mockk.CapturingSlot
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ViewPlayerReportsNavigationImplTest {

    private lateinit var viewPlayerReportsNavigationImpl: ViewPlayerReportsNavigationImpl

    private var navigator = mockk<Navigator>(relaxed = true)

    @BeforeEach
    fun beforeEach() {
        viewPlayerReportsNavigationImpl = ViewPlayerReportsNavigationImpl(navigator = navigator)
    }

    @Test
    fun `alert action`() {
        val alert = Alert(title = "title")

        viewPlayerReportsNavigationImpl.alert(alert = alert)

        verify { navigator.alert(alertAction = alert) }
    }

    @Test
    fun `disable progress`() {
        viewPlayerReportsNavigationImpl.disableProgress()

        verify { navigator.progress(progressAction = null) }
    }

    @Test
    fun `enable progress`() {
        val progress = Progress()

        viewPlayerReportsNavigationImpl.enableProgress(progress = progress)

        verify { navigator.progress(progressAction = progress) }
    }

    @Test
    fun `pop action`() {
        val argumentCapture: CapturingSlot<String> = slot()

        viewPlayerReportsNavigationImpl.pop()

        verify { navigator.pop(capture(argumentCapture)) }

        val capturedArgument = argumentCapture.captured
        val expectedAction = Constants.POP_DEFAULT_ACTION

        Assertions.assertEquals(expectedAction, capturedArgument)
    }
}
