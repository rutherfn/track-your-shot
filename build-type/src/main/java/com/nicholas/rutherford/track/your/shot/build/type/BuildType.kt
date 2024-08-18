package com.nicholas.rutherford.track.your.shot.build.type

interface BuildType {
    val sdk: Int

    fun isDebug(): Boolean
    fun isRelease(): Boolean
    fun isStage(): Boolean
}
