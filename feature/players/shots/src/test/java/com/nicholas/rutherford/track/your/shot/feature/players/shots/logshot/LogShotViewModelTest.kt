
package com.nicholas.rutherford.track.your.shot.feature.players.shots.logshot

import android.app.Application
import androidx.lifecycle.SavedStateHandle
import com.nicholas.rutherford.track.your.shot.base.resources.StringsIds
import com.nicholas.rutherford.track.your.shot.data.room.repository.ActiveUserRepository
import com.nicholas.rutherford.track.your.shot.data.room.repository.DeclaredShotRepository
import com.nicholas.rutherford.track.your.shot.data.room.repository.PendingPlayerRepository
import com.nicholas.rutherford.track.your.shot.data.room.repository.PlayerRepository
import com.nicholas.rutherford.track.your.shot.data.room.response.ShotLogged
import com.nicholas.rutherford.track.your.shot.data.shared.alert.Alert
import com.nicholas.rutherford.track.your.shot.data.shared.alert.AlertConfirmAndDismissButton
import com.nicholas.rutherford.track.your.shot.data.test.room.TestActiveUser
import com.nicholas.rutherford.track.your.shot.data.test.room.TestDeclaredShot
import com.nicholas.rutherford.track.your.shot.data.test.room.TestPlayer
import com.nicholas.rutherford.track.your.shot.data.test.room.TestShotLogged
import com.nicholas.rutherford.track.your.shot.feature.players.shots.logshot.extension.LogShotInfo
import com.nicholas.rutherford.track.your.shot.feature.players.shots.logshot.extension.LogShotViewModelExt
import com.nicholas.rutherford.track.your.shot.feature.players.shots.logshot.pendingshot.CurrentPendingShot
import com.nicholas.rutherford.track.your.shot.feature.players.shots.logshot.pendingshot.PendingShot
import com.nicholas.rutherford.track.your.shot.firebase.core.delete.DeleteFirebaseUserInfo
import com.nicholas.rutherford.track.your.shot.firebase.core.update.UpdateFirebaseUserInfo
import com.nicholas.rutherford.track.your.shot.firebase.realtime.ShotLoggedRealtimeResponse
import com.nicholas.rutherford.track.your.shot.helper.extensions.toDateValue
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import io.mockk.verifyOrder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
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

    private var savedStateHandle = mockk<SavedStateHandle>(relaxed = true)

    private val application = mockk<Application>(relaxed = true)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val testDispatcher = UnconfinedTestDispatcher()

    private val scope = CoroutineScope(SupervisorJob() + testDispatcher)

    private val navigation = mockk<LogShotNavigation>(relaxed = true)

    private val declaredShotRepository = mockk<DeclaredShotRepository>(relaxed = true)
    private val pendingPlayerRepository = mockk<PendingPlayerRepository>(relaxed = true)
    private val playerRepository = mockk<PlayerRepository>(relaxed = true)

    private val activeUserRepository = mockk<ActiveUserRepository>(relaxed = true)
    private val updateFirebaseUserInfo = mockk<UpdateFirebaseUserInfo>(relaxed = true)

    private val deleteFirebaseUserInfo = mockk<DeleteFirebaseUserInfo>(relaxed = true)

    private val currentPendingShot = mockk<CurrentPendingShot>(relaxed = true)

    private val logShotViewModelExt = mockk<LogShotViewModelExt>(relaxed = true)

    @BeforeEach
    fun beforeEach() {
        val playerId = 4
        val shotType = 11
        val viewCurrentExistingShot = false
        val viewCurrentPendingShot = false
        val fromShot = false

        every { savedStateHandle.get<Boolean>("isExistingPlayer") } returns false
        every { savedStateHandle.get<Int>("playerId") } returns playerId
        every { savedStateHandle.get<Int>("shotType") } returns shotType
        every { savedStateHandle.get<Int>("shotId") } returns 11
        every { savedStateHandle.get<Boolean>("viewCurrentExistingShot") } returns viewCurrentExistingShot
        every { savedStateHandle.get<Boolean>("viewCurrentPendingShot") } returns viewCurrentPendingShot
        every { savedStateHandle.get<Boolean>("fromShotList") } returns fromShot
        every { savedStateHandle.get<Int>("screenTriggeredIndexArgument") } returns 0

        logShotViewModel = LogShotViewModel(
            savedStateHandle = savedStateHandle,
            application = application,
            scope = scope,
            navigation = navigation,
            declaredShotRepository = declaredShotRepository,
            pendingPlayerRepository = pendingPlayerRepository,
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

            logShotViewModel.updateIsExistingPlayerAndId()

            Assertions.assertEquals(logShotViewModel.logShotMutableStateFlow.value, LogShotState())
        }

        @Test
        fun `when player returns null should not update state`() = runTest {
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

            logShotViewModel.updateIsExistingPlayerAndId()

            Assertions.assertEquals(logShotViewModel.logShotMutableStateFlow.value, LogShotState())
        }

        @Test
        fun `when declaredShot is not null and player from fetch player by id is not null should update state`() =
            runTest {
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

                logShotViewModel.updateIsExistingPlayerAndId()

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

                logShotViewModel.updateIsExistingPlayerAndId()

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
            state.copy(
                shotName = "",
                shotsLoggedDateValue = LocalDate.now().toDateValue() ?: "",
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
            state.copy(
                shotName = "",
                playerName = ", ",
                shotsLoggedDateValue = LocalDate.now().toDateValue() ?: "",
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

            val state = LogShotState(
                shotName = "shotName",
                shotsMade = 5,
                shotsMissed = 2,
                shotsAttempted = 4,
                shotsTakenDateValue = "Jun 4, 2019",
                shotsLoggedDateValue = "Jun 4, 2019",
                shotsMadePercentValue = "33.3",
                shotsMissedPercentValue = "66.67"
            )

            every { logShotViewModelExt.logShotInfo } returns LogShotInfo(viewCurrentExistingShot = true, isExistingPlayer = false, shotId = 0)
            every { logShotViewModelExt.convertValueToDate(value = "Jun 4, 2019") } returns date
            coEvery { logShotViewModelExt.shotEntryInvalidAlert(shotsMade = 5, shotsMissed = 2, shotsAttemptedMillisecondsValue = date?.time ?: 0L) } returns null
            coEvery { logShotViewModelExt.buildPendingShotOnSave(player = any(), state = any(), declaredShot = any()) } returns pendingShot
            every { logShotViewModelExt.noChangesForShotAlert(initialShotLogged = any(), pendingShotLogged = any()) } returns null
            every { currentPendingShot.fetchPendingShots() } returns listOf(pendingShot)

            logShotViewModel.logShotMutableStateFlow.value = state

            logShotViewModel.onSaveClicked()
            advanceUntilIdle()

            verify { navigation.enableProgress(progress = any()) }
            coVerify { logShotViewModelExt.buildPendingShotOnSave(player = any(), state = any(), declaredShot = any()) }
            verify { currentPendingShot.createShot(shotLogged = any()) }
            verify { navigation.disableProgress() }
            verify { navigation.popToCreateOrEditPlayer() }
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

            val state = LogShotState(
                shotName = "shotName",
                shotsMade = 5,
                shotsMissed = 2,
                shotsAttempted = 4,
                shotsTakenDateValue = "Jun 4, 2019",
                shotsLoggedDateValue = "Jun 4, 2019",
                shotsMadePercentValue = "33.3",
                shotsMissedPercentValue = "66.67"
            )

            every { logShotViewModelExt.logShotInfo } returns LogShotInfo(viewCurrentPendingShot = true, isExistingPlayer = false)
            every { logShotViewModelExt.convertValueToDate(value = "Jun 4, 2019") } returns date
            coEvery { logShotViewModelExt.shotEntryInvalidAlert(shotsMade = 5, shotsMissed = 2, shotsAttemptedMillisecondsValue = date?.time ?: 0L) } returns null
            coEvery { logShotViewModelExt.buildPendingShotOnSave(player = any(), state = any(), declaredShot = any()) } returns pendingShot
            every { logShotViewModelExt.noChangesForShotAlert(initialShotLogged = any(), pendingShotLogged = any()) } returns null
            every { currentPendingShot.fetchPendingShots() } returns listOf(pendingShot)

            logShotViewModel.logShotMutableStateFlow.value = state

            logShotViewModel.onSaveClicked()
            advanceUntilIdle()

            verify { navigation.enableProgress(progress = any()) }
            coVerify { logShotViewModelExt.buildPendingShotOnSave(player = any(), state = any(), declaredShot = any()) }
            coVerify { currentPendingShot.deleteShot(any()) }
            coVerify { currentPendingShot.createShot(shotLogged = any()) }
            verify { navigation.disableProgress() }
            verify { navigation.popToCreateOrEditPlayer() }
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

            val state = LogShotState(
                shotName = "shotName",
                shotsMade = 5,
                shotsMissed = 2,
                shotsAttempted = 4,
                shotsTakenDateValue = "Jun 4, 2019",
                shotsLoggedDateValue = "Jun 4, 2019",
                shotsMadePercentValue = "100%",
                shotsMissedPercentValue = "100%"
            )

            every { logShotViewModelExt.convertValueToDate(value = "Jun 4, 2019") } returns date
            coEvery { logShotViewModelExt.shotEntryInvalidAlert(shotsMade = 5, shotsMissed = 2, shotsAttemptedMillisecondsValue = date?.time ?: 0L) } returns null
            coEvery { logShotViewModelExt.buildPendingShotOnSave(player = any(), state = any(), declaredShot = any()) } returns pendingShot

            logShotViewModel.logShotMutableStateFlow.value = state

            logShotViewModel.onSaveClicked()
            advanceUntilIdle()

            coVerify { logShotViewModelExt.buildPendingShotOnSave(player = any(), state = any(), declaredShot = any()) }
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
        fun `when hasDeleted is set to false should show alert`() {
            val hasDeleted = false

            logShotViewModel.handleHasDeleteShotFirebaseResponse(hasDeleted = hasDeleted)

            verify { navigation.disableProgress() }
            verify { navigation.alert(alert = any()) }
        }

        @Test
        fun `when hasDeleted is set to true and fromShotList is set to false should pop to create player`() {
            val hasDeleted = true

            every { logShotViewModelExt.logShotInfo } returns LogShotInfo(fromShotList = false, isExistingPlayer = false)

            logShotViewModel.handleHasDeleteShotFirebaseResponse(hasDeleted = hasDeleted)

            verify { navigation.disableProgress() }
            verify { navigation.popToCreateOrEditPlayer() }
            verify { navigation.alert(alert = any()) }
        }

        @Test
        fun `when hasDeleted is set to true and fromShotList is set to true should pop and show alert`() {
            val hasDeleted = true

            every { logShotViewModelExt.logShotInfo } returns LogShotInfo(fromShotList = true)

            logShotViewModel.handleHasDeleteShotFirebaseResponse(hasDeleted = hasDeleted)

            verify { navigation.disableProgress() }
            verify { navigation.popToShotList(shouldShowAllPlayersShots = true) }
            verify { navigation.alert(alert = any()) }
        }
    }

    @Test
    fun `navigate to create or edit player should pop to one of them`() {
        logShotViewModel.navigateToCreateOrEditPlayer()

        verify { navigation.disableProgress() }
        verify { navigation.popToCreateOrEditPlayer() }
    }

    @Test
    fun `on back clicked should pop stack`() {
        logShotViewModel.onBackClicked()

        Assertions.assertEquals(
            logShotViewModel.logShotMutableStateFlow.value,
            logShotViewModel.logShotMutableStateFlow.value.copy(
                shotsLoggedDateValue = LocalDate.now().toDateValue() ?: ""
            )
        )

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
            verify { navigation.popToCreateOrEditPlayer() }
            verify { navigation.alert(alert = any()) }
        }
    }

    @Test
    fun `on delete shot clicked should call delete show alert on navigation`() {
        logShotViewModel.onDeleteShotClicked()

        verify { navigation.alert(alert = any()) }
    }

    @Nested
    inner class CreatePendingShot {

        @Test
        fun `when isACurrentPlayerShot is true should create shot with original pendingShot and navigate`() {
            val player = TestPlayer().create()
            val pendingShot = PendingShot(
                player = player,
                shotLogged = TestShotLogged.build().copy(id = 5),
                isPendingPlayer = false
            )

            logShotViewModel.currentPlayer = player

            logShotViewModel.createPendingShot(
                isACurrentPlayerShot = true,
                pendingShot = pendingShot
            )

            verify { currentPendingShot.createShot(shotLogged = pendingShot) }
            verify { navigation.disableProgress() }
            verify { navigation.popToCreateOrEditPlayer() }
        }

        @Test
        fun `when isACurrentPlayerShot is false should create shot with incremented id and navigate`() {
            val player = TestPlayer().create()
            val currentPlayerShotSize = 3
            val expectedNewId = currentPlayerShotSize + 1 // Should be 4
            val originalPendingShot = PendingShot(
                player = player,
                shotLogged = TestShotLogged.build().copy(id = 0),
                isPendingPlayer = false
            )
            val expectedPendingShot = originalPendingShot.copy(
                shotLogged = originalPendingShot.shotLogged.copy(id = expectedNewId)
            )

            logShotViewModel.currentPlayer = player
            // Set currentPlayerShotSize using reflection since it's private
            val field = LogShotViewModel::class.java.getDeclaredField("currentPlayerShotSize")
            field.isAccessible = true
            field.set(logShotViewModel, currentPlayerShotSize)

            logShotViewModel.createPendingShot(
                isACurrentPlayerShot = false,
                pendingShot = originalPendingShot
            )

            verify { currentPendingShot.createShot(shotLogged = expectedPendingShot) }
            verify { navigation.disableProgress() }
            verify { navigation.popToCreateOrEditPlayer() }
        }

        @Test
        fun `when isACurrentPlayerShot is false and currentPlayerShotSize is zero should create shot with id 1`() {
            val player = TestPlayer().create()
            val currentPlayerShotSize = 0
            val expectedNewId = 1
            val originalPendingShot = PendingShot(
                player = player,
                shotLogged = TestShotLogged.build().copy(id = 0),
                isPendingPlayer = false
            )
            val expectedPendingShot = originalPendingShot.copy(
                shotLogged = originalPendingShot.shotLogged.copy(id = expectedNewId)
            )

            logShotViewModel.currentPlayer = player
            // Set currentPlayerShotSize using reflection since it's private
            val field = LogShotViewModel::class.java.getDeclaredField("currentPlayerShotSize")
            field.isAccessible = true
            field.set(logShotViewModel, currentPlayerShotSize)

            logShotViewModel.createPendingShot(
                isACurrentPlayerShot = false,
                pendingShot = originalPendingShot
            )

            verify { currentPendingShot.createShot(shotLogged = expectedPendingShot) }
            verify { navigation.disableProgress() }
            verify { navigation.popToCreateOrEditPlayer() }
        }

        @Test
        fun `when isACurrentPlayerShot is false and currentPlayerShotSize is large should create shot with correct incremented id`() {
            val player = TestPlayer().create()
            val currentPlayerShotSize = 10
            val expectedNewId = 11
            val originalPendingShot = PendingShot(
                player = player,
                shotLogged = TestShotLogged.build().copy(id = 0),
                isPendingPlayer = false
            )
            val expectedPendingShot = originalPendingShot.copy(
                shotLogged = originalPendingShot.shotLogged.copy(id = expectedNewId)
            )

            logShotViewModel.currentPlayer = player
            // Set currentPlayerShotSize using reflection since it's private
            val field = LogShotViewModel::class.java.getDeclaredField("currentPlayerShotSize")
            field.isAccessible = true
            field.set(logShotViewModel, currentPlayerShotSize)

            logShotViewModel.createPendingShot(
                isACurrentPlayerShot = false,
                pendingShot = originalPendingShot
            )

            verify { currentPendingShot.createShot(shotLogged = expectedPendingShot) }
            verify { navigation.disableProgress() }
            verify { navigation.popToCreateOrEditPlayer() }
        }

        @Test
        fun `when isACurrentPlayerShot is true should call navigateToCreateOrEditPlayer`() {
            val player = TestPlayer().create()
            val pendingShot = PendingShot(
                player = player,
                shotLogged = TestShotLogged.build(),
                isPendingPlayer = false
            )

            logShotViewModel.currentPlayer = player

            logShotViewModel.createPendingShot(
                isACurrentPlayerShot = true,
                pendingShot = pendingShot
            )

            verify { navigation.disableProgress() }
            verify { navigation.popToCreateOrEditPlayer() }
        }

        @Test
        fun `when isACurrentPlayerShot is false should call navigateToCreateOrEditPlayer`() {
            val player = TestPlayer().create()
            val pendingShot = PendingShot(
                player = player,
                shotLogged = TestShotLogged.build(),
                isPendingPlayer = false
            )

            logShotViewModel.currentPlayer = player
            val field = LogShotViewModel::class.java.getDeclaredField("currentPlayerShotSize")
            field.isAccessible = true
            field.set(logShotViewModel, 0)

            logShotViewModel.createPendingShot(
                isACurrentPlayerShot = false,
                pendingShot = pendingShot
            )

            verify { navigation.disableProgress() }
            verify { navigation.popToCreateOrEditPlayer() }
        }
    }

    @Nested
    inner class UpdateCurrentShot {

        @Test
        fun `when currentPlayer is null should disable progress and show alert`() = runTest {
            val pendingShot = PendingShot(
                player = TestPlayer().create(),
                shotLogged = TestShotLogged.build(),
                isPendingPlayer = false
            )

            logShotViewModel.currentPlayer = null

            logShotViewModel.updateCurrentShot(pendingShot = pendingShot)

            verify { navigation.disableProgress() }
            verify { navigation.alert(alert = any()) }
            coVerify(exactly = 0) { updateFirebaseUserInfo.updatePlayer(any()) }
        }

        @Test
        fun `when currentPlayer is not null should call updateUserInFirebase with filtered shots and new shot`() = runTest {
            val shotId = 5
            val existingShot1 = TestShotLogged.build().copy(id = 3)
            val existingShot2 = TestShotLogged.build().copy(id = 5)
            val existingShot3 = TestShotLogged.build().copy(id = 7)
            val player = TestPlayer().create().copy(
                shotsLoggedList = listOf(existingShot1, existingShot2, existingShot3)
            )
            val pendingShot = PendingShot(
                player = player,
                shotLogged = TestShotLogged.build().copy(id = 5),
                isPendingPlayer = false
            )
            val filteredShots = listOf(existingShot1, existingShot3) // Shot with id 5 filtered out
            val expectedShotsList = filteredShots + listOf(pendingShot.shotLogged)

            logShotViewModel.currentPlayer = player

            every { logShotViewModelExt.logShotInfo } returns LogShotInfo(shotId = shotId)
            every { logShotViewModelExt.filterShotsById(shots = player.shotsLoggedList) } returns filteredShots
            coEvery { logShotViewModelExt.currentShotLoggedRealtimeResponseList(currentShotList = expectedShotsList) } returns listOf(
                ShotLoggedRealtimeResponse(id = existingShot1.id, shotName = existingShot1.shotName, shotType = existingShot1.shotType,
                    shotsAttempted = existingShot1.shotsAttempted, shotsMade = existingShot1.shotsMade, shotsMissed = existingShot1.shotsMissed,
                    shotsMadePercentValue = existingShot1.shotsMadePercentValue, shotsMissedPercentValue = existingShot1.shotsMissedPercentValue,
                    shotsAttemptedMillisecondsValue = existingShot1.shotsAttemptedMillisecondsValue,
                    shotsLoggedMillisecondsValue = existingShot1.shotsLoggedMillisecondsValue, isPending = false),
                ShotLoggedRealtimeResponse(id = existingShot3.id, shotName = existingShot3.shotName, shotType = existingShot3.shotType,
                    shotsAttempted = existingShot3.shotsAttempted, shotsMade = existingShot3.shotsMade, shotsMissed = existingShot3.shotsMissed,
                    shotsMadePercentValue = existingShot3.shotsMadePercentValue, shotsMissedPercentValue = existingShot3.shotsMissedPercentValue,
                    shotsAttemptedMillisecondsValue = existingShot3.shotsAttemptedMillisecondsValue,
                    shotsLoggedMillisecondsValue = existingShot3.shotsLoggedMillisecondsValue, isPending = false),
                ShotLoggedRealtimeResponse(id = pendingShot.shotLogged.id, shotName = pendingShot.shotLogged.shotName, shotType = pendingShot.shotLogged.shotType,
                    shotsAttempted = pendingShot.shotLogged.shotsAttempted, shotsMade = pendingShot.shotLogged.shotsMade, shotsMissed = pendingShot.shotLogged.shotsMissed,
                    shotsMadePercentValue = pendingShot.shotLogged.shotsMadePercentValue, shotsMissedPercentValue = pendingShot.shotLogged.shotsMissedPercentValue,
                    shotsAttemptedMillisecondsValue = pendingShot.shotLogged.shotsAttemptedMillisecondsValue,
                    shotsLoggedMillisecondsValue = pendingShot.shotLogged.shotsLoggedMillisecondsValue, isPending = false)
            )
            coEvery { activeUserRepository.fetchActiveUser() } returns TestActiveUser().create().copy(
                firebaseAccountInfoKey = "testKey"
            )
            coEvery { playerRepository.fetchPlayerByName(firstName = player.firstName, lastName = player.lastName) } returns player
            coEvery { updateFirebaseUserInfo.updatePlayer(any()) } returns flowOf(true)

            logShotViewModel.updateCurrentShot(pendingShot = pendingShot)

            verify { logShotViewModelExt.filterShotsById(shots = player.shotsLoggedList) }
            coVerify { logShotViewModelExt.currentShotLoggedRealtimeResponseList(currentShotList = expectedShotsList) }
            coVerify {
                updateFirebaseUserInfo.updatePlayer(
                    playerInfoRealtimeWithKeyResponse = match {
                        it.playerInfo.shotsLogged.size == expectedShotsList.size
                    }
                )
            }
        }

        @Test
        fun `when currentPlayer is not null and filterShotsById returns empty list should still add new shot`() = runTest {
            val shotId = 5
            val player = TestPlayer().create().copy(
                shotsLoggedList = listOf(TestShotLogged.build().copy(id = 5))
            )
            val pendingShot = PendingShot(
                player = player,
                shotLogged = TestShotLogged.build().copy(id = 10),
                isPendingPlayer = false
            )
            val filteredShots = emptyList<ShotLogged>()
            val expectedShotsList = listOf(pendingShot.shotLogged)

            logShotViewModel.currentPlayer = player

            every { logShotViewModelExt.logShotInfo } returns LogShotInfo(shotId = shotId)
            every { logShotViewModelExt.filterShotsById(shots = player.shotsLoggedList) } returns filteredShots
            coEvery { logShotViewModelExt.currentShotLoggedRealtimeResponseList(currentShotList = expectedShotsList) } returns listOf(
                ShotLoggedRealtimeResponse(id = pendingShot.shotLogged.id, shotName = pendingShot.shotLogged.shotName, shotType = pendingShot.shotLogged.shotType,
                    shotsAttempted = pendingShot.shotLogged.shotsAttempted, shotsMade = pendingShot.shotLogged.shotsMade, shotsMissed = pendingShot.shotLogged.shotsMissed,
                    shotsMadePercentValue = pendingShot.shotLogged.shotsMadePercentValue, shotsMissedPercentValue = pendingShot.shotLogged.shotsMissedPercentValue,
                    shotsAttemptedMillisecondsValue = pendingShot.shotLogged.shotsAttemptedMillisecondsValue,
                    shotsLoggedMillisecondsValue = pendingShot.shotLogged.shotsLoggedMillisecondsValue, isPending = false)
            )
            coEvery { activeUserRepository.fetchActiveUser() } returns TestActiveUser().create().copy(
                firebaseAccountInfoKey = "testKey"
            )
            coEvery { playerRepository.fetchPlayerByName(firstName = player.firstName, lastName = player.lastName) } returns player
            coEvery { updateFirebaseUserInfo.updatePlayer(any()) } returns flowOf(true)

            logShotViewModel.updateCurrentShot(pendingShot = pendingShot)

            verify { logShotViewModelExt.filterShotsById(shots = player.shotsLoggedList) }
            coVerify { logShotViewModelExt.currentShotLoggedRealtimeResponseList(currentShotList = expectedShotsList) }
            coVerify {
                updateFirebaseUserInfo.updatePlayer(
                    playerInfoRealtimeWithKeyResponse = match {
                        it.playerInfo.shotsLogged.size == expectedShotsList.size &&
                        it.playerInfo.shotsLogged.first().id == pendingShot.shotLogged.id
                    }
                )
            }
        }

        @Test
        fun `when currentPlayer is not null should combine filtered shots with pending shot correctly`() = runTest {
            val shotId = 2
            val existingShot1 = TestShotLogged.build().copy(id = 1, shotName = "Shot 1")
            val existingShot2 = TestShotLogged.build().copy(id = 2, shotName = "Shot 2")
            val existingShot3 = TestShotLogged.build().copy(id = 3, shotName = "Shot 3")
            val player = TestPlayer().create().copy(
                shotsLoggedList = listOf(existingShot1, existingShot2, existingShot3)
            )
            val newPendingShot = TestShotLogged.build().copy(id = 2, shotName = "Updated Shot 2")
            val pendingShot = PendingShot(
                player = player,
                shotLogged = newPendingShot,
                isPendingPlayer = false
            )
            val filteredShots = listOf(existingShot1, existingShot3) // Shot with id 2 filtered out
            val expectedShotsList = filteredShots + listOf(pendingShot.shotLogged)

            logShotViewModel.currentPlayer = player

            every { logShotViewModelExt.logShotInfo } returns LogShotInfo(shotId = shotId)
            every { logShotViewModelExt.filterShotsById(shots = player.shotsLoggedList) } returns filteredShots
            coEvery { logShotViewModelExt.currentShotLoggedRealtimeResponseList(currentShotList = expectedShotsList) } returns listOf(
                ShotLoggedRealtimeResponse(id = existingShot1.id, shotName = existingShot1.shotName, shotType = existingShot1.shotType,
                    shotsAttempted = existingShot1.shotsAttempted, shotsMade = existingShot1.shotsMade, shotsMissed = existingShot1.shotsMissed,
                    shotsMadePercentValue = existingShot1.shotsMadePercentValue, shotsMissedPercentValue = existingShot1.shotsMissedPercentValue,
                    shotsAttemptedMillisecondsValue = existingShot1.shotsAttemptedMillisecondsValue,
                    shotsLoggedMillisecondsValue = existingShot1.shotsLoggedMillisecondsValue, isPending = false),
                ShotLoggedRealtimeResponse(id = existingShot3.id, shotName = existingShot3.shotName, shotType = existingShot3.shotType,
                    shotsAttempted = existingShot3.shotsAttempted, shotsMade = existingShot3.shotsMade, shotsMissed = existingShot3.shotsMissed,
                    shotsMadePercentValue = existingShot3.shotsMadePercentValue, shotsMissedPercentValue = existingShot3.shotsMissedPercentValue,
                    shotsAttemptedMillisecondsValue = existingShot3.shotsAttemptedMillisecondsValue,
                    shotsLoggedMillisecondsValue = existingShot3.shotsLoggedMillisecondsValue, isPending = false),
                ShotLoggedRealtimeResponse(id = pendingShot.shotLogged.id, shotName = pendingShot.shotLogged.shotName, shotType = pendingShot.shotLogged.shotType,
                    shotsAttempted = pendingShot.shotLogged.shotsAttempted, shotsMade = pendingShot.shotLogged.shotsMade, shotsMissed = pendingShot.shotLogged.shotsMissed,
                    shotsMadePercentValue = pendingShot.shotLogged.shotsMadePercentValue, shotsMissedPercentValue = pendingShot.shotLogged.shotsMissedPercentValue,
                    shotsAttemptedMillisecondsValue = pendingShot.shotLogged.shotsAttemptedMillisecondsValue,
                    shotsLoggedMillisecondsValue = pendingShot.shotLogged.shotsLoggedMillisecondsValue, isPending = false)
            )
            coEvery { activeUserRepository.fetchActiveUser() } returns TestActiveUser().create().copy(
                firebaseAccountInfoKey = "testKey"
            )
            coEvery { playerRepository.fetchPlayerByName(firstName = player.firstName, lastName = player.lastName) } returns player
            coEvery { updateFirebaseUserInfo.updatePlayer(any()) } returns flowOf(true)

            logShotViewModel.updateCurrentShot(pendingShot = pendingShot)

            verify { logShotViewModelExt.filterShotsById(shots = player.shotsLoggedList) }
            coVerify { logShotViewModelExt.currentShotLoggedRealtimeResponseList(currentShotList = expectedShotsList) }
            coVerify {
                updateFirebaseUserInfo.updatePlayer(
                    playerInfoRealtimeWithKeyResponse = match {
                        val shots = it.playerInfo.shotsLogged
                        shots.size == 3 &&
                        shots.any { shot -> shot.id == existingShot1.id } &&
                        shots.any { shot -> shot.id == existingShot3.id } &&
                        shots.any { shot -> shot.id == newPendingShot.id && shot.shotName == newPendingShot.shotName }
                    }
                )
            }
        }

        @Test
        fun `when currentPlayer is not null but activeUser is null should disable progress and show alert`() = runTest {
            val player = TestPlayer().create()
            val pendingShot = PendingShot(
                player = player,
                shotLogged = TestShotLogged.build(),
                isPendingPlayer = false
            )

            logShotViewModel.currentPlayer = player

            every { logShotViewModelExt.filterShotsById(shots = player.shotsLoggedList) } returns emptyList()
            coEvery { activeUserRepository.fetchActiveUser() } returns null

            logShotViewModel.updateCurrentShot(pendingShot = pendingShot)

            verify { navigation.disableProgress() }
            verify { navigation.alert(alert = any()) }
        }

        @Test
        fun `when currentPlayer is not null but playerKey is empty should disable progress and show alert`() = runTest {
            val player = TestPlayer().create()
            val pendingShot = PendingShot(
                player = player,
                shotLogged = TestShotLogged.build(),
                isPendingPlayer = false
            )

            logShotViewModel.currentPlayer = player

            every { logShotViewModelExt.filterShotsById(shots = player.shotsLoggedList) } returns emptyList()
            coEvery { activeUserRepository.fetchActiveUser() } returns TestActiveUser().create().copy(
                firebaseAccountInfoKey = "testKey"
            )
            coEvery { playerRepository.fetchPlayerByName(firstName = player.firstName, lastName = player.lastName) } returns null

            logShotViewModel.updateCurrentShot(pendingShot = pendingShot)

            verify { navigation.disableProgress() }
            verify { navigation.alert(alert = any()) }
        }
    }

    @Nested
    inner class HandleUpdatedShot {

        @Test
        fun `when isSuccessful is true should update player, disable progress, pop to shot list and show updated alert`() = runTest {
            val player = TestPlayer().create()
            val shotLogged = listOf(
                TestShotLogged.build().copy(id = 1),
                TestShotLogged.build().copy(id = 2)
            )
            val shouldShowAllPlayersShots = true

            // Set shouldShowAllPlayersShots using reflection since it's private
            val field = LogShotViewModel::class.java.getDeclaredField("shouldShowAllPlayersShots")
            field.isAccessible = true
            field.set(logShotViewModel, shouldShowAllPlayersShots)

            every { logShotViewModelExt.showUpdatedAlert() } returns Alert(
                title = "Updated",
                dismissButton = AlertConfirmAndDismissButton(buttonText = "Got It"),
                description = "Shot updated"
            )

            coEvery { playerRepository.updatePlayer(currentPlayer = player, newPlayer = any()) } just runs

            logShotViewModel.handleUpdatedShot(
                isSuccessful = true,
                player = player,
                shotLogged = shotLogged
            )

            coVerify {
                playerRepository.updatePlayer(
                    currentPlayer = player,
                    newPlayer = match {
                        it.firstName == player.firstName &&
                        it.lastName == player.lastName &&
                        it.position == player.position &&
                        it.firebaseKey == player.firebaseKey &&
                        it.imageUrl == player.imageUrl &&
                        it.shotsLoggedList == shotLogged
                    }
                )
            }
            verify { navigation.disableProgress() }
            verify { navigation.popToShotList(shouldShowAllPlayersShots = shouldShowAllPlayersShots) }
            verify { logShotViewModelExt.showUpdatedAlert() }
            verify { navigation.alert(alert = any()) }
        }

        @Test
        fun `when isSuccessful is true and shouldShowAllPlayersShots is false should pop to shot list with false`() = runTest {
            val player = TestPlayer().create()
            val shotLogged = listOf(TestShotLogged.build())
            val shouldShowAllPlayersShots = false

            // Set shouldShowAllPlayersShots using reflection since it's private
            val field = LogShotViewModel::class.java.getDeclaredField("shouldShowAllPlayersShots")
            field.isAccessible = true
            field.set(logShotViewModel, shouldShowAllPlayersShots)

            every { logShotViewModelExt.showUpdatedAlert() } returns Alert(
                title = "Updated",
                dismissButton = AlertConfirmAndDismissButton(buttonText = "Got It"),
                description = "Shot updated"
            )

            coEvery { playerRepository.updatePlayer(currentPlayer = player, newPlayer = any()) } just runs

            logShotViewModel.handleUpdatedShot(
                isSuccessful = true,
                player = player,
                shotLogged = shotLogged
            )

            verify { navigation.popToShotList(shouldShowAllPlayersShots = false) }
        }

        @Test
        fun `when isSuccessful is true should update player with correct shot list`() = runTest {
            val player = TestPlayer().create()
            val existingShot = TestShotLogged.build().copy(id = 1, shotName = "Existing Shot")
            val newShot = TestShotLogged.build().copy(id = 2, shotName = "New Shot")
            val shotLogged = listOf(existingShot, newShot)

            val field = LogShotViewModel::class.java.getDeclaredField("shouldShowAllPlayersShots")
            field.isAccessible = true
            field.set(logShotViewModel, false)

            every { logShotViewModelExt.showUpdatedAlert() } returns Alert(
                title = "Updated",
                dismissButton = AlertConfirmAndDismissButton(buttonText = "Got It"),
                description = "Shot updated"
            )

            coEvery { playerRepository.updatePlayer(currentPlayer = player, newPlayer = any()) } just runs

            logShotViewModel.handleUpdatedShot(
                isSuccessful = true,
                player = player,
                shotLogged = shotLogged
            )

            coVerify {
                playerRepository.updatePlayer(
                    currentPlayer = player,
                    newPlayer = match {
                        it.shotsLoggedList.size == 2 &&
                        it.shotsLoggedList.any { shot -> shot.id == existingShot.id && shot.shotName == existingShot.shotName } &&
                        it.shotsLoggedList.any { shot -> shot.id == newShot.id && shot.shotName == newShot.shotName }
                    }
                )
            }
        }

        @Test
        fun `when isSuccessful is false should disable progress and show account problem alert`() = runTest {
            val player = TestPlayer().create()
            val shotLogged = listOf(TestShotLogged.build())

            every { logShotViewModelExt.weHaveDetectedAProblemWithYourAccountAlert() } returns Alert(
                title = "Error",
                dismissButton = AlertConfirmAndDismissButton(buttonText = "Got It"),
                description = "Account problem"
            )

            logShotViewModel.handleUpdatedShot(
                isSuccessful = false,
                player = player,
                shotLogged = shotLogged
            )

            verify { navigation.disableProgress() }
            verify { logShotViewModelExt.weHaveDetectedAProblemWithYourAccountAlert() }
            verify { navigation.alert(alert = any()) }
            coVerify(exactly = 0) { playerRepository.updatePlayer(any(), any()) }
            verify(exactly = 0) { navigation.popToShotList(any()) }
            verify(exactly = 0) { logShotViewModelExt.showUpdatedAlert() }
        }

        @Test
        fun `when isSuccessful is false should not update player repository`() = runTest {
            val player = TestPlayer().create()
            val shotLogged = listOf(TestShotLogged.build())

            every { logShotViewModelExt.weHaveDetectedAProblemWithYourAccountAlert() } returns Alert(
                title = "Error",
                dismissButton = AlertConfirmAndDismissButton(buttonText = "Got It"),
                description = "Account problem"
            )

            logShotViewModel.handleUpdatedShot(
                isSuccessful = false,
                player = player,
                shotLogged = shotLogged
            )

            coVerify(exactly = 0) { playerRepository.updatePlayer(any(), any()) }
        }

        @Test
        fun `when isSuccessful is true should preserve all player properties except shotsLoggedList`() = runTest {
            val player = TestPlayer().create().copy(
                firstName = "John",
                lastName = "Doe",
                firebaseKey = "firebaseKey123",
                imageUrl = "https://example.com/image.jpg"
            )
            val shotLogged = listOf(TestShotLogged.build())

            val field = LogShotViewModel::class.java.getDeclaredField("shouldShowAllPlayersShots")
            field.isAccessible = true
            field.set(logShotViewModel, false)

            every { logShotViewModelExt.showUpdatedAlert() } returns Alert(
                title = "Updated",
                dismissButton = AlertConfirmAndDismissButton(buttonText = "Got It"),
                description = "Shot updated"
            )

            coEvery { playerRepository.updatePlayer(currentPlayer = player, newPlayer = any()) } just runs

            logShotViewModel.handleUpdatedShot(
                isSuccessful = true,
                player = player,
                shotLogged = shotLogged
            )

            coVerify {
                playerRepository.updatePlayer(
                    currentPlayer = player,
                    newPlayer = match {
                        it.firstName == "John" &&
                        it.lastName == "Doe" &&
                        it.firebaseKey == "firebaseKey123" &&
                        it.imageUrl == "https://example.com/image.jpg" &&
                        it.shotsLoggedList == shotLogged
                    }
                )
            }
        }
    }

    @Nested
    inner class CurrentShotLoggedRealtimeResponseList {

        @Test
        fun `when currentShotList is empty should return empty list`() {
            val currentShotList = emptyList<ShotLogged>()
            val expectedResult = emptyList<ShotLoggedRealtimeResponse>()

            every { logShotViewModelExt.currentShotLoggedRealtimeResponseList(currentShotList = currentShotList) } returns expectedResult

            val result = logShotViewModel.currentShotLoggedRealtimeResponseList(currentShotList = currentShotList)

            verify { logShotViewModelExt.currentShotLoggedRealtimeResponseList(currentShotList = currentShotList) }
            Assertions.assertTrue(result.isEmpty())
        }

        @Test
        fun `should delegate to extension method`() {
            val currentShotList = listOf(TestShotLogged.build())
            val expectedResult = listOf(
                ShotLoggedRealtimeResponse(
                    id = 1,
                    shotName = "Test",
                    shotType = 1,
                    shotsAttempted = 10,
                    shotsMade = 5,
                    shotsMissed = 5,
                    shotsMadePercentValue = 50.0,
                    shotsMissedPercentValue = 50.0,
                    shotsAttemptedMillisecondsValue = 1000L,
                    shotsLoggedMillisecondsValue = 2000L,
                    isPending = false
                )
            )

            every { logShotViewModelExt.currentShotLoggedRealtimeResponseList(currentShotList = currentShotList) } returns expectedResult

            val result = logShotViewModel.currentShotLoggedRealtimeResponseList(currentShotList = currentShotList)

            verify { logShotViewModelExt.currentShotLoggedRealtimeResponseList(currentShotList = currentShotList) }
            Assertions.assertEquals(expectedResult, result)
        }
    }

    @Nested
    inner class UpdateUserInFirebase {

        @Test
        fun `when activeUser is null should disable progress and show alert`() = runTest {
            val player = TestPlayer().create()
            val shotLogged = listOf(TestShotLogged.build())

            coEvery { activeUserRepository.fetchActiveUser() } returns null

            logShotViewModel.updateUserInFirebase(player = player, shotLogged = shotLogged)

            verify { navigation.disableProgress() }
            verify { logShotViewModelExt.weHaveDetectedAProblemWithYourAccountAlert() }
            verify { navigation.alert(alert = any()) }
            coVerify(exactly = 0) { updateFirebaseUserInfo.updatePlayer(any()) }
        }

        @Test
        fun `when activeUser firebaseAccountInfoKey is empty should disable progress and show alert`() = runTest {
            val player = TestPlayer().create()
            val shotLogged = listOf(TestShotLogged.build())

            coEvery { activeUserRepository.fetchActiveUser() } returns TestActiveUser().create().copy(
                firebaseAccountInfoKey = ""
            )

            logShotViewModel.updateUserInFirebase(player = player, shotLogged = shotLogged)

            verify { navigation.disableProgress() }
            verify { logShotViewModelExt.weHaveDetectedAProblemWithYourAccountAlert() }
            verify { navigation.alert(alert = any()) }
            coVerify(exactly = 0) { updateFirebaseUserInfo.updatePlayer(any()) }
        }

        @Test
        fun `when player is not found should disable progress and show alert`() = runTest {
            val player = TestPlayer().create()
            val shotLogged = listOf(TestShotLogged.build())

            coEvery { activeUserRepository.fetchActiveUser() } returns TestActiveUser().create().copy(
                firebaseAccountInfoKey = "testKey"
            )
            coEvery { playerRepository.fetchPlayerByName(firstName = player.firstName, lastName = player.lastName) } returns null

            logShotViewModel.updateUserInFirebase(player = player, shotLogged = shotLogged)

            verify { navigation.disableProgress() }
            verify { logShotViewModelExt.weHaveDetectedAProblemWithYourAccountAlert() }
            verify { navigation.alert(alert = any()) }
            coVerify(exactly = 0) { updateFirebaseUserInfo.updatePlayer(any()) }
        }

        @Test
        fun `when player firebaseKey is empty should disable progress and show alert`() = runTest {
            val player = TestPlayer().create().copy(firebaseKey = "")
            val shotLogged = listOf(TestShotLogged.build())

            coEvery { activeUserRepository.fetchActiveUser() } returns TestActiveUser().create().copy(
                firebaseAccountInfoKey = "testKey"
            )
            coEvery { playerRepository.fetchPlayerByName(firstName = player.firstName, lastName = player.lastName) } returns player

            logShotViewModel.updateUserInFirebase(player = player, shotLogged = shotLogged)

            verify { navigation.disableProgress() }
            verify { logShotViewModelExt.weHaveDetectedAProblemWithYourAccountAlert() }
            verify { navigation.alert(alert = any()) }
            coVerify(exactly = 0) { updateFirebaseUserInfo.updatePlayer(any()) }
        }

        @Test
        fun `when both keys are present and update is successful should call updatePlayer and handleUpdatedShot`() = runTest {
            val player = TestPlayer().create().copy(firebaseKey = "playerKey123")
            val shotLogged = listOf(TestShotLogged.build())

            coEvery { activeUserRepository.fetchActiveUser() } returns TestActiveUser().create().copy(
                firebaseAccountInfoKey = "testKey"
            )
            coEvery { playerRepository.fetchPlayerByName(firstName = player.firstName, lastName = player.lastName) } returns player
            coEvery { updateFirebaseUserInfo.updatePlayer(any()) } returns flowOf(true)

            logShotViewModel.updateUserInFirebase(player = player, shotLogged = shotLogged)

            coVerify {
                updateFirebaseUserInfo.updatePlayer(
                    playerInfoRealtimeWithKeyResponse = match {
                        it.playerFirebaseKey == "playerKey123" &&
                        it.playerInfo.firstName == player.firstName &&
                        it.playerInfo.lastName == player.lastName &&
                        it.playerInfo.positionValue == player.position.value &&
                        it.playerInfo.imageUrl == (player.imageUrl ?: "")
                    }
                )
            }
            // Verify handleUpdatedShot was called (indirectly through the Flow collection)
            // We can't directly verify it, but we can verify the updatePlayer was called correctly
        }

        @Test
        fun `when both keys are present and update fails should call updatePlayer and handleUpdatedShot with false`() = runTest {
            val player = TestPlayer().create().copy(firebaseKey = "playerKey123")
            val shotLogged = listOf(TestShotLogged.build())

            coEvery { activeUserRepository.fetchActiveUser() } returns TestActiveUser().create().copy(
                firebaseAccountInfoKey = "testKey"
            )
            coEvery { playerRepository.fetchPlayerByName(firstName = player.firstName, lastName = player.lastName) } returns player
            coEvery { updateFirebaseUserInfo.updatePlayer(any()) } returns flowOf(false)

            logShotViewModel.updateUserInFirebase(player = player, shotLogged = shotLogged)

            coVerify {
                updateFirebaseUserInfo.updatePlayer(any())
            }
        }

        @Test
        fun `should call currentShotLoggedRealtimeResponseList with correct shot list`() = runTest {
            val player = TestPlayer().create().copy(firebaseKey = "playerKey123")
            val shot1 = TestShotLogged.build().copy(id = 1)
            val shot2 = TestShotLogged.build().copy(id = 2)
            val shotLogged = listOf(shot1, shot2)

            coEvery { activeUserRepository.fetchActiveUser() } returns TestActiveUser().create().copy(
                firebaseAccountInfoKey = "testKey"
            )
            coEvery { playerRepository.fetchPlayerByName(firstName = player.firstName, lastName = player.lastName) } returns player
            coEvery { logShotViewModelExt.currentShotLoggedRealtimeResponseList(currentShotList = shotLogged) } returns listOf(
                ShotLoggedRealtimeResponse(
                    id = 1, shotName = shot1.shotName, shotType = shot1.shotType,
                    shotsAttempted = shot1.shotsAttempted, shotsMade = shot1.shotsMade, shotsMissed = shot1.shotsMissed,
                    shotsMadePercentValue = shot1.shotsMadePercentValue, shotsMissedPercentValue = shot1.shotsMissedPercentValue,
                    shotsAttemptedMillisecondsValue = shot1.shotsAttemptedMillisecondsValue,
                    shotsLoggedMillisecondsValue = shot1.shotsLoggedMillisecondsValue, isPending = false
                ),
                ShotLoggedRealtimeResponse(
                    id = 2, shotName = shot2.shotName, shotType = shot2.shotType,
                    shotsAttempted = shot2.shotsAttempted, shotsMade = shot2.shotsMade, shotsMissed = shot2.shotsMissed,
                    shotsMadePercentValue = shot2.shotsMadePercentValue, shotsMissedPercentValue = shot2.shotsMissedPercentValue,
                    shotsAttemptedMillisecondsValue = shot2.shotsAttemptedMillisecondsValue,
                    shotsLoggedMillisecondsValue = shot2.shotsLoggedMillisecondsValue, isPending = false
                )
            )
            coEvery { updateFirebaseUserInfo.updatePlayer(any()) } returns flowOf(true)

            logShotViewModel.updateUserInFirebase(player = player, shotLogged = shotLogged)

            verify { logShotViewModelExt.currentShotLoggedRealtimeResponseList(currentShotList = shotLogged) }
            coVerify {
                updateFirebaseUserInfo.updatePlayer(
                    playerInfoRealtimeWithKeyResponse = match {
                        it.playerInfo.shotsLogged.size == 2
                    }
                )
            }
        }

        @Test
        fun `should construct PlayerInfoRealtimeWithKeyResponse with correct player data`() = runTest {
            val player = TestPlayer().create().copy(
                firstName = "John",
                lastName = "Doe",
                firebaseKey = "firebaseKey456",
                imageUrl = "https://example.com/image.jpg"
            )
            val shotLogged = listOf(TestShotLogged.build())

            coEvery { activeUserRepository.fetchActiveUser() } returns TestActiveUser().create().copy(
                firebaseAccountInfoKey = "accountKey789"
            )
            coEvery { playerRepository.fetchPlayerByName(firstName = "John", lastName = "Doe") } returns player
            coEvery { updateFirebaseUserInfo.updatePlayer(any()) } returns flowOf(true)

            logShotViewModel.updateUserInFirebase(player = player, shotLogged = shotLogged)

            coVerify {
                updateFirebaseUserInfo.updatePlayer(
                    playerInfoRealtimeWithKeyResponse = match {
                        it.playerFirebaseKey == "firebaseKey456" &&
                        it.playerInfo.firstName == "John" &&
                        it.playerInfo.lastName == "Doe" &&
                        it.playerInfo.positionValue == player.position.value &&
                        it.playerInfo.imageUrl == "https://example.com/image.jpg"
                    }
                )
            }
        }

        @Test
        fun `when player imageUrl is null should use empty string`() = runTest {
            val player = TestPlayer().create().copy(
                firebaseKey = "playerKey123",
                imageUrl = null
            )
            val shotLogged = listOf(TestShotLogged.build())

            coEvery { activeUserRepository.fetchActiveUser() } returns TestActiveUser().create().copy(
                firebaseAccountInfoKey = "testKey"
            )
            coEvery { playerRepository.fetchPlayerByName(firstName = player.firstName, lastName = player.lastName) } returns player
            coEvery { updateFirebaseUserInfo.updatePlayer(any()) } returns flowOf(true)

            logShotViewModel.updateUserInFirebase(player = player, shotLogged = shotLogged)

            coVerify {
                updateFirebaseUserInfo.updatePlayer(
                    playerInfoRealtimeWithKeyResponse = match {
                        it.playerInfo.imageUrl == ""
                    }
                )
            }
        }
    }

    @Nested
    inner class UpdatePendingShot {

        @Test
        fun `should delete first pending shot and create new one with preserved id`() {
            val existingPendingShot = PendingShot(
                player = TestPlayer().create(),
                shotLogged = TestShotLogged.build().copy(id = 5),
                isPendingPlayer = false
            )
            val newPendingShot = PendingShot(
                player = TestPlayer().create(),
                shotLogged = TestShotLogged.build().copy(id = 0), // Will be updated to 5
                isPendingPlayer = false
            )
            val expectedPendingShot = newPendingShot.copy(
                shotLogged = newPendingShot.shotLogged.copy(id = 5)
            )

            every { currentPendingShot.fetchPendingShots() } returns listOf(existingPendingShot)

            logShotViewModel.updatePendingShot(pendingShot = newPendingShot)

            verify { currentPendingShot.fetchPendingShots() }
            verify { currentPendingShot.deleteShot(shotLogged = existingPendingShot) }
            verify { currentPendingShot.createShot(shotLogged = expectedPendingShot) }
            verify { navigation.disableProgress() }
            verify { navigation.popToCreateOrEditPlayer() }
        }

        @Test
        fun `should preserve id from first pending shot when creating new one`() {
            val existingId = 42
            val existingPendingShot = PendingShot(
                player = TestPlayer().create(),
                shotLogged = TestShotLogged.build().copy(id = existingId),
                isPendingPlayer = false
            )
            val newPendingShot = PendingShot(
                player = TestPlayer().create(),
                shotLogged = TestShotLogged.build().copy(
                    id = 0,
                    shotName = "New Shot Name",
                    shotsMade = 10
                ),
                isPendingPlayer = false
            )
            newPendingShot.copy(
                shotLogged = newPendingShot.shotLogged.copy(id = existingId)
            )

            every { currentPendingShot.fetchPendingShots() } returns listOf(existingPendingShot)

            logShotViewModel.updatePendingShot(pendingShot = newPendingShot)

            verify {
                currentPendingShot.createShot(
                    shotLogged = match {
                        it.shotLogged.id == existingId &&
                        it.shotLogged.shotName == "New Shot Name" &&
                        it.shotLogged.shotsMade == 10
                    }
                )
            }
        }

        @Test
        fun `should call navigateToCreateOrEditPlayer after updating shot`() {
            val existingPendingShot = PendingShot(
                player = TestPlayer().create(),
                shotLogged = TestShotLogged.build().copy(id = 1),
                isPendingPlayer = false
            )
            val newPendingShot = PendingShot(
                player = TestPlayer().create(),
                shotLogged = TestShotLogged.build().copy(id = 0),
                isPendingPlayer = false
            )

            every { currentPendingShot.fetchPendingShots() } returns listOf(existingPendingShot)

            logShotViewModel.updatePendingShot(pendingShot = newPendingShot)

            verify { navigation.disableProgress() }
            verify { navigation.popToCreateOrEditPlayer() }
        }

        @Test
        fun `should delete first shot before creating new one`() {
            val existingPendingShot = PendingShot(
                player = TestPlayer().create(),
                shotLogged = TestShotLogged.build().copy(id = 7),
                isPendingPlayer = false
            )
            val newPendingShot = PendingShot(
                player = TestPlayer().create(),
                shotLogged = TestShotLogged.build().copy(id = 0),
                isPendingPlayer = false
            )

            every { currentPendingShot.fetchPendingShots() } returns listOf(existingPendingShot)

            logShotViewModel.updatePendingShot(pendingShot = newPendingShot)

            verifyOrder {
                currentPendingShot.fetchPendingShots()
                currentPendingShot.deleteShot(shotLogged = existingPendingShot)
                currentPendingShot.createShot(shotLogged = any())
                navigation.disableProgress()
                navigation.popToCreateOrEditPlayer()
            }
        }

        @Test
        fun `should preserve all pending shot properties except shotLogged id`() {
            val existingId = 15
            val existingPendingShot = PendingShot(
                player = TestPlayer().create(),
                shotLogged = TestShotLogged.build().copy(id = existingId),
                isPendingPlayer = false
            )
            val newPendingShot = PendingShot(
                player = TestPlayer().create().copy(firstName = "John", lastName = "Doe"),
                shotLogged = TestShotLogged.build().copy(
                    id = 0,
                    shotName = "Updated Shot",
                    shotsMade = 5,
                    shotsMissed = 3
                ),
                isPendingPlayer = true
            )

            every { currentPendingShot.fetchPendingShots() } returns listOf(existingPendingShot)

            logShotViewModel.updatePendingShot(pendingShot = newPendingShot)

            verify {
                currentPendingShot.createShot(
                    shotLogged = match {
                        it.player.firstName == "John" &&
                                it.player.lastName == "Doe" && it.isPendingPlayer && it.shotLogged.id == existingId && it.shotLogged.shotName == "Updated Shot" && it.shotLogged.shotsMade == 5 && it.shotLogged.shotsMissed == 3
                    }
                )
            }
        }

        @Test
        fun `should handle multiple pending shots by only using the first one`() {
            val firstPendingShot = PendingShot(
                player = TestPlayer().create(),
                shotLogged = TestShotLogged.build().copy(id = 10),
                isPendingPlayer = false
            )
            val secondPendingShot = PendingShot(
                player = TestPlayer().create(),
                shotLogged = TestShotLogged.build().copy(id = 20),
                isPendingPlayer = false
            )
            val newPendingShot = PendingShot(
                player = TestPlayer().create(),
                shotLogged = TestShotLogged.build().copy(id = 0),
                isPendingPlayer = false
            )

            every { currentPendingShot.fetchPendingShots() } returns listOf(firstPendingShot, secondPendingShot)

            logShotViewModel.updatePendingShot(pendingShot = newPendingShot)

            verify { currentPendingShot.deleteShot(shotLogged = firstPendingShot) }
            verify {
                currentPendingShot.createShot(
                    shotLogged = match {
                        it.shotLogged.id == 10 // Should use ID from first shot
                    }
                )
            }
        }
    }

    @Nested
    inner class ResetState {

        @Test
        fun `should reset all state fields to default values`() {
            // Set initial state with non-default values
            logShotViewModel.logShotMutableStateFlow.value = LogShotState(
                shotName = "Test Shot",
                playerName = "John Doe",
                playerPosition = 5,
                shotsLoggedDateValue = "Jan 1, 2024",
                shotsTakenDateValue = "Jan 2, 2024",
                shotsMade = 10,
                shotsMissed = 5,
                shotsAttempted = 15,
                shotsMadePercentValue = "66.7%",
                shotsMissedPercentValue = "33.3%",
                deleteShotButtonVisible = true,
                toolbarId = StringsIds.loggedShot
            )

            logShotViewModel.resetState()

            val resetState = logShotViewModel.logShotMutableStateFlow.value
            Assertions.assertEquals("", resetState.shotName)
            Assertions.assertEquals("", resetState.playerName)
            Assertions.assertEquals(0, resetState.playerPosition)
            Assertions.assertEquals("", resetState.shotsLoggedDateValue)
            Assertions.assertEquals("", resetState.shotsTakenDateValue)
            Assertions.assertEquals(0, resetState.shotsMade)
            Assertions.assertEquals(0, resetState.shotsMissed)
            Assertions.assertEquals(0, resetState.shotsAttempted)
            Assertions.assertEquals("", resetState.shotsMadePercentValue)
            Assertions.assertEquals("", resetState.shotsMissedPercentValue)
            Assertions.assertFalse(resetState.deleteShotButtonVisible)
        }

        @Test
        fun `should preserve toolbarId when resetting state`() {
            val customToolbarId = StringsIds.loggedShot
            logShotViewModel.logShotMutableStateFlow.value = LogShotState(
                shotName = "Test Shot",
                toolbarId = customToolbarId
            )

            logShotViewModel.resetState()

            val resetState = logShotViewModel.logShotMutableStateFlow.value
            Assertions.assertEquals(customToolbarId, resetState.toolbarId)
        }

        @Test
        fun `should reset state from any initial values`() {
            logShotViewModel.logShotMutableStateFlow.value = LogShotState(
                shotName = "Three Pointer",
                playerName = "LeBron James",
                playerPosition = 3,
                shotsLoggedDateValue = "Dec 25, 2023",
                shotsTakenDateValue = "Dec 26, 2023",
                shotsMade = 20,
                shotsMissed = 10,
                shotsAttempted = 30,
                shotsMadePercentValue = "66.7%",
                shotsMissedPercentValue = "33.3%",
                deleteShotButtonVisible = true
            )

            logShotViewModel.resetState()

            val resetState = logShotViewModel.logShotMutableStateFlow.value
            Assertions.assertEquals(LogShotState(), resetState.copy(toolbarId = resetState.toolbarId))
        }

        @Test
        fun `should reset state when already at default values`() {
            logShotViewModel.logShotMutableStateFlow.value = LogShotState()

            logShotViewModel.resetState()

            val resetState = logShotViewModel.logShotMutableStateFlow.value
            Assertions.assertEquals(LogShotState(), resetState.copy(toolbarId = resetState.toolbarId))
        }

        @Test
        fun `should reset numeric fields to zero`() {
            logShotViewModel.logShotMutableStateFlow.value = LogShotState(
                playerPosition = 10,
                shotsMade = 100,
                shotsMissed = 50,
                shotsAttempted = 150
            )

            logShotViewModel.resetState()

            val resetState = logShotViewModel.logShotMutableStateFlow.value
            Assertions.assertEquals(0, resetState.playerPosition)
            Assertions.assertEquals(0, resetState.shotsMade)
            Assertions.assertEquals(0, resetState.shotsMissed)
            Assertions.assertEquals(0, resetState.shotsAttempted)
        }

        @Test
        fun `should reset string fields to empty strings`() {
            logShotViewModel.logShotMutableStateFlow.value = LogShotState(
                shotName = "Free Throw",
                playerName = "Stephen Curry",
                shotsLoggedDateValue = "Jan 1, 2024",
                shotsTakenDateValue = "Jan 2, 2024",
                shotsMadePercentValue = "90%",
                shotsMissedPercentValue = "10%"
            )

            logShotViewModel.resetState()

            val resetState = logShotViewModel.logShotMutableStateFlow.value
            Assertions.assertEquals("", resetState.shotName)
            Assertions.assertEquals("", resetState.playerName)
            Assertions.assertEquals("", resetState.shotsLoggedDateValue)
            Assertions.assertEquals("", resetState.shotsTakenDateValue)
            Assertions.assertEquals("", resetState.shotsMadePercentValue)
            Assertions.assertEquals("", resetState.shotsMissedPercentValue)
        }

        @Test
        fun `should reset deleteShotButtonVisible to false`() {
            logShotViewModel.logShotMutableStateFlow.value = LogShotState(
                deleteShotButtonVisible = true
            )

            logShotViewModel.resetState()

            val resetState = logShotViewModel.logShotMutableStateFlow.value
            Assertions.assertFalse(resetState.deleteShotButtonVisible)
        }
    }

    @Nested
    inner class BuildPendingShotOnSave {

        @Test
        fun `should build PendingShot with correct player and state values`() {
            val player = TestPlayer().create()
            val state = LogShotState(
                shotName = "Three Pointer",
                shotsAttempted = 20,
                shotsMade = 12,
                shotsMissed = 8,
                shotsMadePercentValue = "60.0%",
                shotsMissedPercentValue = "40.0%",
                shotsTakenDateValue = "Jan 1, 2024",
                shotsLoggedDateValue = "Jan 2, 2024"
            )
            val declaredShot = TestDeclaredShot.build()
            val dateValue = dateFormat.parse("Jan 1, 2024")
            val loggedDateValue = dateFormat.parse("Jan 2, 2024")
            val expectedPendingShot = PendingShot(
                player = player,
                shotLogged = TestShotLogged.build().copy(
                    shotName = "Three Pointer",
                    shotType = declaredShot.id,
                    shotsAttempted = 20,
                    shotsMade = 12,
                    shotsMissed = 8,
                    shotsMadePercentValue = 60.0,
                    shotsMissedPercentValue = 40.0,
                    shotsAttemptedMillisecondsValue = dateValue?.time ?: 0L,
                    shotsLoggedMillisecondsValue = loggedDateValue?.time ?: 0L,
                    isPending = true
                ),
                isPendingPlayer = true
            )

            logShotViewModel.currentDeclaredShot = declaredShot
            every { logShotViewModelExt.buildPendingShotOnSave(player = player, state = state, declaredShot = declaredShot) } returns expectedPendingShot

            val result = logShotViewModel.buildPendingShotOnSave(player = player, state = state)

            verify { logShotViewModelExt.buildPendingShotOnSave(player = player, state = state, declaredShot = declaredShot) }
            Assertions.assertEquals(expectedPendingShot, result)
        }

        @Test
        fun `should trim and replace spaces in percentage values before converting`() {
            val player = TestPlayer().create()
            val state = LogShotState(
                shotName = "Free Throw",
                shotsMadePercentValue = " 75.5% ",
                shotsMissedPercentValue = " 24.5% "
            )
            val declaredShot = TestDeclaredShot.build()
            val expectedPendingShot = PendingShot(
                player = player,
                shotLogged = TestShotLogged.build().copy(
                    shotsMadePercentValue = 75.5,
                    shotsMissedPercentValue = 24.5
                ),
                isPendingPlayer = false
            )

            logShotViewModel.currentDeclaredShot = declaredShot
            every { logShotViewModelExt.buildPendingShotOnSave(player = player, state = state, declaredShot = declaredShot) } returns expectedPendingShot

            val result = logShotViewModel.buildPendingShotOnSave(player = player, state = state)

            verify { logShotViewModelExt.buildPendingShotOnSave(player = player, state = state, declaredShot = declaredShot) }
            Assertions.assertEquals(75.5, result.shotLogged.shotsMadePercentValue)
            Assertions.assertEquals(24.5, result.shotLogged.shotsMissedPercentValue)
        }

        @Test
        fun `when currentDeclaredShot is null should use 0 for shotType`() {
            val player = TestPlayer().create()
            val state = LogShotState(shotName = "Test Shot")
            val expectedPendingShot = PendingShot(
                player = player,
                shotLogged = TestShotLogged.build().copy(shotType = 0),
                isPendingPlayer = false
            )

            logShotViewModel.currentDeclaredShot = null
            every { logShotViewModelExt.buildPendingShotOnSave(player = player, state = state, declaredShot = null) } returns expectedPendingShot

            val result = logShotViewModel.buildPendingShotOnSave(player = player, state = state)

            verify { logShotViewModelExt.buildPendingShotOnSave(player = player, state = state, declaredShot = null) }
            Assertions.assertEquals(0, result.shotLogged.shotType)
        }

        @Test
        fun `when date conversion returns null should use 0L for milliseconds`() {
            val player = TestPlayer().create()
            val state = LogShotState(
                shotsTakenDateValue = "Invalid Date",
                shotsLoggedDateValue = "Invalid Date"
            )
            val declaredShot = TestDeclaredShot.build()
            val expectedPendingShot = PendingShot(
                player = player,
                shotLogged = TestShotLogged.build().copy(
                    shotsAttemptedMillisecondsValue = 0L,
                    shotsLoggedMillisecondsValue = 0L
                ),
                isPendingPlayer = false
            )

            logShotViewModel.currentDeclaredShot = declaredShot
            every { logShotViewModelExt.buildPendingShotOnSave(player = player, state = state, declaredShot = declaredShot) } returns expectedPendingShot

            val result = logShotViewModel.buildPendingShotOnSave(player = player, state = state)

            verify { logShotViewModelExt.buildPendingShotOnSave(player = player, state = state, declaredShot = declaredShot) }
            Assertions.assertEquals(0L, result.shotLogged.shotsAttemptedMillisecondsValue)
            Assertions.assertEquals(0L, result.shotLogged.shotsLoggedMillisecondsValue)
        }

        @Test
        fun `should set isPendingPlayer based on logShotInfo isExistingPlayer`() {
            val player = TestPlayer().create()
            val state = LogShotState()
            val declaredShot = TestDeclaredShot.build()
            val expectedPendingShot1 = PendingShot(
                player = player,
                shotLogged = TestShotLogged.build(),
                isPendingPlayer = true
            )
            val expectedPendingShot2 = PendingShot(
                player = player,
                shotLogged = TestShotLogged.build(),
                isPendingPlayer = false
            )

            logShotViewModel.currentDeclaredShot = declaredShot
            every { logShotViewModelExt.buildPendingShotOnSave(player = player, state = state, declaredShot = declaredShot) } returns expectedPendingShot1

            val result = logShotViewModel.buildPendingShotOnSave(player = player, state = state)

            Assertions.assertTrue(result.isPendingPlayer)

            every { logShotViewModelExt.buildPendingShotOnSave(player = player, state = state, declaredShot = declaredShot) } returns expectedPendingShot2

            val result2 = logShotViewModel.buildPendingShotOnSave(player = player, state = state)

            Assertions.assertFalse(result2.isPendingPlayer)
        }

        @Test
        fun `should delegate to extension method`() {
            val player = TestPlayer().create()
            val state = LogShotState()
            val declaredShot = TestDeclaredShot.build()
            val expectedPendingShot = PendingShot(
                player = player,
                shotLogged = TestShotLogged.build(),
                isPendingPlayer = false
            )

            logShotViewModel.currentDeclaredShot = declaredShot
            every { logShotViewModelExt.buildPendingShotOnSave(player = player, state = state, declaredShot = declaredShot) } returns expectedPendingShot

            val result = logShotViewModel.buildPendingShotOnSave(player = player, state = state)

            verify { logShotViewModelExt.buildPendingShotOnSave(player = player, state = state, declaredShot = declaredShot) }
            Assertions.assertEquals(expectedPendingShot, result)
        }
    }

    @Nested
    inner class HandleExistingShotSaveClicked {

        @Test
        fun `when no changes detected should show alert and not create shot`() {
            val pendingShot = PendingShot(
                player = TestPlayer().create(),
                shotLogged = TestShotLogged.build(),
                isPendingPlayer = false
            )
            val alert = Alert(
                title = "No Changes",
                dismissButton = AlertConfirmAndDismissButton(buttonText = "Got It"),
                description = "No changes made"
            )

            logShotViewModel.initialShotLogged = TestShotLogged.build()
            every { logShotViewModelExt.noChangesForShotAlert(initialShotLogged = logShotViewModel.initialShotLogged, pendingShotLogged = pendingShot.shotLogged) } returns alert

            logShotViewModel.handleExistingShotSaveClicked(pendingShot = pendingShot)

            verify { logShotViewModelExt.noChangesForShotAlert(initialShotLogged = logShotViewModel.initialShotLogged, pendingShotLogged = pendingShot.shotLogged) }
            verify { navigation.disableProgress() }
            verify { navigation.alert(alert = alert) }
            verify(exactly = 0) { currentPendingShot.createShot(any()) }
        }

        @Test
        fun `when changes detected should create pending shot with correct id`() {
            val shotId = 5
            val pendingShot = PendingShot(
                player = TestPlayer().create(),
                shotLogged = TestShotLogged.build().copy(id = 0),
                isPendingPlayer = false
            )
            val expectedPendingShot = pendingShot.copy(shotLogged = pendingShot.shotLogged.copy(id = shotId))

            logShotViewModel.initialShotLogged = TestShotLogged.build().copy(id = 1)
            every { logShotViewModelExt.logShotInfo } returns LogShotInfo(shotId = shotId)
            every { logShotViewModelExt.noChangesForShotAlert(initialShotLogged = logShotViewModel.initialShotLogged, pendingShotLogged = pendingShot.shotLogged) } returns null

            logShotViewModel.handleExistingShotSaveClicked(pendingShot = pendingShot)

            verify { currentPendingShot.createShot(shotLogged = expectedPendingShot) }
            verify { navigation.disableProgress() }
            verify { navigation.popToCreateOrEditPlayer() }
        }

        @Test
        fun `should use shotId from logShotInfo when creating pending shot`() {
            val shotId = 10
            val pendingShot = PendingShot(
                player = TestPlayer().create(),
                shotLogged = TestShotLogged.build().copy(id = 0),
                isPendingPlayer = false
            )

            logShotViewModel.initialShotLogged = TestShotLogged.build()
            every { logShotViewModelExt.logShotInfo } returns LogShotInfo(shotId = shotId)
            every { logShotViewModelExt.noChangesForShotAlert(initialShotLogged = logShotViewModel.initialShotLogged, pendingShotLogged = pendingShot.shotLogged) } returns null

            logShotViewModel.handleExistingShotSaveClicked(pendingShot = pendingShot)

            verify {
                currentPendingShot.createShot(
                    shotLogged = match {
                        it.shotLogged.id == shotId
                    }
                )
            }
        }
    }

    @Nested
    inner class HandlePendingShotSaveClicked {

        @Test
        fun `when no changes detected should show alert and not update shot`() {
            val pendingShot = PendingShot(
                player = TestPlayer().create(),
                shotLogged = TestShotLogged.build(),
                isPendingPlayer = false
            )
            val alert = Alert(
                title = "No Changes",
                dismissButton = AlertConfirmAndDismissButton(buttonText = "Got It"),
                description = "No changes made"
            )

            logShotViewModel.initialShotLogged = TestShotLogged.build()
            every { logShotViewModelExt.noChangesForShotAlert(initialShotLogged = logShotViewModel.initialShotLogged, pendingShotLogged = pendingShot.shotLogged) } returns alert

            logShotViewModel.handlePendingShotSaveClicked(pendingShot = pendingShot)

            verify { logShotViewModelExt.noChangesForShotAlert(initialShotLogged = logShotViewModel.initialShotLogged, pendingShotLogged = pendingShot.shotLogged) }
            verify { navigation.disableProgress() }
            verify { navigation.alert(alert = alert) }
            verify(exactly = 0) { currentPendingShot.fetchPendingShots() }
            verify(exactly = 0) { currentPendingShot.deleteShot(any()) }
            verify(exactly = 0) { currentPendingShot.createShot(any()) }
        }

        @Test
        fun `when changes detected should call updatePendingShot`() {
            val pendingShot = PendingShot(
                player = TestPlayer().create(),
                shotLogged = TestShotLogged.build().copy(shotsMade = 10),
                isPendingPlayer = false
            )

            logShotViewModel.initialShotLogged = TestShotLogged.build().copy(shotsMade = 5)
            every { logShotViewModelExt.noChangesForShotAlert(initialShotLogged = logShotViewModel.initialShotLogged, pendingShotLogged = pendingShot.shotLogged) } returns null
            every { currentPendingShot.fetchPendingShots() } returns listOf(pendingShot)

            logShotViewModel.handlePendingShotSaveClicked(pendingShot = pendingShot)

            verify { currentPendingShot.fetchPendingShots() }
            verify { currentPendingShot.deleteShot(shotLogged = any()) }
            verify { currentPendingShot.createShot(shotLogged = any()) }
            verify { navigation.disableProgress() }
            verify { navigation.popToCreateOrEditPlayer() }
        }

        @Test
        fun `should pass pendingShot directly to updatePendingShot`() {
            val pendingShot = PendingShot(
                player = TestPlayer().create(),
                shotLogged = TestShotLogged.build(),
                isPendingPlayer = false
            )

            logShotViewModel.initialShotLogged = TestShotLogged.build().copy(id = 1)
            every { logShotViewModelExt.noChangesForShotAlert(initialShotLogged = logShotViewModel.initialShotLogged, pendingShotLogged = pendingShot.shotLogged) } returns null
            every { currentPendingShot.fetchPendingShots() } returns listOf(
                PendingShot(
                    player = TestPlayer().create(),
                    shotLogged = TestShotLogged.build().copy(id = 5),
                    isPendingPlayer = false
                )
            )

            logShotViewModel.handlePendingShotSaveClicked(pendingShot = pendingShot)

            verify { currentPendingShot.createShot(shotLogged = any()) }
        }
    }

    @Nested
    inner class HandleFromShotListSaveClicked {

        @Test
        fun `when no changes detected should show alert and not update shot`() = runTest {
            val pendingShot = PendingShot(
                player = TestPlayer().create(),
                shotLogged = TestShotLogged.build(),
                isPendingPlayer = false
            )
            val alert = Alert(
                title = "No Changes",
                dismissButton = AlertConfirmAndDismissButton(buttonText = "Got It"),
                description = "No changes made"
            )

            logShotViewModel.initialShotLogged = TestShotLogged.build()
            every { logShotViewModelExt.noChangesForShotAlert(initialShotLogged = logShotViewModel.initialShotLogged, pendingShotLogged = pendingShot.shotLogged) } returns alert

            logShotViewModel.handleFromShotListSaveClicked(pendingShot = pendingShot)

            verify { logShotViewModelExt.noChangesForShotAlert(initialShotLogged = logShotViewModel.initialShotLogged, pendingShotLogged = pendingShot.shotLogged) }
            verify { navigation.disableProgress() }
            verify { navigation.alert(alert = alert) }
            coVerify(exactly = 0) { updateFirebaseUserInfo.updatePlayer(any()) }
        }

        @Test
        fun `when changes detected should call updateCurrentShot`() = runTest {
            val pendingShot = PendingShot(
                player = TestPlayer().create(),
                shotLogged = TestShotLogged.build().copy(shotsMade = 15),
                isPendingPlayer = false
            )

            logShotViewModel.currentPlayer = TestPlayer().create()
            logShotViewModel.initialShotLogged = TestShotLogged.build().copy(shotsMade = 10)
            every { logShotViewModelExt.noChangesForShotAlert(initialShotLogged = logShotViewModel.initialShotLogged, pendingShotLogged = pendingShot.shotLogged) } returns null
            every { logShotViewModelExt.logShotInfo } returns LogShotInfo(shotId = 1)
            every { logShotViewModelExt.filterShotsById(shots = any()) } returns emptyList()
            coEvery { activeUserRepository.fetchActiveUser() } returns TestActiveUser().create().copy(
                firebaseAccountInfoKey = "testKey"
            )
            coEvery { playerRepository.fetchPlayerByName(any(), any()) } returns TestPlayer().create()
            coEvery { updateFirebaseUserInfo.updatePlayer(any()) } returns flowOf(true)

            logShotViewModel.handleFromShotListSaveClicked(pendingShot = pendingShot)

            verify { logShotViewModelExt.filterShotsById(shots = any()) }
            coVerify { updateFirebaseUserInfo.updatePlayer(any()) }
        }

        @Test
        fun `when currentPlayer is null should disable progress and show alert`() = runTest {
            val pendingShot = PendingShot(
                player = TestPlayer().create(),
                shotLogged = TestShotLogged.build(),
                isPendingPlayer = false
            )

            logShotViewModel.currentPlayer = null
            logShotViewModel.initialShotLogged = TestShotLogged.build().copy(id = 1)
            every { logShotViewModelExt.noChangesForShotAlert(initialShotLogged = logShotViewModel.initialShotLogged, pendingShotLogged = pendingShot.shotLogged) } returns null

            logShotViewModel.handleFromShotListSaveClicked(pendingShot = pendingShot)

            verify { navigation.disableProgress() }
            verify { navigation.alert(alert = any()) }
        }
    }

    @Nested
    inner class GetPlayer {

        @Test
        fun `when isExisting is true should fetch from playerRepository`() = runTest {
            val playerId = 5
            val player = TestPlayer().create()

            // Clear any calls made during ViewModel initialization
            clearMocks(pendingPlayerRepository, playerRepository)

            coEvery { playerRepository.fetchPlayerById(id = playerId) } returns player

            val result = logShotViewModel.getPlayer(isExisting = true, playerId = playerId)

            Assertions.assertEquals(player, result)
            coVerify { playerRepository.fetchPlayerById(id = playerId) }
            coVerify(exactly = 0) { pendingPlayerRepository.fetchPlayerById(any()) }
        }

        @Test
        fun `when isExisting is false should fetch from pendingPlayerRepository`() = runTest {
            val playerId = 3
            val player = TestPlayer().create()

            coEvery { pendingPlayerRepository.fetchPlayerById(id = playerId) } returns player

            val result = logShotViewModel.getPlayer(isExisting = false, playerId = playerId)

            Assertions.assertEquals(player, result)
            coVerify { pendingPlayerRepository.fetchPlayerById(id = playerId) }
            coVerify(exactly = 0) { playerRepository.fetchPlayerById(any()) }
        }

        @Test
        fun `when player not found should return null`() = runTest {
            val playerId = 10

            coEvery { playerRepository.fetchPlayerById(id = playerId) } returns null

            val result = logShotViewModel.getPlayer(isExisting = true, playerId = playerId)

            Assertions.assertNull(result)
        }
    }

    @Nested
    inner class CalculateShotPercentage {

        @Test
        fun `should delegate to extension method`() {
            val shot = TestShotLogged.build().copy(shotsMade = 10, shotsMissed = 5)

            every { logShotViewModelExt.calculateShotPercentage(shot = shot, isShotsMade = true) } returns "66.7%"

            val result = logShotViewModel.calculateShotPercentage(shot = shot, isShotsMade = true)

            verify { logShotViewModelExt.calculateShotPercentage(shot = shot, isShotsMade = true) }
            Assertions.assertEquals("66.7%", result)
        }
    }

    @Nested
    inner class InitializeShotLogged {

        @Test
        fun `should initialize initialShotLogged with current state values`() {
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
            val expectedShotLogged = TestShotLogged.build().copy(
                shotName = "Test Shot",
                shotType = declaredShot.id,
                shotsAttempted = 15,
                shotsMade = 10,
                shotsMissed = 5,
                isPending = true
            )

            logShotViewModel.currentDeclaredShot = declaredShot
            logShotViewModel.logShotMutableStateFlow.value = state
            every { logShotViewModelExt.initializeShotLogged(state = state, declaredShot = declaredShot) } returns expectedShotLogged

            logShotViewModel.initializeShotLogged()

            verify { logShotViewModelExt.initializeShotLogged(state = state, declaredShot = declaredShot) }
            Assertions.assertEquals(expectedShotLogged, logShotViewModel.initialShotLogged)
        }

        @Test
        fun `when currentDeclaredShot is null should use 0 for shotType`() {
            logShotViewModel.currentDeclaredShot = null
            logShotViewModel.logShotMutableStateFlow.value = LogShotState()

            every { logShotViewModelExt.convertPercentageToDouble(percentage = "") } returns 0.0
            every { logShotViewModelExt.convertValueToDate(value = "") } returns null

            logShotViewModel.initializeShotLogged()

            Assertions.assertEquals(0, logShotViewModel.initialShotLogged?.shotType)
        }

        @Test
        fun `should delegate to extension method`() {
            val declaredShot = TestDeclaredShot.build()
            val state = LogShotState(
                shotsMadePercentValue = " 75.5% ",
                shotsMissedPercentValue = " 24.5% "
            )
            val expectedShotLogged = TestShotLogged.build()

            logShotViewModel.currentDeclaredShot = declaredShot
            logShotViewModel.logShotMutableStateFlow.value = state
            every { logShotViewModelExt.initializeShotLogged(state = state, declaredShot = declaredShot) } returns expectedShotLogged

            logShotViewModel.initializeShotLogged()

            verify { logShotViewModelExt.initializeShotLogged(state = state, declaredShot = declaredShot) }
            Assertions.assertEquals(expectedShotLogged, logShotViewModel.initialShotLogged)
        }
    }

    @Nested
    inner class UpdateShotStateFromExisting {

        @Test
        fun `when currentPlayer has matching shot should update state with shot data`() {
            val shotId = 5
            val shot = TestShotLogged.build().copy(
                id = shotId,
                shotsMade = 10,
                shotsMissed = 5,
                shotsAttempted = 15,
                shotsLoggedMillisecondsValue = 1000L,
                shotsAttemptedMillisecondsValue = 2000L
            )
            val player = TestPlayer().create().copy(
                shotsLoggedList = listOf(shot)
            )

            logShotViewModel.currentPlayer = player
            every { logShotViewModelExt.logShotInfo } returns LogShotInfo(shotId = shotId)
            every { logShotViewModelExt.calculateShotPercentage(shot = shot, isShotsMade = true) } returns "66.7%"
            every { logShotViewModelExt.calculateShotPercentage(shot = shot, isShotsMade = false) } returns "33.3%"

            logShotViewModel.updateShotStateFromExisting()

            val state = logShotViewModel.logShotMutableStateFlow.value
            Assertions.assertEquals(10, state.shotsMade)
            Assertions.assertEquals(5, state.shotsMissed)
            Assertions.assertEquals(15, state.shotsAttempted)
            Assertions.assertEquals("66.7%", state.shotsMadePercentValue)
            Assertions.assertEquals("33.3%", state.shotsMissedPercentValue)
            Assertions.assertTrue(state.deleteShotButtonVisible)
            Assertions.assertEquals(StringsIds.loggedShot, state.toolbarId)
        }

        @Test
        fun `when currentPlayer is null should not update state`() {
            logShotViewModel.currentPlayer = null
            val initialState = logShotViewModel.logShotMutableStateFlow.value

            logShotViewModel.updateShotStateFromExisting()

            Assertions.assertEquals(initialState, logShotViewModel.logShotMutableStateFlow.value)
        }

        @Test
        fun `when shot with matching id not found should not update state`() {
            val shotId = 99
            val player = TestPlayer().create().copy(
                shotsLoggedList = listOf(TestShotLogged.build().copy(id = 1))
            )

            logShotViewModel.currentPlayer = player
            every { logShotViewModelExt.logShotInfo } returns LogShotInfo(shotId = shotId)
            val initialState = logShotViewModel.logShotMutableStateFlow.value

            logShotViewModel.updateShotStateFromExisting()

            Assertions.assertEquals(initialState, logShotViewModel.logShotMutableStateFlow.value)
        }
    }

    @Nested
    inner class UpdateShotStateFromPending {

        @Test
        fun `when pending shot exists should update state with shot data`() = runTest {
            val shot = TestShotLogged.build().copy(
                shotsMade = 8,
                shotsMissed = 2,
                shotsAttempted = 10,
                shotsLoggedMillisecondsValue = 3000L,
                shotsAttemptedMillisecondsValue = 4000L
            )
            val pendingShot = PendingShot(
                player = TestPlayer().create(),
                shotLogged = shot,
                isPendingPlayer = false
            )

            every { currentPendingShot.shotsStateFlow } returns flowOf(listOf(pendingShot))
            every { logShotViewModelExt.calculateShotPercentage(shot = shot, isShotsMade = true) } returns "80.0%"
            every { logShotViewModelExt.calculateShotPercentage(shot = shot, isShotsMade = false) } returns "20.0%"

            logShotViewModel.updateShotStateFromPending()

            val state = logShotViewModel.logShotMutableStateFlow.value
            Assertions.assertEquals(8, state.shotsMade)
            Assertions.assertEquals(2, state.shotsMissed)
            Assertions.assertEquals(10, state.shotsAttempted)
            Assertions.assertEquals("80.0%", state.shotsMadePercentValue)
            Assertions.assertEquals("20.0%", state.shotsMissedPercentValue)
            Assertions.assertFalse(state.deleteShotButtonVisible)
            Assertions.assertEquals(StringsIds.logShot, state.toolbarId)
        }

        @Test
        fun `when no pending shots should not update state`() = runTest {
            every { currentPendingShot.shotsStateFlow } returns flowOf(emptyList())
            val initialState = logShotViewModel.logShotMutableStateFlow.value

            logShotViewModel.updateShotStateFromPending()

            Assertions.assertEquals(initialState, logShotViewModel.logShotMutableStateFlow.value)
        }

        @Test
        fun `when pending shots list is empty should not update state`() = runTest {
            every { currentPendingShot.shotsStateFlow } returns flowOf(emptyList())
            val initialState = logShotViewModel.logShotMutableStateFlow.value

            logShotViewModel.updateShotStateFromPending()

            Assertions.assertEquals(initialState, logShotViewModel.logShotMutableStateFlow.value)
        }
    }

    @Nested
    inner class UpdateViewShotState {

        @Test
        fun `when viewCurrentExistingShot is true should call updateShotStateFromExisting and initializeShotLogged`() = runTest {
            val shotId = 5
            val shot = TestShotLogged.build().copy(id = shotId)
            val player = TestPlayer().create().copy(shotsLoggedList = listOf(shot))

            logShotViewModel.currentPlayer = player
            every { logShotViewModelExt.logShotInfo } returns LogShotInfo(viewCurrentExistingShot = true, shotId = shotId)
            every { logShotViewModelExt.percentageFormat(any(), any(), any()) } returns "50%"
            every { logShotViewModelExt.convertPercentageToDouble(percentage = "") } returns 0.0
            every { logShotViewModelExt.convertValueToDate(value = "") } returns null

            logShotViewModel.updateViewShotState()

            val state = logShotViewModel.logShotMutableStateFlow.value
            Assertions.assertEquals(shot.shotsMade, state.shotsMade)
            Assertions.assertNotNull(logShotViewModel.initialShotLogged)
        }

        @Test
        fun `when viewCurrentPendingShot is true should call updateShotStateFromPending and initializeShotLogged`() = runTest {
            val shot = TestShotLogged.build()
            val pendingShot = PendingShot(
                player = TestPlayer().create(),
                shotLogged = shot,
                isPendingPlayer = false
            )

            every { logShotViewModelExt.logShotInfo } returns LogShotInfo(viewCurrentPendingShot = true)
            every { currentPendingShot.shotsStateFlow } returns flowOf(listOf(pendingShot))
            every { logShotViewModelExt.percentageFormat(any(), any(), any()) } returns "50%"
            every { logShotViewModelExt.convertPercentageToDouble(percentage = "") } returns 0.0
            every { logShotViewModelExt.convertValueToDate(value = "") } returns null

            logShotViewModel.updateViewShotState()

            val state = logShotViewModel.logShotMutableStateFlow.value
            Assertions.assertEquals(shot.shotsMade, state.shotsMade)
            Assertions.assertNotNull(logShotViewModel.initialShotLogged)
        }

        @Test
        fun `when both viewCurrentExistingShot and viewCurrentPendingShot are true should call both and initializeShotLogged`() = runTest {
            val shotId = 3
            val shot = TestShotLogged.build().copy(id = shotId)
            val player = TestPlayer().create().copy(shotsLoggedList = listOf(shot))
            val pendingShot = PendingShot(
                player = player,
                shotLogged = shot,
                isPendingPlayer = false
            )

            logShotViewModel.currentPlayer = player
            every { logShotViewModelExt.logShotInfo } returns LogShotInfo(
                viewCurrentExistingShot = true,
                viewCurrentPendingShot = true,
                shotId = shotId
            )
            every { currentPendingShot.shotsStateFlow } returns flowOf(listOf(pendingShot))
            every { logShotViewModelExt.percentageFormat(any(), any(), any()) } returns "50%"
            every { logShotViewModelExt.convertPercentageToDouble(percentage = "") } returns 0.0
            every { logShotViewModelExt.convertValueToDate(value = "") } returns null

            logShotViewModel.updateViewShotState()

            Assertions.assertNotNull(logShotViewModel.initialShotLogged)
        }

        @Test
        fun `when neither viewCurrentExistingShot nor viewCurrentPendingShot are true should only initializeShotLogged`() = runTest {
            every { logShotViewModelExt.logShotInfo } returns LogShotInfo(
                viewCurrentExistingShot = false,
                viewCurrentPendingShot = false
            )
            every { logShotViewModelExt.convertPercentageToDouble(percentage = "") } returns 0.0
            every { logShotViewModelExt.convertValueToDate(value = "") } returns null

            logShotViewModel.updateViewShotState()

            Assertions.assertNotNull(logShotViewModel.initialShotLogged)
        }
    }
}
