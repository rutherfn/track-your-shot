package com.nicholas.rutherford.track.your.shot.data.test.room

import com.nicholas.rutherford.track.your.shot.data.room.entities.toDeclaredShot
import com.nicholas.rutherford.track.your.shot.data.room.response.DeclaredShot

object TestDeclaredShot {

    fun build(): DeclaredShot {
        return TestDeclaredShotEntity.build().toDeclaredShot()
    }
}
