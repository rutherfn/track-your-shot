package com.nicholas.rutherford.track.your.shot.feature.settings.managedeclaredshots.declaredshotlist

import com.nicholas.rutherford.track.your.shot.feature.settings.managedeclaredshots.declaredshotslist.DeclaredShotsListNavigationImpl
import com.nicholas.rutherford.track.your.shot.helper.constants.Constants
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

class DeclaredShotsListNavigationImplTest {

    private lateinit var navigationImpl: DeclaredShotsListNavigationImpl

    private var navigator = mockk<Navigator>(relaxed = true)

    @BeforeEach
    fun beforeEach() {
        navigationImpl = DeclaredShotsListNavigationImpl(navigator = navigator)
    }

    @Test
    fun `create edit declared shot`() {
        val argumentCapture: CapturingSlot<NavigationAction> = slot()

        navigationImpl.createEditDeclaredShot()

        verify { navigator.navigate(capture(argumentCapture)) }

        val capturedArgument = argumentCapture.captured
        val expectedAction = NavigationActions.DeclaredShotsList.createEditDeclaredShot()

        Assertions.assertEquals(expectedAction.destination, capturedArgument.destination)
    }

    @Test
    fun `pop action`() {
        val argumentCapture: CapturingSlot<String> = slot()

        navigationImpl.pop()

        verify { navigator.pop(capture(argumentCapture)) }

        val capturedArgument = argumentCapture.captured
        val expectedAction = Constants.POP_DEFAULT_ACTION

        Assertions.assertEquals(expectedAction, capturedArgument)
    }
}