package com.nicholas.rutherford.track.my.shot.data.test.room

import com.nicholas.rutherford.track.my.shot.data.room.entities.ActiveUserEntity
import com.nicholas.rutherford.track.my.shot.helper.constants.Constants

class TestActiveUserEntity {
    fun create(): ActiveUserEntity {
        return ActiveUserEntity(
            id = ID,
            accountHasBeenCreated = ACCOUNT_HAS_BEEN_CREATED,
            email = EMAIL
        )
    }
}

const val ID = Constants.ACTIVE_USER_ID
const val ACCOUNT_HAS_BEEN_CREATED = false
const val EMAIL = "unverifiedemail@test.org"
