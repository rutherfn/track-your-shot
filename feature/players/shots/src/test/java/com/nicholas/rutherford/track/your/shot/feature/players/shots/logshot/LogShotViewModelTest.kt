
package com.nicholas.rutherford.track.your.shot.feature.players.shots.logshot

import android.app.Application
import com.nicholas.rutherford.track.your.shot.data.room.repository.DeclaredShotRepository
import com.nicholas.rutherford.track.your.shot.data.room.repository.PendingPlayerRepository
import com.nicholas.rutherford.track.your.shot.data.room.repository.PlayerRepository
import com.nicholas.rutherford.track.your.shot.data.shared.alert.Alert
import com.nicholas.rutherford.track.your.shot.data.shared.alert.AlertConfirmAndDismissButton
import com.nicholas.rutherford.track.your.shot.data.shared.progress.Progress
import com.nicholas.rutherford.track.your.shot.data.test.room.TestDeclaredShot
import com.nicholas.rutherford.track.your.shot.data.test.room.TestPlayer
import com.nicholas.rutherford.track.your.shot.feature.players.shots.logshot.pendingshot.CurrentPendingShot
import com.nicholas.rutherford.track.your.shot.feature.splash.StringsIds
import com.nicholas.rutherford.track.your.shot.helper.extensions.toDateValue
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.util.Date

@OptIn(ExperimentalCoroutinesApi::class)
class LogShotViewModelTest {

    private lateinit var logShotViewModel: LogShotViewModel

    private val application = mockk<Application>(relaxed = true)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val testDispatcher = UnconfinedTestDispatcher()

    private val scope = CoroutineScope(SupervisorJob() + testDispatcher)

    private val navigation = mockk<LogShotNavigation>(relaxed = true)

    private val declaredShotRepository = mockk<DeclaredShotRepository>(relaxed = true)
    private val pendingPlayerRepository = mockk<PendingPlayerRepository>(relaxed = true)
    private val playerRepository = mockk<PlayerRepository>(relaxed = true)

    private val currentPendingShot = mockk<CurrentPendingShot>(relaxed = true)

    @BeforeEach
    fun beforeEach() {
        logShotViewModel = LogShotViewModel(
            application = application,
            scope = scope,
            navigation = navigation,
            declaredShotRepository = declaredShotRepository,
            pendingPlayerRepository = pendingPlayerRepository,
            playerRepository = playerRepository,
            currentPendingShot = currentPendingShot
        )
    }

    @Nested
    inner class UpdateIsExistingPlayerAndId {

        @Test
        fun `when declared shot returns null should not update state`() = runTest {
            val shotId = 2
            val playerId = 4

            coEvery { declaredShotRepository.fetchDeclaredShotFromId(id = shotId) } returns null

            logShotViewModel.updateIsExistingPlayerAndId(
                isExistingPlayerArgument = false,
                playerIdArgument = playerId,
                shotIdArgument = shotId
            )

            Assertions.assertEquals(logShotViewModel.logShotMutableStateFlow.value, LogShotState())
        }

        @Test
        fun `when player returns null should not update state`() = runTest {
            val shotId = 2
            val playerId = 4

            coEvery { declaredShotRepository.fetchDeclaredShotFromId(id = shotId) } returns TestDeclaredShot.build()
            coEvery { playerRepository.fetchPlayerById(id = playerId) } returns null

            logShotViewModel.updateIsExistingPlayerAndId(
                isExistingPlayerArgument = true,
                playerIdArgument = playerId,
                shotIdArgument = shotId
            )

            Assertions.assertEquals(logShotViewModel.logShotMutableStateFlow.value, LogShotState())
        }

        @Test
        fun `when declaredShot is not null and player from fetch player by id is not null should update state`() =
            runTest {
                val shotId = 2
                val playerId = 4

                coEvery { declaredShotRepository.fetchDeclaredShotFromId(id = shotId) } returns TestDeclaredShot.build()
                coEvery { playerRepository.fetchPlayerById(id = playerId) } returns TestPlayer().create()

                logShotViewModel.updateIsExistingPlayerAndId(
                    isExistingPlayerArgument = true,
                    playerIdArgument = playerId,
                    shotIdArgument = shotId
                )

                Assertions.assertEquals(
                    logShotViewModel.logShotMutableStateFlow.value,
                    LogShotState(
                        shotName = "Hook Shot",
                        playerName = "first, last",
                        playerPosition = StringsIds.center,
                        shotsLoggedDateValue = LocalDate.now().toDateValue() ?: "",
                        shotsTakenDateValue = "",
                        shotsMade = 0,
                        shotsMissed = 0,
                        shotsAttempted = 0,
                        shotsMadePercentValue = "",
                        shotsMissedPercentValue = ""
                    )
                )
            }

        @Test
        fun `when declaredShot is not null and player from fetch pending player by id is not null should update state`() =
            runTest {
                val shotId = 2
                val playerId = 4

                coEvery { declaredShotRepository.fetchDeclaredShotFromId(id = shotId) } returns TestDeclaredShot.build()
                coEvery { pendingPlayerRepository.fetchPlayerById(id = playerId) } returns TestPlayer().create()

                logShotViewModel.updateIsExistingPlayerAndId(
                    isExistingPlayerArgument = false,
                    playerIdArgument = playerId,
                    shotIdArgument = shotId
                )

                Assertions.assertEquals(
                    logShotViewModel.logShotMutableStateFlow.value,
                    LogShotState(
                        shotName = "Hook Shot",
                        playerName = "first, last",
                        playerPosition = StringsIds.center,
                        shotsLoggedDateValue = LocalDate.now().toDateValue() ?: "",
                        shotsTakenDateValue = "",
                        shotsMade = 0,
                        shotsMissed = 0,
                        shotsAttempted = 0,
                        shotsMadePercentValue = "",
                        shotsMissedPercentValue = ""
                    )
                )
            }
    }

