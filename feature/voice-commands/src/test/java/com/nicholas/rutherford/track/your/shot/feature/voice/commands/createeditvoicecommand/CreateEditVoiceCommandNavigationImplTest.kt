package com.nicholas.rutherford.track.your.shot.feature.voice.commands.createeditvoicecommand

import com.nicholas.rutherford.track.your.shot.data.shared.alert.Alert
import com.nicholas.rutherford.track.your.shot.data.shared.progress.Progress
import com.nicholas.rutherford.track.your.shot.navigation.NavigationDestinations
import com.nicholas.rutherford.track.your.shot.navigation.Navigator
import io.mockk.CapturingSlot
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class CreateEditVoiceCommandNavigationImplTest {

    private lateinit var createEditVoiceCommandNavigationImpl: CreateEditVoiceCommandNavigationImpl

    private var navigator = mockk<Navigator>(relaxed = true)

    @BeforeEach
    fun beforeEach() {
        createEditVoiceCommandNavigationImpl = CreateEditVoiceCommandNavigationImpl(navigator = navigator)
    }

    @Test
    fun `alert action`() {
        val alert = Alert(title = "title")

        createEditVoiceCommandNavigationImpl.alert(alert = alert)

        verify { navigator.alert(alertAction = alert) }
    }

    @Test
    fun `enable progress`() {
        val progress = Progress()

        createEditVoiceCommandNavigationImpl.enableProgress(progress = progress)

        verify { navigator.progress(progressAction = progress) }
    }

    @Test
    fun `disable progress`() {
        createEditVoiceCommandNavigationImpl.disableProgress()

        verify { navigator.progress(progressAction = null) }
    }

    @Test
    fun `pop action`() {
        val argumentCapture: CapturingSlot<String> = slot()

        createEditVoiceCommandNavigationImpl.pop()

        verify { navigator.pop(capture(argumentCapture)) }

        val capturedArgument = argumentCapture.captured
        val expectedAction = NavigationDestinations.VOICE_COMMANDS_SCREEN

        Assertions.assertEquals(expectedAction, capturedArgument)
    }
}