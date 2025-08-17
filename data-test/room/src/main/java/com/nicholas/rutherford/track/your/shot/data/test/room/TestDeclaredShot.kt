package com.nicholas.rutherford.track.your.shot.data.test.room

import com.nicholas.rutherford.track.your.shot.data.room.entities.toDeclaredShot
import com.nicholas.rutherford.track.your.shot.data.room.response.DeclaredShot

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Utility object for creating test instances of [DeclaredShot].
 * Converts a test [com.nicholas.rutherford.track.your.shot.data.room.entities.DeclaredShotEntity] into its domain model representation.
 */
object TestDeclaredShot {

    /**
     * Builds a test [DeclaredShot] instance.
     *
     * @return a new [DeclaredShot] based on the test [com.nicholas.rutherford.track.your.shot.data.room.entities.DeclaredShotEntity].
     */
    fun build(): DeclaredShot {
        return TestDeclaredShotEntity.build().toDeclaredShot()
    }
}
