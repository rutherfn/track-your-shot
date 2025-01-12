package com.nicholas.rutherford.track.your.shot.helper.extensions.dataadditionupdates

import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class DataAdditionUpdatesImplTest {

    private lateinit var dataAdditionUpdatesImpl: DataAdditionUpdatesImpl

    @BeforeEach
    fun beforeEach() {
        dataAdditionUpdatesImpl = DataAdditionUpdatesImpl()
    }

    @Nested
    inner class NewPlayerHasBeenAddedSharedFlow {
        @Test
        fun `should initially be set to null if not updated`() = runTest {
            val result = dataAdditionUpdatesImpl.newPlayerHasBeenAddedMutableSharedFlow.replayCache.firstOrNull()

            Assertions.assertEquals(null, result)
        }

        @Test
        fun `should be updated if updateNewPlayerAddedFlow is called`() = runTest {
            val dataAdditionUpdatesImpl = DataAdditionUpdatesImpl()
            val expectedValue = true

            dataAdditionUpdatesImpl.updateNewPlayerHasBeenAddedSharedFlow(expectedValue)

            val result = dataAdditionUpdatesImpl.newPlayerHasBeenAddedMutableSharedFlow.replayCache.firstOrNull()

            Assertions.assertEquals(expectedValue, result)
        }
    }

    @Nested
    inner class NewReportHasBeenAddedSharedFlow {
        @Test
        fun `should initially be set to null if not updated`() = runTest {
            val result = dataAdditionUpdatesImpl.newReportHasBeenAddedMutableSharedFlow.replayCache.firstOrNull()

            Assertions.assertEquals(null, result)
        }

        @Test
        fun `should be updated if updateNewReportAddedFlow is called`() = runTest {
            val dataAdditionUpdatesImpl = DataAdditionUpdatesImpl()
            val expectedValue = true

            dataAdditionUpdatesImpl.updateNewReportHasBeenAddedSharedFlow(expectedValue)

            val result = dataAdditionUpdatesImpl.newReportHasBeenAddedMutableSharedFlow.replayCache.firstOrNull()

            Assertions.assertEquals(expectedValue, result)
        }
    }
}
