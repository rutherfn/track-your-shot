package com.nicholas.rutherford.track.your.shot.firebase.realtime

object TestDeclaredShotRealtimeResponse {
    fun create(): DeclaredShotRealtimeResponse {
        return DeclaredShotRealtimeResponse(
            id = DECLARED_SHOT_REALTIME_ID,
            shotCategory = DECLARED_SHOT_REALTIME_CATEGORY,
            title = DECLARED_SHOT_TITLE,
            description = DECLARED_SHOT_DESCRIPTION
        )
    }
}

const val DECLARED_SHOT_REALTIME_ID = 1
const val DECLARED_SHOT_REALTIME_CATEGORY = "category"
const val DECLARED_SHOT_TITLE = "title"
const val DECLARED_SHOT_DESCRIPTION = "description"
