package com.nicholas.rutherford.track.your.shot.feature.players.shots.selectshot

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

class SelectShotNavigationImplTest {

    private lateinit var selectShotNavigationImpl: SelectShotNavigationImpl

    private var navigator = mockk<Navigator>(relaxed = true)

    @BeforeEach
    fun beforeEach() {
        selectShotNavigationImpl = SelectShotNavigationImpl(navigator = navigator)
    }

    @Test
    fun `pop from create player`() {
        val argumentCapture: CapturingSlot<String> = slot()

        selectShotNavigationImpl.popFromCreatePlayer()

        verify { navigator.pop(capture(argumentCapture)) }

        val capturedArgument = argumentCapture.captured
        val expected = NavigationDestinations.CREATE_EDIT_PLAYER_SCREEN

        Assertions.assertEquals(expected, capturedArgument)
    }

    @Test
    fun `pop from edit player`() {
        val argumentCapture: CapturingSlot<String> = slot()

        selectShotNavigationImpl.popFromEditPlayer()

        verify { navigator.pop(capture(argumentCapture)) }

        val capturedArgument = argumentCapture.captured
        val expected = NavigationDestinations.CREATE_EDIT_PLAYER_SCREEN_WITH_PARAMS

        Assertions.assertEquals(expected, capturedArgument)
    }

    @Test
    fun `navigate to log shot`() {
        val isExistingPlayer = false
        val playerId = 5
        val shotId = 3
        val isExistingShot = false
        val argumentCapture: CapturingSlot<NavigationAction> = slot()

        selectShotNavigationImpl.navigateToLogShot(
            isExistingPlayer = isExistingPlayer,
            playerId = playerId,
            shotId = shotId
        )

        verify { navigator.navigate(capture(argumentCapture)) }

        val capturedArgument = argumentCapture.captured
        val expectedAction = NavigationActions.SelectShot.logShot(
            isExistingPlayer = isExistingPlayer,
            playerId = playerId,
            shotId = shotId,
            isExistingShot = isExistingShot
        )

        Assertions.assertEquals(
            expectedAction.destination,
            capturedArgument.destination
        )
    }
}
