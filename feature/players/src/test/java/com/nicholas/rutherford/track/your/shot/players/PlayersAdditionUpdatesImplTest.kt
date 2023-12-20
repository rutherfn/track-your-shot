package com.nicholas.rutherford.track.your.shot.players

import com.nicholas.rutherford.track.your.shot.feature.players.PlayersAdditionUpdatesImpl
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class PlayersAdditionUpdatesImplTest {

    private lateinit var playersAdditionUpdatesImpl: PlayersAdditionUpdatesImpl

    @BeforeEach
    fun beforeEach() {
        playersAdditionUpdatesImpl = PlayersAdditionUpdatesImpl()
    }

    @Nested
    inner class NewPlayerHasBeenAddedSharedFlow {
        @Test
        fun `should initially be set to null if not updated`() = runTest {
            val result = playersAdditionUpdatesImpl.newPlayerHasBeenAddedMutableSharedFlow.replayCache.firstOrNull()

            Assertions.assertEquals(null, result)
        }

        @Test
        fun `should be updated if updateNewPlayerAddedFlow is called`() = runTest {
            val playersAdditionUpdatesImpl = PlayersAdditionUpdatesImpl()
            val expectedValue = true

            playersAdditionUpdatesImpl.updateNewPlayerHasBeenAddedSharedFlow(expectedValue)

            val result = playersAdditionUpdatesImpl.newPlayerHasBeenAddedMutableSharedFlow.replayCache.firstOrNull()

            Assertions.assertEquals(expectedValue, result)
        }
    }
}
