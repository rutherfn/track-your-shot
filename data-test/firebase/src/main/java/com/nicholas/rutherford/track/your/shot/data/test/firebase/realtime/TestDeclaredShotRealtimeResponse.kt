package com.nicholas.rutherford.track.your.shot.data.test.firebase.realtime

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Utility object for creating test instances of [com.nicholas.rutherford.track.your.shot.firebase.realtime.DeclaredShotRealtimeResponse].
 * Provides predefined test data for ID, category, title, and description.
 */
object TestDeclaredShotRealtimeResponse {

    /**
     * Creates a test [com.nicholas.rutherford.track.your.shot.firebase.realtime.DeclaredShotRealtimeResponse] instance with predefined values.
     *
     * @return a [com.nicholas.rutherford.track.your.shot.firebase.realtime.DeclaredShotRealtimeResponse] containing test ID, category, title, and description.
     */
    fun create(): com.nicholas.rutherford.track.your.shot.firebase.realtime.DeclaredShotRealtimeResponse {
        return _root_ide_package_.com.nicholas.rutherford.track.your.shot.firebase.realtime.DeclaredShotRealtimeResponse(
            id = DECLARED_SHOT_REALTIME_ID,
            shotCategory = DECLARED_SHOT_REALTIME_CATEGORY,
            title = DECLARED_SHOT_TITLE,
            description = DECLARED_SHOT_DESCRIPTION
        )
    }
}

/** Predefined test ID for [com.nicholas.rutherford.track.your.shot.firebase.realtime.DeclaredShotRealtimeResponse]. */
const val DECLARED_SHOT_REALTIME_ID = 1

/** Predefined test category for [com.nicholas.rutherford.track.your.shot.firebase.realtime.DeclaredShotRealtimeResponse]. */
const val DECLARED_SHOT_REALTIME_CATEGORY = "category"

/** Predefined test title for [com.nicholas.rutherford.track.your.shot.firebase.realtime.DeclaredShotRealtimeResponse]. */
const val DECLARED_SHOT_TITLE = "title"

/** Predefined test description for [com.nicholas.rutherford.track.your.shot.firebase.realtime.DeclaredShotRealtimeResponse]. */
const val DECLARED_SHOT_DESCRIPTION = "description"
