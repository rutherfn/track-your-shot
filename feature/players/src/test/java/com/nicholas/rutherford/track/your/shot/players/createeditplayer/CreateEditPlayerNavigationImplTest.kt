package com.nicholas.rutherford.track.your.shot.players.createeditplayer

import com.nicholas.rutherford.track.your.shot.data.shared.alert.Alert
import com.nicholas.rutherford.track.your.shot.data.shared.progress.Progress
import com.nicholas.rutherford.track.your.shot.feature.players.createeditplayer.CreateEditPlayerNavigationImpl
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

class CreateEditPlayerNavigationImplTest {

    private lateinit var createEditPlayerNavigationImpl: CreateEditPlayerNavigationImpl

    private var navigator = mockk<Navigator>(relaxed = true)

    @BeforeEach
    fun beforeEach() {
        createEditPlayerNavigationImpl = CreateEditPlayerNavigationImpl(navigator = navigator)
    }

    @Test
    fun `alert action`() {
        val alert = Alert(title = "title")

        createEditPlayerNavigationImpl.alert(alert = alert)

        verify { navigator.alert(alertAction = alert) }
    }

    @Test
    fun `app settings action`() {
        createEditPlayerNavigationImpl.appSettings()

        verify { navigator.appSettings(appSettingsAction = true) }
    }

    @Test
    fun `disable progress`() {
        createEditPlayerNavigationImpl.disableProgress()

        verify { navigator.progress(progressAction = null) }
    }

    @Test
    fun `enable progress`() {
        val progress = Progress()
        createEditPlayerNavigationImpl.enableProgress(progress = progress)

        verify { navigator.progress(progressAction = progress) }
    }

    @Test
    fun `pop action`() {
        val argumentCapture: CapturingSlot<String> = slot()

        createEditPlayerNavigationImpl.pop()

        verify { navigator.pop(capture(argumentCapture)) }

        val capturedArgument = argumentCapture.captured
        val expectedAction = NavigationDestinations.PLAYERS_LIST_SCREEN

        Assertions.assertEquals(expectedAction, capturedArgument)
    }

    @Test
    fun `navigate to select shot`() {
        val isExistingPlayer = false
        val playerId = 5
        val argumentCapture: CapturingSlot<NavigationAction> = slot()

        createEditPlayerNavigationImpl.navigateToSelectShot(isExistingPlayer = isExistingPlayer, playerId = playerId)

        verify { navigator.navigate(capture(argumentCapture)) }

        val capturedArgument = argumentCapture.captured
        val expectedAction = NavigationActions.CreateEditPlayer.selectShot(isExistingPlayer = isExistingPlayer, playerId = playerId)

        Assertions.assertEquals(expectedAction.destination, capturedArgument.destination)
    }

    @Test
    fun `navigate to log shot`() {
        val isExistingPlayer = false
        val playerId = 5
        val shotType = 4
        val shotId = 2
        val viewCurrentExistingShot = false
        val viewCurrentPendingShot = false
        val fromShotList = false
        val argumentCapture: CapturingSlot<NavigationAction> = slot()

        createEditPlayerNavigationImpl.navigateToLogShot(
            isExistingPlayer = isExistingPlayer,
            playerId = playerId,
            shotType = shotType,
            shotId = shotId,
            viewCurrentExistingShot = viewCurrentExistingShot,
            viewCurrentPendingShot = viewCurrentPendingShot,
            fromShotList = fromShotList
        )

        verify { navigator.navigate(capture(argumentCapture)) }

        val capturedArgument = argumentCapture.captured
        val expectedAction = NavigationActions.CreateEditPlayer.logShot(
            isExistingPlayer = isExistingPlayer,
            playerId = playerId,
            shotType = shotType,
            shotId = shotId,
            viewCurrentExistingShot = viewCurrentExistingShot,
            viewCurrentPendingShot = viewCurrentPendingShot,
            fromShotList = fromShotList
        )

        Assertions.assertEquals(expectedAction.destination, capturedArgument.destination)
    }
}
