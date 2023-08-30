package com.nicholas.rutherford.track.my.shot.data.test.firebase

import com.nicholas.rutherford.track.my.shot.data.firebase.PlayerInfoRealtimeResponse

class TestPlayerInfoRealtimeResponse {

    fun create(): PlayerInfoRealtimeResponse {
        return PlayerInfoRealtimeResponse(
            firstName = FIRST_NAME,
            lastName = LAST_NAME,
            positionValue = POSITION_VALUE,
            imageUrl = IMAGE_URL
        )
    }
}

const val FIRST_NAME = "firstName"
const val LAST_NAME = "lastName"
const val POSITION_VALUE = 2
const val IMAGE_URL = "imageUrl"
