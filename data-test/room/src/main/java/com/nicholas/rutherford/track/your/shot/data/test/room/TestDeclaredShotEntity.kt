package com.nicholas.rutherford.track.your.shot.data.test.room

import com.nicholas.rutherford.track.your.shot.data.room.entities.DeclaredShotEntity

object TestDeclaredShotEntity {

    fun build(): DeclaredShotEntity {
        return DeclaredShotEntity(
            id = DECLARED_SHOT_ID,
            shotCategory = SHOT_CATEGORY,
            title = TITLE,
            description = DESCRIPTION,
            firebaseKey = FIREBASE_KEY
        )
    }
}

const val DECLARED_SHOT_ID = 1
const val SHOT_CATEGORY = "other"
const val TITLE = "Hook Shot"
const val DESCRIPTION = "description 1"
const val FIREBASE_KEY = "firebase-key"
