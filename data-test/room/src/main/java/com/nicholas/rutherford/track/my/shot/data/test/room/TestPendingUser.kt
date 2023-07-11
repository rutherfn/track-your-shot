package com.nicholas.rutherford.track.my.shot.data.test.room

import com.nicholas.rutherford.track.my.shot.data.room.PendingUser

class TestPendingUser {
    fun create(): PendingUser {
        return PendingUser(
            id = ID,
            accountHasBeenCreated = ACCOUNT_HAS_BEEN_CREATED,
            unverifiedEmail = UNVERIFIED_EMAIL,
            unverifiedUsername = UNVERIFIED_USERNAME
        )
    }
}

const val ID = 2
const val ACCOUNT_HAS_BEEN_CREATED = true
const val UNVERIFIED_EMAIL = "unverifiedemail@test.org"
const val UNVERIFIED_USERNAME = "unverifiedUsername"
