package com.nicholas.rutherford.track.your.shot.players.createplayer

import com.nicholas.rutherford.track.your.shot.data.shared.alert.Alert
import com.nicholas.rutherford.track.your.shot.data.shared.progress.Progress
import com.nicholas.rutherford.track.your.shot.feature.players.createplayer.CreatePlayerNavigationImpl
import com.nicholas.rutherford.track.your.shot.navigation.NavigationAction
import com.nicholas.rutherford.track.your.shot.navigation.NavigationActions
import com.nicholas.rutherford.track.your.shot.navigation.NavigationDestinations
import com.nicholas.rutherford.track.your.shot.navigation.Navigator
import io.mockk.CapturingSlot
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class CreatePlayerNavigationImplTest {

    private lateinit var createPlayerNavigationImpl: CreatePlayerNavigationImpl

    private var navigator = mockk<Navigator>(relaxed = true)

    @BeforeEach
    fun beforeEach() {
        createPlayerNavigationImpl = CreatePlayerNavigationImpl(navigator = navigator)
    }

    @Test
    fun `alert action`() {
        val alert = Alert(title = "title")

        createPlayerNavigationImpl.alert(alert = alert)

        verify { navigator.alert(alertAction = alert) }
    }

    @Test
    fun `app settings action`() {
        createPlayerNavigationImpl.appSettings()

        verify { navigator.appSettings(appSettingsAction = true) }
    }

    @Test
    fun `disable progress`() {
        createPlayerNavigationImpl.disableProgress()

        verify { navigator.progress(progressAction = null) }
    }

    @Test
    fun `enable progress`() {
        val progress = Progress()
        createPlayerNavigationImpl.enableProgress(progress = progress)

        verify { navigator.progress(progressAction = progress) }
    }

    @Test
    fun `navigate to players list`() {
        val argumentCapture: CapturingSlot<NavigationAction> = slot()

        createPlayerNavigationImpl.navigateToPlayersList()

        verify { navigator.navigate(capture(argumentCapture)) }

        val capturedArgument = argumentCapture.captured
        val expectedAction = NavigationActions.CreatePlayer.playersList()

        Assertions.assertEquals(expectedAction.destination, capturedArgument.destination)
    }

    @Test
    fun `pop action`() {
        val argumentCapture: CapturingSlot<String> = slot()

        createPlayerNavigationImpl.pop()

        verify { navigator.pop(capture(argumentCapture)) }

        val capturedArgument = argumentCapture.captured
        val expectedAction = NavigationDestinations.PLAYERS_LIST_SCREEN

        Assertions.assertEquals(expectedAction, capturedArgument)
    }
}
