
package com.nicholas.rutherford.track.your.shot.feature.players.shots.logshot

import android.app.Application
import com.nicholas.rutherford.track.your.shot.data.room.repository.DeclaredShotRepository
import com.nicholas.rutherford.track.your.shot.data.room.repository.PendingPlayerRepository
import com.nicholas.rutherford.track.your.shot.data.room.repository.PlayerRepository
import com.nicholas.rutherford.track.your.shot.data.room.response.ShotLogged
import com.nicholas.rutherford.track.your.shot.data.shared.alert.Alert
import com.nicholas.rutherford.track.your.shot.data.shared.alert.AlertConfirmAndDismissButton
import com.nicholas.rutherford.track.your.shot.data.test.room.TestDeclaredShot
import com.nicholas.rutherford.track.your.shot.data.test.room.TestPlayer
import com.nicholas.rutherford.track.your.shot.feature.players.shots.logshot.pendingshot.CurrentPendingShot
import com.nicholas.rutherford.track.your.shot.feature.players.shots.logshot.pendingshot.PendingShot
import com.nicholas.rutherford.track.your.shot.feature.splash.StringsIds
import com.nicholas.rutherford.track.your.shot.helper.extensions.toDateValue
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.time.LocalDate

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
            val viewCurrentExistingShot = false
            val viewCurrentPendingShot = false

            coEvery { declaredShotRepository.fetchDeclaredShotFromId(id = shotId) } returns null

            logShotViewModel.updateIsExistingPlayerAndId(
                isExistingPlayerArgument = false,
                playerIdArgument = playerId,
                shotIdArgument = shotId,
                viewCurrentExistingShotArgument = viewCurrentExistingShot,
                viewCurrentPendingShotArgument = viewCurrentPendingShot
            )

            Assertions.assertEquals(logShotViewModel.logShotMutableStateFlow.value, LogShotState())
        }

        @Test
        fun `when player returns null should not update state`() = runTest {
            val shotId = 2
            val playerId = 4
            val viewCurrentExistingShot = false
            val viewCurrentPendingShot = false

            coEvery { declaredShotRepository.fetchDeclaredShotFromId(id = shotId) } returns TestDeclaredShot.build()
            coEvery { playerRepository.fetchPlayerById(id = playerId) } returns null

            logShotViewModel.updateIsExistingPlayerAndId(
                isExistingPlayerArgument = true,
                playerIdArgument = playerId,
                shotIdArgument = shotId,
                viewCurrentExistingShotArgument = viewCurrentExistingShot,
                viewCurrentPendingShotArgument = viewCurrentPendingShot
            )

            Assertions.assertEquals(logShotViewModel.logShotMutableStateFlow.value, LogShotState())
        }

        @Test
        fun `when declaredShot is not null and player from fetch player by id is not null should update state`() =
            runTest {
                val shotId = 2
                val playerId = 4
                val viewCurrentExistingShot = false
                val viewCurrentPendingShot = false

                coEvery { declaredShotRepository.fetchDeclaredShotFromId(id = shotId) } returns TestDeclaredShot.build()
                coEvery { playerRepository.fetchPlayerById(id = playerId) } returns TestPlayer().create()

                logShotViewModel.updateIsExistingPlayerAndId(
                    isExistingPlayerArgument = true,
                    playerIdArgument = playerId,
                    shotIdArgument = shotId,
                    viewCurrentExistingShotArgument = viewCurrentExistingShot,
                    viewCurrentPendingShotArgument = viewCurrentPendingShot
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
                val viewCurrentExistingShot = false
                val viewCurrentPendingShot = false

                coEvery { declaredShotRepository.fetchDeclaredShotFromId(id = shotId) } returns TestDeclaredShot.build()
                coEvery { pendingPlayerRepository.fetchPlayerById(id = playerId) } returns TestPlayer().create()

                logShotViewModel.updateIsExistingPlayerAndId(
                    isExistingPlayerArgument = false,
                    playerIdArgument = playerId,
                    shotIdArgument = shotId,
                    viewCurrentExistingShotArgument = viewCurrentExistingShot,
                    viewCurrentPendingShotArgument = viewCurrentPendingShot
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

    // todo -> figure out to test these for date properties
//    @Nested
//    inner class UpdateStateForViewShot {
//
//        @Test
//        fun `when viewCurrentExistingShot and viewCurrentPendingShot is set to false should not update state`() = runTest {
//            val player = TestPlayer().create()
//
//            logShotViewModel.viewCurrentExistingShot = false
//            logShotViewModel.viewCurrentPendingShot = false
//
//            logShotViewModel.currentPlayer = player
//            logShotViewModel.shotId = player.shotsLoggedList.first().id
//
//            logShotViewModel.updateStateForViewShot()
//
//            Assertions.assertEquals(
//                logShotViewModel.logShotMutableStateFlow.value,
//                LogShotState()
//            )
//        }
//
//        @Test
//        fun `when viewCurrentExistingShot is true but currentPlayer is set to null should not update state`() = runTest {
//            logShotViewModel.viewCurrentExistingShot = true
//            logShotViewModel.viewCurrentPendingShot = false
//
//            logShotViewModel.currentPlayer = null
//            logShotViewModel.shotId = 0
//
//            logShotViewModel.updateStateForViewShot()
//
//            Assertions.assertEquals(
//                logShotViewModel.logShotMutableStateFlow.value,
//                LogShotState()
//            )
//        }
//
//        @Test
//        fun `when viewCurrentExistingShot is true and is currentPlayer is not set to null should update state`() = runTest {
//            val player = TestPlayer().create()
//
//            logShotViewModel.viewCurrentExistingShot = true
//            logShotViewModel.viewCurrentPendingShot = false
//
//            logShotViewModel.currentPlayer = player
//            logShotViewModel.shotId = player.shotsLoggedList.first().id
//
//            logShotViewModel.updateStateForViewShot()
//
//            Assertions.assertEquals(
//                logShotViewModel.logShotMutableStateFlow.value,
//                LogShotState(
//                    shotsLoggedDateValue = "December 31, 1969",
//                    shotsTakenDateValue = "December 31, 1969",
//                    shotsMade = 5,
//                    shotsMissed = 10,
//                    shotsAttempted = 15,
//                    shotsMadePercentValue = "", // can't be tested due to limitation with mocking
//                    shotsMissedPercentValue = "" // can't be tested due to limitation with mocking
//                )
//            )
//        }
//
//        @Test
//        fun `when viewCurrentPendingShot is true and pending shot state flow returns a empty list should not update state`() = runTest {
//            val pendingShotList: List<PendingShot> = emptyList()
//
//            logShotViewModel.viewCurrentExistingShot = false
//            logShotViewModel.viewCurrentPendingShot = true
//
//            coEvery { currentPendingShot.shotsStateFlow } returns flowOf(pendingShotList)
//
//            logShotViewModel.updateStateForViewShot()
//
//            Assertions.assertEquals(
//                logShotViewModel.logShotMutableStateFlow.value,
//                LogShotState()
//            )
//        }
//
//        @Test
//        fun `when viewCurrentPendingShot is true and and pending shot flow returns a non empty list should update state`() = runTest {
//            val pendingShotList: List<PendingShot> = listOf(
//                PendingShot(
//                    player = TestPlayer().create(),
//                    shotLogged = TestShotLogged.build(),
//                    isPendingPlayer = true
//                )
//            )
//
//            logShotViewModel.viewCurrentExistingShot = false
//            logShotViewModel.viewCurrentPendingShot = true
//
//            coEvery { currentPendingShot.shotsStateFlow } returns flowOf(pendingShotList)
//
//            logShotViewModel.updateStateForViewShot()
//
//            Assertions.assertEquals(
//                logShotViewModel.logShotMutableStateFlow.value,
//                LogShotState(
//                    shotsLoggedDateValue = "December 31, 1969",
//                    shotsTakenDateValue = "December 31, 1969",
//                    shotsMade = 5,
//                    shotsMissed = 10,
//                    shotsAttempted = 15,
//                    shotsMadePercentValue = "", // can't be tested due to limitation with mocking
//                    shotsMissedPercentValue = "" // can't be tested due to limitation with mocking
//                )
//            )
//        }
//    }

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
        fun `when currentPlayer is not null, logged shot is invalid should show a alert`() = runTest {
            logShotViewModel.currentPlayer = TestPlayer().create()

            val description = "You haven\'t recorded any missed shots. Please input the number of shots missed to proceed with logging the shot."
            val alert = Alert(
                title = "",
                dismissButton = AlertConfirmAndDismissButton(buttonText = "Got It"),
                description = description
            )

            every { application.getString(StringsIds.empty) } returns ""
            every { application.getString(StringsIds.gotIt) } returns "Got It"
            every { application.getString(StringsIds.missedShotsNotRecordedDescription) } returns description

            logShotViewModel.logShotMutableStateFlow.value = LogShotState(
                shotName = "shotName",
                shotsMade = 1,
                shotsMissed = 0,
                shotsAttempted = 4,
                shotsTakenDateValue = "Jun 4, 2019",
                shotsLoggedDateValue = "June 4, 2019",
                shotsMadePercentValue = "100%",
                shotsMissedPercentValue = "100%"
            )

            logShotViewModel.onSaveClicked()

            verify { navigation.disableProgress() }
            verify { navigation.alert(alert = alert) }
        }

        @Test
        fun `when currentPlayer is not null, logged shot is valid, and viewCurrentExistingShot set to true should call create pending shot`() = runTest {
            logShotViewModel.currentDeclaredShot = TestDeclaredShot.build()
            logShotViewModel.currentPlayer = TestPlayer().create()

            logShotViewModel.viewCurrentExistingShot = true

            val pendingShot = PendingShot(
                player = TestPlayer().create(),
                shotLogged = ShotLogged(
                    id = 1,
                    shotName = "shotName",
                    shotType = 1,
                    shotsAttempted = 4,
                    shotsMade = 5,
                    shotsMissed = 2,
                    shotsMadePercentValue = logShotViewModel.convertPercentageToDouble(percentage = "100%"),
                    shotsMissedPercentValue = logShotViewModel.convertPercentageToDouble(percentage = "100%"),
                    shotsAttemptedMillisecondsValue = logShotViewModel.convertValueToDate(value = "June 4, 2019")?.time
                        ?: 0L,
                    shotsLoggedMillisecondsValue = logShotViewModel.convertValueToDate(value = "June 4, 2019")?.time
                        ?: 0L,
                    isPending = true
                ),
                isPendingPlayer = false
            )

            logShotViewModel.logShotMutableStateFlow.value = LogShotState(
                shotName = "shotName",
                shotsMade = 5,
                shotsMissed = 2,
                shotsAttempted = 4,
                shotsTakenDateValue = "Jun 4, 2019",
                shotsLoggedDateValue = "June 4, 2019",
                shotsMadePercentValue = "100%",
                shotsMissedPercentValue = "100%"
            )

            logShotViewModel.onSaveClicked()

            verify { logShotViewModel.createPendingShot(
                isACurrentPlayerShot = true,
                pendingShot = pendingShot
            )
            }
        }

        @Test
        fun `when currentPlayer is not null, logged shot is valid, and viewCurrentPendingShot set to true should call create pending shot`() = runTest {
            logShotViewModel.currentDeclaredShot = TestDeclaredShot.build()
            logShotViewModel.currentPlayer = TestPlayer().create()

            logShotViewModel.viewCurrentPendingShot = true

            val pendingShot = PendingShot(
                player = TestPlayer().create(),
                shotLogged = ShotLogged(
                    id = 1,
                    shotName = "shotName",
                    shotType = 1,
                    shotsAttempted = 4,
                    shotsMade = 5,
                    shotsMissed = 2,
                    shotsMadePercentValue = logShotViewModel.convertPercentageToDouble(percentage = "100%"),
                    shotsMissedPercentValue = logShotViewModel.convertPercentageToDouble(percentage = "100%"),
                    shotsAttemptedMillisecondsValue = logShotViewModel.convertValueToDate(value = "June 4, 2019")?.time
                        ?: 0L,
                    shotsLoggedMillisecondsValue = logShotViewModel.convertValueToDate(value = "June 4, 2019")?.time
                        ?: 0L,
                    isPending = true
                ),
                isPendingPlayer = false
            )

            currentPendingShot.shotsStateFlow

            every { currentPendingShot.shotsStateFlow } returns flowOf(listOf(pendingShot))

            logShotViewModel.logShotMutableStateFlow.value = LogShotState(
                shotName = "shotName",
                shotsMade = 5,
                shotsMissed = 2,
                shotsAttempted = 4,
                shotsTakenDateValue = "Jun 4, 2019",
                shotsLoggedDateValue = "June 4, 2019",
                shotsMadePercentValue = "100%",
                shotsMissedPercentValue = "100%"
            )

            logShotViewModel.onSaveClicked()

            coVerify { logShotViewModel.updatePendingShot(pendingShot = pendingShot) }
        }

        @Test
        fun `when currentPlayer is not null, logged shot is valid, and no previous booleans are set to true should navigate to create edit player`() = runTest {
            logShotViewModel.currentDeclaredShot = TestDeclaredShot.build()
            logShotViewModel.currentPlayer = TestPlayer().create()

            val pendingShot = PendingShot(
                player = TestPlayer().create(),
                shotLogged = ShotLogged(
                    id = 1,
                    shotName = "shotName",
                    shotType = 1,
                    shotsAttempted = 4,
                    shotsMade = 5,
                    shotsMissed = 2,
                    shotsMadePercentValue = logShotViewModel.convertPercentageToDouble(percentage = "100%"),
                    shotsMissedPercentValue = logShotViewModel.convertPercentageToDouble(percentage = "100%"),
                    shotsAttemptedMillisecondsValue = logShotViewModel.convertValueToDate(value = "June 4, 2019")?.time
                        ?: 0L,
                    shotsLoggedMillisecondsValue = logShotViewModel.convertValueToDate(value = "June 4, 2019")?.time
                        ?: 0L,
                    isPending = true
                ),
                isPendingPlayer = false
            )

            logShotViewModel.logShotMutableStateFlow.value = LogShotState(
                shotName = "shotName",
                shotsMade = 5,
                shotsMissed = 2,
                shotsAttempted = 4,
                shotsTakenDateValue = "Jun 4, 2019",
                shotsLoggedDateValue = "June 4, 2019",
                shotsMadePercentValue = "100%",
                shotsMissedPercentValue = "100%"
            )

            logShotViewModel.onSaveClicked()

            verify { logShotViewModel.createPendingShot(
                isACurrentPlayerShot = false,
                pendingShot = pendingShot
            )
            }
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
