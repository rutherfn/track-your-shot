package com.nicholas.rutherford.track.your.shot.data.test.room

import com.nicholas.rutherford.track.your.shot.data.room.entities.toActiveUser
import com.nicholas.rutherford.track.your.shot.data.room.response.ActiveUser

class TestActiveUser {

    fun create(): ActiveUser {
        return TestActiveUserEntity().create().toActiveUser()
    }
}
