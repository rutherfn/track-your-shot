package com.nicholas.rutherford.track.your.shot.data.test.room

import com.nicholas.rutherford.track.your.shot.data.room.entities.ActiveUserEntity
import com.nicholas.rutherford.track.your.shot.helper.constants.Constants

class TestActiveUserEntity {
    fun create(): ActiveUserEntity {
        return ActiveUserEntity(
            id = ID,
            accountHasBeenCreated = ACCOUNT_HAS_BEEN_CREATED,
            email = EMAIL,
            username = USERNAME,
            firebaseAccountInfoKey = FIREBASE_ACCOUNT_INFO_KEY
        )
    }
}

const val ID = Constants.ACTIVE_USER_ID
const val ACCOUNT_HAS_BEEN_CREATED = false
const val EMAIL = "unverifiedemail@test.org"
const val USERNAME = "unverifiedUsername"
const val FIREBASE_ACCOUNT_INFO_KEY = "-Ne4L6shsEBIH-zX9wld"
