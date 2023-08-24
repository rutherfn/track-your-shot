package com.nicholas.rutherford.track.my.shot.account.info.realtime

data class PlayerInfoRealtimeResponse (
        val email: String = "",
        val firstName: String = "",
        val lastName: String = "",
        val positionValue: Int = 0,
        val imageUrl: String? = null
)