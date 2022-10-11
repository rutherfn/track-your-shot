package com.nicholas.rutherford.track.my.shot.build.type

interface BuildType {
    fun isDebug(): Boolean
    fun isRelease(): Boolean
    fun isStage(): Boolean
}
