package com.nicholas.rutherford.track.your.shot.firebase.realtime

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Utility object for creating test instances of [DeclaredShotRealtimeResponse].
 * Provides predefined test data for ID, category, title, and description.
 */
object TestDeclaredShotRealtimeResponse {

    /**
     * Creates a test [DeclaredShotRealtimeResponse] instance with predefined values.
     *
     * @return a [DeclaredShotRealtimeResponse] containing test ID, category, title, and description.
     */
    fun create(): DeclaredShotRealtimeResponse {
        return DeclaredShotRealtimeResponse(
            id = DECLARED_SHOT_REALTIME_ID,
            shotCategory = DECLARED_SHOT_REALTIME_CATEGORY,
            title = DECLARED_SHOT_TITLE,
            description = DECLARED_SHOT_DESCRIPTION
        )
    }
}

/** Predefined test ID for [DeclaredShotRealtimeResponse]. */
const val DECLARED_SHOT_REALTIME_ID = 1

/** Predefined test category for [DeclaredShotRealtimeResponse]. */
const val DECLARED_SHOT_REALTIME_CATEGORY = "category"

/** Predefined test title for [DeclaredShotRealtimeResponse]. */
const val DECLARED_SHOT_TITLE = "title"

/** Predefined test description for [DeclaredShotRealtimeResponse]. */
const val DECLARED_SHOT_DESCRIPTION = "description"
