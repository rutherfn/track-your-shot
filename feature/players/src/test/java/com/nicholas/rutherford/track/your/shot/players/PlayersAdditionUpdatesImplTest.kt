package com.nicholas.rutherford.track.your.shot.players

import com.nicholas.rutherford.track.your.shot.data.test.room.TestPlayer
import com.nicholas.rutherford.track.your.shot.feature.players.PlayersAdditionUpdatesImpl
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class PlayersAdditionUpdatesImplTest {

    private lateinit var playersAdditionUpdatesImpl: PlayersAdditionUpdatesImpl

    @BeforeEach
    fun beforeEach() {
        playersAdditionUpdatesImpl = PlayersAdditionUpdatesImpl()
    }

    @Test
    fun `newPlayerHasBeenAddedSharedFlow emits correct values`() = runTest {
        // Given
        val playerUpdates = PlayersAdditionUpdatesImpl()
        val expectedValues = listOf(true, false, true, true, false)

        // When
        val actualValues = mutableListOf<Boolean>()
        val job = launch {
            playerUpdates.newPlayerHasBeenAddedSharedFlow
                .take(expectedValues.size)
                .collect { actualValues.add(it) }
        }

        expectedValues.forEach {
            playerUpdates.updateNewPlayerHasBeenAddedSharedFlow(it)
        }

        // Then
        assertEquals(expectedValues, actualValues)

        // Cleanup
        job.cancel()
    }

    @Test
    fun `newPlayerHasBeenAddedSharedFlow replays values`() = runTest {
        // Given
        val playerUpdates = PlayersAdditionUpdatesImpl()
        val expectedValues = listOf(true, false, true, true, false)

        // When
        val actualValues = mutableListOf<Boolean>()
        expectedValues.forEach {
            playerUpdates.updateNewPlayerHasBeenAddedSharedFlow(it)
        }

        val job = launch {
            playerUpdates.newPlayerHasBeenAddedSharedFlow
                .collect { actualValues.add(it) }
        }

        // Then
        assertEquals(expectedValues, actualValues)

        // Cleanup
        job.cancel()
    }
}
