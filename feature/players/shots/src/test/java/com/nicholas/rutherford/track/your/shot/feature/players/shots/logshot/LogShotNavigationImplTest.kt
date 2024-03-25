package com.nicholas.rutherford.track.your.shot.feature.players.shots.logshot

import com.nicholas.rutherford.track.your.shot.data.shared.InputInfo
import com.nicholas.rutherford.track.your.shot.data.shared.datepicker.DatePickerInfo
import com.nicholas.rutherford.track.your.shot.feature.splash.StringsIds
import com.nicholas.rutherford.track.your.shot.helper.constants.Constants
import com.nicholas.rutherford.track.your.shot.navigation.Navigator
import io.mockk.CapturingSlot
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class LogShotNavigationImplTest {

    private lateinit var logShotNavigationImpl: LogShotNavigationImpl

    private var navigator = mockk<Navigator>(relaxed = true)

    @BeforeEach
    fun beforeEach() {
        logShotNavigationImpl = LogShotNavigationImpl(navigator = navigator)
    }

    @Test
    fun `pop action`() {
        val argumentCapture: CapturingSlot<String> = slot()

        logShotNavigationImpl.pop()

        verify { navigator.pop(capture(argumentCapture)) }

        val capturedArgument = argumentCapture.captured
        val expectedAction = Constants.POP_DEFAULT_ACTION

        Assertions.assertEquals(expectedAction, capturedArgument)
    }

    @Test
    fun `date picker action`() {
        val datePickerInfo = DatePickerInfo(
            onDateOkClicked = {},
            onDismissClicked = {},
            dateValue = "date"
        )
        val argumentCapture: CapturingSlot<DatePickerInfo> = slot()

        logShotNavigationImpl.datePicker(datePickerInfo = datePickerInfo)

        verify { navigator.datePicker(capture(argumentCapture)) }

        val capturedArgument = argumentCapture.captured

        Assertions.assertEquals(datePickerInfo, capturedArgument)
    }

    @Test
    fun `input info action`() {
        val inputInfo = InputInfo(
            titleResId = StringsIds.enterShotsMade,
            confirmButtonResId = StringsIds.ok,
            dismissButtonResId = StringsIds.cancel,
            placeholderResId = StringsIds.shotsMade,
            startingInputAmount = 1,
            onConfirmButtonClicked = {}
        )
        val argumentCapture: CapturingSlot<InputInfo> = slot()

        logShotNavigationImpl.inputInfo(inputInfo = inputInfo)

        verify { navigator.inputInfo(capture(argumentCapture)) }

        val capturedArgument = argumentCapture.captured

        Assertions.assertEquals(inputInfo, capturedArgument)
    }
}
