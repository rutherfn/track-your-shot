package com.nicholas.rutherford.track.your.shot.feature.players.shots.logshot.extension

import android.app.Application
import com.nicholas.rutherford.track.your.shot.base.resources.StringsIds
import com.nicholas.rutherford.track.your.shot.data.room.response.ShotLogged
import com.nicholas.rutherford.track.your.shot.data.shared.alert.Alert
import com.nicholas.rutherford.track.your.shot.data.shared.alert.AlertConfirmAndDismissButton
import com.nicholas.rutherford.track.your.shot.data.test.room.TestDeclaredShot
import com.nicholas.rutherford.track.your.shot.data.test.room.TestPlayer
import com.nicholas.rutherford.track.your.shot.data.test.room.TestShotLogged
import com.nicholas.rutherford.track.your.shot.feature.players.shots.logshot.LogShotState
import com.nicholas.rutherford.track.your.shot.feature.players.shots.logshot.pendingshot.PendingShot
import com.nicholas.rutherford.track.your.shot.firebase.realtime.ShotLoggedRealtimeResponse
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.text.SimpleDateFormat
import java.util.Locale

class LogShotViewModelExtImplTest {

    private val application = mockk<Application>(relaxed = true)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val dispatcher = UnconfinedTestDispatcher()

    private val scope = CoroutineScope(SupervisorJob() + dispatcher)

    private val datePattern = "MMMM dd, yyyy"
    private val dateFormat = SimpleDateFormat(datePattern, Locale.ENGLISH)

    private lateinit var logShotViewModelExtImpl: LogShotViewModelExtImpl

    @BeforeEach
    fun beforeEach() {
        logShotViewModelExtImpl = LogShotViewModelExtImpl(
            application = application,
            scope = scope
        )
    }

    @Nested
    inner class ConvertPercentageToDouble {

        @Test
        fun `when percentage does not contain a percent sign should return default value`() {
            Assertions.assertEquals(
                logShotViewModelExtImpl.convertPercentageToDouble(percentage = "1"),
                0.0
            )
        }

        @Test
        fun `if value contains a period should convert it to a percent value`() {
            Assertions.assertEquals(
                logShotViewModelExtImpl.convertPercentageToDouble(percentage = "33.3%"),
                33.3
            )
        }

        @Test
        fun `if value contains no period should convert it to a percent value with addition of a zero`() {
            Assertions.assertEquals(
                logShotViewModelExtImpl.convertPercentageToDouble(percentage = "1%"),
                1.0
            )
        }
    }

    @Nested
    inner class ConvertValueToDate {

        @Test
        fun `when empty string should return null`() {
            val result = logShotViewModelExtImpl.convertValueToDate(value = "")

            Assertions.assertNull(result)
        }

        @Test
        fun `when value returns correct date should return valid date string`() {
            val dateString = "February 06, 2025"
            val expectedDate = dateFormat.parse(dateString)!!

            val result = logShotViewModelExtImpl.convertValueToDate(value = dateString)

            Assertions.assertNotNull(result)
            Assertions.assertEquals(expectedDate.time, result!!.time)
        }
    }

    @Test
    fun `set initial info should update log shot info`() {
        val shotInfo = LogShotInfo(
            isExistingPlayer = true,
            playerId = 22,
            shotType = 44,
            shotId = 41,
            viewCurrentExistingShot = true,
            viewCurrentPendingShot = false,
            fromShotList = false
        )

        logShotViewModelExtImpl.setInitialInfo(logShotInfo = shotInfo)

        Assertions.assertEquals(logShotViewModelExtImpl.logShotInfo, shotInfo)
    }

    @Nested
    inner class ShotsAttempted {

        @Test
        fun `when shotsMissed is not equal to zero should return total value`() {
            val result = logShotViewModelExtImpl.shotsAttempted(shotsMade = 0, shotsMissed = 2)

            Assertions.assertEquals(result, 2)
        }

        @Test
        fun `when shotsMade is not equal to zero should return total value`() {
            val result = logShotViewModelExtImpl.shotsAttempted(shotsMade = 2, shotsMissed = 0)

            Assertions.assertEquals(result, 2)
        }

        @Test
        fun `when shotsMade and shotsMissed is equal to zero should return zero`() {
            val result = logShotViewModelExtImpl.shotsAttempted(shotsMade = 0, shotsMissed = 0)

            Assertions.assertEquals(result, 0)
        }
    }

