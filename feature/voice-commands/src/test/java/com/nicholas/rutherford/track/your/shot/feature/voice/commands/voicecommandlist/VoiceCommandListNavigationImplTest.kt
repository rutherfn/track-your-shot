package com.nicholas.rutherford.track.your.shot.feature.voice.commands.voicecommandlist

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

class VoiceCommandListNavigationImplTest {

    private lateinit var voiceCommandListNavigationImpl: VoiceCommandListNavigationImpl

    private var navigator = mockk<Navigator>(relaxed = true)

    @BeforeEach
    fun beforeEach() {
        voiceCommandListNavigationImpl = VoiceCommandListNavigationImpl(navigator = navigator)
    }

    @Test
    fun `open navigation drawer`() {
        voiceCommandListNavigationImpl.openNavigationDrawer()

        verify { navigator.showNavigationDrawer(navigationDrawerAction = true) }
    }

    @Test
    fun `navigate to create edit voice command`() {
        val type = 2
        val phrase = "Start"

        val argumentCapture: CapturingSlot<NavigationAction> = slot()

        voiceCommandListNavigationImpl.navigateToCreateEditVoiceCommand(type = type, phrase = phrase)

        verify { navigator.navigate(capture(argumentCapture)) }

        val capturedArgument = argumentCapture.captured

        val expectedAction = NavigationActions.VoiceCommands.createEditVoiceCommandsWithParams(type = type, phrase = phrase)

        Assertions.assertEquals(expectedAction.destination, capturedArgument.destination)
    }
}
