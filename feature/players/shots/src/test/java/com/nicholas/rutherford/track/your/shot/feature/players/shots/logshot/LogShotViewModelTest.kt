
package com.nicholas.rutherford.track.your.shot.feature.players.shots.logshot

import android.app.Application
import com.nicholas.rutherford.track.your.shot.base.resources.StringsIds
import com.nicholas.rutherford.track.your.shot.data.room.repository.ActiveUserRepository
import com.nicholas.rutherford.track.your.shot.data.room.repository.DeclaredShotRepository
import com.nicholas.rutherford.track.your.shot.data.room.repository.PendingPlayerRepository
import com.nicholas.rutherford.track.your.shot.data.room.repository.PlayerRepository
import com.nicholas.rutherford.track.your.shot.data.room.response.ShotLogged
import com.nicholas.rutherford.track.your.shot.data.shared.alert.Alert
import com.nicholas.rutherford.track.your.shot.data.shared.alert.AlertConfirmAndDismissButton
import com.nicholas.rutherford.track.your.shot.data.test.room.TestDeclaredShot
import com.nicholas.rutherford.track.your.shot.data.test.room.TestPlayer
import com.nicholas.rutherford.track.your.shot.data.test.room.TestShotLogged
import com.nicholas.rutherford.track.your.shot.feature.players.shots.logshot.extension.LogShotInfo
import com.nicholas.rutherford.track.your.shot.feature.players.shots.logshot.extension.LogShotViewModelExt
import com.nicholas.rutherford.track.your.shot.feature.players.shots.logshot.pendingshot.CurrentPendingShot
import com.nicholas.rutherford.track.your.shot.feature.players.shots.logshot.pendingshot.PendingShot
import com.nicholas.rutherford.track.your.shot.firebase.core.delete.DeleteFirebaseUserInfo
import com.nicholas.rutherford.track.your.shot.firebase.core.update.UpdateFirebaseUserInfo
import com.nicholas.rutherford.track.your.shot.helper.extensions.dataadditionupdates.DataAdditionUpdates
import com.nicholas.rutherford.track.your.shot.helper.extensions.toDateValue
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Locale

@OptIn(ExperimentalCoroutinesApi::class)
class LogShotViewModelTest {

    private lateinit var logShotViewModel: LogShotViewModel

    private val datePattern = "MMMM dd, yyyy"
    private val dateFormat = SimpleDateFormat(datePattern, Locale.ENGLISH)

    private val application = mockk<Application>(relaxed = true)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val testDispatcher = UnconfinedTestDispatcher()

    private val scope = CoroutineScope(SupervisorJob() + testDispatcher)

    private val navigation = mockk<LogShotNavigation>(relaxed = true)

    private val declaredShotRepository = mockk<DeclaredShotRepository>(relaxed = true)
    private val pendingPlayerRepository = mockk<PendingPlayerRepository>(relaxed = true)
    private val playerRepository = mockk<PlayerRepository>(relaxed = true)
    private val dataAdditionUpdates = mockk<DataAdditionUpdates>(relaxed = true)

    private val activeUserRepository = mockk<ActiveUserRepository>(relaxed = true)
    private val updateFirebaseUserInfo = mockk<UpdateFirebaseUserInfo>(relaxed = true)

    private val deleteFirebaseUserInfo = mockk<DeleteFirebaseUserInfo>(relaxed = true)

    private val currentPendingShot = mockk<CurrentPendingShot>(relaxed = true)

    private val logShotViewModelExt = mockk<LogShotViewModelExt>(relaxed = true)

    @BeforeEach
    fun beforeEach() {
        logShotViewModel = LogShotViewModel(
            application = application,
            scope = scope,
            navigation = navigation,
            declaredShotRepository = declaredShotRepository,
            pendingPlayerRepository = pendingPlayerRepository,
            dataAdditionUpdates = dataAdditionUpdates,
            playerRepository = playerRepository,
            activeUserRepository = activeUserRepository,
            updateFirebaseUserInfo = updateFirebaseUserInfo,
            deleteFirebaseUserInfo = deleteFirebaseUserInfo,
            currentPendingShot = currentPendingShot,
            logShotViewModelExt = logShotViewModelExt
        )
    }