    @Nested
    inner class ShotsPercentValue {

        @Test
        fun `when isShotsMade is set to true and both values are not 0 should calculate percent`() {
            val result = logShotViewModelExtImpl.shotsPercentValue(
                shotsMade = 5.0,
                shotsMissed = 3.0,
                isShotsMade = true
            )

            Assertions.assertEquals(result, 62.5)
        }

        @Test
        fun `when isShotsMade is set to false and both values are not 0 should calculate percent`() {
            val result = logShotViewModelExtImpl.shotsPercentValue(
                shotsMade = 5.0,
                shotsMissed = 3.0,
                isShotsMade = false
            )

            Assertions.assertEquals(result, 37.5)
        }

        @Test
        fun `when isShotMade is set to true but shotsMade is 0 should return default value`() {
            val result = logShotViewModelExtImpl.shotsPercentValue(
                shotsMade = 0.0,
                shotsMissed = 3.0,
                isShotsMade = true
            )

            Assertions.assertEquals(result, 0.0)
        }

        @Test
        fun `when isShotMade is set to true but shotsMissed is 0 should return default value`() {
            val result = logShotViewModelExtImpl.shotsPercentValue(
                shotsMade = 1.0,
                shotsMissed = 0.0,
                isShotsMade = true
            )

            Assertions.assertEquals(result, 0.0)
        }

        @Test
        fun `when isShotMade is set to false but shotsMissed is 0 should return default value`() {
            val result = logShotViewModelExtImpl.shotsPercentValue(
                shotsMade = 1.0,
                shotsMissed = 0.0,
                isShotsMade = false
            )

            Assertions.assertEquals(result, 0.0)
        }

        @Test
        fun `when isShotMade is set to false but shotsMade is 0 should return default value`() {
            val result = logShotViewModelExtImpl.shotsPercentValue(
                shotsMade = 0.0,
                shotsMissed = 1.0,
                isShotsMade = false
            )

            Assertions.assertEquals(result, 0.0)
        }
    }

    @Nested
    inner class PercentageFormat {

        @Test
        fun `when percent is the default value should return empty string`() {
            every { application.getString(StringsIds.empty) } returns ""

            val result = logShotViewModelExtImpl.percentageFormat(
                shotsMade = 0.0,
                shotsMissed = 2.0,
                isShotsMade = true
            )

            Assertions.assertEquals(result, "")
        }

        @Test
        fun `when percent ends with a 0 should return percent value`() {
            val percentValue = 37.5
            val percentageRoundedValue = String.format("%.1f", percentValue)

            every {
                application.getString(
                    StringsIds.shotPercentage,
                    percentageRoundedValue
                )
            } returns "37.5%"

            val result = logShotViewModelExtImpl.percentageFormat(
                shotsMade = 3.0,
                shotsMissed = 5.0,
                isShotsMade = true
            )

            Assertions.assertEquals(
                result,
                "37.5%"
            )
        }
    }

    @Test
    fun `filter shots by id should filter out shots matching the local param shotId`() {
        val shotId = 2
        val shotLoggedList = listOf(
            TestShotLogged.build().copy(id = 11),
            TestShotLogged.build().copy(id = 22),
            TestShotLogged.build().copy(id = 2)
        )

        logShotViewModelExtImpl.logShotInfo = logShotViewModelExtImpl.logShotInfo.copy(shotId = shotId)

        val result = logShotViewModelExtImpl.filterShotsById(shots = shotLoggedList)

        Assertions.assertEquals(result, listOf(TestShotLogged.build().copy(id = 11), TestShotLogged.build().copy(id = 22)))
    }

    @Test
    fun `invalid shot alert should return alert`() {
        val description = "description"

        every { application.getString(StringsIds.error) } returns "Error"
        every { application.getString(StringsIds.gotIt) } returns "Got It"

        Assertions.assertEquals(
            logShotViewModelExtImpl.invalidLogShotAlert(description = description),
            Alert(
                title = "Error",
                dismissButton = AlertConfirmAndDismissButton(buttonText = "Got It"),
                description = description
            )
        )
    }

