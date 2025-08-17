package com.nicholas.rutherford.track.your.shot.feature.settings.managedeclaredshots.declaredshotlist

import com.nicholas.rutherford.track.your.shot.feature.settings.managedeclaredshots.declaredshotslist.DeclaredShotsListNavigationImpl
import com.nicholas.rutherford.track.your.shot.helper.extensions.UriEncoder
import com.nicholas.rutherford.track.your.shot.navigation.NavigationAction
import com.nicholas.rutherford.track.your.shot.navigation.NavigationActions
import com.nicholas.rutherford.track.your.shot.navigation.NavigationDestinations
import com.nicholas.rutherford.track.your.shot.navigation.Navigator
import io.mockk.CapturingSlot
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.slot
import io.mockk.unmockkObject
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
        val shotName = "shotName"

        val argumentCapture: CapturingSlot<NavigationAction> = slot()
        mockkObject(UriEncoder)

        every { UriEncoder.encode(any()) } answers { firstArg() }

        navigationImpl.createEditDeclaredShot(shotName = shotName)

        verify { navigator.navigate(capture(argumentCapture)) }

        val capturedArgument = argumentCapture.captured
        val expectedAction = NavigationActions.DeclaredShotsList.createEditDeclaredShot(shotName = shotName)

        Assertions.assertEquals(expectedAction.destination, capturedArgument.destination)

        unmockkObject(UriEncoder)
    }

    @Test
    fun `pop action`() {
        val argumentCapture: CapturingSlot<String> = slot()

        navigationImpl.pop()

        verify { navigator.pop(capture(argumentCapture)) }

        val capturedArgument = argumentCapture.captured
        val expectedAction = NavigationDestinations.SETTINGS_SCREEN

        Assertions.assertEquals(expectedAction, capturedArgument)
    }
}
