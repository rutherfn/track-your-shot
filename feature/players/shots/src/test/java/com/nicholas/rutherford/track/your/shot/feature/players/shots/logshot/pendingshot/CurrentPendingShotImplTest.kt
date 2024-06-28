package com.nicholas.rutherford.track.your.shot.feature.players.shots.logshot.pendingshot

import com.nicholas.rutherford.track.your.shot.data.test.room.TestPlayer
import com.nicholas.rutherford.track.your.shot.data.test.room.TestShotLogged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class CurrentPendingShotImplTest {

    private val currentPendingShotImpl = CurrentPendingShotImpl()
    private val pendingShot = PendingShot(
        player = TestPlayer().create(),
        shotLogged = TestShotLogged.build(),
        isPendingPlayer = true
    )

    @Test
    fun `shotStateFlow should be updated from the MutableStateFlow`() = runTest {
        val pendingShotEmptyList: List<PendingShot> = emptyList()

        Assertions.assertEquals(
            currentPendingShotImpl.shotsStateFlow.first(),
            flowOf(pendingShotEmptyList).first()
        )

        currentPendingShotImpl.createShot(shotLogged = pendingShot)

        Assertions.assertEquals(
            currentPendingShotImpl.shotsStateFlow.first(),
            flowOf(listOf(pendingShot)).first()
        )
    }

    @Test
    fun `shotStateFlow should be deleted when a shot is passed in that is in the state flow`() = runTest {
        val pendingShot2 = PendingShot(
            player = TestPlayer().create(),
            shotLogged = TestShotLogged.build().copy(shotName = "Test Shot Name"),
            isPendingPlayer = false
        )

        val pendingShotEmptyList: List<PendingShot> = emptyList()

        Assertions.assertEquals(
            currentPendingShotImpl.shotsStateFlow.first(),
            flowOf(pendingShotEmptyList).first()
        )

        currentPendingShotImpl.createShot(shotLogged = pendingShot)
        currentPendingShotImpl.createShot(shotLogged = pendingShot2)

        Assertions.assertEquals(
            currentPendingShotImpl.shotsStateFlow.first(),
            flowOf(listOf(pendingShot, pendingShot2)).first()
        )

        currentPendingShotImpl.deleteShot(shotLogged = pendingShot)

        Assertions.assertEquals(
            currentPendingShotImpl.shotsStateFlow.first(),
            flowOf(listOf(pendingShot2)).first()
        )
    }

    @Test
    fun `create shot should update the contents of the MutableStateFlow`() {
        val newPendingShot = pendingShot.copy(isPendingPlayer = false)
        val newPendingShotList = listOf(pendingShot) + listOf(newPendingShot)

        currentPendingShotImpl.shotsMutableStateFlow.value = listOf(pendingShot)

        Assertions.assertEquals(
            currentPendingShotImpl.shotsMutableStateFlow.value,
            listOf(pendingShot)
        )

        currentPendingShotImpl.createShot(shotLogged = newPendingShot)

        Assertions.assertEquals(
            currentPendingShotImpl.shotsMutableStateFlow.value,
            newPendingShotList
        )
    }

    @Test
    fun `clear shot list should empty out the MutableStateFlow`() {
        val pendingShotEmptyList: List<PendingShot> = emptyList()

        currentPendingShotImpl.shotsMutableStateFlow.value = listOf(pendingShot)

        Assertions.assertEquals(
            currentPendingShotImpl.shotsMutableStateFlow.value,
            listOf(pendingShot)
        )

        currentPendingShotImpl.clearShotList()

        Assertions.assertEquals(
            currentPendingShotImpl.shotsMutableStateFlow.value,
            pendingShotEmptyList
        )
    }
}