    @Test
    fun `delete shot alert`() {
        val shotName = "Hook Shot"

        every { application.getString(StringsIds.deleteShot) } returns "Delete Shot"
        every { application.getString(StringsIds.yes) } returns "Yes"
        every { application.getString(StringsIds.no) } returns "No"
        every { application.getString(StringsIds.areYouSureYouWantToDeleteXShot, "Hook Shot") } returns "Are you sure you want to delete Hook Shot?"

        val result = logShotViewModelExtImpl.deleteShotAlert(onYesDeleteShot = {}, shotName = shotName)

        Assertions.assertEquals(result.title, "Delete Shot")
        Assertions.assertEquals(result.description, "Are you sure you want to delete Hook Shot?")
        Assertions.assertEquals(result.confirmButton!!.buttonText, "Yes")
        Assertions.assertEquals(result.dismissButton!!.buttonText, "No")
    }

    @Test
    fun `delete shot confirm alert`() {
        val shotName = "Hook Shot"

        every { application.getString(StringsIds.shotHasBeenDeleted) } returns "Shot Has Been Deleted"
        every { application.getString(StringsIds.gotIt) } returns "Got It"
        every { application.getString(StringsIds.xShotHasBeenRemovedDescription, "Hook Shot") } returns "Hook Shot has been removed from this player\'s shot history"

        val result = logShotViewModelExtImpl.deleteShotConfirmAlert(shotName = shotName)

        Assertions.assertEquals(result.title, "Shot Has Been Deleted")
        Assertions.assertEquals(result.description, "Hook Shot has been removed from this player's shot history")
        Assertions.assertEquals(result.confirmButton!!.buttonText, "Got It")
    }

    @Test
    fun `delete shot error alert`() {
        val shotName = "Hook Shot"

        every { application.getString(StringsIds.shotHasNotBeenDeleted) } returns "Shot Has Not Been Deleted"
        every { application.getString(StringsIds.gotIt) } returns "Got It"
        every { application.getString(StringsIds.xShotHasNotBeenRemovedDescription, "Hook Shot") } returns "Hook Shot has not been removed from this player\'s shot history"

        val result = logShotViewModelExtImpl.deleteShotErrorAlert(shotName = shotName)

        Assertions.assertEquals(result.title, "Shot Has Not Been Deleted")
        Assertions.assertEquals(result.description, "Hook Shot has not been removed from this player's shot history")
        Assertions.assertEquals(result.confirmButton!!.buttonText, "Got It")
    }

    @Nested
    inner class NoChangesForShotAlert {
        private val pendingShotLogged = TestShotLogged.build()

        @Test
        fun `when initialShotLogged is set to null should return null`() {
            Assertions.assertEquals(
                logShotViewModelExtImpl.noChangesForShotAlert(initialShotLogged = null, pendingShotLogged = pendingShotLogged),
                null
            )
        }

        @Test
        fun `when initialShotLogged is not the same as pendingShotLogged should return null`() {
            Assertions.assertEquals(
                logShotViewModelExtImpl.noChangesForShotAlert(initialShotLogged = pendingShotLogged, pendingShotLogged = pendingShotLogged.copy(shotsMissed = 111)),
                null
            )
        }

        @Test
        fun `when initialShotLogged is the same as pendingShotLogged should return a alert`() {
            val alert = Alert(
                title = "No Changes Made",
                dismissButton = AlertConfirmAndDismissButton(buttonText = "Got It"),
                description = "There haven\'t been any recent updates or modifications to this shot. Please make adjustments to the existing shot to proceed."
            )

            every { application.getString(StringsIds.noChangesMade) } returns "No Changes Made"
            every { application.getString(StringsIds.gotIt) } returns "Got It"
            every { application.getString(StringsIds.currentShotHasNoChangesDescription) } returns "There haven\'t been any recent updates or modifications to this shot. Please make adjustments to the existing shot to proceed."

            Assertions.assertEquals(
                logShotViewModelExtImpl.noChangesForShotAlert(initialShotLogged = pendingShotLogged, pendingShotLogged = pendingShotLogged),
                alert
            )
        }
    }

