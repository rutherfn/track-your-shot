package com.nicholas.rutherford.track.your.shot.data.test.room

import com.nicholas.rutherford.track.your.shot.data.room.entities.toUser
import com.nicholas.rutherford.track.your.shot.data.room.response.User

class TestUser {

    fun create(): User {
        return TestUserEntity().create().toUser()
    }
}
