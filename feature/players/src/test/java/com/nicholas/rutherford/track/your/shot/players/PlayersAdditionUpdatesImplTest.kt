package com.nicholas.rutherford.track.your.shot.players

import com.nicholas.rutherford.track.your.shot.data.room.response.Player
import com.nicholas.rutherford.track.your.shot.data.test.room.TestPlayer
import com.nicholas.rutherford.track.your.shot.feature.players.PlayersAdditionUpdatesImpl
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
    inner class NewPlayerAddedStateFlow {
        @Test
        fun `should initially be set to null if not updated`() {
            val result = playersAdditionUpdatesImpl.newPlayerAddedStateFlow.value

            Assertions.assertEquals(null, result)
        }

        @Test
        fun `should be updated if updateNewPlayerAddedFlow is called`() {
            val player = TestPlayer().create()
            playersAdditionUpdatesImpl.updateNewPlayerAddedFlow(player)

            val result = playersAdditionUpdatesImpl.newPlayerAddedStateFlow.value

            Assertions.assertEquals(player, result)
        }
    }
}