    @Nested
    inner class UpdateIsExistingPlayerAndId {

        @Test
        fun `when declared shot returns null should not update state`() = runTest {
            val shotId = 2
            val playerId = 4
            val shotType = 4
            val viewCurrentExistingShot = false
            val viewCurrentPendingShot = false
            val fromShot = false

            coEvery { logShotViewModelExt.logShotInfo } returns LogShotInfo(
                isExistingPlayer = false,
                playerId = playerId,
                shotType = shotType,
                viewCurrentExistingShot = viewCurrentExistingShot,
                viewCurrentPendingShot = viewCurrentPendingShot,
                fromShotList = fromShot
            )
            coEvery { declaredShotRepository.fetchDeclaredShotFromId(id = shotType) } returns null

            logShotViewModel.updateIsExistingPlayerAndId(
                isExistingPlayerArgument = false,
                playerIdArgument = playerId,
                shotTypeArgument = shotType,
                shotIdArgument = shotId,
                viewCurrentExistingShotArgument = viewCurrentExistingShot,
                viewCurrentPendingShotArgument = viewCurrentPendingShot,
                fromShotListArgument = fromShot
            )

            Assertions.assertEquals(logShotViewModel.logShotMutableStateFlow.value, LogShotState())
        }

        @Test
        fun `when player returns null should not update state`() = runTest {
            val shotId = 2
            val playerId = 4
            val shotType = 4
            val viewCurrentExistingShot = false
            val viewCurrentPendingShot = false
            val fromShot = false

            coEvery { logShotViewModelExt.logShotInfo } returns LogShotInfo(
                isExistingPlayer = true,
                playerId = playerId,
                shotType = shotType,
                viewCurrentExistingShot = viewCurrentExistingShot,
                viewCurrentPendingShot = viewCurrentPendingShot,
                fromShotList = fromShot
            )
            coEvery { declaredShotRepository.fetchDeclaredShotFromId(id = shotType) } returns TestDeclaredShot.build()
            coEvery { playerRepository.fetchPlayerById(id = playerId) } returns null

            logShotViewModel.updateIsExistingPlayerAndId(
                isExistingPlayerArgument = true,
                playerIdArgument = playerId,
                shotTypeArgument = shotType,
                shotIdArgument = shotId,
                viewCurrentExistingShotArgument = viewCurrentExistingShot,
                viewCurrentPendingShotArgument = viewCurrentPendingShot,
                fromShotListArgument = fromShot
            )

            Assertions.assertEquals(logShotViewModel.logShotMutableStateFlow.value, LogShotState())
        }

        @Test
        fun `when declaredShot is not null and player from fetch player by id is not null should update state`() =
            runTest {
                val shotId = 2
                val playerId = 4
                val shotType = 9
                val viewCurrentExistingShot = false
                val viewCurrentPendingShot = false
                val fromShot = false

                coEvery { logShotViewModelExt.logShotInfo } returns LogShotInfo(
                    isExistingPlayer = true,
                    playerId = playerId,
                    shotType = shotType,
                    viewCurrentExistingShot = viewCurrentExistingShot,
                    viewCurrentPendingShot = viewCurrentPendingShot,
                    fromShotList = fromShot
                )
                coEvery { declaredShotRepository.fetchDeclaredShotFromId(id = shotType) } returns TestDeclaredShot.build()
                coEvery { playerRepository.fetchPlayerById(id = playerId) } returns TestPlayer().create()

                logShotViewModel.updateIsExistingPlayerAndId(
                    isExistingPlayerArgument = true,
                    playerIdArgument = playerId,
                    shotTypeArgument = shotType,
                    shotIdArgument = shotId,
                    viewCurrentExistingShotArgument = viewCurrentExistingShot,
                    viewCurrentPendingShotArgument = viewCurrentPendingShot,
                    fromShotListArgument = fromShot
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
                val shotType = 11
                val viewCurrentExistingShot = false
                val viewCurrentPendingShot = false
                val fromShot = false

                coEvery { logShotViewModelExt.logShotInfo } returns LogShotInfo(
                    isExistingPlayer = false,
                    playerId = playerId,
                    shotType = shotType,
                    viewCurrentExistingShot = viewCurrentExistingShot,
                    viewCurrentPendingShot = viewCurrentPendingShot,
                    fromShotList = fromShot
                )
                coEvery { declaredShotRepository.fetchDeclaredShotFromId(id = shotType) } returns TestDeclaredShot.build()
                coEvery { pendingPlayerRepository.fetchPlayerById(id = playerId) } returns TestPlayer().create()

                logShotViewModel.updateIsExistingPlayerAndId(
                    isExistingPlayerArgument = false,
                    playerIdArgument = playerId,
                    shotTypeArgument = shotType,
                    shotIdArgument = shotId,
                    viewCurrentExistingShotArgument = viewCurrentExistingShot,
                    viewCurrentPendingShotArgument = viewCurrentPendingShot,
                    fromShotListArgument = fromShot
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

    @Test
    fun `on shots made upward or downward clicked should update state`() {
        val shots = 2

        every { logShotViewModelExt.shotsAttempted(shotsMade = shots, shotsMissed = 0) } returns 2

        logShotViewModel.onShotsMadeUpwardOrDownwardClicked(shots = shots)

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

    @Test
    fun `on shots missed upward or downward clicked should update state`() {
        val shots = 2

        every { logShotViewModelExt.shotsAttempted(shotsMade = 0, shotsMissed = shots) } returns 2

        logShotViewModel.onShotsMissedUpwardOrDownwardClicked(shots = shots)

        val state = logShotViewModel.logShotMutableStateFlow.value
        Assertions.assertEquals(
            state,
            LogShotState(
                shotName = "",
                playerName = "",
                shotsLoggedDateValue = "",
                shotsTakenDateValue = "",
                shotsMissed = shots,
                shotsAttempted = 2,
                shotsMadePercentValue = "",
                shotsMissedPercentValue = ""
            )
        )
    }

    @Test
    fun `disable progress and show alert`() {
        val alert = Alert(
            title = "",
            dismissButton = AlertConfirmAndDismissButton(buttonText = "Got It"),
            description = "Test"
        )

        logShotViewModel.disableProgressAndShowAlert(alert = alert)

        verify { navigation.disableProgress() }
        verify { navigation.alert(alert = alert) }
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
            verify { navigation.alert(alert = any()) }
        }

        @Test
        fun `when currentPlayer is not null, logged shot is valid, and viewCurrentExistingShot set to true should call create pending shot`() = runTest {
            val dateValue = "Jun 4, 2019"
            val date = dateFormat.parse(dateValue)

            logShotViewModel.currentDeclaredShot = TestDeclaredShot.build()
            logShotViewModel.currentPlayer = TestPlayer().create()

            val pendingShot = PendingShot(
                player = TestPlayer().create(),
                shotLogged = ShotLogged(
                    id = 0,
                    shotName = "shotName",
                    shotType = 1,
                    shotsAttempted = 4,
                    shotsMade = 5,
                    shotsMissed = 2,
                    shotsMadePercentValue = 33.3,
                    shotsMissedPercentValue = 66.67,
                    shotsAttemptedMillisecondsValue = date?.time ?: 0L,
                    shotsLoggedMillisecondsValue = date?.time ?: 0L,
                    isPending = true
                ),
                isPendingPlayer = false
            )

            every { logShotViewModelExt.logShotInfo } returns LogShotInfo(viewCurrentExistingShot = true, isExistingPlayer = false)
            every { logShotViewModelExt.convertValueToDate(value = dateValue) } returns date
            every { logShotViewModelExt.convertPercentageToDouble(percentage = "33.3") } returns 33.3
            every { logShotViewModelExt.convertPercentageToDouble(percentage = "66.67") } returns 66.67
            every { logShotViewModelExt.noChangesForShotAlert(initialShotLogged = logShotViewModel.initialShotLogged, pendingShotLogged = pendingShot.shotLogged) } returns null
            every { logShotViewModelExt.shotEntryInvalidAlert(shotsMade = 5, shotsMissed = 2, shotsAttemptedMillisecondsValue = date?.time ?: 0L) } returns null
            every { currentPendingShot.fetchPendingShots() } returns listOf(pendingShot)

            logShotViewModel.logShotMutableStateFlow.value = LogShotState(
                shotName = "shotName",
                shotsMade = 5,
                shotsMissed = 2,
                shotsAttempted = 4,
                shotsTakenDateValue = "Jun 4, 2019",
                shotsLoggedDateValue = "Jun 4, 2019",
                shotsMadePercentValue = "33.3",
                shotsMissedPercentValue = "66.67"
            )

            logShotViewModel.onSaveClicked()

            verify { navigation.enableProgress(progress = any()) }
            verify { currentPendingShot.createShot(shotLogged = any()) }
            verify { navigation.disableProgress() }
            verify { navigation.popToCreatePlayer() }
        }

        @Test
        fun `when currentPlayer is not null, logged shot is valid, and viewCurrentPendingShot set to true should call create pending shot`() = runTest {
            val dateValue = "Jun 4, 2019"
            val date = dateFormat.parse(dateValue)

            logShotViewModel.currentDeclaredShot = TestDeclaredShot.build()
            logShotViewModel.currentPlayer = TestPlayer().create()

            val pendingShot = PendingShot(
                player = TestPlayer().create(),
                shotLogged = ShotLogged(
                    id = 0,
                    shotName = "shotName",
                    shotType = 1,
                    shotsAttempted = 4,
                    shotsMade = 5,
                    shotsMissed = 2,
                    shotsMadePercentValue = 33.3,
                    shotsMissedPercentValue = 66.67,
                    shotsAttemptedMillisecondsValue = date?.time ?: 0L,
                    shotsLoggedMillisecondsValue = date?.time ?: 0L,
                    isPending = true
                ),
                isPendingPlayer = false
            )

            every { logShotViewModelExt.logShotInfo } returns LogShotInfo(viewCurrentPendingShot = true, isExistingPlayer = false)
            every { logShotViewModelExt.convertValueToDate(value = dateValue) } returns date
            every { logShotViewModelExt.convertPercentageToDouble(percentage = "33.3") } returns 33.3
            every { logShotViewModelExt.convertPercentageToDouble(percentage = "66.67") } returns 66.67
            every { logShotViewModelExt.noChangesForShotAlert(initialShotLogged = logShotViewModel.initialShotLogged, pendingShotLogged = pendingShot.shotLogged) } returns null
            every { currentPendingShot.fetchPendingShots() } returns listOf(pendingShot)
            every { logShotViewModelExt.shotEntryInvalidAlert(shotsMade = 5, shotsMissed = 2, shotsAttemptedMillisecondsValue = date?.time ?: 0L) } returns null

            logShotViewModel.logShotMutableStateFlow.value = LogShotState(
                shotName = "shotName",
                shotsMade = 5,
                shotsMissed = 2,
                shotsAttempted = 4,
                shotsTakenDateValue = "Jun 4, 2019",
                shotsLoggedDateValue = "Jun 4, 2019",
                shotsMadePercentValue = "33.3",
                shotsMissedPercentValue = "66.67"
            )

            logShotViewModel.onSaveClicked()

            verify { navigation.enableProgress(progress = any()) }
            coVerify { currentPendingShot.deleteShot(pendingShot) }
            coVerify { currentPendingShot.createShot(shotLogged = pendingShot.copy(shotLogged = pendingShot.shotLogged.copy(id = pendingShot.shotLogged.id))) }
            verify { navigation.disableProgress() }
            verify { navigation.popToCreatePlayer() }
        }

        @Test
        fun `when currentPlayer is not null, logged shot is valid, and no previous booleans are set to true should navigate to create edit player`() = runTest {
            val dateValue = "Jun 4, 2019"
            val date = dateFormat.parse(dateValue)
            val percentage = 100.0

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
                    shotsMadePercentValue = percentage,
                    shotsMissedPercentValue = percentage,
                    shotsAttemptedMillisecondsValue = date?.time ?: 0L,
                    shotsLoggedMillisecondsValue = date?.time ?: 0L,
                    isPending = true
                ),
                isPendingPlayer = false
            )

            every { logShotViewModelExt.convertValueToDate(value = dateValue) } returns date
            every { logShotViewModelExt.convertPercentageToDouble(percentage = "100%") } returns percentage
            every { logShotViewModelExt.shotEntryInvalidAlert(shotsMade = 5, shotsMissed = 2, shotsAttemptedMillisecondsValue = date?.time ?: 0L) } returns null

            logShotViewModel.logShotMutableStateFlow.value = LogShotState(
                shotName = "shotName",
                shotsMade = 5,
                shotsMissed = 2,
                shotsAttempted = 4,
                shotsTakenDateValue = "Jun 4, 2019",
                shotsLoggedDateValue = "Jun 4, 2019",
                shotsMadePercentValue = "100%",
                shotsMissedPercentValue = "100%"
            )

            logShotViewModel.onSaveClicked()

            verify {
                logShotViewModel.createPendingShot(
                    isACurrentPlayerShot = false,
                    pendingShot = pendingShot
                )
            }
        }
    }

    @Nested
    inner class HandleHasDeleteShotFirebaseResponse {

        @Test
        fun `when hasDeleted is set to false should show alert`() = runTest {
            val hasDeleted = false

            logShotViewModel.handleHasDeleteShotFirebaseResponse(hasDeleted = hasDeleted)

            verify { navigation.disableProgress() }
            verify { navigation.alert(alert = any()) }
        }

        @Test
        fun `when hasDeleted is set to true and fromShotList is set to false should pop to create player`() = runTest {
            val hasDeleted = true

            every { logShotViewModelExt.logShotInfo } returns LogShotInfo(fromShotList = false, isExistingPlayer = false)

            logShotViewModel.handleHasDeleteShotFirebaseResponse(hasDeleted = hasDeleted)

            verify { navigation.disableProgress() }
            verify { navigation.popToCreatePlayer() }
            verify { navigation.alert(alert = any()) }
        }

        @Test
        fun `when hasDeleted is set to true and fromShotList is set to true should pop and show alert`() = runTest {
            val hasDeleted = true

            every { logShotViewModelExt.logShotInfo } returns LogShotInfo(fromShotList = true)

            logShotViewModel.handleHasDeleteShotFirebaseResponse(hasDeleted = hasDeleted)

            verify { navigation.disableProgress() }
            verify { navigation.popToShotList() }
            verify { navigation.alert(alert = any()) }
        }
    }

    @Nested
    inner class NavigateToCreateOrEditPlayer {

        @Test
        fun `when isExistingPlayer is set to true should call navigateToCreateEditPlayer`() {
            every { logShotViewModelExt.logShotInfo } returns LogShotInfo(isExistingPlayer = true)

            logShotViewModel.navigateToCreateOrEditPlayer()

            verify { navigation.disableProgress() }
            verify { navigation.popToEditPlayer() }
        }

        @Test
        fun `when isExistingPlayer is set to false should call pop to create player`() {
            every { logShotViewModelExt.logShotInfo } returns LogShotInfo(isExistingPlayer = false)

            logShotViewModel.navigateToCreateOrEditPlayer()

            verify { navigation.disableProgress() }
            verify { navigation.popToCreatePlayer() }
        }
    }

    @Test
    fun `on back clicked should pop stack`() {
        logShotViewModel.onBackClicked()

        Assertions.assertEquals(logShotViewModel.logShotMutableStateFlow.value, LogShotState())

        verify { navigation.pop() }
    }

    @Nested
    inner class OnYesDeleteShot {

        @Test
        fun `when edited player is set to null should not navigate to alert`() = runTest {
            logShotViewModel.currentPlayer = null

            logShotViewModel.onYesDeleteShot()

            verify { navigation.enableProgress(progress = any()) }
            verify { navigation.disableProgress() }
            verify(exactly = 0) { navigation.alert(alert = any()) }
        }

        @Test
        fun `when edited player is not null and hasDeleted returns false should navigate to alert`() = runTest {
            val shotId = 22
            val currentPlayer = TestPlayer().create().copy(
                shotsLoggedList = listOf(
                    TestShotLogged.build().copy(id = 11),
                    TestShotLogged.build().copy(id = 22)
                )
            )
            val newPlayer = TestPlayer().create().copy(
                shotsLoggedList = listOf(
                    TestShotLogged.build().copy(id = 11)
                )
            )

            logShotViewModel.currentPlayer = currentPlayer

            every { logShotViewModelExt.logShotInfo } returns LogShotInfo(shotId = shotId)
            coEvery { playerRepository.updatePlayer(currentPlayer = currentPlayer, newPlayer = newPlayer) } just runs
            coEvery { deleteFirebaseUserInfo.deleteShot(playerKey = currentPlayer.firebaseKey, index = 21) } returns flowOf(value = false)

            logShotViewModel.onYesDeleteShot()

            verify { navigation.disableProgress() }
            verify { navigation.alert(alert = any()) }
        }

        @Test
        fun `when edited player is not null and hasDeleted returns true should pop and navigate to alert`() = runTest {
            val shotId = 22
            val currentPlayer = TestPlayer().create().copy(
                shotsLoggedList = listOf(
                    TestShotLogged.build().copy(id = 11),
                    TestShotLogged.build().copy(id = 22)
                )
            )
            val newPlayer = TestPlayer().create().copy(
                shotsLoggedList = listOf(
                    TestShotLogged.build().copy(id = 11)
                )
            )
            logShotViewModel.currentPlayer = currentPlayer

            every { logShotViewModelExt.logShotInfo } returns LogShotInfo(shotId = shotId, isExistingPlayer = true)
            coEvery { playerRepository.updatePlayer(currentPlayer = currentPlayer, newPlayer = newPlayer) } just runs
            coEvery { deleteFirebaseUserInfo.deleteShot(playerKey = currentPlayer.firebaseKey, index = 21) } returns flowOf(value = true)

            logShotViewModel.onYesDeleteShot()

            verify { navigation.disableProgress() }
            verify { navigation.popToEditPlayer() }
            verify { navigation.alert(alert = any()) }
        }
    }

    @Test
    fun `on delete shot clicked should call delete show alert on navigation`() {
        logShotViewModel.onDeleteShotClicked()

        verify { navigation.alert(alert = any()) }
    }
}