    @Nested
    inner class ShotEntryInvalidAlert {

        @Test
        fun `when description is not set to null should return null alert`() {
            Assertions.assertEquals(
                logShotViewModelExtImpl.shotEntryInvalidAlert(
                    shotsMade = 6,
                    shotsMissed = 2,
                    shotsAttemptedMillisecondsValue = 2L
                ),
                null
            )
        }

        @Test
        fun `when shotsMade is set to 0 should set a description which should build alert`() {
            val description = "You haven\'t recorded any successful shots. Please input the number of shots made to proceed with logging the shot."

            every { application.getString(StringsIds.empty) } returns ""
            every { application.getString(StringsIds.gotIt) } returns "Got It"
            every { application.getString(StringsIds.shotsNotRecordedDescription) } returns description

            Assertions.assertEquals(
                logShotViewModelExtImpl.shotEntryInvalidAlert(
                    shotsMade = 0,
                    shotsMissed = 2,
                    shotsAttemptedMillisecondsValue = 2L
                ),
                Alert(
                    title = "",
                    dismissButton = AlertConfirmAndDismissButton(buttonText = "Got It"),
                    description = description
                )
            )
        }

        @Test
        fun `when shotsMissed is set 0 should set description which should build alert`() {
            val description = "You haven\'t recorded any missed shots. Please input the number of shots missed to proceed with logging the shot."

            every { application.getString(StringsIds.empty) } returns ""
            every { application.getString(StringsIds.gotIt) } returns "Got It"
            every { application.getString(StringsIds.missedShotsNotRecordedDescription) } returns description

            Assertions.assertEquals(
                logShotViewModelExtImpl.shotEntryInvalidAlert(
                    shotsMade = 2,
                    shotsMissed = 0,
                    shotsAttemptedMillisecondsValue = 2L
                ),
                Alert(
                    title = "",
                    dismissButton = AlertConfirmAndDismissButton(buttonText = "Got It"),
                    description = description
                )
            )
        }

        @Test
        fun `when shotsAttemptedMillisecondsValue is set 0 should set description which should build alert`() {
            val description = "You haven\'t entered the date the shot was taken. Please input the date the shot was taken to proceed with logging ths shot."

            every { application.getString(StringsIds.empty) } returns ""
            every { application.getString(StringsIds.gotIt) } returns "Got It"
            every { application.getString(StringsIds.dateShotWasTakenDescription) } returns description

            Assertions.assertEquals(
                logShotViewModelExtImpl.shotEntryInvalidAlert(
                    shotsMade = 2,
                    shotsMissed = 2,
                    shotsAttemptedMillisecondsValue = 0L
                ),
                Alert(
                    title = "",
                    dismissButton = AlertConfirmAndDismissButton(buttonText = "Got It"),
                    description = description
                )
            )
        }
    }

    @Test
    fun `we have detected a problem with your account alert should return alert info`() {
        every { application.getString(StringsIds.issueOccurred) } returns "Issue Occurred"
        every { application.getString(StringsIds.weHaveDetectedAProblemWithYourAccountPleaseContactSupportToResolveIssue) } returns "We have detected a problem with your account. Please contact support to resolve issue."
        every { application.getString(StringsIds.gotIt) } returns "Got It"

        val alert = logShotViewModelExtImpl.weHaveDetectedAProblemWithYourAccountAlert()

        Assertions.assertEquals(alert.title, "Issue Occurred")
        Assertions.assertEquals(alert.dismissButton!!.buttonText, "Got It")
        Assertions.assertEquals(alert.description, "We have detected a problem with your account. Please contact support to resolve issue.")
    }

    @Test
    fun `show updated alert should return alert`() {
        every { application.getString(StringsIds.shotUpdated) } returns "Shot Updated"
        every { application.getString(StringsIds.gotIt) } returns "Got It"
        every { application.getString(StringsIds.currentShotHasBeenUpdatedDescription) } returns "The current shot has been updated. To see the latest information, click on the shot to view the updates"

        val alert = Alert(
            title = "Shot Updated",
            dismissButton = AlertConfirmAndDismissButton(buttonText = "Got It"),
            description = "The current shot has been updated. To see the latest information, click on the shot to view the updates"
        )

        Assertions.assertEquals(
            logShotViewModelExtImpl.showUpdatedAlert(),
            alert
        )
    }

