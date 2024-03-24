package com.nicholas.rutherford.track.your.shot.feature.players.shots.logshot

import android.app.Application
import com.nicholas.rutherford.track.your.shot.data.room.repository.DeclaredShotRepository
import com.nicholas.rutherford.track.your.shot.data.room.repository.PendingPlayerRepository
import com.nicholas.rutherford.track.your.shot.data.room.repository.PlayerRepository
import com.nicholas.rutherford.track.your.shot.data.test.room.TestDeclaredShot
import com.nicholas.rutherford.track.your.shot.data.test.room.TestPlayer
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

    @BeforeEach
    fun beforeEach() {
        logShotViewModel = LogShotViewModel(
            application = application,
            scope = scope,
            navigation = navigation,
            declaredShotRepository = declaredShotRepository,
            pendingPlayerRepository = pendingPlayerRepository,
            playerRepository = playerRepository
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
                    logShotViewModel.logShotMutableStateFlow.value, LogShotState(
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
                    logShotViewModel.logShotMutableStateFlow.value, LogShotState(
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
    fun `on back clicked should pop stack`() {
        logShotViewModel.onBackClicked()

        Assertions.assertEquals(logShotViewModel.logShotMutableStateFlow.value, LogShotState())

        verify { navigation.pop() }
    }
}