package com.nicholas.rutherford.track.my.shot.feature.players

import com.nicholas.rutherford.track.my.shot.data.room.response.Player
import com.nicholas.rutherford.track.my.shot.data.room.response.PlayerPositions
import com.nicholas.rutherford.track.my.shot.firebase.core.read.ReadFirebaseUserInfo
import io.mockk.mockk
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class PlayerListViewModelTest {

    private lateinit var playerListViewModel: PlayersListViewModel

    private var readFirebaseUserInfo = mockk<ReadFirebaseUserInfo>(relaxed = true)

    @BeforeEach
    fun beforeEach() {
        playerListViewModel = PlayersListViewModel(readFirebaseUserInfo = readFirebaseUserInfo)
    }

    @Nested
    inner class validatePlayer {

        @Test
        fun `first test`() {
            playerListViewModel.validatePlayer(player = Player(firstName = "", lastName = "", imageUrl = "", position = PlayerPositions.Center))

            Assertions.assertFalse(playerListViewModel.isValidPlayer)
        }
    }
}