    @Nested
    inner class ShotsAttempted {

        @Test
        fun `when shotsMissed is not equal to zero should return total value`() {
            val result = logShotViewModel.shotsAttempted(shotsMade = 0, shotsMissed = 2)

            Assertions.assertEquals(result, 2)
        }

        @Test
        fun `when shotsMade is not equal to zero should return total value`() {
            val result = logShotViewModel.shotsAttempted(shotsMade = 2, shotsMissed = 0)

            Assertions.assertEquals(result, 2)
        }

        @Test
        fun `when shotsMade and shotsMissed is equal to zero should return zero`() {
            val result = logShotViewModel.shotsAttempted(shotsMade = 0, shotsMissed = 0)

            Assertions.assertEquals(result, 0)
        }
    }

    @Nested
    inner class ShotsPercentValue {

        @Test
        fun `when isShotsMade is set to true and both values are not 0 should calculate percent`() {
            val result = logShotViewModel.shotsPercentValue(
                shotsMade = 5.0,
                shotsMissed = 3.0,
                isShotsMade = true
            )

            Assertions.assertEquals(result, 62.5)
        }

        @Test
        fun `when isShotsMade is set to false and both values are not 0 should calculate percent`() {
            val result = logShotViewModel.shotsPercentValue(
                shotsMade = 5.0,
                shotsMissed = 3.0,
                isShotsMade = false
            )

            Assertions.assertEquals(result, 37.5)
        }

        @Test
        fun `when isShotMade is set to true but shotsMade is 0 should return default value`() {
            val result = logShotViewModel.shotsPercentValue(
                shotsMade = 0.0,
                shotsMissed = 3.0,
                isShotsMade = true
            )

            Assertions.assertEquals(result, 0.0)
        }

        @Test
        fun `when isShotMade is set to true but shotsMissed is 0 should return default value`() {
            val result = logShotViewModel.shotsPercentValue(
                shotsMade = 1.0,
                shotsMissed = 0.0,
                isShotsMade = true
            )

            Assertions.assertEquals(result, 0.0)
        }

        @Test
        fun `when isShotMade is set to false but shotsMissed is 0 should return default value`() {
            val result = logShotViewModel.shotsPercentValue(
                shotsMade = 1.0,
                shotsMissed = 0.0,
                isShotsMade = false
            )

            Assertions.assertEquals(result, 0.0)
        }

        @Test
        fun `when isShotMade is set to false but shotsMade is 0 should return default value`() {
            val result = logShotViewModel.shotsPercentValue(
                shotsMade = 0.0,
                shotsMissed = 1.0,
                isShotsMade = false
            )

            Assertions.assertEquals(result, 0.0)
        }
    }

    @Test
    fun `update state after shots made input should update state`() {
        val shots = 2

        logShotViewModel.updateStateAfterShotsMadeInput(shots = shots)

        val state = logShotViewModel.logShotMutableStateFlow.value
        Assertions.assertEquals(
            state,
            LogShotState(
                shotName = "",
                playerName = "",
                shotsLoggedDateValue = "",
                shotsTakenDateValue = "",
                shotsMade = shots,
                shotsAttempted = 2,
                shotsMadePercentValue = "",
                shotsMissedPercentValue = ""
            )
        )
    }

