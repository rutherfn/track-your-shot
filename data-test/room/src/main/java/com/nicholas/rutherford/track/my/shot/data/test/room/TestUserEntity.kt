package com.nicholas.rutherford.track.your.shot.data.test.room

import com.nicholas.rutherford.track.your.shot.data.room.entities.UserEntity

class TestUserEntity {
    fun create(): UserEntity {
        return UserEntity(
            id = ID,
            email = EMAIL,
            username = USERNAME
        )
    }
}
