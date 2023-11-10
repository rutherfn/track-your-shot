package com.nicholas.rutherford.track.your.shot.data.test.room

import com.nicholas.rutherford.track.your.shot.data.room.entities.toPlayer
import com.nicholas.rutherford.track.your.shot.data.room.response.Player

class TestPlayer {

    fun create(): Player {
        return TestPlayerEntity().create().toPlayer()
    }
}