    @Nested
    inner class PercentageFormat {

        @Test
        fun `when percent is the default value should return empty string`() {
            every { application.getString(StringsIds.empty) } returns ""

            val result = logShotViewModel.percentageFormat(
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

            val result = logShotViewModel.percentageFormat(
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

    @Nested
    inner class StartingInputAmount {

        @Test
        fun `when currentShotsMissed is greater then 0 should return currentShotsMissed`() {
            val currentShotsMissed = 2
            val value = logShotViewModel.startingInputAmount(amount = currentShotsMissed)

            Assertions.assertEquals(value, currentShotsMissed)
        }

        @Test
        fun `when currentShotMissed is zero should return null`() {
            val currentShotsMissed = 0
            val value = logShotViewModel.startingInputAmount(amount = currentShotsMissed)

            Assertions.assertEquals(value, null)
        }
    }

    @Test
    fun `on shots made clicked should start inputInfo`() {
        logShotViewModel.onShotsMadeClicked()

        verify { navigation.inputInfo(inputInfo = any()) }
    }

    @Test
    fun `on shots missed clicked should start inputInfo`() {
        logShotViewModel.onShotsMissedClicked()

        verify { navigation.inputInfo(inputInfo = any()) }
    }

    @Test
    fun `invalid shot alert should return alert`() {
        val description = "description"

        every { application.getString(StringsIds.empty) } returns ""
        every { application.getString(StringsIds.gotIt) } returns "Got It"

        Assertions.assertEquals(
            logShotViewModel.invalidLogShotAlert(description = description),
            Alert(
                title = "",
                dismissButton = AlertConfirmAndDismissButton(buttonText = "Got It"),
                description = description
            )
        )
    }

    @Nested
    inner class ShotEntryInvalidAlert {

        @Test
        fun `when description is not set to null should return null alert`() {
            Assertions.assertEquals(
                logShotViewModel.shotEntryInvalidAlert(
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
                logShotViewModel.shotEntryInvalidAlert(
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
                logShotViewModel.shotEntryInvalidAlert(
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
                logShotViewModel.shotEntryInvalidAlert(
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

    @Nested
    inner class OnSaveClicked {

        @Test
        fun `when currentPlayer is null should show alert`() = runTest {
            logShotViewModel.currentPlayer = null

            logShotViewModel.onSaveClicked()

            verify { navigation.alert(alert = any()) }
        }

        @Test
        fun `when currentPlayer is not null but logged shot is invalid should show alert`() = runTest {
            logShotViewModel.currentDeclaredShot = TestDeclaredShot.build()
            logShotViewModel.currentPlayer = TestPlayer().create()

            val description = "You haven\'t recorded any missed shots. Please input the number of shots missed to proceed with logging the shot."

            every { application.getString(StringsIds.empty) } returns ""
            every { application.getString(StringsIds.gotIt) } returns "Got It"
            every { application.getString(StringsIds.missedShotsNotRecordedDescription) } returns description

            logShotViewModel.logShotMutableStateFlow.value = LogShotState(
                shotsMade = 5,
                shotsMissed = 2,
                shotsAttempted = 4,
                shotsTakenDateValue = "Jun 4, 2019",
                shotsLoggedDateValue = "June 4, 2019",
                shotsMadePercentValue = "100%",
                shotsMissedPercentValue = "100%"
            )

            logShotViewModel.onSaveClicked()

            verify { navigation.enableProgress(progress = Progress()) }
            verify { currentPendingShot.createShot(any()) }
            verify { logShotViewModel.navigateToCreateOrEditPlayer() }
        }

        @Test
        fun `when currentPlayer is not null and logged shot is valid should create a new pending show and call navigateToCreateorEditPlayer`() {
            logShotViewModel.currentDeclaredShot = TestDeclaredShot.build()
            logShotViewModel.currentPlayer = TestPlayer().create()

            logShotViewModel.logShotMutableStateFlow.value = LogShotState(
                shotsMade = 5,
                shotsMissed = 5,
                shotsTakenDateValue = "Jun 4, 2019"
            )
        }
    }

    @Nested
    inner class ConvertPercentageToDouble {

        @Test
        fun `when percentage does not contain a percent sign should return default value`() {
            Assertions.assertEquals(
                logShotViewModel.convertPercentageToDouble(percentage = "1"),
                0.0
            )
        }

        @Test
        fun `if value contains a period should convert it to a percent value`() {
            Assertions.assertEquals(
                logShotViewModel.convertPercentageToDouble(percentage = "33.3%"),
                33.3
            )
        }

        @Test
        fun `if value contains no period should convert it to a percent value with addition of a zero`() {
            Assertions.assertEquals(
                logShotViewModel.convertPercentageToDouble(percentage = "1%"),
                1.0
            )
        }
    }

    @Nested
    inner class NavigateToCreateOrEditPlayer {

        @Test
        fun `when isExistingPlayer is set to true should call navigateToCreateEditPlayer`() {
            logShotViewModel.isExistingPlayer = true

            logShotViewModel.navigateToCreateOrEditPlayer()

            verify { navigation.disableProgress() }
            verify { navigation.popToEditPlayer() }
        }

        @Test
        fun `when isExistingPlayer is set to false should call pop to create player`() {
            logShotViewModel.isExistingPlayer = false

            logShotViewModel.navigateToCreateOrEditPlayer()

            verify { navigation.disableProgress() }
            verify { navigation.popToCreatePlayer() }
        }
    }

    @Nested
    inner class ConvertValueToDate {

        @Test
        fun `when value is empty should return null`() {
            Assertions.assertEquals(
                logShotViewModel.convertValueToDate(value = ""),
                null
            )
        }

        // todo -> Figure how we can unit test the dates via CI run because it fails given the date as of now
//        @Test
//        fun `when value is not empty should return back a date`() {
//            Assertions.assertEquals(
//                logShotViewModel.convertValueToDate(value = "Jun 4, 2019"),
//                Date(1559624400000)
//            )
//        }
    }

    @Test
    fun `on back clicked should pop stack`() {
        logShotViewModel.onBackClicked()

        Assertions.assertEquals(logShotViewModel.logShotMutableStateFlow.value, LogShotState())

        verify { navigation.pop() }
    }
}