    @Nested
    inner class CalculateShotPercentage {

        @Test
        fun `when isShotsMade is true should format made shots percentage`() {
            val shot = TestShotLogged.build().copy(shotsMade = 10, shotsMissed = 5)

            every { application.getString(StringsIds.shotPercentage, "66.7") } returns "66.7%"

            val result = logShotViewModelExtImpl.calculateShotPercentage(shot = shot, isShotsMade = true)

            Assertions.assertEquals("66.7%", result)
        }

        @Test
        fun `when isShotsMade is false should format missed shots percentage`() {
            val shot = TestShotLogged.build().copy(shotsMade = 8, shotsMissed = 12)

            every { application.getString(StringsIds.shotPercentage, "60") } returns "60.0%"

            val result = logShotViewModelExtImpl.calculateShotPercentage(shot = shot, isShotsMade = false)

            Assertions.assertEquals("60.0%", result)
        }
    }

    @Nested
    inner class CurrentShotLoggedRealtimeResponseList {

        @Test
        fun `when currentShotList is empty should return empty list`() {
            val currentShotList = emptyList<ShotLogged>()

            val result = logShotViewModelExtImpl.currentShotLoggedRealtimeResponseList(currentShotList = currentShotList)

            Assertions.assertTrue(result.isEmpty())
        }

        @Test
        fun `when currentShotList has one item should convert to ShotLoggedRealtimeResponse with isPending false`() {
            val shotLogged = TestShotLogged.build().copy(
                id = 1,
                shotName = "Test Shot",
                shotType = 5,
                shotsAttempted = 10,
                shotsMade = 7,
                shotsMissed = 3,
                shotsMadePercentValue = 70.0,
                shotsMissedPercentValue = 30.0,
                shotsAttemptedMillisecondsValue = 1000L,
                shotsLoggedMillisecondsValue = 2000L,
                isPending = true // Should be converted to false
            )
            val currentShotList = listOf(shotLogged)

            val result = logShotViewModelExtImpl.currentShotLoggedRealtimeResponseList(currentShotList = currentShotList)

            Assertions.assertEquals(1, result.size)
            val response = result.first()
            Assertions.assertEquals(shotLogged.id, response.id)
            Assertions.assertEquals(shotLogged.shotName, response.shotName)
            Assertions.assertEquals(shotLogged.shotType, response.shotType)
            Assertions.assertEquals(shotLogged.shotsAttempted, response.shotsAttempted)
            Assertions.assertEquals(shotLogged.shotsMade, response.shotsMade)
            Assertions.assertEquals(shotLogged.shotsMissed, response.shotsMissed)
            Assertions.assertEquals(shotLogged.shotsMadePercentValue, response.shotsMadePercentValue)
            Assertions.assertEquals(shotLogged.shotsMissedPercentValue, response.shotsMissedPercentValue)
            Assertions.assertEquals(shotLogged.shotsAttemptedMillisecondsValue, response.shotsAttemptedMillisecondsValue)
            Assertions.assertEquals(shotLogged.shotsLoggedMillisecondsValue, response.shotsLoggedMillisecondsValue)
            Assertions.assertFalse(response.isPending) // Should always be false
        }

        @Test
        fun `when currentShotList has multiple items should convert all to ShotLoggedRealtimeResponse`() {
            val shot1 = TestShotLogged.build().copy(id = 1, shotName = "Shot 1")
            val shot2 = TestShotLogged.build().copy(id = 2, shotName = "Shot 2")
            val shot3 = TestShotLogged.build().copy(id = 3, shotName = "Shot 3")
            val currentShotList = listOf(shot1, shot2, shot3)

            val result = logShotViewModelExtImpl.currentShotLoggedRealtimeResponseList(currentShotList = currentShotList)

            Assertions.assertEquals(3, result.size)
            Assertions.assertEquals(shot1.id, result[0].id)
            Assertions.assertEquals(shot1.shotName, result[0].shotName)
            Assertions.assertEquals(shot2.id, result[1].id)
            Assertions.assertEquals(shot2.shotName, result[1].shotName)
            Assertions.assertEquals(shot3.id, result[2].id)
            Assertions.assertEquals(shot3.shotName, result[2].shotName)
        }

        @Test
        fun `when currentShotList has multiple items all should have isPending false`() {
            val shot1 = TestShotLogged.build().copy(id = 1, isPending = true)
            val shot2 = TestShotLogged.build().copy(id = 2, isPending = false)
            val shot3 = TestShotLogged.build().copy(id = 3, isPending = true)
            val currentShotList = listOf(shot1, shot2, shot3)

            val result = logShotViewModelExtImpl.currentShotLoggedRealtimeResponseList(currentShotList = currentShotList)

            Assertions.assertEquals(3, result.size)
            result.forEach { response ->
                Assertions.assertFalse(response.isPending)
            }
        }
    }

