package com.nicholas.rutherford.track.your.shot.feature.shots

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

class ShotsListNavigationImplTest {

    private lateinit var navigationImpl: ShotsListNavigationImpl

    private var navigator = mockk<Navigator>(relaxed = true)

    @BeforeEach
    fun beforeEach() {
        navigationImpl = ShotsListNavigationImpl(navigator = navigator)
    }

    @Test
    fun `open navigation drawer`() {
        navigationImpl.openNavigationDrawer()

        verify { navigator.showNavigationDrawer(navigationDrawerAction = true) }
    }

    @Test
    fun `navigate to log shot`() {
        val isExistingPlayer = false
        val playerId = 5
        val shotType = 4
        val shotId = 2
        val viewCurrentExistingShot = false
        val viewCurrentPendingShot = false
        val argumentCapture: CapturingSlot<NavigationAction> = slot()

        navigationImpl.navigateToLogShot(
            isExistingPlayer = isExistingPlayer,
            playerId = playerId,
            shotType = shotType,
            shotId = shotId,
            viewCurrentExistingShot = viewCurrentExistingShot,
            viewCurrentPendingShot = viewCurrentPendingShot
        )

        verify { navigator.navigate(capture(argumentCapture)) }

        val capturedArgument = argumentCapture.captured
        val expectedAction = NavigationActions.CreateEditPlayer.logShot(
            isExistingPlayer = isExistingPlayer,
            playerId = playerId,
            shotType = shotType,
            shotId = shotId,
            viewCurrentExistingShot = viewCurrentExistingShot,
            viewCurrentPendingShot = viewCurrentPendingShot
        )

        Assertions.assertEquals(expectedAction.destination, capturedArgument.destination)
    }
}