    @Nested
    inner class BuildPendingShotOnSave {

        @Test
        fun `should build PendingShot with correct values from state`() {
            val player = TestPlayer().create()
            val declaredShot = TestDeclaredShot.build()
            val state = LogShotState(
                shotName = "Test Shot",
                shotsAttempted = 15,
                shotsMade = 10,
                shotsMissed = 5,
                shotsMadePercentValue = "66.7%",
                shotsMissedPercentValue = "33.3%",
                shotsTakenDateValue = "Jan 1, 2024",
                shotsLoggedDateValue = "Jan 2, 2024"
            )

            logShotViewModelExtImpl.logShotInfo = LogShotInfo(isExistingPlayer = true)
            every { application.getString(StringsIds.empty) } returns ""
            val dateValue = dateFormat.parse("Jan 1, 2024")
            val loggedDateValue = dateFormat.parse("Jan 2, 2024")

            val result = logShotViewModelExtImpl.buildPendingShotOnSave(
                player = player,
                state = state,
                declaredShot = declaredShot
            )

            Assertions.assertEquals(player, result.player)
            Assertions.assertEquals("Test Shot", result.shotLogged.shotName)
            Assertions.assertEquals(declaredShot.id, result.shotLogged.shotType)
            Assertions.assertEquals(15, result.shotLogged.shotsAttempted)
            Assertions.assertEquals(10, result.shotLogged.shotsMade)
            Assertions.assertEquals(5, result.shotLogged.shotsMissed)
            Assertions.assertTrue(result.isPendingPlayer)
            Assertions.assertEquals(0, result.shotLogged.id)
            Assertions.assertTrue(result.shotLogged.isPending)
        }

        @Test
        fun `when declaredShot is null should use 0 for shotType`() {
            val player = TestPlayer().create()
            val state = LogShotState(shotName = "Test Shot")

            every { application.getString(StringsIds.empty) } returns ""

            val result = logShotViewModelExtImpl.buildPendingShotOnSave(
                player = player,
                state = state,
                declaredShot = null
            )

            Assertions.assertEquals(0, result.shotLogged.shotType)
        }
    }

    @Nested
    inner class InitializeShotLogged {

        @Test
        fun `should initialize ShotLogged with correct values from state`() {
            val declaredShot = TestDeclaredShot.build()
            val state = LogShotState(
                shotName = "Test Shot",
                shotsAttempted = 15,
                shotsMade = 10,
                shotsMissed = 5,
                shotsMadePercentValue = "66.7%",
                shotsMissedPercentValue = "33.3%",
                shotsTakenDateValue = "Jan 1, 2024",
                shotsLoggedDateValue = "Jan 2, 2024"
            )

            every { application.getString(StringsIds.empty) } returns ""
            val dateValue = dateFormat.parse("Jan 1, 2024")
            val loggedDateValue = dateFormat.parse("Jan 2, 2024")

            val result = logShotViewModelExtImpl.initializeShotLogged(
                state = state,
                declaredShot = declaredShot
            )

            Assertions.assertEquals("Test Shot", result.shotName)
            Assertions.assertEquals(declaredShot.id, result.shotType)
            Assertions.assertEquals(15, result.shotsAttempted)
            Assertions.assertEquals(10, result.shotsMade)
            Assertions.assertEquals(5, result.shotsMissed)
            Assertions.assertEquals(0, result.id)
            Assertions.assertTrue(result.isPending)
        }

        @Test
        fun `when declaredShot is null should use 0 for shotType`() {
            val state = LogShotState()

            every { application.getString(StringsIds.empty) } returns ""

            val result = logShotViewModelExtImpl.initializeShotLogged(
                state = state,
                declaredShot = null
            )

            Assertions.assertEquals(0, result.shotType)
        }
    }
}
